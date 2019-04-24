package daniel.rad.radiotabsdrawer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;

import daniel.rad.radiotabsdrawer.login.LoginActivity;
import daniel.rad.radiotabsdrawer.myMediaPlayer.ProgramsReceiver;
import daniel.rad.radiotabsdrawer.playlist.Playlist;
import daniel.rad.radiotabsdrawer.playlist.PlaylistJsonReader;
import daniel.rad.radiotabsdrawer.playlist.PlaylistsJsonWriter;

public class MainActivity extends AppCompatActivity {
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkIfAlreadyLogged();

        //TODO: move to LoginActivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            //we are connected to a network
//            //checks if username exists in db, and if not creates one:
//            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("userPlaylists");
//            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot snapshot) {
//                    if (!snapshot.hasChild("Fraimo@gmail_com")) {
//                        ArrayList<Playlist> playlists = new ArrayList<>();
//                        playlists.add(new Playlist("מועדפים", new ArrayList<>()));
//                        playlists.add(new Playlist("מומלצים", new ArrayList<>()));
//                        new PlaylistsJsonWriter(playlists, context).execute();
//                    } else {
//                        PlaylistsJsonWriter.isLoaded = true;
//                        new ProgramsReceiver(MainActivity.this).execute();
//                    }
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
            //todo: check why the app doesn't wait for permission and continues
//            String writingToDisk = Manifest.permission.WRITE_EXTERNAL_STORAGE;
//
//            if (ActivityCompat.checkSelfPermission(
//                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{writingToDisk}, 1);
//            }
            //checks if file json of playlists exists, and if not creates one:
//            String path = "/data/data/" + getPackageName();
//            File filePath = new File(path, "playlists.json");
//            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//            if (user != null) {
//            if (!filePath.exists()) {
//                    ArrayList<Playlist> playlists = new ArrayList<>();
//                    playlists.add(new Playlist("מועדפים", new ArrayList<>()));
//                    playlists.add(new Playlist("מומלצים", new ArrayList<>()));
////                    PlaylistJsonReader.fromFile = true;
//                    new PlaylistsJsonWriter(playlists, this,user.getUid()).execute();
//                } else {
//                    PlaylistsJsonWriter.isLoaded = true;
                    new ProgramsReceiver(this).execute();
//                }
//            }

        } else {
            //there is no connection
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.
                    setTitle("אין חיבור אינטרנט").
                    setMessage("אנא בדוק את החיבור ונסה שנית").
                    setPositiveButton("הבנתי", (dialog, which) -> {
                        startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                    }).
                    show();
        }

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


        getSupportActionBar().hide();
    }

    private void checkIfAlreadyLogged() {
        SharedPreferences sharedPreferences = getSharedPreferences("userName", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", null);
        if (name == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
