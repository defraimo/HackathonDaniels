package daniel.rad.radiotabsdrawer;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import daniel.rad.radiotabsdrawer.login.LoginActivity;
import daniel.rad.radiotabsdrawer.myMediaPlayer.BroadcastsJson;
import daniel.rad.radiotabsdrawer.myMediaPlayer.LoadPrograms;
import daniel.rad.radiotabsdrawer.myMediaPlayer.ProgramsReceiver;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;

public class DrawerActivity extends AppCompatActivity {

    FragmentManager fm;
    FrameLayout player_frame;
    Button btnMyStreams;
    Button btnLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        TabLayout tabLayout = findViewById(R.id.tabs_in_drawer);
        ViewPager viewPager = findViewById(R.id.view_pager_drawer);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        player_frame = findViewById(R.id.player_frame);

        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.player_frame,new MediaPlayerFragment()).commit();
        onBtnClicked();

    }

    private void passArgs() {
        Intent intent = getIntent();
        ProgramsData programsData = intent.getParcelableExtra("programIndex");
        Bundle bundle = new Bundle();
        if (programsData != null) {
            bundle.putParcelable("programIndex", programsData);
            MediaPlayerFragment playerFragment = new MediaPlayerFragment();
            playerFragment.setArguments(bundle);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void onBtnClicked(){
        btnMyStreams = findViewById(R.id.btnMyStreams);
        btnLogOut = findViewById(R.id.btnLogOut);

        btnMyStreams.setOnClickListener((v)->{
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });

        btnLogOut.setOnClickListener((v)->{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("האם אתה בטוח שברצונך להתנתק?").
                    setNegativeButton("לא", (dialog, which) -> {}).
                    setPositiveButton("כן",(dialog, which) -> {
                        Intent loggedOut = new Intent("loggedOut");
                        loggedOut.putExtra("loginStatus",false);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(loggedOut);
                        SharedPreferences SPrefUser = getSharedPreferences("userName",MODE_PRIVATE);
                        SPrefUser.edit().putString("name",null).apply();
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }).show();
        });

    }

    private void downloadJson() {
        String writingToDisk = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (ActivityCompat.checkSelfPermission(
                this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{writingToDisk},1);

            //can't continue
            return;
        }
        new BroadcastsJson().execute();
        new ProgramsReceiver().execute();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            //call the method that requires
            downloadJson();
    }
}
