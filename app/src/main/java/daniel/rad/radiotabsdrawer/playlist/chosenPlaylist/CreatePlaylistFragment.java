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

import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.myMediaPlayer.ProgramsReceiver;
import daniel.rad.radiotabsdrawer.playlist.AllPlaylistsFragment;
import daniel.rad.radiotabsdrawer.playlist.Playlist;
import daniel.rad.radiotabsdrawer.playlist.PlaylistAdapterInterface;
import daniel.rad.radiotabsdrawer.playlist.PlaylistsJsonWriter;
import daniel.rad.radiotabsdrawer.playlist.addProgramToPlaylist.AddProgramToPlaylistAdapter;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreatePlaylistFragment extends Fragment {

    RecyclerView rvChooseProgram;
    EditText etListName;
    EditText etSearchAddPlaylist;
    ImageView ivCreatePlaylist;
    ImageView ivSearchButton;
    Playlist playlist;
    private static boolean isCreateNew = true;

    private ArrayList<ProgramsData> programsToCreate;
    private ArrayList<Playlist> playlistArrayList;

    public static CreatePlaylistFragment newInstance(Playlist playlist) {
        isCreateNew = false;
        Bundle args = new Bundle();
        args.putParcelable("playlist", playlist);
        CreatePlaylistFragment fragment = new CreatePlaylistFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public static CreatePlaylistFragment newInstance(ArrayList<Playlist> playlists) {
        isCreateNew = true;
        Bundle args = new Bundle();
        args.putParcelableArrayList("playlists", playlists);
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
        PlaylistAdapterInterface adapterInterface = new PlaylistAdapterInterface() {
            @Override
            public void OnItemClicked(ArrayList<ProgramsData> programs) {
                programsToCreate = new ArrayList<>(programs);
            }
        };

        rvChooseProgram = view.findViewById(R.id.rvChooseProgram);
        etListName = view.findViewById(R.id.etListName);
        etSearchAddPlaylist = view.findViewById(R.id.etSearchProgram);
        ivCreatePlaylist = view.findViewById(R.id.ivCreatePlaylist);
        ivSearchButton = view.findViewById(R.id.ivSearchButton);

        rvChooseProgram.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<ProgramsData> dataArrayList = ProgramsReceiver.getPrograms();
        RecyclerView.Adapter adapter = new CreatePlaylistAdapter(dataArrayList, getContext(), adapterInterface);

        ArrayList<ProgramsData> sortedList = new ArrayList<>(dataArrayList);

        if (getArguments() != null) {
            if (isCreateNew) {
                 playlistArrayList = getArguments().
                        getParcelableArrayList("playlists");
            }else{
                playlist = getArguments().getParcelable("playlist");
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
            ArrayList<ProgramsData> allPrograms = ProgramsReceiver.getPrograms();
            String search = etSearchAddPlaylist.getText().toString().trim().toLowerCase();
            for (ProgramsData program : allPrograms) {
                if (program.getProgramName().toLowerCase().contains(search)) {
                    newList.add(program);
                } else if (program.getStudentName().toLowerCase().contains(search)) {
                    newList.add(program);
                }
            }
            if (!search.isEmpty()) {
                rvChooseProgram.setAdapter(new CreatePlaylistAdapter(newList, this.getContext(), adapterInterface));
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
            //an instance of the data source in order to write to the data base:
            ArrayList<Playlist> playlists = new ArrayList<>(playlistArrayList);

            String s = etListName.getText().toString();// playlist name
            playlists.add(new Playlist(s, programsToCreate));


            //write all playlists to json:
            new PlaylistsJsonWriter(playlists, getContext()).execute();




        });
    }
}