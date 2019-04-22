package daniel.rad.radiotabsdrawer.programs;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class ChosenProgramFragment extends Fragment {

    TextView tvChosenStudentName;
    TextView tvChosenProgramName;
    RecyclerView rvChosenComments;
    ImageView ivAddToFav;
    ImageView ivShare;
    ImageView ivComment;
    EditText etComment;

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

    public static ChosenProgramFragment newInstance(ProgramsData programsData) {
        Bundle args = new Bundle();
        args.putParcelable("model", programsData);
        ChosenProgramFragment fragment = new ChosenProgramFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chosen_program, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if ( getArguments() != null) {
            //receiving the data we passed with Parcelable:
           ProgramsData model =  getArguments().getParcelable("model");

           //defining the elements in the fragment:
            tvChosenStudentName = view.findViewById(R.id.tvChosenStudentName);
            tvChosenProgramName = view.findViewById(R.id.tvChosenProgramName);
            rvChosenComments = view.findViewById(R.id.rvChosenComments);
            ivAddToFav = view.findViewById(R.id.ivAddToFav);
            ivShare = view.findViewById(R.id.ivShare);
            ivComment = view.findViewById(R.id.ivComment);
            etComment = view.findViewById(R.id.etComment);

            rvChosenComments.setLayoutManager(new LinearLayoutManager(getContext()));

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            ivComment.setOnClickListener(v -> {
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

                                    if (etComment.getText().toString().equals("")){
                                        etComment.setError("חובה להזין טקסט");
                                    }
                                    else {
                                        String comment = etComment.getText().toString();
                                        etComment.setText("");
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
                    rvChosenComments.setAdapter(new CommentsAdapter(comments,getContext()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            tvChosenStudentName.setText(model.getStudentName());
            tvChosenStudentName.setSelected(true);
            tvChosenProgramName.setText(model.getProgramName());
            tvChosenProgramName.setSelected(true);

            ivShare.setOnClickListener(v -> {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, "מוזמנ/ת להקשיב לתוכנית - "+model.getProgramName()+" של "+model.getStudentName()+"! *קישור כניסה לאפליקצייה*");

                startActivity(Intent.createChooser(share, "היכן תרצה לשתף את התוכנית?"));
            });
        }
    }
}
