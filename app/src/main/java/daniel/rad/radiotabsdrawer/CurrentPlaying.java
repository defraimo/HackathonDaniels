package daniel.rad.radiotabsdrawer;

import java.util.ArrayList;

import daniel.rad.radiotabsdrawer.myMediaPlayer.ProgramsReceiver;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;

public class CurrentPlaying {
    private static ArrayList<ProgramsData> currentPlaying;

    private CurrentPlaying() {
    }

    public static ArrayList<ProgramsData> getCurrentPlaying(){
        currentPlaying = new ArrayList<>();
        ProgramsData programsData = ProgramsReceiver.getPrograms().get(0);
        if (DataHolder.getInstance().getPassedProgramsData() != null){
            programsData = DataHolder.getInstance().getPassedProgramsData();
        }
        currentPlaying.add(programsData);
        return currentPlaying;
    }
}
