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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.myMediaPlayer.ProgramsReceiver;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;

/**
 * A simple {@link Fragment} subclass.
 */
public class RadioButtomFragment extends Fragment {

    ImageView ivLike;
    ImageView ivComment;
    ImageView ivShare;
    boolean liked;
    boolean fromUser = true;
    ProgramsData program;

    List<ProgramsData> programs;

    ProgramsData likedProgram;

    private DatabaseReference programLikes =
            FirebaseDatabase.getInstance()
                    .getReference("ProgramLikes");

    public RadioButtomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_radio_buttom, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivLike = view.findViewById(R.id.ivLike);
        ivComment = view.findViewById(R.id.ivComment);
        ivShare = view.findViewById(R.id.ivShare);

        programs = ProgramsReceiver.getPrograms();

        ivLike.setOnClickListener(v -> {
            if (!liked) {
                programLikes
                        .child(program.getVodId())
                        .child("radshun")
                        .setValue(1);
                ivLike.setImageResource(R.drawable.ic_like_red);
                liked = true;
            }
            else {
                programLikes
                        .child(program.getVodId())
                        .child("radshun")
                        .setValue(0);
                ivLike.setImageResource(R.drawable.ic_like_grey);
                liked = false;
            }
        });

        ivComment.setOnClickListener(v -> {
            getFragmentManager().
                    beginTransaction().
                    addToBackStack("radioHomePage").
                    replace(R.id.buttom_frame,new ButtomCommentsRadioFragment()).
                    commit();
        });

        ivShare.setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, "מוזמנ/ת להקשיב לתוכנית *שם התוכנית* של *שם התלמיד*! *קישור כניסה לאפליקצייה*");

            startActivity(Intent.createChooser(share, "היכן תרצה לשתף את התוכנית?"));
        });
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String programName = intent.getStringExtra("programName");
            for (ProgramsData programsData : programs) {
                if (programsData.getProgramName().equals(programName)){
                    program = programsData;
                    break;
                }
            }

            programLikes.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!fromUser) return;

                    Long o = (Long) dataSnapshot.child(program.getVodId()).child("radshun").getValue();

                    int value = o != null ? o.intValue(): 0;
                    if (value == 0){
                        ivLike.setImageResource(R.drawable.ic_like_grey);
                        liked = false;
                    }
                    else if (value == 1){
                        ivLike.setImageResource(R.drawable.ic_like_red);
                        liked = true;
                    }

                    fromUser = false;
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    };

    BroadcastReceiver likesReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            likedProgram = intent.getParcelableExtra("program");
            fromUser = true;
            programLikes.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!fromUser) return;

                    Long o = (Long) dataSnapshot.child(likedProgram.getVodId()).child("radshun").getValue();

                    int value = o != null ? o.intValue(): 0;
                    if (value == 0){
                        ivLike.setImageResource(R.drawable.ic_like_grey);
                        liked = false;
                    }
                    else if (value == 1){
                        ivLike.setImageResource(R.drawable.ic_like_red);
                        liked = true;
                    }

                    fromUser = false;
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(likesReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver,new IntentFilter("currentPlayingRadio"));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(likesReceiver,new IntentFilter("currentLikePressed"));
    }
}
