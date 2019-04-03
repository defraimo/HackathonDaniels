package daniel.rad.radiotabsdrawer.playlist.dataManagment;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import daniel.rad.radiotabsdrawer.programs.ProgramsData;

public class ProgramDAO {
    private SQLiteDatabase db;
    private static String PLAYLIST_NAME;

    public ProgramDAO(Context context,String playlistName) {
        ProgramDBHelper helper = new ProgramDBHelper(context,playlistName);
        db = helper.getWritableDatabase();
        PLAYLIST_NAME = playlistName;
    }

    private static ProgramDAO instance;

    public static ProgramDAO getInstance(Context context){
        if (instance == null){
            instance = new ProgramDAO(context,PLAYLIST_NAME);
        }
        return instance;
    }

    public void addProgram(ProgramsData programsData){
        ContentValues cv = new ContentValues();
        cv.put("programName",programsData.getProgramName());
        cv.put("studentName",programsData.getStudentName());
        cv.put("studentImage",programsData.getImage());
        cv.put("mediaSource",programsData.getMediaSource());

        db.insert(PLAYLIST_NAME,null,cv);
    }

    public void removeProgram(PlaylistSQLData playlist){
        String sql = "DELETE FROM "+PLAYLIST_NAME+" WHERE playlistsName='" +
                "" +playlist.getPlaylistName()+
                "';" +
                "";
        db.execSQL(sql);
    }
}
