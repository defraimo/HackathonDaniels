package daniel.rad.radiotabsdrawer.admin.programsManager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;

public class ProgramsManagerAdapter extends RecyclerView.Adapter<ProgramsManagerAdapter.ProgramsViewHolder> {
    List<ProgramsData> programs;
    Context context;

    public ProgramsManagerAdapter(List<ProgramsData> programs, Context context) {
        this.programs = programs;
        this.context = context;
    }

    @NonNull
    @Override
    public ProgramsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.admin_program_item,viewGroup,false);
        return new ProgramsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramsViewHolder holder, int i) {
        ProgramsData programsData = programs.get(i);
        holder.tvChosenProgramName.setText(programsData.getProgramName());
        holder.program = programsData;
    }

    @Override
    public int getItemCount() {
        return programs.size();
    }

    class ProgramsViewHolder extends RecyclerView.ViewHolder{
        TextView tvChosenProgramName;
        ProgramsData program;

        public ProgramsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChosenProgramName = itemView.findViewById(R.id.tvChosenProgramName);
            tvChosenProgramName.setSelected(true);

            itemView.setOnClickListener(v -> {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().
                        addToBackStack("programsManager").
                        replace(R.id.program_manager_layout,ProgramsManagerOptionsFragment.newInstance(program)).
                        commit();
            });
        }
    }
}
