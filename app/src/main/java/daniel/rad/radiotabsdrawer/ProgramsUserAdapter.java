package daniel.rad.radiotabsdrawer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import daniel.rad.radiotabsdrawer.admin.programsManager.ProgramsManagerOptionsFragment;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;

public class ProgramsUserAdapter extends RecyclerView.Adapter<ProgramsUserAdapter.ProgramsViewHolder> {
    List<ProgramsData> programs;
    Context context;

    private UserProgramAdapterInterface userProgramAdapterInterface;

    public ProgramsUserAdapter(List<ProgramsData> programs, Context context, UserProgramAdapterInterface userProgramAdapterInterface) {
        this.programs = programs;
        this.context = context;
        this.userProgramAdapterInterface = userProgramAdapterInterface;
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
                userProgramAdapterInterface.onItemClicked(program);
            });
        }
    }

    public interface UserProgramAdapterInterface {
        void onItemClicked(ProgramsData chosenProgram);
    }
}
