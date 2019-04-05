package daniel.rad.radiotabsdrawer;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import daniel.rad.radiotabsdrawer.myMediaPlayer.client.MediaBrowserHelper;
import daniel.rad.radiotabsdrawer.myMediaPlayer.service.MusicService;
import daniel.rad.radiotabsdrawer.myMediaPlayer.ui.MediaSeekBar;
import daniel.rad.radiotabsdrawer.radioFragments.RadioTopFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class MediaPlayerFragment extends Fragment {

    public ImageView bnPlay;
    ImageView bnBack;
    ImageView bnForward;
    TextView tvProgramName;
    TextView tvStudentName;
    TextView tvPlayerLine;
    public MediaSeekBar sbSong;
    public TextView tvTime;
    ProgressBar pbLoading;

    public MediaBrowserHelper mMediaBrowserHelper;

    static public boolean mIsPlaying;
//    static public boolean mIsLoading;

    public MediaPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media_player, container, false);

        initBnPlay(view);
        bnBack = view.findViewById(R.id.bnBack);
        bnForward = view.findViewById(R.id.bnForward);
        sbSong = view.findViewById(R.id.sbSong);
        initTvTime(view);

        tvProgramName = view.findViewById(R.id.tvProgramName);
        tvStudentName = view.findViewById(R.id.tvStudentName);
        tvPlayerLine = view.findViewById(R.id.tvPlayerLine);
        initProgressBar(view);


        bnBack.setOnClickListener(v -> {
//            mIsLoading = true;
            setProgressBarVisible();
            bnBack.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).withEndAction(() -> {
                bnBack.animate().scaleX(1).scaleY(1).setDuration(200);
                mMediaBrowserHelper.getTransportControls().skipToPrevious();
                bnPlay.setImageResource(R.drawable.ic_pause);
            });
        });

        bnForward.setOnClickListener(v -> {
//            mIsLoading = true;
            setProgressBarVisible();
            bnForward.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).withEndAction(() -> {
                bnForward.animate().scaleX(1).scaleY(1).setDuration(200);
                mMediaBrowserHelper.getTransportControls().skipToNext();
                bnPlay.setImageResource(R.drawable.ic_pause);
            });
        });

        bnPlay.setOnClickListener(v -> {
//            mIsLoading = true;
            setProgressBarVisible();
            playFunction();
        });

        mMediaBrowserHelper = new MediaPlayerFragment.MediaBrowserConnection(getContext());
        mMediaBrowserHelper.registerCallback(new MediaPlayerFragment.MediaBrowserListener());

        return view;
    }

    public void initProgressBar(View view) {
        pbLoading = view.findViewById(R.id.pbLoading);
    }

    public void setProgressBarVisible(){
        pbLoading.setVisibility(View.VISIBLE);
    }

    public void setProgressBarInvisible(){
        pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        mMediaBrowserHelper.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        sbSong.disconnectController();
        mMediaBrowserHelper.onStop();
    }

    public void initTvTime(View view){
        tvTime = view.findViewById(R.id.tvTime);
    }

    public void initBnPlay(View view){
        bnPlay = view.findViewById(R.id.ivMediaPlay);
    }

    public void initSbSong(View view){
        sbSong = view.findViewById(R.id.sbSong);
    }

    public void playFunction(){
        if (mIsPlaying) {
            mMediaBrowserHelper.getTransportControls().pause();
            bnPlay.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).withEndAction(() -> {
                bnPlay.setImageResource(R.drawable.ic_play);
                bnPlay.animate().scaleX(1).scaleY(1).setDuration(200);
            });
        } else {
            mMediaBrowserHelper.getTransportControls().play();
            bnPlay.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).withEndAction(() -> {
                bnPlay.setImageResource(R.drawable.ic_pause);
                bnPlay.animate().scaleX(1).scaleY(1).setDuration(200);
            });
        }
    }

    public void stopFunction(){

    }

    /**
     * Customize the connection to our {@link android.support.v4.media.MediaBrowserServiceCompat}
     * and implement our app specific desires.
     */
    class MediaBrowserConnection extends MediaBrowserHelper {
        private MediaBrowserConnection(Context context) {
            super(context, MusicService.class);
        }

        @Override
        protected void onConnected(@NonNull MediaControllerCompat mediaController) {
            sbSong.setMediaController(mediaController);
        }

        @Override
        protected void onChildrenLoaded(@NonNull String parentId,
                                        @NonNull List<MediaBrowserCompat.MediaItem> children) {
            super.onChildrenLoaded(parentId, children);

            final MediaControllerCompat mediaController = getMediaController();

            // Queue up all media items for this simple sample.
            for (final MediaBrowserCompat.MediaItem mediaItem : children) {
                mediaController.addQueueItem(mediaItem.getDescription());
            }

            // Call prepare now so pressing play just works.
            mediaController.getTransportControls().prepare();
        }
    }

    /**
     * Implementation of the {@link MediaControllerCompat.Callback} methods we're interested in.
     * <p>
     * Here would also be where one could override
     * {@code onQueueChanged(List<MediaSessionCompat.QueueItem> queue)} to get informed when items
     * are added or removed from the queue. We don't do this here in order to keep the UI
     * simple.
     */
    class MediaBrowserListener extends MediaControllerCompat.Callback {
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat playbackState) {
            mIsPlaying = playbackState != null &&
                    playbackState.getState() == PlaybackStateCompat.STATE_PLAYING;
            bnPlay.setPressed(mIsPlaying);
        } //TODO: change

        @Override
        public void onMetadataChanged(MediaMetadataCompat mediaMetadata) {
            if (mediaMetadata == null) {
                return;
            }
            tvProgramName.setText(
                    mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
            tvProgramName.setSelected(true);
            tvStudentName.setText(
                    mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST));
            tvStudentName.setSelected(true);
        }

        @Override
        public void onSessionDestroyed() {
            super.onSessionDestroyed();
        }

        @Override
        public void onQueueChanged(List<MediaSessionCompat.QueueItem> queue) {
            super.onQueueChanged(queue);
        }
    }
}
