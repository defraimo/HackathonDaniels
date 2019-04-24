package daniel.rad.radiotabsdrawer.playlist;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import java.util.HashMap;
import java.util.Iterator;

import daniel.rad.radiotabsdrawer.MainActivity;
import daniel.rad.radiotabsdrawer.myMediaPlayer.ProgramsReceiver;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;

public class PlaylistJsonReader extends AsyncTask<Void, Void, ArrayList<Playlist>> {
    public ArrayList<Playlist> playlists;
    private WeakReference<Context> contextWeakReference;
    private WeakReference<ProgressBar> progressBarWeakReference;
    public JsonReaderInterface dataPasser = null;
//    public static boolean fromFile = false;


    public PlaylistJsonReader(Context context, ArrayList<Playlist> playlists, ProgressBar progressBar) {
        contextWeakReference = new WeakReference<>(context);
        this.playlists = playlists;
        this.progressBarWeakReference = new WeakReference<>(progressBar);
    }

    public PlaylistJsonReader(ProgressBar progressBar, Context context) {
        progressBarWeakReference = new WeakReference<>(progressBar);
        contextWeakReference = new WeakReference<>(context);
    }

    public PlaylistJsonReader() {
    }

    @Override
    protected ArrayList<Playlist> doInBackground(Void... voids) {
        Context context = contextWeakReference.get();
        playlists = new ArrayList<>();

        ArrayList<Playlist> buildPlaylists;
//        System.out.println("from file?" + fromFile);
//        if (fromFile) {
//            buildPlaylists = new ArrayList<>(Arrays.asList(readFromFile(context)));
//        } else {
//            buildPlaylists = new ArrayList<>();
//        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference userName = FirebaseDatabase.getInstance().
                    getReference("userPlaylists").child(user.getUid());


            userName.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean isFav = false;
                    for (DataSnapshot playlist : dataSnapshot.getChildren()) {
                        ArrayList<ProgramsData> buildPrograms = new ArrayList<>();
                        for (DataSnapshot program : playlist.getChildren()) {
                            ProgramsData programsData = program.getValue(ProgramsData.class);
                            buildPrograms.add(programsData);
                        }
                        if(playlist.getKey() != null && playlist.getKey().equals("מועדפים"))isFav = true;

                        playlists.add(new Playlist(playlist.getKey(), buildPrograms));
                    }
                    if(!isFav){
                        playlists.add(new Playlist("מועדפים", new ArrayList<>()));
                    }

                    dataPasser.passData(playlists);
                    ProgressBar pb = progressBarWeakReference.get();
                    if (pb == null) return;
                    pb.setVisibility(View.GONE);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        }
        return playlists;
    }


    private Playlist[] readFromFile(Context context) {
        String path = "/data/data/" + context.getPackageName();
        File filePath = new File(path, "playlists.json");
        FileInputStream is = null;
        Playlist[] playlists = null;
        try {
            is = new FileInputStream(filePath);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String mResponse = new String(buffer);


            Gson gson = new Gson();
            playlists = gson.fromJson(mResponse, Playlist[].class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playlists;
    }


}





