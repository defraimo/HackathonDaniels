package daniel.rad.radiotabsdrawer.playlist.removeProgramsFromPlaylist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;
import de.hdodenhof.circleimageview.CircleImageView;

public class RemovePlaylistAdapter extends RecyclerView.Adapter<RemovePlaylistViewHolder> {
    List<ProgramsData> programsDataList;
    Context context;

    public RemovePlaylistAdapter(List<ProgramsData> programsDataList, Context context) {
        this.programsDataList = programsDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public RemovePlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.program_in_playlist_item, viewGroup, false);
        return new RemovePlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RemovePlaylistViewHolder holder, int i) {
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

class RemovePlaylistViewHolder extends RecyclerView.ViewHolder {

    TextView tvProgramName;
    TextView tvStudentName;
    TextView tvLine;
    CircleImageView ivProfilePic;
    ImageView ivPlay;
    ProgramsData programsData;

    public RemovePlaylistViewHolder(@NonNull View itemView) {
        super(itemView);
        tvProgramName = itemView.findViewById(R.id.tvProgramName);
        tvStudentName = itemView.findViewById(R.id.tvStudentName);
        ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
        tvLine = itemView.findViewById(R.id.tvLine);
        ivPlay = itemView.findViewById(R.id.ivPlay);
        ivPlay.setImageResource(R.drawable.ic_delete_red);

        ivPlay.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("האם אתה בטוח שברצונך למחוק את התוכנית מרשימת ההשמעה?").
                    setPositiveButton("כן", (dialog, which) -> {

                    }).
                    setNegativeButton("לא", (dialog, which) -> {

                    }).show();

            String mediaSourse = programsData.getMediaSource();
            //TODO: play the media player and change the icon to pause
        });
    }
}
