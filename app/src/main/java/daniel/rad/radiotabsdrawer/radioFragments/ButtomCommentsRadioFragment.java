package daniel.rad.radiotabsdrawer.radioFragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.login.User;
import daniel.rad.radiotabsdrawer.programs.CommentsAdapter;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class ButtomCommentsRadioFragment extends Fragment {

    ImageView ivBack;
    EditText etRadioComment;
    ImageView ivRadioComment;
    RecyclerView rvRadioComments;

    private DatabaseReference programComments =
            FirebaseDatabase.getInstance()
                    .getReference("ProgramComments");

    private DatabaseReference users =
            FirebaseDatabase.getInstance()
                    .getReference("Users");
    private User currentUser;

    List<String> comments = new ArrayList<>();

    public static long counter = 0;
    private boolean fromUser;
    private ProgramsData model;

    public ButtomCommentsRadioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buttom_comments_radio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivBack = view.findViewById(R.id.ivBack);
        etRadioComment = view.findViewById(R.id.etRadioComment);
        ivRadioComment = view.findViewById(R.id.ivRadioComment);
        rvRadioComments = view.findViewById(R.id.rvRadioComments);

        ivBack.setOnClickListener(v -> {
//            getFragmentManager().
//                    beginTransaction().
//                    replace(R.id.buttom_frame,new RadioButtomFragment()).
//                    commit();
        });

        rvRadioComments.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ivRadioComment.setOnClickListener(v -> {
            if (user != null){
                String uid = user.getUid();

                fromUser = true;

                programComments.child(model.getVodId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!fromUser) return;

                        counter = dataSnapshot.getChildrenCount();

                        fromUser = false;

                        users.child(uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                currentUser = dataSnapshot.getValue(User.class);

                                if (etRadioComment.getText().toString().equals("")){
                                    etRadioComment.setError("חובה להזין טקסט");
                                }
                                else {
                                    String comment = etRadioComment.getText().toString();
                                    String fullComment = currentUser.getUsername()+": "+comment;
                                    programComments.child(model.getVodId()).child(""+(counter+1)).setValue(fullComment);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        programComments.child(model.getVodId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comments.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String comment = (String) child.getValue();
                    if (!comment.equals("")){
                        comments.add(comment);
                    }
                }
                rvRadioComments.setAdapter(new CommentsAdapter(comments,getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            model = intent.getParcelableExtra("program");
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver,new IntentFilter("currentProgram"));
    }
}
