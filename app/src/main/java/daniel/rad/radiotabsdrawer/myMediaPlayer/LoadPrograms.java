package daniel.rad.radiotabsdrawer.myMediaPlayer;

import android.media.MediaPlayer;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import daniel.rad.radiotabsdrawer.programs.ProgramsData;

public class LoadPrograms extends AsyncTask<Void, Void, List<ProgramsData>> {
    private MediaPlayer mp = new MediaPlayer();


    @Override
    protected List<ProgramsData> doInBackground(Void... voids) {

        List<ProgramsData> loadedPrograms = ProgramsReceiver.getPrograms();

        for (ProgramsData model : loadedPrograms) {

            mp.reset();
            try {
                mp.setDataSource(model.getMediaSource());
                mp.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            model.setDuration(mp.getDuration());
            model.setLoaded(true);
        }

        return loadedPrograms;
    }

    @Override
    protected void onPostExecute(List<ProgramsData> programsData) {
        super.onPostExecute(programsData);


    }
}
