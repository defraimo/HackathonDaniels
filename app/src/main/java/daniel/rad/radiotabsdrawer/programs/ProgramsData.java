package daniel.rad.radiotabsdrawer.programs;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class ProgramsData implements Parcelable {
    private String programName;
    private String studentName;
    private int image;
    private long creationDate;
    private int duration;
    private String vodId;
    private String mediaSource;

    public ProgramsData(String programName, String studentName, String mediaSource, int image) {
        this.programName = programName;
        this.studentName = studentName;
        this.mediaSource = mediaSource;
        this.image =image;
        duration = 0;
        creationDate = 0;
    }

    public ProgramsData(String programName, String studentName, int image, long creationDate, int duration, String vodId, String mediaSource) {
        this.programName = programName;
        this.studentName = studentName;
        this.image = image;
        this.creationDate = creationDate;
        this.duration = duration;
        this.vodId = vodId;
        this.mediaSource = mediaSource;
    }

    public String getProgramName() {
        return programName;
    }
    public int getImage() {
        return image;
    }
    public void setImage(int image) {
        this.image = image;
    }
    public void setProgramName(String programName) {
        this.programName = programName;
    }
    public String getStudentName() {
        return studentName;
    }
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    public String getMediaSource() {
        return mediaSource;
    }
    public void setMediaSource(String mediaSource) {
        this.mediaSource = mediaSource;
    }
    public long getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public String getVodId() {
        return vodId;
    }
    public void setVodId(String vodId) {
        this.vodId = vodId;
    }

    @Override
    public String toString() {
        return "ProgramsData{" +
                "programName='" + programName + '\'' +
                ", studentName='" + studentName + '\'' +
                ", image=" + image +
                ", creationDate=" + creationDate +
                ", duration=" + duration +
                ", vodId='" + vodId + '\'' +
                ", mediaSource='" + mediaSource + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProgramsData that = (ProgramsData) o;
        return image == that.image &&
                creationDate == that.creationDate &&
                duration == that.duration &&
                Objects.equals(programName, that.programName) &&
                Objects.equals(studentName, that.studentName) &&
                Objects.equals(vodId, that.vodId) &&
                Objects.equals(mediaSource, that.mediaSource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(programName, studentName, image, creationDate, duration, vodId, mediaSource);
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.programName);
        dest.writeString(this.studentName);
        dest.writeInt(this.image);
        dest.writeLong(this.creationDate);
        dest.writeInt(this.duration);
        dest.writeString(this.vodId);
        dest.writeString(this.mediaSource);
    }
    protected ProgramsData(Parcel in) {
        this.programName = in.readString();
        this.studentName = in.readString();
        this.image = in.readInt();
        this.creationDate = in.readLong();
        this.duration = in.readInt();
        this.vodId = in.readString();
        this.mediaSource = in.readString();
    }
    public static final Creator<ProgramsData> CREATOR = new Creator<ProgramsData>() {
        @Override
        public ProgramsData createFromParcel(Parcel source) {
            return new ProgramsData(source);
        }

        @Override
        public ProgramsData[] newArray(int size) {
            return new ProgramsData[size];
        }
    };
}





