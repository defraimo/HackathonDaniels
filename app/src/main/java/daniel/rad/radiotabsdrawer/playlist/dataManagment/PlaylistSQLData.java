package daniel.rad.radiotabsdrawer.playlist.dataManagment;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Objects;

import daniel.rad.radiotabsdrawer.programs.ProgramsData;

public class PlaylistSQLData implements Parcelable {
    private String playlistName;
    private ArrayList<ProgramsData> programsInPlaylist;

    public PlaylistSQLData(String playlistName) {
        this.playlistName = playlistName;
        programsInPlaylist = new ArrayList<>();
    }

    public String getPlaylistName() {
        return playlistName;
    }
    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    @Override
    public String toString() {
        return "PlaylistSQLData{" +
                "playlistName='" + playlistName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaylistSQLData that = (PlaylistSQLData) o;
        return Objects.equals(playlistName, that.playlistName) &&
                Objects.equals(programsInPlaylist, that.programsInPlaylist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playlistName, programsInPlaylist);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.playlistName);
    }

    protected PlaylistSQLData(Parcel in) {
        this.playlistName = in.readString();
    }

    public static final Parcelable.Creator<PlaylistSQLData> CREATOR = new Parcelable.Creator<PlaylistSQLData>() {
        @Override
        public PlaylistSQLData createFromParcel(Parcel source) {
            return new PlaylistSQLData(source);
        }

        @Override
        public PlaylistSQLData[] newArray(int size) {
            return new PlaylistSQLData[size];
        }
    };


    public void addProgramToPlaylist(ProgramsData program){
        for (ProgramsData programsData : programsInPlaylist) {
            //if there is already such a program in the playlist don't add it and return
            if (programsData.equals(program)) return;
        }
        programsInPlaylist.add(program);

    }

    public void removeProgramFromPlaylist(ProgramsData program){
        for (ProgramsData programsData : programsInPlaylist) {
            if (programsData.equals(program)){
                programsInPlaylist.remove(program);
            }
        }
    }
}
