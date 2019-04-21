package daniel.rad.radiotabsdrawer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
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

import daniel.rad.radiotabsdrawer.login.LoginActivity;
import daniel.rad.radiotabsdrawer.login.User;
import daniel.rad.radiotabsdrawer.myMediaPlayer.ProgramsReceiver;
import daniel.rad.radiotabsdrawer.programs.ProgramsAdapter;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;

public class ProfileActivity extends AppCompatActivity {

    TextView tvDisconnect;
    TextView tvUserName;
    ImageView ivProfilePic;
    User currentUser;
    ProgressBar pbLoadingPic;
    RecyclerView rvStudentsPrograms;
    ProgressBar pbStudentsPrograms;

    private DatabaseReference users =
            FirebaseDatabase.getInstance()
                    .getReference("Users");

    private DatabaseReference broadcastingUsers =
            FirebaseDatabase.getInstance()
                    .getReference("BroadcastingUsers");

    List<ProgramsData> usersPrograms;

    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvDisconnect = findViewById(R.id.tvDisconnect);
        tvUserName = findViewById(R.id.tvUserName);
        ivProfilePic = findViewById(R.id.ivProfilePic);
        pbLoadingPic = findViewById(R.id.pbLoadingPic);
        rvStudentsPrograms = findViewById(R.id.rvStudentsPrograms);
        pbStudentsPrograms = findViewById(R.id.pbStudentsPrograms);

        tvDisconnect.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("האם אתה בטוח שברצונך להתנתק?").
                    setNegativeButton("לא", (dialog, which) -> {}).
                    setPositiveButton("כן",(dialog, which) -> {
                        SharedPreferences SPrefUser = getSharedPreferences("userName",MODE_PRIVATE);
                        SPrefUser.edit().putString("name",null).apply();
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                        finish();
            }).show();
        });

        ProgramsUserAdapter.UserProgramAdapterInterface adapterInterface = chosenProgram -> {
            System.out.println(chosenProgram);

            Intent intent = new Intent("currentProgram");
            intent.putExtra("program",chosenProgram);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            Intent intentDrawer = new Intent(this,DrawerActivity.class);
            startActivity(intentDrawer);
        };

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
                    tvUserName.setText(currentUser.getUsername());
                    pbLoadingPic.setVisibility(View.VISIBLE);

                    storageRef.child("images/"+user.getUid()).
                            getDownloadUrl().addOnSuccessListener(uri -> {
                        pbLoadingPic.setVisibility(View.INVISIBLE);
                        currentUser.setUriPic(uri);
//                                if (currentUser.getUserPic() != null) {
//                                   Glide.with(getApplicationContext()).
//                                        load(Uri.parse(currentUser.getUserPic())).
//                                        into(ivProfile);
//                                }
                        Glide.with(getApplicationContext()).load(uri).into(ivProfilePic);

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("failed downloading pic");
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            usersPrograms = new ArrayList<>();
            List<ProgramsData> allPrograms = ProgramsReceiver.getPrograms();
            pbStudentsPrograms.setVisibility(View.VISIBLE);
            for (ProgramsData program : allPrograms) {
                broadcastingUsers.child(program.getVodId()).
                        addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            User userValue = child.getValue(User.class);
                            if (userValue.getUserID().equals(user.getUid())){
                                usersPrograms.add(program);
                                break;
                            }
                        }
                        rvStudentsPrograms.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        rvStudentsPrograms.setAdapter(new ProgramsUserAdapter(usersPrograms,getApplicationContext(),adapterInterface));
                        pbStudentsPrograms.setVisibility(View.INVISIBLE);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,DrawerActivity.class);
        startActivity(intent);
    }
}
