package daniel.rad.radiotabsdrawer.playlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import daniel.rad.radiotabsdrawer.programs.ProgramsData;
import daniel.rad.radiotabsdrawer.programs.ProgramsDataSource;


public class PlaylistsDataSource {
    public String playlistName;
    public ArrayList<ProgramsData> programsList;


    public PlaylistsDataSource(String playlistName, ArrayList<ProgramsData> programsList) {
        this.playlistName = playlistName;
        this.programsList = programsList;
    }

    public PlaylistsDataSource() {}

    @SuppressLint("StaticFieldLeak")
    public void writeDB(Context context, String name, ArrayList<ProgramsData> list) {
        final String TEXT_API_KEY = "turlethoutentlybovesittl";
        final String TEXT_API_SECRET = "1ee256d2a05f35fed219d24cac64ab4798a9fa98";
        final String DB_USER_NAME = "29e30966-c6c3-45c2-92bf-b07a8f189feb-bluemix";
        final String DB_NAME_TEXT = "playlists";

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                CloudantClient client = ClientBuilder.account(DB_USER_NAME).
                        username(TEXT_API_KEY).
                        password(TEXT_API_SECRET).
                        build();
                //return databases.toString();
                Database db = client.database(DB_NAME_TEXT, false);

                // A Java type that can be serialized to JSON
//                Gson gson = new Gson();
//                String playlist = gson.toJson(new PlaylistsDataSource(name, list));
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                try {
                    jsonObject.put("Name:",name);
                    for (ProgramsData program : list) {
                        jsonArray.put(program);
                    }
                    jsonObject.put("Programs:", jsonArray);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                db.save(jsonObject);
                Log.e("write", "doInBackground: cloudant data was saved.... " );
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Toast.makeText(context, "Data Ok", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }
    @SuppressLint("StaticFieldLeak")
    private void readDB(Context context) {
        final String TEXT_API_KEY = "turlethoutentlybovesittl";
        final String TEXT_API_SECRET = "1ee256d2a05f35fed219d24cac64ab4798a9fa98";
        final String DB_USER_NAME = "framiframo96@gmail.com";
        final String DB_NAME_TEXT = "playlists";

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                CloudantClient client = ClientBuilder.account(DB_USER_NAME).
                        username(TEXT_API_KEY).
                        password(TEXT_API_SECRET).
                        build();
                //return databases.toString();
                Database db = client.database(DB_NAME_TEXT, false);

                Log.e("read", "doInBackground: cloudant data was saved.... " );
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Toast.makeText(context, "Data Ok", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }


    public static ArrayList<Playlist> getPlaylists() {
        ArrayList<Playlist> playlists = new ArrayList<>();
        ArrayList<String> playlistsNames = new ArrayList<>();
        ArrayList<ProgramsData> dataArrayListFavorite = new ArrayList<>();
        ArrayList<ProgramsData> dataArrayList = ProgramsDataSource.getPrograms();

        dataArrayListFavorite.add(dataArrayList.get(0));

        playlists.add(new Playlist("מועדפים", dataArrayList));
        playlists.add(new Playlist("מומלצים", dataArrayListFavorite));

        return playlists;
    }
}