package daniel.rad.radiotabsdrawer.playlist;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import daniel.rad.radiotabsdrawer.myMediaPlayer.ProgramsReceiver;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;


public class PlaylistsDataSource {
    public ArrayList<Playlist> playlists;

    public PlaylistsDataSource(ArrayList<Playlist> playlists) {
        this.playlists = playlists;
    }

    public PlaylistsDataSource() {
    }

    @SuppressLint("StaticFieldLeak")
    public void writeDB(Context context, ArrayList<Playlist> playlists) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                //method to connect to cloudant database
//                dbAccess();
                JSONArray jsonArray = new JSONArray();
                JSONObject singlePlaylist = new JSONObject();
                JSONArray playlistPrograms = new JSONArray();
                String path = "/data/data/" + context.getPackageName();
                File filePath = new File(path, "playlists.json");

                //checking for writing permission:
                String writingToDisk = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                if (ActivityCompat.checkSelfPermission(
                        context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{writingToDisk}, 1);
                }

                try (FileWriter out = new FileWriter(filePath)) {
                    System.out.println();
                    for (Playlist playlist : playlists) {
                        for (ProgramsData program : playlist.getProgramsData()) {
                            JSONObject singleProgram = new JSONObject();
                            singleProgram.put("vodId", program.getVodId());
                            singleProgram.put("programName", program.getProgramName());
                            singleProgram.put("studentName", program.getStudentName());
                            singleProgram.put("duration", program.getDuration());
                            singleProgram.put("durationUnit", program.getDurationUnit());
                            singleProgram.put("mediaSource", program.getMediaSource());
                            singleProgram.put("profilePic", program.getProfilePic());
                            singleProgram.put("creationDate", program.getCreationDate());
                            playlistPrograms.put(singleProgram);
                        }

                        singlePlaylist.put("name", playlist.getName());
                        singlePlaylist.put("programs", playlistPrograms);
                        jsonArray.put(singlePlaylist);
                    }
                    out.write(jsonArray.toString());
                    out.flush();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Log.e("write", "doInBackground: data was saved.... ");
                return null;
            }
        }.execute();

    }

    private ArrayList<Playlist> readJsonFile(Context context) throws IOException {
        ArrayList<Playlist> buildPlaylists = new ArrayList<>();
        ArrayList<ProgramsData> buildPrograms = new ArrayList<>();
        String path = "/data/data/" + context.getPackageName();
        File filePath = new File(path, "playlists.json");
        FileInputStream is = new FileInputStream(filePath);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        String mResponse = new String(buffer);
        try {
            JSONArray jsonArray = new JSONArray(mResponse);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String playlistName = jsonObject.getString("name");
                JSONArray playlistPrograms = jsonObject.getJSONArray("programs");
                for (int j = 0; j < playlistPrograms.length(); j++) {
                    JSONObject singleProgram = (JSONObject) playlistPrograms.get(i);
                    String vodId = singleProgram.getString("vodId");
                    String programName = singleProgram.getString("programName");
                    String studentName = singleProgram.getString("studentName");
                    long duration = singleProgram.getLong("duration");
                    TimeUnit durationUnit = (TimeUnit) singleProgram.get("durationUnit");
                    String mediaSource = singleProgram.getString("mediaSource");
                    int profilePic = singleProgram.getInt("profilePic");
                    long creationDate = singleProgram.getLong("creationDate");
                    ProgramsData pd = new ProgramsData(vodId,programName,studentName,duration,mediaSource,profilePic,creationDate);
                    buildPrograms.add(pd);
                }
                Playlist p = new Playlist(playlistName, buildPrograms);
                buildPlaylists.add(p);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return buildPlaylists;
    }

    private void dbAccess() {
        final String TEXT_API_KEY = "soomselasselleforroppeci";
        final String TEXT_API_SECRET = "70340284fc864897911fc4e92f120132b8fe93bd";
        final String DB_USER_NAME = "332c23f3-878c-44a8-bc54-2afd813b684b-bluemix";
        final String DB_NAME_TEXT = "playlists";

        CloudantClient client = ClientBuilder.account(DB_USER_NAME).
                username(TEXT_API_KEY).
                password(TEXT_API_SECRET).
                build();
        //return databases.toString();
        Database db = client.database(DB_NAME_TEXT, false);
    }

    @SuppressLint("StaticFieldLeak")
    private ArrayList<Playlist> readDB() {
//        final String TEXT_API_KEY = "turlethoutentlybovesittl";
//        final String TEXT_API_SECRET = "1ee256d2a05f35fed219d24cac64ab4798a9fa98";
//        final String DB_USER_NAME = "framiframo96@gmail.com";
//        final String DB_NAME_TEXT = "playlists";

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
//                CloudantClient client = ClientBuilder.account(DB_USER_NAME).
//                        username(TEXT_API_KEY).
//                        password(TEXT_API_SECRET).
//                        build();
//                //return databases.toString();
//                Database db = client.database(DB_NAME_TEXT, false);
                String myJson = "";
                //todo: write the reading and parsing json thing

//                List<PlaylistsDataSource> test = db.findByIndex(myJson, PlaylistsDataSource.class);
//                for (PlaylistsDataSource item : test) {
//                    Log.e("check", "checkResult: " + item.playlistName);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                System.out.println("db read ok!");
            }
        }.execute();
        return null;
    }


    public static ArrayList<Playlist> getPlaylists(Context context) {
        PlaylistsDataSource playlistsDataSource = new PlaylistsDataSource();
        ArrayList<Playlist> playlists = new ArrayList<>();


        try {
            playlistsDataSource.readJsonFile(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<ProgramsData> temp = new ArrayList<>();
        playlists.add(new Playlist("מועדפים", temp));
        playlists.add(new Playlist("מומלצים", ProgramsReceiver.getPrograms()));

        return playlists;
    }
}