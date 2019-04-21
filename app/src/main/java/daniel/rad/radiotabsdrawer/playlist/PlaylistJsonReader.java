package daniel.rad.radiotabsdrawer.playlist;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;

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

            Gson gson = new Gson();
            Playlist[] playlists = gson.fromJson(mResponse, Playlist[].class);

            buildPlaylists.addAll(Arrays.asList(playlists));

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




