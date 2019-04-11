package daniel.rad.radiotabsdrawer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import daniel.rad.radiotabsdrawer.myMediaPlayer.ProgramsReceiver;
import daniel.rad.radiotabsdrawer.myMediaPlayer.service.contentcatalogs.MusicLibrary;
import daniel.rad.radiotabsdrawer.playlist.Playlist;
import daniel.rad.radiotabsdrawer.playlist.PlaylistJsonReader;
import daniel.rad.radiotabsdrawer.playlist.PlaylistsJsonWriter;
import daniel.rad.radiotabsdrawer.playlist.chosenPlaylist.CreatePlaylistFragment;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView gifImageView = findViewById(R.id.ivLoadingGif);
        Glide.with(this).
                asGif().
                load(R.drawable.loading_icon).
                into(gifImageView);

        //asking for permission to write the file:
        //todo: check why the app doesn't wait for permission and continues
        String writingToDisk = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{writingToDisk}, 1);
        }
        //checks if file json of playlists exists, and if not creates one:
        String path = "/data/data/" + getPackageName();
        File filePath = new File(path, "playlists.json");
        if (!filePath.exists()) {
            ArrayList<Playlist> playlists = new ArrayList<>();
            playlists.add(new Playlist("מועדפים", new ArrayList<>()));
            playlists.add(new Playlist("מומלצים", new ArrayList<>()));
            new PlaylistsJsonWriter(playlists, this).execute();
        } else {
            PlaylistsJsonWriter.isLoaded = true;
            new ProgramsReceiver(this).execute();
        }


        getSupportActionBar().hide();
    }
    private void checkIfAlreadyLogged(){
        SharedPreferences sharedPreferences = getSharedPreferences("userName",MODE_PRIVATE);
        String name1 = sharedPreferences.getString("userName1", null);
        if (name1 == null){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
