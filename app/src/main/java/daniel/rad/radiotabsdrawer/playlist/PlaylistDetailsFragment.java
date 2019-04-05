package daniel.rad.radiotabsdrawer.playlist;


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

import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.playlist.chosenPlaylist.ChosenPlaylistAdapter;
import daniel.rad.radiotabsdrawer.playlist.removeProgramsFromPlaylist.RemovePlaylistAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistDetailsFragment extends Fragment {

    TextView tvPlaylistName;
    ImageView ivSearchButton;
    ImageView ivPlayPlaylist;
    EditText etSearchProgram;
    public static RecyclerView rvPlaylistPrograms;

    public static PlaylistDetailsFragment newInstance(Playlist playlist , boolean isRemove){
        Bundle args = new Bundle();
        args.putParcelable("playlist", playlist);
        args.putBoolean("isRemove",isRemove);
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

        if(getArguments() != null) {
            Playlist playlist = getArguments().getParcelable("playlist");
            boolean isRemove = getArguments().getBoolean("isRemove");

            tvPlaylistName.setText(playlist.getName());

            rvPlaylistPrograms.setLayoutManager(new LinearLayoutManager(getContext()));
            RecyclerView.Adapter adapter = new ChosenPlaylistAdapter(playlist.getProgramsData(), getContext());
            RecyclerView.Adapter adapterRemove = new RemovePlaylistAdapter(playlist.getProgramsData(), getContext());

            if(isRemove){
                rvPlaylistPrograms.setAdapter(adapterRemove);
            }else {
                rvPlaylistPrograms.setAdapter(adapter);
            }
        }

        ivPlayPlaylist.setOnClickListener(v -> {

        });

    }
}