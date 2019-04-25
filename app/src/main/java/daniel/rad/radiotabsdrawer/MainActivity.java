package daniel.rad.radiotabsdrawer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import daniel.rad.radiotabsdrawer.login.LoginActivity;
import daniel.rad.radiotabsdrawer.myMediaPlayer.ProgramsReceiver;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkIfAlreadyLogged();

        //TODO: move to LoginActivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    new ProgramsReceiver(this).execute();
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
