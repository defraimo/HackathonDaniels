package daniel.rad.radiotabsdrawer.admin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import daniel.rad.radiotabsdrawer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentsManagerFragment extends Fragment {


    public CommentsManagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comments_manager, container, false);
    }

}
