package daniel.rad.radiotabsdrawer.admin.statisticsManager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;

public class LikesStatisticsAdapter extends RecyclerView.Adapter<LikesStatisticsAdapter.StatisticsViewHolder> {
    private List<ProgramsData> programs;
    private Context context;

    private DatabaseReference programLikes =
            FirebaseDatabase.getInstance()
                    .getReference("ProgramLikes");

    private long likesCounter;

    public LikesStatisticsAdapter(List<ProgramsData> programs, Context context) {
        this.programs = programs;
        this.context = context;
    }

    @NonNull
    @Override
    public StatisticsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.admin_statistics_item, viewGroup, false);
        return new StatisticsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticsViewHolder holder, int i) {
        ProgramsData programsData = programs.get(i);

        holder.tvChosenProgramName.setText(programsData.getProgramName());

        programLikes.child(programsData.getVodId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likesCounter = 0;
                if (dataSnapshot.getValue() != null) {
                    HashMap<Long, Long> value = (HashMap<Long, Long>) dataSnapshot.getValue();
                    for (Long like : value.values()) {
                        likesCounter += like;
                    }
                    holder.tvStatsNumber.setText(String.valueOf(likesCounter));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return programs.size();
    }


    class StatisticsViewHolder extends RecyclerView.ViewHolder{
        TextView tvChosenProgramName;
        TextView tvStatsNumber;

        public StatisticsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChosenProgramName = itemView.findViewById(R.id.tvChosenProgramName);
            tvStatsNumber = itemView.findViewById(R.id.tvStatsNumber);

            tvChosenProgramName.setSelected(true);
        }
    }
}
