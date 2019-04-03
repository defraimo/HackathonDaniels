package daniel.rad.radiotabsdrawer.playlist.dataManagment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlaylistDBHelper extends SQLiteOpenHelper {

    public static final String DB_PLAYLIST_NAME = "PlaylistDB";
    public static final int DB_VERSION = 1;

    public PlaylistDBHelper(Context context) {
        super(context, DB_PLAYLIST_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE PlaylistsNames(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "playlistName TEXT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("INSERT INTO Tem SELECT * FROM PlaylistNames;");
        db.execSQL("DROP TABLE PlaylistNames");
        db.execSQL("INSERT INTO PlaylistNames SELECT * FROM Tem;");
    }
}
