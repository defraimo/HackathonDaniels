package daniel.rad.radiotabsdrawer.playlist.chosenPlaylist;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

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
    public static final String TAG = "Testing";

    RecyclerView rvChooseProgram;
    EditText etListName;
    EditText etSearchAddPlaylist;
    ImageView ivCreatePlaylist;
    ImageView ivSearchButton;
    Playlist playlist;
    BroadcastReceiver receiver;
    private static boolean isCreateNew = true;

    private ArrayList<ProgramsData> programsToCreate;
    private ArrayList<Playlist> playlistArrayList;
    CreatePlaylistAdapter adapter;

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

        rvChooseProgram = view.findViewById(R.id.rvChooseProgram);
        etListName = view.findViewById(R.id.etListName);
        etSearchAddPlaylist = view.findViewById(R.id.etSearchProgram);
        ivCreatePlaylist = view.findViewById(R.id.ivCreatePlaylist);
        ivSearchButton = view.findViewById(R.id.ivSearchButton);

        CreatePlaylistAdapter.CreatePlaylistAdapterInterface adapterInterface = new CreatePlaylistAdapter.CreatePlaylistAdapterInterface() {
            @Override
            public void passProgram(ProgramsData programsData) {
                System.out.println("This Is WEIRD! " + programsData);
            }
        };

//        receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                ProgramsData addProgram = intent.getParcelableExtra("addProgram");
//                ProgramsData removeProgram = intent.getParcelableExtra("removeProgram");
//                if (addProgram != null) {
//                    programsToCreate.add(addProgram);
//                } else if (removeProgram != null) {
//                    programsToCreate.remove(removeProgram);
//                }
//            }
//        };

        ArrayList<ProgramsData> dataArrayList = ProgramsReceiver.getPrograms();
        rvChooseProgram.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CreatePlaylistAdapter(dataArrayList, this.getContext(), adapterInterface);

        ArrayList<ProgramsData> sortedList = new ArrayList<>(dataArrayList);

        if (getArguments() != null) {
            if (isCreateNew) {
                playlistArrayList = getArguments().
                        getParcelableArrayList("playlists");
                rvChooseProgram.setAdapter(adapter);
            } else {
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
                rvChooseProgram.setAdapter(new CreatePlaylistAdapter(newList, this.getContext(),adapterInterface));
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
            String playlistName = etListName.getText().toString();
            playlistArrayList.add(new Playlist(playlistName, programsToCreate));
            //write all playlists to json:
            new PlaylistsJsonWriter(playlistArrayList, getContext()).execute();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext()));
//        IntentFilter intentFilter = new IntentFilter("createPlaylist");
//        broadcastManager.registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
//        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).unregisterReceiver(receiver);

    }
}