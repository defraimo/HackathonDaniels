package daniel.rad.radiotabsdrawer.programs;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import daniel.rad.radiotabsdrawer.MediaPlayerFragment;
import daniel.rad.radiotabsdrawer.R;

public class ProgramsAdapter extends RecyclerView.Adapter<ProgramsAdapter.ProgramViewHolder> {
    private List<ProgramsData> programs;
    private Context context;
    List<Boolean> isSelectedArr;

    private ProgramAdapterInterface programAdapterInterface;

    private DatabaseReference programLikes =
            FirebaseDatabase.getInstance()
                    .getReference("ProgramLikes");

    boolean fromUser = true;

    public ProgramsAdapter(List<ProgramsData> programs, Context context, ProgramAdapterInterface programAdapterInterface) {
        this.programs = programs;
        this.context = context;
        this.programAdapterInterface = programAdapterInterface;
        this.isSelectedArr = new ArrayList<>();

        for (int i = 0; i < programs.size(); i++) {
            isSelectedArr.add(false);
        }
        programLikes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!fromUser) return;

                for (int i = 0; i < programs.size(); i++) {

                    Long o = (Long) dataSnapshot.child(programs.get(i).getVodId()).child("radshun").getValue();

                    int value = o != null ? o.intValue(): 0;
                    if (value == 0){
                        isSelectedArr.set(i,false);
                    }
                    else if (value == 1){
                        isSelectedArr.set(i,true);

                    }
                }

                fromUser = false;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @NonNull
    @Override
    public ProgramViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View programView = inflater.inflate(R.layout.program_item, viewGroup, false);

        ProgramViewHolder holder = new ProgramViewHolder(programView, programAdapterInterface);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramViewHolder holder, int position) {
        ProgramsData program = programs.get(position);

        holder.tvProgramName.setText(program.getProgramName());
        holder.tvPName.setText(program.getStudentName());
        holder.ivProfilePic.setImageResource(program.getProfilePic());
        holder.program = program;
        //todo: replace ImageResource with and image from Facebook

        if (isSelectedArr.get(position)){
            holder.ivLike.setImageResource(R.drawable.ic_filled_heart);
        }
        else {
            holder.ivLike.setImageResource(R.drawable.ic_empty_heart);
        }

        holder.ivLike.setOnClickListener(v -> {
            if (!isSelectedArr.get(position)) {
                programLikes
                        .child(program.getVodId())
                        .child("radshun")
                        .setValue(1);
                holder.ivLike.setImageResource(R.drawable.ic_filled_heart);
                isSelectedArr.set(position,true);
            }
            else {
                programLikes
                        .child(program.getVodId())
                        .child("radshun")
                        .setValue(0);
                holder.ivLike.setImageResource(R.drawable.ic_empty_heart);
                isSelectedArr.set(position,false);
            }
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
            Intent intent = new Intent("currentLikePressed");
            intent.putExtra("program",program);
            localBroadcastManager.sendBroadcast(intent);
        });
    }

    @Override
    public int getItemCount() {
        return programs.size();
    }

    class ProgramViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfilePic;
        TextView tvPName;
        TextView tvProgramName;
        ImageView ivPlayProgram;
        ImageView ivLike;

        ProgramsData program;

        boolean liked;

        public ProgramViewHolder(@NonNull View itemView, final ProgramAdapterInterface programAdapterInterface) {
            super(itemView);
            this.ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            this.tvPName = itemView.findViewById(R.id.tvChosenStudentName);
            this.tvProgramName = itemView.findViewById(R.id.tvChosenProgramName);
            this.ivPlayProgram = itemView.findViewById(R.id.ivPlayProgram);
            this.ivLike = itemView.findViewById(R.id.ivLike);

            itemView.setOnClickListener((v) -> {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().
                        beginTransaction().
                        addToBackStack("chosenProgram").
                        replace(R.id.programsFrame, ChosenProgramFragment.newInstance(program)).
                        commit();
            });

            ivPlayProgram.setOnClickListener(v -> {
                programAdapterInterface.onItemClicked(program);
            });
        }
    }

    public interface ProgramAdapterInterface {
        void onItemClicked(ProgramsData chosenProgram);
    }
}
