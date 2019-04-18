package daniel.rad.radiotabsdrawer.playlist.removeProgramsFromPlaylist;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.playlist.Playlist;
import daniel.rad.radiotabsdrawer.playlist.PlaylistsJsonWriter;

/**
 * A simple {@link Fragment} subclass.
 */
public class RemoveProgramFromPlaylistFragment extends Fragment {
    TextView tvRemovePlaylistName;
    ImageView ivRemoveSearchButton;
    EditText etRemoveSearchProgram;
    public RecyclerView rvRemovePlaylistPrograms;
    ArrayList<Playlist> playlistArrayList;
    Playlist playlist;
    RemovePlaylistAdapter removePlaylistAdapter;

    public static RemoveProgramFromPlaylistFragment newInstance(Playlist playlist, ArrayList<Playlist> playlistArrayList) {
        Bundle args = new Bundle();
        args.putParcelable("playlist", playlist);
        args.putParcelableArrayList("playlists", playlistArrayList);
        RemoveProgramFromPlaylistFragment fragment = new RemoveProgramFromPlaylistFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_remove_program_from_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvRemovePlaylistName = view.findViewById(R.id.tvRemovePlaylistName);
        ivRemoveSearchButton = view.findViewById(R.id.ivRemoveSearchButton);
        etRemoveSearchProgram = view.findViewById(R.id.etRemoveSearchProgram);
        rvRemovePlaylistPrograms = view.findViewById(R.id.rvRemovePlaylistPrograms);


        RemovePlaylistAdapter.RemoveProgramInteface removeProgramInterface = programsData -> {
             playlist.getProgramsData().remove(programsData);
            //todo: debug for why it doesn't reach this place when it should
                for (Playlist playlistRemoveProgram : playlistArrayList) {
                    if (playlistRemoveProgram.getName().equals(playlist.getName())) {
                        playlistRemoveProgram = playlist;
                    }
                }
                new PlaylistsJsonWriter(playlistArrayList, view.getContext()).execute();
        };

        if (getArguments() != null) {
            playlistArrayList = getArguments().getParcelableArrayList("playlists");
            if(getArguments().getParcelable("playlist") != null)
            playlist = getArguments().getParcelable("playlist");

            tvRemovePlaylistName.setText(playlist.getName());

            removePlaylistAdapter = new RemovePlaylistAdapter(playlist.getProgramsData(), getContext(), removeProgramInterface);

            rvRemovePlaylistPrograms.setLayoutManager(new LinearLayoutManager(view.getContext()));
            rvRemovePlaylistPrograms.setAdapter(removePlaylistAdapter);
        }



    }
}

