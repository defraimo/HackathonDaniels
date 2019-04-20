package daniel.rad.radiotabsdrawer.playlist.removeProgramsFromPlaylist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;
import de.hdodenhof.circleimageview.CircleImageView;

public class RemovePlaylistAdapter extends RecyclerView.Adapter<RemovePlaylistAdapter.RemovePlaylistViewHolder> {
    List<ProgramsData> programsDataList;
    Context context;

    private RemoveProgramInteface removeProgramInteface;

    public RemovePlaylistAdapter(List<ProgramsData> programsDataList, Context context, RemoveProgramInteface removeProgramInteface) {
        this.programsDataList = programsDataList;
        this.context = context;
        this.removeProgramInteface = removeProgramInteface;
    }

    @NonNull
    @Override
    public RemovePlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.program_in_playlist_item, viewGroup, false);
        RemovePlaylistViewHolder holder = new RemovePlaylistViewHolder(view, removeProgramInteface);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RemovePlaylistViewHolder holder, int i) {
        ProgramsData programsData = programsDataList.get(i);

        holder.tvProgramName.setText(programsData.getProgramName());

        holder.ivProfilePic.setImageResource(programsData.getProfilePic());

        holder.programsData = programsData;

    }

    @Override
    public int getItemCount() {
        return programsDataList.size();
    }


    class RemovePlaylistViewHolder extends RecyclerView.ViewHolder {

        TextView tvProgramName;
        CircleImageView ivProfilePic;
        ImageView ivPlay;
        ProgramsData programsData;

        public RemovePlaylistViewHolder(@NonNull View itemView, final RemoveProgramInteface removeProgramInteface) {
            super(itemView);
            tvProgramName = itemView.findViewById(R.id.tvProgramName);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            ivPlay = itemView.findViewById(R.id.ivPlay);
            ivPlay.setImageResource(R.drawable.ic_delete_red);

            ivPlay.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("האם אתה בטוח שברצונך למחוק את התוכנית מרשימת ההשמעה?").
                        setPositiveButton("כן", (dialog, which) -> {
                            removeProgramInteface.passProgramToRemove(programsData);
                        }).
                        setNegativeButton("לא", (dialog, which) -> {
                        }).show();

            });

        }
    }

    public interface RemoveProgramInteface{
        void passProgramToRemove(ProgramsData programsData);
    }
}