package daniel.rad.radiotabsdrawer.playlist.chosenPlaylist;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import daniel.rad.radiotabsdrawer.playlist.PlaylistAdapter;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;
import daniel.rad.radiotabsdrawer.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class CreatePlaylistAdapter extends RecyclerView.Adapter<CreatePlaylistAdapter.CreatePlaylistViewHolder> {
    List<ProgramsData> programsDataList;
    Context context;

    private CreatePlaylistAdapterInterface createPlaylistAdapterInterface;

    public CreatePlaylistAdapter(List<ProgramsData> programsDataList, Context context,  CreatePlaylistAdapterInterface createPlaylistAdapterInterface) {
        this.programsDataList = programsDataList;
        this.context = context;
        this.createPlaylistAdapterInterface = createPlaylistAdapterInterface;
    }

    @NonNull
    @Override
    public CreatePlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.chose_program_to_playlist_item, viewGroup, false);

        CreatePlaylistViewHolder holder = new CreatePlaylistViewHolder(view, createPlaylistAdapterInterface);
        return holder;
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

    class CreatePlaylistViewHolder extends RecyclerView.ViewHolder {

        TextView tvProgramName;
        TextView tvStudentName;
        TextView tvLine;
        CircleImageView ivProfilePic;
        ToggleButton tbCheck;
        ProgramsData programsData;
//    ProgramsData nullProgram = null;


        public CreatePlaylistViewHolder(@NonNull View itemView, final CreatePlaylistAdapterInterface createPlaylistAdapterInterface) {
            super(itemView);
            tvProgramName = itemView.findViewById(R.id.tvProgramName);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvLine = itemView.findViewById(R.id.tvLine);
            tbCheck = itemView.findViewById(R.id.tbCheck);
//        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(itemView.getContext());
//         Intent intent = new Intent("createPlaylist");

            itemView.setOnClickListener(v -> {
                createPlaylistAdapterInterface.passProgram(programsData);
            });


            tbCheck.setOnClickListener(v -> {
                createPlaylistAdapterInterface.passProgram(programsData);
                if (tbCheck.isChecked()) {
                    tbCheck.setBackgroundResource(R.drawable.ic_radio_button_checked);
//                Log.d("Testing Playlist", "CreatePlaylistViewHolder: sent add broadcast");
//                intent.putExtra("addProgram",programsData);
//                intent.putExtra("removeProgram",nullProgram);
//                broadcastManager.sendBroadcast(intent);
                } else if (!tbCheck.isChecked()) {
                    tbCheck.setBackgroundResource(R.drawable.ic_check);
//                Log.d("Testing Playlist", "CreatePlaylistViewHolder: sent remove broadcast");
//                intent.putExtra("removeProgram",programsData);
//                intent.putExtra("addProgram",nullProgram);
//                broadcastManager.sendBroadcast(intent);
                }
            });
        }
    }
    public interface CreatePlaylistAdapterInterface {
        void passProgram(ProgramsData programsData);
    }
}