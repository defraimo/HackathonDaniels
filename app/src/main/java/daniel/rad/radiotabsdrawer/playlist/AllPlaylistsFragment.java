package daniel.rad.radiotabsdrawer.playlist;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import daniel.rad.radiotabsdrawer.playlist.chosenPlaylist.CreatePlaylistFragment;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;
import daniel.rad.radiotabsdrawer.programs.ProgramsDataSource;
import daniel.rad.radiotabsdrawer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllPlaylistsFragment extends Fragment {

    RecyclerView rvPlaylist;
    TextView tvPlaylist;
    ImageView ivAddPlaylist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_playlists, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPlaylist = view.findViewById(R.id.rvPlaylist);
        tvPlaylist = view.findViewById(R.id.tvPlaylist);
        ivAddPlaylist = view.findViewById(R.id.ivAddPlaylist);

        ArrayList<Playlist> playlists = PlaylistsDataSource.getPlaylists();
        rvPlaylist.setLayoutManager(new GridLayoutManager(getContext(),2));
        rvPlaylist.setAdapter(new PlaylistAdapter(playlists,getContext()));

        ivAddPlaylist.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            activity.getSupportFragmentManager().
                    beginTransaction().
                    addToBackStack("allPlaylists").
                    replace(R.id.playlist_frame,new CreatePlaylistFragment()).
                    commit();
        });

    }

}