/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package daniel.rad.radiotabsdrawer.myMediaPlayer.service.contentcatalogs;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import daniel.rad.radiotabsdrawer.BuildConfig;
import daniel.rad.radiotabsdrawer.CurrentPlaying;
import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.myMediaPlayer.ProgramsReceiver;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;


public class MusicLibrary {

    private static final TreeMap<String, MediaMetadataCompat> music = new TreeMap<>();
    private static final HashMap<String, Integer> albumRes = new HashMap<>();
    private static final HashMap<String, String> musicFileName = new HashMap<>();
    private static List<ProgramsData> programs;

//    static {
//        createMediaMetadataCompat(
//                "Jazz_In_Paris",
//                "Jazz in Paris",
//                "Media Right Productions",
//                103,
//                TimeUnit.SECONDS,
//                "jazz_in_paris.mp3",
//                R.drawable.profile_pic2,
//                "album_jazz_blues");
//        createMediaMetadataCompat(
//                "The_Coldest_Shoulder",
//                "The Coldest Shoulder",
//                "The 126ers",
//                160,
//                TimeUnit.SECONDS,
//                "the_coldest_shoulder.mp3",
//                R.drawable.profile_pic3,
//                "album_youtube_audio_library_rock_2");
//    }

    /*private String vodId;
    private String programName;
    private String studentName;
    private long duration;
    private TimeUnit durationUnit;
    private String mediaSource;
    private int profilePic;
    private long creationDate;*/

    static {
        getBroadcasts();
        for (int i = 0; i < programs.size(); i++) {
            ProgramsData model = programs.get(i);
            createMediaMetadataCompat(
                   model.getVodId(),
                    model.getProgramName(),
                    model.getStudentName(),
                    getDuration(model),
                    model.getDurationUnit(),
                    model.getMediaSource(),
                    model.getProfilePic(),
                    model.getCreationDate(),
                    false
            );
        }
    }

    private static void getBroadcasts(){
        programs = ProgramsReceiver.getPrograms();
        //init different program to the mediaPlayer every time the app runs
        Collections.shuffle(programs);
    }

//    public void apdaterCurrentPlaying(Context context){
//        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver,new IntentFilter("currentProgram")); //TODO: check the context
//        for (int i = 0; i < programs.size(); i++) {
//            ProgramsData model = programs.get(i);
//            createMediaMetadataCompat(
//                    model.getVodId(),
//                    model.getProgramName(),
//                    model.getStudentName(),
//                    getDuration(model),
//                    model.getDurationUnit(),
//                    model.getMediaSource(),
//                    model.getProfilePic(),
//                    model.getCreationDate()
//            );
//        }
//    }
//
//    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            ProgramsData program = intent.getParcelableExtra("program");
//            programs.clear();
//            programs.add(program);
//        }
//    };

    public static long getDuration(ProgramsData model){
        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource(model.getMediaSource());
            mp.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

            }
        });
        return mp.getDuration();
    }

    public static String getRoot() {
        return "root";
    }

    private static String getAlbumArtUri(String albumArtResName) {
        return ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                BuildConfig.APPLICATION_ID + "/drawable/" + albumArtResName;
    }

    public static String getMusicFilename(String mediaId) {
        return musicFileName.containsKey(mediaId) ? musicFileName.get(mediaId) : null;
    }

    private static int getAlbumRes(String mediaId) {
        return albumRes.containsKey(mediaId) ? albumRes.get(mediaId) : 0;
    }

    public static Bitmap getAlbumBitmap(Context context, String mediaId){
        return BitmapFactory.decodeResource(context.getResources(),
                MusicLibrary.getAlbumRes(mediaId));
    }

    public static List<MediaBrowserCompat.MediaItem> getMediaItems() {
        List<MediaBrowserCompat.MediaItem> result = new ArrayList<>();
        for (MediaMetadataCompat metadata : music.values()) {
            result.add(
                    new MediaBrowserCompat.MediaItem(
                            metadata.getDescription(), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE));
        }
        return result;
    }

//    public static MediaMetadataCompat getMetadata(Context context, String mediaId) {
//        MediaMetadataCompat metadataWithoutBitmap = music.get(mediaId);
//        Bitmap albumArt = getAlbumBitmap(context, mediaId);
//
//        // Since MediaMetadataCompat is immutable, we need to create a copy to set the album art.
//        // We don't set it initially on all items so that they don't take unnecessary memory.
//        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
//        for (String key :
//                new String[]{
//                        MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
//                        MediaMetadataCompat.METADATA_KEY_ALBUM,
//                        MediaMetadataCompat.METADATA_KEY_ARTIST,
//                        MediaMetadataCompat.METADATA_KEY_GENRE,
//                        MediaMetadataCompat.METADATA_KEY_TITLE
//                }) {
//            builder.putString(key, metadataWithoutBitmap.getString(key));
//        }
//        builder.putLong(
//                MediaMetadataCompat.METADATA_KEY_DURATION,
//                metadataWithoutBitmap.getLong(MediaMetadataCompat.METADATA_KEY_DURATION));
//        builder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt);
//        return builder.build();
//    }

    public static MediaMetadataCompat getMetadata(Context context, String mediaId) {
//        if (mediaId.length() > 4){
//            return music.get(mediaId);
//        }
        MediaMetadataCompat metadataWithoutBitmap = music.get("i");
//        Bitmap albumArt = getAlbumBitmap(context, mediaId);
//
//        // Since MediaMetadataCompat is immutable, we need to create a copy to set the album art.
//        // We don't set it initially on all items so that they don't take unnecessary memory.
//        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
//        for (String key :
//                new String[]{
//                        MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
//                        MediaMetadataCompat.METADATA_KEY_ALBUM,
//                        MediaMetadataCompat.METADATA_KEY_ARTIST,
//                        MediaMetadataCompat.METADATA_KEY_GENRE,
//                        MediaMetadataCompat.METADATA_KEY_TITLE
//                }) {
//            builder.putString(key, metadataWithoutBitmap.getString(key));
//        }
//        builder.putLong(
//                MediaMetadataCompat.METADATA_KEY_DURATION,
//                metadataWithoutBitmap.getLong(MediaMetadataCompat.METADATA_KEY_DURATION));
//        builder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt);
        return metadataWithoutBitmap;
    }

    public static void createMediaMetadataCompat(
            String programID,
            String programTitle,
            String studentName,
            long duration,
            TimeUnit durationUnit,
            String musicFilename,
            int profilePic,
            String creationDate,
            boolean isOne) {
        if (isOne)
            music.clear();
        music.put(
                "i",
                new MediaMetadataCompat.Builder()
                        .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, programID)
                        .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, studentName)
                        .putLong(MediaMetadataCompat.METADATA_KEY_DURATION,
                                 TimeUnit.MILLISECONDS.convert(duration, durationUnit))
//                        .putString(
//                                MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI,
//                                getAlbumArtUri(musicFilename))
//                        .putString(
//                                MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI,
//                                getAlbumArtUri(musicFilename))
                        .putString(MediaMetadataCompat.METADATA_KEY_TITLE, programTitle)
                        .putString(MediaMetadataCompat.METADATA_KEY_DATE,creationDate)
                        .build());
        albumRes.put(programID, profilePic);
        musicFileName.put(programID, musicFilename);
    }
}