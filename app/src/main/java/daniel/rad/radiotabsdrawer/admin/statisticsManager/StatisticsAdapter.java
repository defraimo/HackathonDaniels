package daniel.rad.radiotabsdrawer.admin.statisticsManager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.StatisticsViewHolder> {
    private List<ProgramsData> programs;
    private Context context;

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
        Random random = new Random();
        holder.tvStatsNumber.setText(String.valueOf(random.nextInt(100)));
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
