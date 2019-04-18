package daniel.rad.radiotabsdrawer.playlist.chosenPlaylist;


import android.os.Bundle;
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
import android.widget.TextView;

import java.util.ArrayList;

import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.myMediaPlayer.ProgramsReceiver;
import daniel.rad.radiotabsdrawer.playlist.Playlist;
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
    TextView tvCreatePlaylist;
    ImageView ivCreatePlaylist;
    ImageView ivSearchButton;
    Playlist playlist;
    private boolean isCreateNew;

    private ArrayList<ProgramsData> programsToCreate;
    private ArrayList<Playlist> playlistArrayList;
    CreatePlaylistAdapter adapter;
    AddProgramToPlaylistAdapter adapterAddedProg;

    public static CreatePlaylistFragment newInstance(Playlist playlist, ArrayList<Playlist> playlists, boolean isCreate) {
        Bundle args = new Bundle();
        args.putParcelable("playlist", playlist);
        args.putParcelableArrayList("playlists", playlists);
        args.putBoolean("isCreate", isCreate);
        CreatePlaylistFragment fragment = new CreatePlaylistFragment();
        fragment.setArguments(args);
        return fragment;
    }

//    public static CreatePlaylistFragment newInstance(ArrayList<Playlist> playlists) {
//        Bundle args = new Bundle();
//        args.putParcelableArrayList("playlists", playlists);
//        CreatePlaylistFragment fragment = new CreatePlaylistFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }

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
        tvCreatePlaylist = view.findViewById(R.id.tvCreatePlaylist);
        ivSearchButton = view.findViewById(R.id.ivSearchButton);
        programsToCreate = new ArrayList<>();

        CreatePlaylistAdapter.CreatePlaylistAdapterInterface createPlaylistAdapterInterface = (toAdd, programsData) -> {
            if(toAdd) programsToCreate.add(programsData);
            else programsToCreate.remove(programsData);
        };

        AddProgramToPlaylistAdapter.AddProgramInterface addProgramInterface = (toAdd, programsData) ->{
            if (toAdd) playlist.getProgramsData().add(programsData);
            else playlist.getProgramsData().remove(programsData);
        };

        ArrayList<ProgramsData> dataArrayList = ProgramsReceiver.getPrograms();
        rvChooseProgram.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CreatePlaylistAdapter(dataArrayList, this.getContext(), createPlaylistAdapterInterface);

        ArrayList<ProgramsData> sortedList = new ArrayList<>(dataArrayList);

        if (getArguments() != null) {
            playlistArrayList = getArguments().
                    getParcelableArrayList("playlists");
            isCreateNew = getArguments().getBoolean("isCreate");

            if (!isCreateNew){
                playlist = getArguments().getParcelable("playlist");
                tvCreatePlaylist.setText("עדכן רשימה");
                etListName.setText(playlist.getName());
                for (ProgramsData programAll : dataArrayList) {
                    for (ProgramsData playlistProgram : playlist.getProgramsData()) {
                        if (programAll.getProgramName().equalsIgnoreCase(playlistProgram.getProgramName())) {
                            sortedList.remove(programAll);
                        }
                    }
                }
                adapterAddedProg = new AddProgramToPlaylistAdapter(sortedList, this.getContext(), addProgramInterface);
                rvChooseProgram.setAdapter(adapterAddedProg);
            }else {
                rvChooseProgram.setAdapter(adapter);
            }
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
                if(isCreateNew)
                rvChooseProgram.setAdapter(new CreatePlaylistAdapter(newList, this.getContext(), createPlaylistAdapterInterface));
                else rvChooseProgram.setAdapter(new AddProgramToPlaylistAdapter(newList, this.getContext(), addProgramInterface));
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
                if (etSearchAddPlaylist.getText().toString().isEmpty()){
                    if(isCreateNew)
                    rvChooseProgram.setAdapter(adapter);
                    else rvChooseProgram.setAdapter(adapterAddedProg);
                }
            }
        });

        ivCreatePlaylist.setOnClickListener(v -> {
            String playlistName = etListName.getText().toString();
            if(isCreateNew)
            playlistArrayList.add(new Playlist(playlistName, programsToCreate));
            else
                for (Playlist playlistToAddTo : playlistArrayList) {
                    if(playlistToAddTo.getName().equals(playlist.getName())){
                        playlistToAddTo = playlist;
                    }
                }
            //write all playlists to json:
            new PlaylistsJsonWriter(playlistArrayList, getContext()).execute();
        });
    }

}