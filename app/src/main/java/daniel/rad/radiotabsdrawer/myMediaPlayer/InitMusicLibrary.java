package daniel.rad.radiotabsdrawer.myMediaPlayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import daniel.rad.radiotabsdrawer.DrawerActivity;
import daniel.rad.radiotabsdrawer.MainActivity;
import daniel.rad.radiotabsdrawer.myMediaPlayer.service.contentcatalogs.MusicLibrary;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;


public class InitMusicLibrary extends AsyncTask<Void,Void, List<ProgramsData>> {
    private ArrayList<ProgramsData> programs;
    private WeakReference<MainActivity> mainActivityWeakReference;
    private int i;

    public InitMusicLibrary(ArrayList<ProgramsData> programs, WeakReference<MainActivity> mainActivity,int i) {
        this.programs = programs;
        this.mainActivityWeakReference = mainActivity;
        this.i = i;
    }

    @Override
    protected List<ProgramsData> doInBackground(Void... voids) {
        MediaPlayer mp = new MediaPlayer();
        for (int t = 0 ;t < 1; t++) {
            ProgramsData model = programs.get(i);
            mp.reset();
            try {
                mp.setDataSource(model.getMediaSource());
                mp.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            MusicLibrary.createMediaMetadataCompat(
                    model.getVodId(),
                    model.getProgramName(),
                    model.getStudentName(),
                    mp.getDuration(),
                    model.getDurationUnit(),
                    model.getMediaSource(),
                    model.getProfilePic(),
                    model.getCreationDate(),
                    false
            );
            i++;
            System.out.println(model);
        }
        return programs;
    }

    @Override
    protected void onPostExecute(List<ProgramsData> programsData) {
        super.onPostExecute(programsData);

        Intent intent = new Intent(mainActivityWeakReference.get(), DrawerActivity.class);
        mainActivityWeakReference.get().startActivity(intent);
        mainActivityWeakReference.get().finish();
    }
}
