package daniel.rad.radiotabsdrawer.playlist.dataManagment;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class PlaylistDAO {
    private SQLiteDatabase db;

    public PlaylistDAO(Context context) {
        PlaylistDBHelper dbHelper = new PlaylistDBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    private static PlaylistDAO instance;

    public static PlaylistDAO getInstance(Context context) {
        if (instance == null){
            instance = new PlaylistDAO(context);
        }
        return instance;
    }

    public void addPlaylist(PlaylistSQLData playlist){
        ContentValues cv = new ContentValues();
        cv.put("playlistName",playlist.getPlaylistName());

        db.insert("PlaylistsNames",null,cv);
    }

    public void removePlaylist(PlaylistSQLData playlist){
        db.execSQL("DELETE FROM PlaylistsNames WHERE playlistsName='" +
                "" +playlist.getPlaylistName()+
                "';" +
                "");
    }
}
