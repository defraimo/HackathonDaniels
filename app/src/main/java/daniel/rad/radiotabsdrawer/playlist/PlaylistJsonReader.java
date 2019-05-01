package daniel.rad.radiotabsdrawer.playlist;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import daniel.rad.radiotabsdrawer.programs.ProgramsData;

public class PlaylistJsonReader extends AsyncTask<Void, Void, ArrayList<Playlist>> {
    public ArrayList<Playlist> playlists;
    private WeakReference<Context> contextWeakReference;
    private WeakReference<ProgressBar> progressBarWeakReference;
    public JsonReaderInterface dataPasser = null;

    public PlaylistJsonReader(ProgressBar progressBar, Context context) {
        progressBarWeakReference = new WeakReference<>(progressBar);
        contextWeakReference = new WeakReference<>(context);
    }

    public PlaylistJsonReader() {
    }

    @Override
    protected ArrayList<Playlist> doInBackground(Void... voids) {
        playlists = new ArrayList<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference userName = FirebaseDatabase.getInstance().
                    getReference("userPlaylists").child(user.getUid());


            userName.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean isFav = false;

                    //reading the playlist:
                    for (DataSnapshot playlist : dataSnapshot.getChildren()) {
                        ArrayList<ProgramsData> buildPrograms = new ArrayList<>();
                        for (DataSnapshot program : playlist.getChildren()) {
                            ProgramsData programsData = program.getValue(ProgramsData.class);
                            buildPrograms.add(programsData);
                        }

                        //checks if the user has a fav playlist in the database:
                        if (playlist.getKey() != null && playlist.getKey().equals("מועדפים"))
                            isFav = true;

                        playlists.add(new Playlist(playlist.getKey(), buildPrograms));
                    }

                    //creates a fav playlist if one doesn't exist yet:
                    if (!isFav) {
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

}





