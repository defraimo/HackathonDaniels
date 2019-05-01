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
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import daniel.rad.radiotabsdrawer.admin.notificationManager.MyNotificationsService;
import daniel.rad.radiotabsdrawer.login.LoginActivity;
import daniel.rad.radiotabsdrawer.login.User;
import daniel.rad.radiotabsdrawer.myMediaPlayer.BroadcastsJson;
import daniel.rad.radiotabsdrawer.myMediaPlayer.ProgramsReceiver;
import daniel.rad.radiotabsdrawer.profile.ProfileActivity;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;

public class DrawerActivity extends AppCompatActivity {

    FragmentManager fm;
    FrameLayout player_frame;
    Button btnMyStreams;
    Button btnLogOut;
    TextView tvUserId;
    ImageView ivProfile;
    User currentUser;
    ProgressBar pbLoadingPic;

    boolean fromManager = false;

    private DatabaseReference users =
            FirebaseDatabase.getInstance()
                    .getReference("Users");

    private DatabaseReference broadcastingUsers =
            FirebaseDatabase.getInstance()
                    .getReference("BroadcastingUsers");

    private DatabaseReference notifications =
            FirebaseDatabase.getInstance()
                    .getReference("Notifications");

    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private List<ProgramsData> usersPrograms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        passingProgramFromProfile();

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

        tvUserId = findViewById(R.id.tvUserId);
        ivProfile = findViewById(R.id.ivProfile);
        pbLoadingPic = findViewById(R.id.pbLoadingPic);

        //start the notification server
        notifications.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (fromManager) {
                    startService(new Intent(DrawerActivity.this, MyNotificationsService.class));
                }
                else {
                    fromManager = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            String email = user.getEmail();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();

            users.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(User.class);
                    tvUserId.setText(currentUser.getUsername());
                    pbLoadingPic.setVisibility(View.VISIBLE);

                    storageRef.child("images/"+user.getUid()).
                            getDownloadUrl().addOnSuccessListener(uri -> {
                                pbLoadingPic.setVisibility(View.INVISIBLE);
                                currentUser.setUriPic(uri);
                                Glide.with(getApplicationContext()).load(uri).into(ivProfile);

                            }).addOnFailureListener(e -> {
                                pbLoadingPic.setVisibility(View.INVISIBLE);
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            List<ProgramsData> programs = ProgramsReceiver.getPrograms();
            for (ProgramsData program : programs) {
                broadcastingUsers.child(program.getVodId()).
                        addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        StringBuilder students = new StringBuilder();
                        int i = 0;
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            User user = child.getValue(User.class);
                            students.append(user.getUsername());
                            i++;
                            if (i < dataSnapshot.getChildrenCount())
                                students.append(", ");
                        }
                        String broadcastingStudents = students.toString();
                        program.setStudentName(broadcastingStudents);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            btnMyStreams.setVisibility(View.INVISIBLE);
            btnMyStreams.setClickable(false);
            usersPrograms = new ArrayList<>();
            for (ProgramsData program : programs) {
                broadcastingUsers.child(program.getVodId()).
                        addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    User userValue = child.getValue(User.class);
                                    if (userValue.getUserID().equals(user.getUid())){
                                        btnMyStreams.setVisibility(View.VISIBLE);
                                        btnMyStreams.setClickable(true);
                                        return;
                                    }
                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
            }
        }
    }

    private void passingProgramFromProfile() {
        ProgramsData programFromProfile = getIntent().getParcelableExtra("program");
        Intent intent = new Intent("currentProgram");
        intent.putExtra("program",programFromProfile);
        LocalBroadcastManager.getInstance(DrawerActivity.this).sendBroadcast(intent);
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
                        stopService(new Intent(DrawerActivity.this, MyNotificationsService.class));
                        FirebaseAuth.getInstance().signOut();
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
