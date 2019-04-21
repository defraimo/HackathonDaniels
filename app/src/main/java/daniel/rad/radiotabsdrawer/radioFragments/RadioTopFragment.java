package daniel.rad.radiotabsdrawer.radioFragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import daniel.rad.radiotabsdrawer.MediaPlayerFragment;
import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.login.User;
import daniel.rad.radiotabsdrawer.myMediaPlayer.ProgramsReceiver;
import daniel.rad.radiotabsdrawer.myMediaPlayer.client.MediaBrowserHelper;
import daniel.rad.radiotabsdrawer.myMediaPlayer.service.MusicService;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class RadioTopFragment extends Fragment {

    private MediaBrowserHelper mMediaBrowserHelper;

    private MediaPlayerFragment mediaPlayerFragment;

    private boolean mIsPlaying;

    ImageView ivRadioPlay;
    public TextView tvProgramTopName;
    public TextView tvStudentTopName;
    ProgressBar pbRadioPic;
    CircleImageView ivProfilePic;

    ProgramsData programsData;

    private DatabaseReference broadcastingUsers =
            FirebaseDatabase.getInstance()
                    .getReference("BroadcastingUsers");

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    public RadioTopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_radio_top, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView gifImageView = view.findViewById(R.id.ivRadio);
        ivRadioPlay = view.findViewById(R.id.ivRadioPlay);
        tvProgramTopName = view.findViewById(R.id.tvManagerProgramName);
        tvStudentTopName = view.findViewById(R.id.tvStudentName);
        pbRadioPic = view.findViewById(R.id.pbRadioPic);
        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        initTextViews(view);
        mediaPlayerFragment = new MediaPlayerFragment();

        Glide.with(this).
                asGif().
                load(R.drawable.radio_gif).
                into(gifImageView);

        mMediaBrowserHelper = new RadioTopFragment.MediaBrowserConnection(getContext());
        mMediaBrowserHelper.registerCallback(new RadioTopFragment.MediaBrowserListener());

        ivRadioPlay.setOnClickListener(v -> {
            if (mIsPlaying) {
                MediaPlayerFragment.shouldStartPlaying = false;
                mMediaBrowserHelper.getTransportControls().pause();
                ivRadioPlay.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100).withEndAction(() -> {
                    ivRadioPlay.setImageResource(R.drawable.ic_play_radio);
                    mediaPlayerFragment.initBnPlay(v.getRootView());
                    mediaPlayerFragment.bnPlay.setImageResource(R.drawable.ic_play);
                    ivRadioPlay.animate().scaleX(1).scaleY(1).setDuration(100);
                });
            } else {
                MediaPlayerFragment.shouldStartPlaying = true;
                mMediaBrowserHelper.getTransportControls().play();
                ivRadioPlay.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100).withEndAction(() -> {
                    ivRadioPlay.setImageResource(R.drawable.ic_pause_fragment);
                    ivRadioPlay.animate().scaleX(1).scaleY(1).setDuration(100);
                });
            }
        });

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            programsData = intent.getParcelableExtra("program");
            tvProgramTopName.setText(programsData.getProgramName());
            tvStudentTopName.setText(programsData.getStudentName());
            tvProgramTopName.setSelected(true);
            tvStudentTopName.setSelected(true);

            pbRadioPic.setVisibility(View.VISIBLE);
            storageRef.child("images/"+programsData.getVodId()).
                    getDownloadUrl().addOnSuccessListener(uri -> {
                pbRadioPic.setVisibility(View.INVISIBLE);
                Glide.with(getApplicationContext()).load(uri).into(ivProfilePic);
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("failed downloading pic");
                    pbRadioPic.setVisibility(View.INVISIBLE);
                    ivProfilePic.setImageResource(R.drawable.ic_default_pic);
                }
            });
        }
    };

    BroadcastReceiver mediaPlayerStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                String playingStatus = intent.getStringExtra("playingStatus");
                if (playingStatus.equals("playing")) {
                    ivRadioPlay.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100).withEndAction(() -> {
                        ivRadioPlay.setImageResource(R.drawable.ic_pause_fragment);
                        ivRadioPlay.animate().scaleX(1).scaleY(1).setDuration(100);
                    });
                } else if (playingStatus.equals("paused")) {
                    ivRadioPlay.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100).withEndAction(() -> {
                        ivRadioPlay.setImageResource(R.drawable.ic_play_radio);
                        ivRadioPlay.animate().scaleX(1).scaleY(1).setDuration(100);
                    });
                }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mediaPlayerStatusReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver,new IntentFilter("currentProgram"));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mediaPlayerStatusReceiver,new IntentFilter("currentPlayerStatus"));
    }

    private void initTextViews(View view) {
        tvProgramTopName = view.findViewById(R.id.tvManagerProgramName);
        tvStudentTopName = view.findViewById(R.id.tvStudentName);
    }

    @Override
    public void onStart() {
        super.onStart();
        mMediaBrowserHelper.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMediaBrowserHelper.onStop();
    }

    /**
     * Customize the connection to our {@link android.support.v4.media.MediaBrowserServiceCompat}
     * and implement our app specific desires.
     */
    private class MediaBrowserConnection extends MediaBrowserHelper {
        private MediaBrowserConnection(Context context) {
            super(context, MusicService.class);
        }

        @Override
        protected void onConnected(@NonNull MediaControllerCompat mediaController) {
//            sbSong.setMediaController(mediaController);
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
    private class MediaBrowserListener extends MediaControllerCompat.Callback {
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat playbackState) {
            mIsPlaying = playbackState != null &&
                    playbackState.getState() == PlaybackStateCompat.STATE_PLAYING;
            ivRadioPlay.setPressed(mIsPlaying);
        } //TODO: change

        @Override
        public void onMetadataChanged(MediaMetadataCompat mediaMetadata) {
            if (mediaMetadata == null) {
                return;
            }
            tvProgramTopName.setText(
                    mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
            tvProgramTopName.setSelected(true);
            List<ProgramsData> programs = ProgramsReceiver.getPrograms();
            for (ProgramsData program : programs) {
                if (program.getProgramName().equals(mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE))){
                    broadcastingUsers.child(program.getVodId()).
                            addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    StringBuilder students = new StringBuilder();
                                    int i = 0;
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        User user = child.getValue(User.class);
                                        students.append(user.getUsername());
                                        i++;
                                        if (i < dataSnapshot.getChildrenCount())
                                            students.append(", ");
                                    }
                                    String broadcastingStudents = students.toString();
                                    tvStudentTopName.setText(broadcastingStudents);
                                    tvStudentTopName.setSelected(true);

                                    pbRadioPic.setVisibility(View.VISIBLE);
                                    storageRef.child("images/"+program.getVodId()).
                                            getDownloadUrl().addOnSuccessListener(uri -> {
                                        pbRadioPic.setVisibility(View.INVISIBLE);
                                        Glide.with(getApplicationContext()).load(uri).into(ivProfilePic);
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            System.out.println("failed downloading pic");
                                            pbRadioPic.setVisibility(View.INVISIBLE);
                                            ivProfilePic.setImageResource(R.drawable.ic_default_pic);
                                        }
                                    });
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                }
            }

            LocalBroadcastManager sendCurrentPlaying = LocalBroadcastManager.getInstance(getContext());
            Intent intent = new Intent("currentPlayingRadio");
            intent.putExtra("programName",mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
            sendCurrentPlaying.sendBroadcast(intent);
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
