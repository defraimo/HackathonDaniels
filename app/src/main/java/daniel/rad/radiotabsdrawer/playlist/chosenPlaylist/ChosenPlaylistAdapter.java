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
    CircleImageView ivProfilePic;
    ImageView ivPlay;
    ProgramsData programsData;
    MediaSeekBar sbSong;

    public ChosenPlaylistViewHolder(@NonNull View itemView) {
        super(itemView);
        tvProgramName = itemView.findViewById(R.id.tvProgramName);
        ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
        ivPlay = itemView.findViewById(R.id.ivPlay);
        sbSong = itemView.findViewById(R.id.sbSong);



        ivPlay.setOnClickListener(v -> {
                if (MediaPlayerFragment.mIsPlaying)
                mediaPlayerFragment.initBnPlay(itemView.getRootView());
                mediaPlayerFragment.playFunction();

            String mediaSourse = programsData.getMediaSource();
            //TODO: play the media player and change the icon to pause
        });
    }
}
