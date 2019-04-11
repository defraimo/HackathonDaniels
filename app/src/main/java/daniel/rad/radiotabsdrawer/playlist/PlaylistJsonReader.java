package daniel.rad.radiotabsdrawer.playlist;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import daniel.rad.radiotabsdrawer.MainActivity;
import daniel.rad.radiotabsdrawer.myMediaPlayer.ProgramsReceiver;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;

public class PlaylistJsonReader extends AsyncTask<Void, Void, ArrayList<Playlist>> {
    public ArrayList<Playlist> playlists;
    private WeakReference<Context> contextWeakReference;
    private WeakReference<ProgressBar> progressBarWeakReference;
    public JsonReaderInterface dataPasser = null;

    public PlaylistJsonReader(Context context, ArrayList<Playlist> playlists, ProgressBar progressBar) {
        contextWeakReference = new WeakReference<>(context);
        this.playlists = playlists;
        this.progressBarWeakReference = new WeakReference<>(progressBar);
    }
    public PlaylistJsonReader(ProgressBar progressBar, Context context){
        progressBarWeakReference = new WeakReference<>(progressBar);
        contextWeakReference = new WeakReference<>(context);
    }

    public PlaylistJsonReader() {
    }


    @Override
    protected ArrayList<Playlist> doInBackground(Void... voids) {
        Context context = contextWeakReference.get();
        ArrayList<Playlist> buildPlaylists = new ArrayList<>();
        try {
            ArrayList<ProgramsData> buildPrograms = new ArrayList<>();
            String path = "/data/data/" + context.getPackageName();
            File filePath = new File(path, "playlists.json");
            FileInputStream is = new FileInputStream(filePath);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String mResponse = new String(buffer);

            JSONArray jsonArray = new JSONArray(mResponse);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String playlistName = jsonObject.getString("name");
                JSONArray playlistPrograms = jsonObject.getJSONArray("programsData");
                for (int j = 0; j < playlistPrograms.length(); j++) {
                    JSONObject singleProgram = (JSONObject) playlistPrograms.get(i);
                    String vodId = singleProgram.getString("vodId");
                    String programName = singleProgram.getString("programName");
                    String studentName = singleProgram.getString("studentName");
                    long duration = singleProgram.getLong("duration");
                    String mediaSource = singleProgram.getString("mediaSource");
                    int profilePic = singleProgram.getInt("profilePic");
                    long creationDate = singleProgram.getLong("creationDate");
                    ProgramsData pd = new ProgramsData(vodId, programName, studentName, duration, mediaSource, profilePic, creationDate);
                    buildPrograms.add(pd);
                }
                Playlist p = new Playlist(playlistName, buildPrograms);
                buildPlaylists.add(p);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buildPlaylists;
    }

    @Override
    protected void onPostExecute(ArrayList<Playlist> playlists) {
//        recyclerViewWeakReference.get().setAdapter(new PlaylistAdapter(playlists, contextWeakReference.get()));
        dataPasser.passData(playlists);
        ProgressBar pb = this.progressBarWeakReference.get();
        if(pb ==null)return;
        pb.setVisibility(View.GONE);


    }

}





