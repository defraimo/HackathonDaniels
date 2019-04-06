package daniel.rad.radiotabsdrawer;

import daniel.rad.radiotabsdrawer.programs.ProgramsData;

public class DataHolder {
    private static DataHolder dataHolder = null;

    private DataHolder() {
    }

    public static DataHolder getInstance() {
        if(dataHolder ==null){
            dataHolder = new DataHolder();
        }
        return dataHolder;
    }

    private ProgramsData PassedProgramsData;

    public ProgramsData getPassedProgramsData() {
        return PassedProgramsData;
    }

    public void setPassedProgramsData(ProgramsData passedProgramsData) {
        PassedProgramsData = passedProgramsData;
    }
}
