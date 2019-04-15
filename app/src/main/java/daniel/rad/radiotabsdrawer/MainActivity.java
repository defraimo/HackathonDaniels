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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import daniel.rad.radiotabsdrawer.login.LoginActivity;
import daniel.rad.radiotabsdrawer.myMediaPlayer.ProgramsReceiver;
import daniel.rad.radiotabsdrawer.playlist.Playlist;
import daniel.rad.radiotabsdrawer.playlist.PlaylistsJsonWriter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkIfAlreadyLogged();

        //TODO: move to LoginActivity
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network

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
        }
        else{
            //there is no connection
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.
                    setTitle("אין חיבור אינטרנט").
                    setMessage("אנא בדוק את החיבור ונסה שנית").
                    setPositiveButton("הבנתי", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                        }
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

    private void checkIfAlreadyLogged(){
        SharedPreferences sharedPreferences = getSharedPreferences("userName",MODE_PRIVATE);
        String name = sharedPreferences.getString("name", null);
        if (name == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
