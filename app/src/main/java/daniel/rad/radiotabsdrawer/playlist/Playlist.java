package daniel.rad.radiotabsdrawer.playlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import daniel.rad.radiotabsdrawer.programs.ProgramsData;

public class Playlist implements Parcelable{
    private String name;
    private List<ProgramsData> programsData;

    public Playlist(String name, List<ProgramsData> programsData) {
        this.name = name;
        this.programsData = programsData;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<ProgramsData> getProgramsData() {
        return programsData;
    }
    public void setProgramsData(List<ProgramsData> programsData) {
        this.programsData = programsData;
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "name='" + name + '\'' +
                ", programsData=" + programsData +
                '}';
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeList(this.programsData);
    }

    protected Playlist(Parcel in) {
        this.name = in.readString();
        this.programsData = new ArrayList<ProgramsData>();
        in.readList(this.programsData, ProgramsData.class.getClassLoader());
    }

    public static final Parcelable.Creator<Playlist> CREATOR = new Parcelable.Creator<Playlist>() {
        @Override
        public Playlist createFromParcel(Parcel source) {
            return new Playlist(source);
        }

        @Override
        public Playlist[] newArray(int size) {
            return new Playlist[size];
        }
    };
}
