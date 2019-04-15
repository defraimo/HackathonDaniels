package daniel.rad.radiotabsdrawer.playlist.chosenPlaylist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import daniel.rad.radiotabsdrawer.playlist.PlaylistAdapterInterface;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;
import daniel.rad.radiotabsdrawer.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class CreatePlaylistAdapter extends RecyclerView.Adapter<CreatePlaylistViewHolder> {
    List<ProgramsData> programsDataList;
    Context context;

    PlaylistAdapterInterface adapterInterface;

    public CreatePlaylistAdapter(List<ProgramsData> programsDataList, Context context, PlaylistAdapterInterface adapterInterface) {
        this.programsDataList = programsDataList;
        this.context = context;

        this.adapterInterface = adapterInterface;
    }

    @NonNull
    @Override
    public CreatePlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.chose_program_to_playlist_item, viewGroup, false);
        return new CreatePlaylistViewHolder(view);
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

class CreatePlaylistViewHolder extends RecyclerView.ViewHolder {

    TextView tvProgramName;
    TextView tvStudentName;
    TextView tvLine;
    CircleImageView ivProfilePic;
    ToggleButton tbCheck;
    ProgramsData programsData;
    ArrayList<ProgramsData> programs;
    PlaylistAdapterInterface adapterInterface;


    public CreatePlaylistViewHolder(@NonNull View itemView) {
        super(itemView);
        tvProgramName = itemView.findViewById(R.id.tvManagerProgramName);
        tvStudentName = itemView.findViewById(R.id.tvStudentName);
        ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
        tvLine = itemView.findViewById(R.id.tvLine);
        tbCheck = itemView.findViewById(R.id.tbCheck);

        programs= new ArrayList<>();
        tbCheck.setOnClickListener(v -> {
            if (tbCheck.isChecked()) {
                tbCheck.setBackgroundResource(R.drawable.ic_radio_button_checked);
                programs.add(programsData);
            } else if (!tbCheck.isChecked()) {
                tbCheck.setBackgroundResource(R.drawable.ic_check);
                programs.remove(programsData);
            }
            adapterInterface.OnItemClicked(programs);

        });

    }


}