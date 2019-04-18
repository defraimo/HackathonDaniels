package daniel.rad.radiotabsdrawer.playlist;

import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.playlist.chosenPlaylist.CreatePlaylistFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllPlaylistsFragment extends Fragment implements JsonReaderInterface {

    RecyclerView rvPlaylist;
    TextView tvPlaylist;
    ImageView ivAddPlaylist;
    ArrayList<Playlist> playlistsList;
    ProgressBar progressBar;
    PlaylistJsonReader jsonReader;
    PlaylistAdapter adapter;

    public static AllPlaylistsFragment newInstance(ArrayList<Playlist> playlistArraylist) {
        Bundle args = new Bundle();
        args.putParcelableArrayList("playlists", playlistArraylist);
        AllPlaylistsFragment fragment = new AllPlaylistsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_playlists, container, false);
        progressBar = view.findViewById(R.id.playlist_progress_bar);
        return view;
    }


    @Override
    public void onResume(){
        super.onResume();
        jsonReader = new PlaylistJsonReader(progressBar, getContext());
        jsonReader.dataPasser = this;
        jsonReader.execute();
    }

    @Override
    public void passData(ArrayList<Playlist> playlists) {
        playlistsList = playlists;
        adapter= new PlaylistAdapter(playlists , getContext());
        rvPlaylist.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPlaylist = view.findViewById(R.id.rvPlaylist);
        tvPlaylist = view.findViewById(R.id.tvPlaylist);
        ivAddPlaylist = view.findViewById(R.id.ivAddPlaylist);

        rvPlaylist.setLayoutManager(new GridLayoutManager(getContext(), 2));

        ivAddPlaylist.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            activity.getSupportFragmentManager().
                    beginTransaction().
                    addToBackStack("allPlaylists").
                    replace(R.id.playlist_frame, CreatePlaylistFragment.newInstance(null , playlistsList, true)).
                    commit();
        });

    }



}
