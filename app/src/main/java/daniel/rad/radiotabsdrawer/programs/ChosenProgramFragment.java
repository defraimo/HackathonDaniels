package daniel.rad.radiotabsdrawer.programs;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import daniel.rad.radiotabsdrawer.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChosenProgramFragment extends Fragment {
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
            TextView tvChosenPName = view.findViewById(R.id.tvChosenPName);
            TextView tvChosenProgramName = view.findViewById(R.id.tvChosenProgramName);
            FloatingActionButton fabChosenLike = view.findViewById(R.id.fabChosenLike);
            FloatingActionButton fabChosenShare= view.findViewById(R.id.fabChosenShare);
            RecyclerView rvChosenComments = view.findViewById(R.id.rvChosenComments);

            tvChosenPName.setText(model.getStudentName());
            tvChosenProgramName.setText(model.getProgramName());
        }
    }
}
