package daniels.hackathon.radio.admin.commentsManager;


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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import daniels.hackathon.radio.R;
import daniels.hackathon.radio.programs.ProgramsData;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentsManagerOfProgramFragment extends Fragment {

    RecyclerView rvManagerComments;
    EditText etSearchComment;
    ImageView ivCommentSearch;
    TextView tvNoComments;

    private DatabaseReference programComments =
            FirebaseDatabase.getInstance()
                    .getReference("ProgramComments");

    List<String> comments = new ArrayList<>();

    ProgramsData model;

    CommentsManagerAdapter adapter;

    public CommentsManagerOfProgramFragment() {
        // Required empty public constructor
    }

    public static CommentsManagerOfProgramFragment newInstance(ProgramsData programsData) {
        Bundle args = new Bundle();
        args.putParcelable("model", programsData);
        CommentsManagerOfProgramFragment fragment = new CommentsManagerOfProgramFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comments_manager_of_program, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = getArguments().getParcelable("model");

        rvManagerComments = view.findViewById(R.id.rvManagerComments);
        etSearchComment = view.findViewById(R.id.etSearchComment);
        ivCommentSearch = view.findViewById(R.id.ivCommentSearch);
        tvNoComments = view.findViewById(R.id.tvNoComments);

        rvManagerComments.setLayoutManager(new LinearLayoutManager(getContext()));

        programComments.child(model.getVodId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comments.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String comment = (String) child.getValue();
                    long key = Long.parseLong(child.getKey());
                    if (!comment.equals("")){
                        comments.add(comment);
                    }
                }
                if (comments.isEmpty()){
                    tvNoComments.setVisibility(View.VISIBLE);
                }
                else {
                    tvNoComments.setVisibility(View.GONE);
                }
                adapter = new CommentsManagerAdapter(comments,getContext(),model);
                rvManagerComments.setAdapter(adapter);
                rvManagerComments.scrollToPosition(comments.size()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ivCommentSearch.setOnClickListener((v)->{
            List<String> newCommentsList = new ArrayList<>();
            String search = etSearchComment.getText().toString().trim().toLowerCase();
            for (String comment : comments) {
                if (comment.toLowerCase().contains(search)){
                    newCommentsList.add(comment);
                }
            }
            if(!search.isEmpty()) {
                CommentsManagerAdapter searchAdapter = new CommentsManagerAdapter(newCommentsList, getContext(),model);
                rvManagerComments.setAdapter(searchAdapter);
            }
        });

        etSearchComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(etSearchComment.getText().toString().isEmpty()){
                    rvManagerComments.setAdapter(adapter);
                    rvManagerComments.scrollToPosition(comments.size()-1);
                }
            }
        });
    }
}
