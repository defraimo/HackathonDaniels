package daniel.rad.radiotabsdrawer.playlist;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import daniel.rad.radiotabsdrawer.MainActivity;
import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.myMediaPlayer.ProgramsReceiver;
import daniel.rad.radiotabsdrawer.playlist.chosenPlaylist.CreatePlaylistFragment;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;


public class PlaylistsJsonWriter extends AsyncTask<Void, Void, Void> {
    public ArrayList<Playlist> playlists;
    public WeakReference<Context> contextWeakReference;
    public static boolean isLoaded;

    public PlaylistsJsonWriter(ArrayList<Playlist> playlists, Context context) {
        this.playlists = playlists;
        contextWeakReference = new WeakReference<>(context);
    }
    public PlaylistsJsonWriter() {
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Context context = contextWeakReference.get();
        String path = "/data/data/" + context.getPackageName();
        File filePath = new File(path, "playlists.json");
        try (FileWriter out = new FileWriter(filePath, false)) {
//        jsonConverter(context);
            String playlistsJson = gsonConverter();
            System.out.println(playlistsJson);
            out.write(playlistsJson);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.e("write", "doInBackground: data was saved.... ");
        if (!isLoaded) {
            isLoaded = true;
            new ProgramsReceiver((MainActivity) contextWeakReference.get()).execute();
        }else{
            AppCompatActivity activity = (AppCompatActivity) contextWeakReference.get();
            activity.getSupportFragmentManager().
                    beginTransaction().
                    addToBackStack("allPlaylists").
                    replace(R.id.playlist_frame, AllPlaylistsFragment.newInstance(playlists)).
                    commit();
        }
    }

    private String gsonConverter() {
        Gson gson = new Gson();
        String playlistsJson = gson.toJson(playlists);
        System.out.println("playlistJson: " + playlistsJson);

        return playlistsJson;

    }

    private void jsonConverter(Context context) {
        JSONArray jsonArray = new JSONArray();
        JSONObject singlePlaylist = new JSONObject();
        JSONArray playlistPrograms = new JSONArray();
        JSONObject singleProgram = new JSONObject();
        //todo: needs to be replaced with db access:
        String path = "/data/data/" + context.getPackageName();
        File filePath = new File(path, "playlists.json");

        try (FileWriter out = new FileWriter(filePath)) {
            int i = 0;
            for (Playlist playlist : playlists) {
                for (ProgramsData program : playlist.getProgramsData()) {
                    singleProgram.put("vodId", program.getVodId());
                    singleProgram.put("programName", program.getProgramName());
                    singleProgram.put("studentName", program.getStudentName());
                    singleProgram.put("duration", program.getDuration());
                    singleProgram.put("mediaSource", program.getMediaSource());
                    singleProgram.put("profilePic", program.getProfilePic());
                    singleProgram.put("creationDate", program.getCreationDate());
//                    playlistPrograms.put(singleProgram);
                    playlistPrograms.put(i, singleProgram);
                }

                singlePlaylist.put("name", playlist.getName());
                singlePlaylist.put("programs", playlistPrograms);
                jsonArray.put(i, singlePlaylist);
                i++;
            }
            out.write(jsonArray.toString());
            out.flush();
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}

