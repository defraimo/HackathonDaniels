package daniels.hackathon.radio.admin.programsManager;


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

import java.util.ArrayList;

import daniels.hackathon.radio.R;
import daniels.hackathon.radio.myMediaPlayer.ProgramsReceiver;
import daniels.hackathon.radio.programs.ProgramsData;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramsManagerListFragment extends Fragment {

    RecyclerView rvProgramsManager;
    ImageView ivProgramSearch;
    EditText etSearch;
    ImageView ivProgramsManager;
    ImageView ivCommentsManager;
    private boolean programsManagerWasChosen = true;
    ProgramsManagerAdapter adapter;

    public ProgramsManagerListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_programs_manager_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvProgramsManager = view.findViewById(R.id.rvProgramsManager);
        ivProgramSearch = view.findViewById(R.id.ivProgramSearch);
        etSearch = view.findViewById(R.id.etSearch);
        ivProgramsManager = view.findViewById(R.id.ivProgramsManager);
        ivCommentsManager = view.findViewById(R.id.ivCommentsManager);

        rvProgramsManager.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ProgramsManagerAdapter(ProgramsReceiver.getPrograms(), getContext(),true);
        rvProgramsManager.setAdapter(adapter);

        ivProgramsManager.setOnClickListener(v -> {
            if (!programsManagerWasChosen) {
                adapter = new ProgramsManagerAdapter(ProgramsReceiver.getPrograms(), getContext(), true);
                rvProgramsManager.setAdapter(adapter);
                ivProgramsManager.setImageResource(R.drawable.ic_choose_right);
                ivCommentsManager.setImageResource(R.drawable.ic_choose_left_unpressed);
                programsManagerWasChosen = true;
            }
        });

        ivCommentsManager.setOnClickListener(v -> {
            if (programsManagerWasChosen) {
                adapter = new ProgramsManagerAdapter(ProgramsReceiver.getPrograms(), getContext(), false);
                rvProgramsManager.setAdapter(adapter);
                ivCommentsManager.setImageResource(R.drawable.ic_choose_left);
                ivProgramsManager.setImageResource(R.drawable.ic_choose_right_unpressed);
                programsManagerWasChosen = false;
            }
        });

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
                ProgramsManagerAdapter searchAdapter;
                if (programsManagerWasChosen){
                    searchAdapter = new ProgramsManagerAdapter(newList, getContext(), true);
                }
                else {
                    searchAdapter = new ProgramsManagerAdapter(newList, getContext(), false);
                }
                rvProgramsManager.setAdapter(searchAdapter);
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
                    rvProgramsManager.setAdapter(adapter);
                }
            }
        });
    }
}
