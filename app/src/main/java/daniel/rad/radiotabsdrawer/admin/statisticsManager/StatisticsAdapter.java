package daniel.rad.radiotabsdrawer.admin.statisticsManager;

import android.content.Context;
import android.os.Build;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.StatisticsViewHolder> {
    private List<ProgramsData> programs;
    private Context context;
    private DatabaseReference programsNumOfPlays =
            FirebaseDatabase.getInstance()
                    .getReference("ProgramsNumOfPlays");
//    private DatabaseReference programLikes =
//            FirebaseDatabase.getInstance()
//                    .getReference("ProgramLikes");
//    int likesCounter;

    public StatisticsAdapter(List<ProgramsData> programs, Context context) {
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
        programsNumOfPlays.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Long o = (Long) dataSnapshot.child(programsData.getVodId()).getValue();

                int value = o != null ? o.intValue(): 0;
                holder.tvStatsNumber.setText(String.valueOf(value));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        List<String> users = new ArrayList<>();
//        users.add("fraimo");
//        users.add("radshun");
//        likesCounter = 0;
//        for (String user : users) {
//            programLikes.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                    Long o = (Long) dataSnapshot.child(programsData.getVodId()).child(user).getValue();
//
//                    int value = o != null ? o.intValue(): 0;
//                    likesCounter += value;
//                    holder.tvStatsNumber.setText(String.valueOf(likesCounter));
//                    System.out.println("value: " +value);
//                    System.out.println("likesCounter: " +likesCounter);
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }
//
//        System.out.println("likesCounterAfter: " +likesCounter);
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
