package daniel.rad.radiotabsdrawer.admin.statisticsManager;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.myMediaPlayer.ProgramsReceiver;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticsFragment extends Fragment {

    RecyclerView rvStatistics;

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

        rvStatistics.setLayoutManager(new LinearLayoutManager(getContext()));
//        List<ProgramsData> sortedPrograms = sortPrograms(ProgramsReceiver.getPrograms());
        rvStatistics.setAdapter(new StatisticsAdapter(ProgramsReceiver.getPrograms(),getContext()));
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
