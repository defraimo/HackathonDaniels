package daniel.rad.radiotabsdrawer.programs;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.playlist.Playlist;
import daniel.rad.radiotabsdrawer.playlist.PlaylistsJsonWriter;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChosenProgramFragment extends Fragment {

    TextView tvChosenStudentName;
    TextView tvChosenProgramName;
    RecyclerView rvChosenComments;
    ImageView ivAddToFav;
    ImageView ivShare;

    public static ChosenProgramFragment newInstance(ProgramsData programsData) {
        Bundle args = new Bundle();
        args.putParcelable("model", programsData);
        ChosenProgramFragment fragment = new ChosenProgramFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chosen_program, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if ( getArguments() != null) {
            //receiving the data we passed with Parcelable:
           ProgramsData model =  getArguments().getParcelable("model");

           //defining the elements in the fragment:
            tvChosenStudentName = view.findViewById(R.id.tvChosenStudentName);
            tvChosenProgramName = view.findViewById(R.id.tvChosenProgramName);
            rvChosenComments = view.findViewById(R.id.rvChosenComments);
            ivAddToFav = view.findViewById(R.id.ivAddToFav);
            ivShare = view.findViewById(R.id.ivShare);

            tvChosenStudentName.setText(model.getStudentName());
            tvChosenProgramName.setText(model.getProgramName());

            ivShare.setOnClickListener(v -> {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, "מוזמנ/ת להקשיב לתוכנית - "+model.getProgramName()+" של "+model.getStudentName()+"! *קישור כניסה לאפליקצייה*");

                startActivity(Intent.createChooser(share, "היכן תרצה לשתף את התוכנית?"));
            });

            ivAddToFav.setOnClickListener(v->{
                Toast.makeText(v.getContext(), "נוסף למועדפים", Toast.LENGTH_SHORT).show();
                new PlaylistsJsonWriter(model, v.getContext(), PlaylistsJsonWriter.ADD_TO_FAVS).execute();
            });
        }
    }
}
