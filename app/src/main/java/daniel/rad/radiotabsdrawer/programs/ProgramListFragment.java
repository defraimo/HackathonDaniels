package daniel.rad.radiotabsdrawer.programs;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.myMediaPlayer.ProgramsReceiver;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramListFragment extends Fragment {
    RecyclerView rvPrograms;
    EditText etSearch;
    ImageView ivProgramSearch;
//    PassingProgramsNames passingProgramsNames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_program_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProgramsAdapter.ProgramAdapterInterface adapterInterface = chosenProgram -> {
            System.out.println(chosenProgram);

            Intent intent = new Intent("currentProgram");
            intent.putExtra("program",chosenProgram);
            intent.putExtra("isFromPlaylist",false);
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
        };

        rvPrograms = view.findViewById(R.id.rvPrograms);
        ProgramsAdapter adapter = new ProgramsAdapter(ProgramsReceiver.getPrograms(),this.getContext(),adapterInterface);
        rvPrograms.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rvPrograms.setAdapter(adapter);

        etSearch = view.findViewById(R.id.etProgramSearch);
        ivProgramSearch = view.findViewById(R.id.ivProgramSearch);

        ivProgramSearch.setOnClickListener((v)->{
            ArrayList<ProgramsData> newList = new ArrayList<>();
            ArrayList<ProgramsData> allPrograms = (ArrayList<ProgramsData>) ProgramsReceiver.getPrograms();
            String search = etSearch.getText().toString().trim().toLowerCase();
            for (ProgramsData program : allPrograms) {
                if (program.getProgramName().toLowerCase().contains(search)){
                    newList.add(program);
                }else if(program.getStudentName().toLowerCase().contains(search)){
                    newList.add(program);
                }
            }
            if(!search.isEmpty()) {
                ProgramsAdapter searchAdapter = new ProgramsAdapter(newList, this.getContext(),adapterInterface);
                rvPrograms.setAdapter(searchAdapter);
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(etSearch.getText().toString().isEmpty()){
                    rvPrograms.setAdapter(adapter);
                }
            }
        });
    }

}
