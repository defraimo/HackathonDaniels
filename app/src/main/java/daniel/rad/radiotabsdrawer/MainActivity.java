package daniel.rad.radiotabsdrawer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.ref.WeakReference;
import java.util.List;

import daniel.rad.radiotabsdrawer.myMediaPlayer.ProgramsReceiver;
import daniel.rad.radiotabsdrawer.myMediaPlayer.service.contentcatalogs.MusicLibrary;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ProgramsReceiver(this).execute();
        getSupportActionBar().hide();
    }
}
