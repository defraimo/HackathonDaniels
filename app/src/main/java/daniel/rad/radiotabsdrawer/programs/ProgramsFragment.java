package daniel.rad.radiotabsdrawer.programs;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import daniel.rad.radiotabsdrawer.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramsFragment extends Fragment {


    public ProgramsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_programs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        FragmentManager fm = activity.getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.programsFrame,new ProgramListFragment()).commit();
    }

}
