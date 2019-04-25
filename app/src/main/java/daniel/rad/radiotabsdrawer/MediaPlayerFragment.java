package daniel.rad.radiotabsdrawer;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import daniel.rad.radiotabsdrawer.login.User;
import daniel.rad.radiotabsdrawer.myMediaPlayer.ProgramsReceiver;
import daniel.rad.radiotabsdrawer.myMediaPlayer.client.MediaBrowserHelper;
import daniel.rad.radiotabsdrawer.myMediaPlayer.service.MusicService;
import daniel.rad.radiotabsdrawer.myMediaPlayer.service.contentcatalogs.MusicLibrary;
import daniel.rad.radiotabsdrawer.myMediaPlayer.ui.MediaSeekBar;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;


/**
 * A simple {@link Fragment} subclass.
 */
public class MediaPlayerFragment extends Fragment {

    public ImageView bnPlay;
    ImageView bnBack;
    ImageView bnForward;
    TextView tvProgramName;
    public MediaSeekBar sbSong;
    public TextView tvTime;
    ProgressBar pbLoading;

    public MediaBrowserHelper mMediaBrowserHelper;

    ProgramsData programsData;
    boolean fromUser;

    public static String currentlyPlayingProgram;
    private ProgramsData nextProgramToPlay;

    //to prevent from the song to start playing when the app is first on
    static public boolean shouldStartPlaying = false;
    static public boolean fromPlaylist = false;

    static public boolean mIsPlaying;
//    static public boolean mIsLoading;

    ArrayList<ProgramsData> programsFromPlaylist;

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

        tvProgramName = view.findViewById(R.id.tvManagerProgramName);
        initProgressBar(view);

        bnBack.setOnClickListener(v -> {
//            mIsLoading = true;
            setProgressBarVisible();
            shouldStartPlaying = true;
            mMediaBrowserHelper.getTransportControls().stop();
            bnBack.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).withEndAction(() -> {
                bnBack.animate().scaleX(1).scaleY(1).setDuration(200);

                //getting the previous program
                List<ProgramsData> programsList;
                if (!fromPlaylist) {
                    //when no playlist selected
                    programsList = ProgramsReceiver.getPrograms();
                }
                else {
                    //when playlist selected
                    programsList = programsFromPlaylist;
                }
                for (int i = 0; i < programsList.size(); i++) {
                    if (programsList.get(i).getProgramName().equals(currentlyPlayingProgram)){
                        if (i == 0){
                            nextProgramToPlay = programsList.get(programsList.size()-1);
                        }
                        else {
                            nextProgramToPlay = programsList.get(i-1);
                        }
                    }
                }
                MusicLibrary.playingProgramsAsync(nextProgramToPlay,getContext());

                mMediaBrowserHelper.getTransportControls().skipToPrevious();
                bnPlay.setImageResource(R.drawable.ic_pause);
                makeTopRadioPlay();
            });
        });

        bnForward.setOnClickListener(v -> {
            setProgressBarVisible();
            shouldStartPlaying = true;
            mMediaBrowserHelper.getTransportControls().stop();
            bnForward.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).withEndAction(() -> {
                bnForward.animate().scaleX(1).scaleY(1).setDuration(200);

                //getting the previous program
                List<ProgramsData> programsList;
                if (!fromPlaylist) {
                    //when no playlist selected
                    programsList = ProgramsReceiver.getPrograms();
                }
                else {
                    //when playlist selected
                    programsList = programsFromPlaylist;
                }
                for (int i = 0; i < programsList.size(); i++) {
                    if (programsList.get(i).getProgramName().equals(currentlyPlayingProgram)){
                        if (programsList.size()-i == 1){
                            nextProgramToPlay = programsList.get(0);
                        }
                        else {
                            nextProgramToPlay = programsList.get(i+1);
                        }
                    }
                }
                MusicLibrary.playingProgramsAsync(nextProgramToPlay,getContext());

                mMediaBrowserHelper.getTransportControls().skipToNext();
                bnPlay.setImageResource(R.drawable.ic_pause);
                makeTopRadioPlay();
            });
        });

        bnPlay.setOnClickListener(v -> {
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
            shouldStartPlaying = false;
            mMediaBrowserHelper.getTransportControls().pause();
            bnPlay.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).withEndAction(() -> {
                bnPlay.setImageResource(R.drawable.ic_play);
                makeTopRadioPause();
                bnPlay.animate().scaleX(1).scaleY(1).setDuration(200);
            });
        } else {
            shouldStartPlaying = true;
            mMediaBrowserHelper.getTransportControls().play();
            bnPlay.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).withEndAction(() -> {
                bnPlay.setImageResource(R.drawable.ic_pause);
                makeTopRadioPlay();
                bnPlay.animate().scaleX(1).scaleY(1).setDuration(200);
            });
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            programsData = intent.getParcelableExtra("program");
            if (programsData.getProgramName().equals(currentlyPlayingProgram)) return;
            fromPlaylist = intent.getBooleanExtra("isFromPlaylist", false);
            tvProgramName.setText(programsData.getProgramName());
            setProgressBarVisible();

            shouldStartPlaying = true;

            mMediaBrowserHelper.getTransportControls().stop();

            MusicLibrary.playingProgramsAsync(programsData,getContext());

            DatabaseReference programsNumOfPlays =
                    FirebaseDatabase.getInstance()
                            .getReference("ProgramsNumOfPlays");

            fromUser = true;

            programsNumOfPlays.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!fromUser) return;

                    Long o = (Long) dataSnapshot.child(programsData.getVodId()).getValue();

                    int value = o != null ? o.intValue(): 0;
                    programsNumOfPlays
                            .child(programsData.getVodId())
                            .setValue(value+ 1)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {

                                }

                            });

                    fromUser = false;
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    };

    BroadcastReceiver playingNowReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String programToPlay = intent.getStringExtra("program");
            if (!programToPlay.equals(currentlyPlayingProgram)){
                System.out.println(programToPlay);
                shouldStartPlaying = true;
                currentlyPlayingProgram = programToPlay;
                mMediaBrowserHelper = new MediaPlayerFragment.MediaBrowserConnection(getContext());
                mMediaBrowserHelper.registerCallback(new MediaPlayerFragment.MediaBrowserListener());

                mMediaBrowserHelper.onStart();
                setProgressBarInvisible();
            }
        }
    };

    BroadcastReceiver playingPlaylistReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            programsFromPlaylist = intent.getParcelableArrayListExtra("playlist");
            System.out.println(programsFromPlaylist);
            if (programsFromPlaylist != null) {
                fromPlaylist = true;
//                if (!programsFromPlaylist.get(0).getProgramName().equals(currentlyPlayingProgram)) {
//                    shouldStartPlaying = true;
//                    currentlyPlayingProgram = programsFromPlaylist.get(0).getProgramName();
//                    mMediaBrowserHelper = new MediaPlayerFragment.MediaBrowserConnection(getContext());
//                    mMediaBrowserHelper.registerCallback(new MediaPlayerFragment.MediaBrowserListener());
//
//                    mMediaBrowserHelper.onStart();
//                    setProgressBarInvisible();
//                }
            }
        }
    };

    BroadcastReceiver isLoggedIn = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean loginStatus = intent.getBooleanExtra("loginStatus", true);
            if (!loginStatus){
                //stop the media player when the user logs out
                mMediaBrowserHelper.getTransportControls().stop();
            }
        }
    };

    public void makeTopRadioPlay(){
        Intent intent = new Intent("currentPlayerStatus");
        intent.putExtra("playingStatus","playing");
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    public void makeTopRadioPause(){
        Intent intent = new Intent("currentPlayerStatus");
        intent.putExtra("playingStatus","paused");
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(playingNowReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(isLoggedIn);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(playingPlaylistReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver,new IntentFilter("currentProgram"));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(playingNowReceiver,new IntentFilter("programToPlay"));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(isLoggedIn,new IntentFilter("loggedOut"));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(playingPlaylistReceiver,new IntentFilter("currentlyPlayingPlaylist"));
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

//            if (getMediaController() == null) return;
            final MediaControllerCompat mediaController = getMediaController();

            // Queue up all media items for this simple sample.
            for (final MediaBrowserCompat.MediaItem mediaItem : children) {
                mediaController.addQueueItem(mediaItem.getDescription());
            }

            // Call prepare now so pressing play just works.
            mediaController.getTransportControls().prepare();

            if (MediaPlayerFragment.shouldStartPlaying) {
                mediaController.getTransportControls().play();
                setProgressBarVisible();
            }
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
            Log.d("Log media", "onPlaybackStateChanged: ");
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
