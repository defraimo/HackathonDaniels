package daniel.rad.radiotabsdrawer.playlist.chosenPlaylist;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.playlist.Playlist;
import daniel.rad.radiotabsdrawer.playlist.PlaylistsDataSource;
import daniel.rad.radiotabsdrawer.playlist.addProgramToPlaylist.AddProgramToPlaylistAdapter;
import daniel.rad.radiotabsdrawer.programs.ProgramsAdapter;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;
import daniel.rad.radiotabsdrawer.programs.ProgramsDataSource;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreatePlaylistFragment extends Fragment {

    RecyclerView rvChooseProgram;
    EditText etListName;
    EditText etSearchAddPlaylist;
    ImageView ivCreatePlaylist;
    ImageView ivSearchButton;

    public static CreatePlaylistFragment newInstance(Playlist playlist, boolean isAddProg) {
        Bundle args = new Bundle();
        args.putParcelable("playlist", playlist);
        args.putBoolean("isAddProg", isAddProg);
        CreatePlaylistFragment fragment = new CreatePlaylistFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvChooseProgram = view.findViewById(R.id.rvChooseProgram);
        etListName = view.findViewById(R.id.etListName);
        etSearchAddPlaylist = view.findViewById(R.id.etSearchProgram);
        ivCreatePlaylist = view.findViewById(R.id.ivCreatePlaylist);
        ivSearchButton = view.findViewById(R.id.ivSearchButton);

        rvChooseProgram.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<ProgramsData> dataArrayList = ProgramsDataSource.getPrograms();
        RecyclerView.Adapter adapter = new CreatePlaylistAdapter(dataArrayList, getContext());

        PlaylistsDataSource playlistsDataSource= new PlaylistsDataSource();
        playlistsDataSource.writeDB(getContext(),"מועדפים",dataArrayList);

        ArrayList<ProgramsData> sortedList = new ArrayList<>(dataArrayList);
        if (getArguments() != null) {
            Playlist playlist = getArguments().getParcelable("playlist");
            if (getArguments().getBoolean("isAddProg")) {
                for (ProgramsData programAll : dataArrayList) {
                    for (ProgramsData playlistProgram : playlist.getProgramsData()) {
                        if (programAll.getProgramName().equalsIgnoreCase(playlistProgram.getProgramName())) {
                            sortedList.remove(programAll);
                        }
                    }
                }
            }
            RecyclerView.Adapter adapterAddedProg = new AddProgramToPlaylistAdapter(sortedList, getContext());
            rvChooseProgram.setAdapter(adapterAddedProg);
        } else {
            rvChooseProgram.setAdapter(adapter);
        }


        ivSearchButton.setOnClickListener((v) -> {
            ArrayList<ProgramsData> newList = new ArrayList<>();
            ArrayList<ProgramsData> allPrograms = (ArrayList<ProgramsData>) ProgramsDataSource.getPrograms();
            String search = etSearchAddPlaylist.getText().toString().trim().toLowerCase();
            for (ProgramsData program : allPrograms) {
                if (program.getProgramName().toLowerCase().contains(search)) {
                    newList.add(program);
                } else if (program.getStudentName().toLowerCase().contains(search)) {
                    newList.add(program);
                }
            }
            if (!search.isEmpty()) {
                rvChooseProgram.setAdapter(new CreatePlaylistAdapter(newList, this.getContext()));
            }
        });
        etSearchAddPlaylist.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etSearchAddPlaylist.getText().toString().isEmpty()) {
                    rvChooseProgram.setAdapter(adapter);
                }
            }
        });

        ivCreatePlaylist.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Creating Playlist!", Toast.LENGTH_SHORT).show();
            //TODO: adds playlist to:
            //1)db
            //2)rvPlaylists adapter:


        });
    }
}