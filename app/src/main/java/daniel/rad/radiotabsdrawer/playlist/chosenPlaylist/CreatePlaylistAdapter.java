package daniel.rad.radiotabsdrawer.playlist.chosenPlaylist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

import daniel.rad.radiotabsdrawer.programs.ProgramsData;
import daniel.rad.radiotabsdrawer.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class CreatePlaylistAdapter extends RecyclerView.Adapter<CreatePlaylistViewHolder> {
    List<ProgramsData> programsDataList;
    Context context;

    public CreatePlaylistAdapter(List<ProgramsData> programsDataList, Context context) {
        this.programsDataList = programsDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public CreatePlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.chose_program_to_playlist_item, viewGroup, false);
        return new  CreatePlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreatePlaylistViewHolder holder, int i) {
        ProgramsData programsData = programsDataList.get(i);

        holder.tvProgramName.setText(programsData.getProgramName());
        if (programsData.getStudentName() != null)
            holder.tvStudentName.setText(programsData.getStudentName());
        else holder.tvLine.setText(""); //if there is no student the line won't be printed

        holder.ivProfilePic.setImageResource(programsData.getProfilePic());

        holder.programsData = programsData;
    }

    @Override
    public int getItemCount() {
        return programsDataList.size();
    }
}

class CreatePlaylistViewHolder extends RecyclerView.ViewHolder{

    TextView tvProgramName;
    TextView tvStudentName;
    TextView tvLine;
    CircleImageView ivProfilePic;
    ToggleButton tbCheck;
    ProgramsData programsData;

    public CreatePlaylistViewHolder(@NonNull View itemView) {
        super(itemView);
        tvProgramName = itemView.findViewById(R.id.tvProgramName);
        tvStudentName = itemView.findViewById(R.id.tvStudentName);
        ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
        tvLine = itemView.findViewById(R.id.tvLine);
        tbCheck = itemView.findViewById(R.id.tbCheck);

        tbCheck.setOnClickListener(v -> {
            if (tbCheck.isChecked()){
                tbCheck.setBackgroundResource(R.drawable.ic_radio_button_checked);
            }
            else if (!tbCheck.isChecked()){
                tbCheck.setBackgroundResource(R.drawable.ic_check);
            }
        });
    }

}