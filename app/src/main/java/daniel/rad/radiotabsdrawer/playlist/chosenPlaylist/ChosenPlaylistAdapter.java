package daniel.rad.radiotabsdrawer.playlist.chosenPlaylist;

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

import daniel.rad.radiotabsdrawer.MediaPlayerFragment;
import daniel.rad.radiotabsdrawer.myMediaPlayer.client.MediaBrowserHelper;
import daniel.rad.radiotabsdrawer.myMediaPlayer.ui.MediaSeekBar;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;
import daniel.rad.radiotabsdrawer.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChosenPlaylistAdapter extends RecyclerView.Adapter<ChosenPlaylistViewHolder>{
    private MediaPlayerFragment playerFragment;
    private List<ProgramsData> programsDataList;
    private Context context;

    public ChosenPlaylistAdapter(List<ProgramsData> programsDataList, Context context) {
        playerFragment = new MediaPlayerFragment();
        this.programsDataList = programsDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChosenPlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.program_in_playlist_item, viewGroup, false);
        return new ChosenPlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChosenPlaylistViewHolder holder, int i) {
        ProgramsData programsData = programsDataList.get(i);

        holder.tvProgramName.setText(programsData.getProgramName());
        if (programsData.getStudentName() != null)
        holder.tvStudentName.setText(programsData.getStudentName());
        else holder.tvLine.setText(""); //if there is no student the line won't be printed

        holder.ivProfilePic.setImageResource(programsData.getProfilePic());

        holder.programsData = programsData;
        holder.mediaPlayerFragment = playerFragment;
    }

    @Override
    public int getItemCount() {
        return programsDataList.size();
    }
}
class ChosenPlaylistViewHolder extends RecyclerView.ViewHolder{

    MediaPlayerFragment mediaPlayerFragment;
    MediaBrowserHelper mMediaBrowserHelper;

    TextView tvProgramName;
    TextView tvStudentName;
    TextView tvLine;
    CircleImageView ivProfilePic;
    ImageView ivPlay;
    ProgramsData programsData;
    MediaSeekBar sbSong;
    private boolean isDoublePressed = false;

    public ChosenPlaylistViewHolder(@NonNull View itemView) {
        super(itemView);
        tvProgramName = itemView.findViewById(R.id.tvProgramName);
        tvStudentName = itemView.findViewById(R.id.tvStudentName);
        ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
        tvLine = itemView.findViewById(R.id.tvLine);
        ivPlay = itemView.findViewById(R.id.ivPlay);
        sbSong = itemView.findViewById(R.id.sbSong);

        itemView.setOnLongClickListener(v -> {
            if (!isDoublePressed) {
                ivPlay.setImageResource(R.drawable.ic_delete_red);
                isDoublePressed = true;
            }
            return true;
        });

        itemView.setOnClickListener(v -> {
            if (isDoublePressed){
                ivPlay.setImageResource(R.drawable.ic_play_grey);
                isDoublePressed = false;
            }
        });

        ivPlay.setOnClickListener(v -> {
            if (isDoublePressed){
                Animation shake = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.shake);
                itemView.startAnimation(shake);
                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setTitle("האם אתה בטוח שברצונך למחוק את התוכנית מרשימת ההשמעה?").setPositiveButton("כן", (dialog, which) -> {

                }).setNegativeButton("לא", (dialog, which) -> {

                }).show();
            }
            else {
                if (MediaPlayerFragment.mIsPlaying)
                mediaPlayerFragment.initBnPlay(itemView.getRootView());
                mediaPlayerFragment.playFunction();
            }
            String mediaSourse = programsData.getMediaSource();
            //TODO: play the media player and change the icon to pause
        });
    }
}
