package daniels.hackathon.radio.admin.statisticsManager;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import daniels.hackathon.radio.R;
import daniels.hackathon.radio.myMediaPlayer.ProgramsReceiver;
import daniels.hackathon.radio.programs.ProgramsData;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticsFragment extends Fragment {

    RecyclerView rvStatistics;
    ImageView ivProgramSearch;
    EditText etSearch;
    ImageView ivNumOfPlays;
    ImageView ivNumOfLikes;

    private boolean playsStatsWasChosen = true;

    private DatabaseReference programsNumOfPlays =
            FirebaseDatabase.getInstance()
                    .getReference("ProgramsNumOfPlays");

    public StatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvStatistics = view.findViewById(R.id.rvStatistics);
        ivProgramSearch = view.findViewById(R.id.ivProgramSearch);
        etSearch = view.findViewById(R.id.etSearch);
        ivNumOfPlays = view.findViewById(R.id.ivNumOfPlays);
        ivNumOfLikes = view.findViewById(R.id.ivNumOfLikes);

        rvStatistics.setLayoutManager(new LinearLayoutManager(getContext()));
//                List<ProgramsData> sortedPrograms = sortPrograms(ProgramsReceiver.getPrograms());
        LikesStatisticsAdapter likesAdapter = new LikesStatisticsAdapter(ProgramsReceiver.getPrograms(), getContext());
        PlaysStatisticsAdapter playsAdapter = new PlaysStatisticsAdapter(ProgramsReceiver.getPrograms(), getContext());
        rvStatistics.setAdapter(playsAdapter);

        ivNumOfPlays.setOnClickListener(v -> {
            if (!playsStatsWasChosen) {
                rvStatistics.setAdapter(playsAdapter);
                ivNumOfPlays.setImageResource(R.drawable.ic_choose_right);
                ivNumOfLikes.setImageResource(R.drawable.ic_choose_left_unpressed);
                playsStatsWasChosen = true;
            }
        });

        ivNumOfLikes.setOnClickListener(v -> {
            if (playsStatsWasChosen) {
                rvStatistics.setAdapter(likesAdapter);
                ivNumOfLikes.setImageResource(R.drawable.ic_choose_left);
                ivNumOfPlays.setImageResource(R.drawable.ic_choose_right_unpressed);
                playsStatsWasChosen = false;
            }
        });

        ivProgramSearch.setOnClickListener((v)->{
            ArrayList<ProgramsData> newList = new ArrayList<>();
            ArrayList<ProgramsData> allPrograms = (ArrayList<ProgramsData>) ProgramsReceiver.getPrograms();
            String search = etSearch.getText().toString().trim().toLowerCase();
            for (ProgramsData program : allPrograms) {
                if (program.getProgramName().toLowerCase().contains(search)){
                    newList.add(program);
                }else if(program.getStudentName().toLowerCase().contains(search)){
                    newList.add(program);
                }
            }
            if(!search.isEmpty()) {
                LikesStatisticsAdapter searchAdapter = new LikesStatisticsAdapter(newList, getContext());
                rvStatistics.setAdapter(searchAdapter);
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(etSearch.getText().toString().isEmpty()){
                    if (playsStatsWasChosen){
                        rvStatistics.setAdapter(playsAdapter);
                    }
                    else {
                        rvStatistics.setAdapter(likesAdapter);
                    }
                }
            }
        });
    }

    private List<ProgramsData> sortPrograms(List<ProgramsData> programs){ //TODO: sort the programs by plays
        for (ProgramsData program : programs) {
            programsNumOfPlays.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Long o = (Long) dataSnapshot.child(program.getVodId()).getValue();

                    int value = o != null ? o.intValue(): 0;
                    program.setNumOfPlays(value);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        Collections.sort(programs, new Comparator<ProgramsData>() {
            public int compare(ProgramsData o1, ProgramsData o2) {
                //Sorts by 'TimeStarted' property
                return o1.getNumOfPlays() - o2.getNumOfPlays();
            }
        });
        return programs;
    }
}
