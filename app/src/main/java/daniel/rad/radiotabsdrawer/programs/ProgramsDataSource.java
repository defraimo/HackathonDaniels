package daniel.rad.radiotabsdrawer.programs;

import java.util.ArrayList;

import daniel.rad.radiotabsdrawer.R;

public class ProgramsDataSource {
    public static ArrayList<ProgramsData> getPrograms(){
        ArrayList<ProgramsData> programs = new ArrayList<>();


        programs.add(new ProgramsData("ביולוגיה","אח יקר לחיפוש","mediaSource", R.drawable.profile_pic_demo));
        programs.add(new ProgramsData("כימיה","אח יקר","mediaSource", R.drawable.profile_pic2));
        programs.add(new ProgramsData("אנגלית","אח יקר","mediaSource", R.drawable.profile_pic3));
        programs.add(new ProgramsData("גיאוגרפיה","אח יקר","mediaSource", R.drawable.profile_pic4));
        programs.add(new ProgramsData("תנך","אח יקר","mediaSource", R.drawable.profile_pic4));
        programs.add(new ProgramsData("פיזיקה","אח יקר","mediaSource", R.drawable.profile_pic4));


        return programs;
    }
}