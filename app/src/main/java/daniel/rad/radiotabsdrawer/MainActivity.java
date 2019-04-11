package daniel.rad.radiotabsdrawer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import daniel.rad.radiotabsdrawer.myMediaPlayer.ProgramsReceiver;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        checkIfAlreadyLogged();

        ImageView gifImageView = findViewById(R.id.ivLoadingGif);

        Glide.with(this).
                asGif().
                load(R.drawable.loading_icon).
                into(gifImageView);

        new ProgramsReceiver(this).execute();
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
