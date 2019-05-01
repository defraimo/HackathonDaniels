package daniel.rad.radiotabsdrawer.admin.programsManager;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import daniel.rad.radiotabsdrawer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramManagerFragment extends Fragment {

    public ProgramManagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_program_manager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getFragmentManager().beginTransaction().
                replace(R.id.programs_manager_frame,new ProgramsManagerListFragment()).
                commit();
    }
}
