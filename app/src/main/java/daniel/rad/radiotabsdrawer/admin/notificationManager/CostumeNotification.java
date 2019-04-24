package daniel.rad.radiotabsdrawer.admin.notificationManager;

import android.os.Parcel;
import android.os.Parcelable;

public class CostumeNotification implements Parcelable {
    private String title;
    private String text;

    public CostumeNotification(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "CostumeNotification{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.text);
    }

    protected CostumeNotification(Parcel in) {
        this.title = in.readString();
        this.text = in.readString();
    }

    public static final Parcelable.Creator<CostumeNotification> CREATOR = new Parcelable.Creator<CostumeNotification>() {
        @Override
        public CostumeNotification createFromParcel(Parcel source) {
            return new CostumeNotification(source);
        }

        @Override
        public CostumeNotification[] newArray(int size) {
            return new CostumeNotification[size];
        }
    };
}
