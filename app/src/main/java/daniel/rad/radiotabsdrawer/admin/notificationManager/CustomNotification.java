package daniel.rad.radiotabsdrawer.admin.notificationManager;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomNotification implements Parcelable {
    private String title;
    private String text;

    public CustomNotification(String title, String text) {
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
        return "CustomNotification{" +
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

    protected CustomNotification(Parcel in) {
        this.title = in.readString();
        this.text = in.readString();
    }

    public static final Parcelable.Creator<CustomNotification> CREATOR = new Parcelable.Creator<CustomNotification>() {
        @Override
        public CustomNotification createFromParcel(Parcel source) {
            return new CustomNotification(source);
        }

        @Override
        public CustomNotification[] newArray(int size) {
            return new CustomNotification[size];
        }
    };
}
