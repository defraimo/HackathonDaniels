package daniel.rad.radiotabsdrawer.playlist.dataManagment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProgramDBHelper extends SQLiteOpenHelper {

    public static final String DB_PLAYLIST_NAME = "ProgramsDB";
    public static final int DB_VERSION = 1;
    public String playlistName;

    public ProgramDBHelper(Context context,String playlistName) {
        super(context, DB_PLAYLIST_NAME, null, DB_VERSION);
        this.playlistName = playlistName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //making a table named with the playlist name
        String sql = "CREATE TABLE "+playlistName+"(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "programName TEXT" +
                "studentName TEXT" +
                "studentImage TEXT" +
                "mediaSource TEXT" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql1 = "INSERT INTO Tem SELECT * FROM "+playlistName+";";
        String sql2 = "DROP TABLE "+playlistName+";";
        String sql3 = "INSERT INTO "+playlistName+" SELECT * FROM Tem;";
        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
    }
}
