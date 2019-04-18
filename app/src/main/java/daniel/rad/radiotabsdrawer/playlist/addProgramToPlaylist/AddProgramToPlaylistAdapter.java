package daniel.rad.radiotabsdrawer.playlist.addProgramToPlaylist;

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

import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddProgramToPlaylistAdapter extends RecyclerView.Adapter<AddProgramToPlaylistAdapter.AddProgramToPlaylistViewHolder> {
    List<ProgramsData> programsDataList;
    Context context;

    private ArrayList<Boolean> isSelectedArr;
    private AddProgramInterface addProgramInterface;

    public AddProgramToPlaylistAdapter(List<ProgramsData> programsDataList, Context context, AddProgramInterface addProgramInterface) {
        this.programsDataList = programsDataList;
        this.context = context;
        this.addProgramInterface = addProgramInterface;

        isSelectedArr = new ArrayList<>();

        for (int i = 0; i < programsDataList.size(); i++) {
            isSelectedArr.add(false);
        }
        System.out.println(isSelectedArr);
    }

    @NonNull
    @Override
    public AddProgramToPlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.chose_program_to_playlist_item, viewGroup, false);
        AddProgramToPlaylistViewHolder holder = new AddProgramToPlaylistViewHolder(view, addProgramInterface);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddProgramToPlaylistViewHolder holder, int position) {
        ProgramsData programsData = programsDataList.get(position);

        if(isSelectedArr.get(position)){
            holder.tbCheck.setBackgroundResource(R.drawable.ic_radio_button_checked);
        }else{
            holder.tbCheck.setBackgroundResource(R.drawable.ic_check);
        }

        holder.tvProgramName.setText(programsData.getProgramName());
        if (programsData.getStudentName() != null)
            holder.tvStudentName.setText(programsData.getStudentName());
        else holder.tvLine.setText(""); //if there is no student the line won't be printed

        holder.ivProfilePic.setImageResource(programsData.getProfilePic());
        holder.programsData = programsData;

        holder.tbCheck.setOnClickListener(v -> {
            if (holder.tbCheck.isChecked()) {
                holder.tbCheck.setBackgroundResource(R.drawable.ic_radio_button_checked);
                isSelectedArr.set(position, true);
                addProgramInterface.passProgram(true , programsData);
            } else if (!holder.tbCheck.isChecked()) {
                holder.tbCheck.setBackgroundResource(R.drawable.ic_check);
                addProgramInterface.passProgram(false,programsData);
                isSelectedArr.set(position, false);
            }
        });

    }

    @Override
    public int getItemCount() {
        return programsDataList.size();
    }


    class AddProgramToPlaylistViewHolder extends RecyclerView.ViewHolder {

        TextView tvProgramName;
        TextView tvStudentName;
        TextView tvLine;
        CircleImageView ivProfilePic;
        ToggleButton tbCheck;
        ProgramsData programsData;

        public AddProgramToPlaylistViewHolder(@NonNull View itemView, final AddProgramInterface addProgramInterface) {
            super(itemView);
            tvProgramName = itemView.findViewById(R.id.tvProgramName);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvLine = itemView.findViewById(R.id.tvLine);
            tbCheck = itemView.findViewById(R.id.tbCheck);
        }

    }
    public interface AddProgramInterface{
        void passProgram(boolean isAdd, ProgramsData programsData);
    }
}

