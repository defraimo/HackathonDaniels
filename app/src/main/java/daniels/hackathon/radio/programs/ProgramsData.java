package daniels.hackathon.radio.programs;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import daniels.hackathon.radio.R;

public class ProgramsData implements Parcelable {

    private String vodId;
    private String programName;
    private String studentName;
    private long duration;
    private TimeUnit durationUnit;
    private String mediaSource;
    private int profilePic;
    private long creationDate;
    private boolean isLoaded;
    private int numOfPlays;

    public ProgramsData(String programName, String studentName, String mediaSource, int profilePic) {
        this.programName = programName;
        this.studentName = studentName;
        this.mediaSource = mediaSource;
        this.profilePic = profilePic;
        duration = 0;
        creationDate = 0;
    }

    public ProgramsData(String vodId, String programName, String studentName, long duration, String mediaSource, int profilePic, long creationDate) {
        this.vodId = vodId;
        this.programName = programName;
        this.studentName = studentName;
        this.duration = duration;
        this.durationUnit = TimeUnit.MILLISECONDS;
        this.mediaSource = mediaSource;
        if (profilePic == 0)
        this.profilePic = R.drawable.ic_default_pic;
        else this.profilePic = profilePic;
        this.creationDate = creationDate;
    }
    public ProgramsData(){}

    public String getProgramName() {
        return programName;
    }
    public int getProfilePic() {
        return profilePic;
    }
    public void setProfilePic(int profilePic) {
        this.profilePic = profilePic;
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
    public long getDuration() {
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
//    public void setDuration(long duration) {
//        this.duration = duration;
//    }
    public TimeUnit getDurationUnit() {
        return durationUnit;
    }
    public void setDurationUnit(TimeUnit durationUnit) {
        this.durationUnit = durationUnit;
    }


    @Override
    public String toString() {
        return "ProgramsData{" +
                "vodId='" + vodId + '\'' +
                ", programName='" + programName + '\'' +
                ", studentName='" + studentName + '\'' +
                ", duration=" + duration +
                ", durationUnit=" + durationUnit +
                ", mediaSource='" + mediaSource + '\'' +
                ", profilePic=" + profilePic +
                ", creationDate=" + creationDate +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.vodId);
        dest.writeString(this.programName);
        dest.writeString(this.studentName);
        dest.writeLong(this.duration);
        dest.writeInt(this.durationUnit == null ? -1 : this.durationUnit.ordinal());
        dest.writeString(this.mediaSource);
        dest.writeInt(this.profilePic);
        dest.writeLong(this.creationDate);
    }

    protected ProgramsData(Parcel in) {
        this.vodId = in.readString();
        this.programName = in.readString();
        this.studentName = in.readString();
        this.duration = in.readLong();
        int tmpDurationUnit = in.readInt();
        this.durationUnit = tmpDurationUnit == -1 ? null : TimeUnit.values()[tmpDurationUnit];
        this.mediaSource = in.readString();
        this.profilePic = in.readInt();
        this.creationDate = in.readLong();
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

    public void setLoaded(boolean loaded) {
        this.isLoaded = loaded;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public int getNumOfPlays() {
        return numOfPlays;
    }

    public void setNumOfPlays(int numOfPlays) {
        this.numOfPlays = numOfPlays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProgramsData that = (ProgramsData) o;
        return duration == that.duration &&
                profilePic == that.profilePic &&
                creationDate == that.creationDate &&
                isLoaded == that.isLoaded &&
                numOfPlays == that.numOfPlays &&
                Objects.equals(vodId, that.vodId) &&
                Objects.equals(programName, that.programName) &&
                Objects.equals(studentName, that.studentName) &&
                durationUnit == that.durationUnit &&
                Objects.equals(mediaSource, that.mediaSource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vodId, programName, studentName, duration, durationUnit, mediaSource, profilePic, creationDate, isLoaded, numOfPlays);
    }
}





