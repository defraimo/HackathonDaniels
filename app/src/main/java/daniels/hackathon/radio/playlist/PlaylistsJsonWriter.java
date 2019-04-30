package daniels.hackathon.radio.playlist;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.ref.WeakReference;

import daniels.hackathon.radio.R;
import daniels.hackathon.radio.playlist.removeProgramsFromPlaylist.RemoveProgramFromPlaylistFragment;
import daniels.hackathon.radio.programs.ProgramsData;


public class PlaylistsJsonWriter extends AsyncTask<Void, Void, Void> {
    public Playlist playlist;
    public ProgramsData program;
    public WeakReference<Context> contextWeakReference;
    private String previousName;

    private int choice;
    public static final int CREATE_PLAYLIST = 1;
    public static final int DELETE_PLAYLIST = 2;
    public static final int ADD_PROGRAM = 3;
    public static final int REMOVE_PROGRAM = 4;
    public static final int ADD_TO_FAVS = 5;
    public static final int REMOVE_FROM_FAVS = 6;
    public static final int RECOMMENDED_PLAYLIST = 7;

    public PlaylistsJsonWriter(Playlist playlist, Context context, int choice) {
        this.playlist = playlist;
        contextWeakReference = new WeakReference<>(context);
        this.choice = choice;
    }
    public PlaylistsJsonWriter(Playlist playlist, ProgramsData program, Context context, int choice) {
        this.program = program;
        this.playlist = playlist;
        contextWeakReference = new WeakReference<>(context);
        this.choice = choice;
    }
    public PlaylistsJsonWriter(ProgramsData program, Context context, int choice) {
        this.program = program;
        contextWeakReference = new WeakReference<>(context);
        this.choice = choice;
    }
    public PlaylistsJsonWriter(String previousName, Playlist playlist, Context context, int choice) {
        this.playlist = playlist;
        contextWeakReference = new WeakReference<>(context);
        this.choice = choice;
        this.previousName = previousName;
    }
    public PlaylistsJsonWriter() {
    }

    @Override
    protected Void doInBackground(Void... voids) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            DatabaseReference userPlaylists = FirebaseDatabase.getInstance().
                    getReference("userPlaylists");
            DatabaseReference userName = userPlaylists.child(user.getUid());

            switch (choice) {
                case CREATE_PLAYLIST:
                    createPlaylist(userName);
                    break;
                case DELETE_PLAYLIST:
                    deletePlaylist(userName);
                    break;
                case ADD_PROGRAM:
                    addProgram(userName);
                    break;
                case REMOVE_PROGRAM:
                    removeProgram(userName);
                    break;
                case ADD_TO_FAVS:
                    addToFavs(userName);
                    break;
                case REMOVE_FROM_FAVS:
                    removeFromFavs(userName);
                    break;
                case RECOMMENDED_PLAYLIST:
                    writeRecommended(userName);
                    break;
                default:
                    System.out.println("Nothing chosen");
                    break;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (choice == REMOVE_PROGRAM) {
            AppCompatActivity activity = (AppCompatActivity) contextWeakReference.get();
            activity.getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.playlist_frame, RemoveProgramFromPlaylistFragment.newInstance(playlist)).
                    commit();
        } else if(choice == RECOMMENDED_PLAYLIST) {
            //not supposed to do anything, made to write the recommended playlist and continue.
        }else{
            AppCompatActivity activity = (AppCompatActivity) contextWeakReference.get();
            activity.getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.playlist_frame, AllPlaylistsFragment.newInstance(playlist)).
                    commit();
        }
    }

    private void removeProgram(DatabaseReference userName) {
        userName.child(playlist.getName()).child(program.getVodId()).removeValue();
    }

    private void addToFavs(DatabaseReference userName) {
        DatabaseReference name = userName.child("מועדפים");
        name.child(program.getVodId()).setValue(program);
    }

    private void removeFromFavs(DatabaseReference userName) {
        DatabaseReference name = userName.child("מועדפים");
        name.child(program.getVodId()).removeValue();
    }

    private void addProgram(DatabaseReference userName) {
        userName.child(previousName).removeValue();
        DatabaseReference name = userName.child(playlist.getName());
        for (ProgramsData programsData : playlist.getProgramsData()) {
            name.child(programsData.getVodId()).setValue(programsData);
        }

    }

    private void deletePlaylist(DatabaseReference userName) {
        userName.child(playlist.getName()).removeValue();
    }

    private void createPlaylist(DatabaseReference userName) {
        for (ProgramsData programsDatum : playlist.getProgramsData()) {
            userName.child(playlist.getName()).child(programsDatum.getVodId()).setValue(programsDatum);
        }
    }

    private void writeRecommended(DatabaseReference userName) {
        //deletes previous version of recommended:
        userName.child(playlist.getName()).removeValue();

        //write the new version:
        for (ProgramsData programsDatum : playlist.getProgramsData()) {
            userName.child(playlist.getName()).child(programsDatum.getVodId()).setValue(programsDatum);
        }
    }
}

