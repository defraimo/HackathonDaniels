package daniel.rad.radiotabsdrawer.playlist;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import daniel.rad.radiotabsdrawer.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistFragment extends Fragment {

    FrameLayout playlist_frame;

    public PlaylistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playlist_frame = view.findViewById(R.id.playlist_frame);

        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        activity.getSupportFragmentManager().
                beginTransaction().
                replace(R.id.playlist_frame,new AllPlaylistsFragment()).
                commit();
    }
}
