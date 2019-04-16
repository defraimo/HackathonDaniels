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

import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.myMediaPlayer.ProgramsReceiver;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticsFragment extends Fragment {

    RecyclerView rvStatistics;


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
        rvStatistics.setAdapter(new StatisticsAdapter(ProgramsReceiver.getPrograms(),getContext()));
    }
}
