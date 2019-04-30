package daniels.hackathon.radio.playlist;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import daniels.hackathon.radio.R;
import daniels.hackathon.radio.playlist.chosenPlaylist.ChosenPlaylistAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistDetailsFragment extends Fragment {

    TextView tvPlaylistName;
    ImageView ivSearchButton;
    ImageView ivPlayPlaylist;
    EditText etSearchProgram;
    public static RecyclerView rvPlaylistPrograms;
    ArrayList<Playlist> playlistArrayList;
    Playlist playlist;

    public static PlaylistDetailsFragment newInstance(Playlist playlist) {
        Bundle args = new Bundle();
        args.putParcelable("playlist", playlist);
        PlaylistDetailsFragment detailsFragment = new PlaylistDetailsFragment();
        detailsFragment.setArguments(args);
        return detailsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvPlaylistName = view.findViewById(R.id.tvPlaylistName);
        rvPlaylistPrograms = view.findViewById(R.id.rvPlaylistPrograms);
        ivSearchButton = view.findViewById(R.id.ivSearchButton);
        ivPlayPlaylist = view.findViewById(R.id.ivPlayPlaylist);
        etSearchProgram = view.findViewById(R.id.etSearchProgram);
        playlistArrayList = new ArrayList<>();

        if (getArguments() != null) {
            playlist = getArguments().getParcelable("playlist");
            if (playlist != null) {
                tvPlaylistName.setText(playlist.getName());
            }

            rvPlaylistPrograms.setLayoutManager(new LinearLayoutManager(getContext()));
            RecyclerView.Adapter adapter = new ChosenPlaylistAdapter(playlist.getProgramsData(), getContext());
            rvPlaylistPrograms.setAdapter(adapter);

            //todo:
            //1) add search
            //2)add the play option

            ivPlayPlaylist.setOnClickListener(v -> {
                Intent intentProgram = new Intent("currentProgram");
                intentProgram.putExtra("program",playlist.getProgramsData().get(0));
                intentProgram.putExtra("isFromPlaylist",true);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intentProgram);

                Intent intentPlaylist = new Intent("currentlyPlayingPlaylist");
                intentPlaylist.putExtra("playlist",(ArrayList) playlist.getProgramsData());
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intentPlaylist);
            });

        }
    }
}