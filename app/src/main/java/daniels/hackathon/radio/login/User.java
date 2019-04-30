package daniels.hackathon.radio.login;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String username;
    private String email;
    private String userID;
    private String userPic;
    private Uri uriPic;

    public User() {
    }

    public User(String username, String email, String userID, String userPic) {
        this.username = username;
        this.email = email;
        this.userID = userID;
        this.userPic = userPic;
    }

    public User(String username, String email, String userID) {
        this.username = username;
        this.email = email;
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getUserPic() {
        return userPic;
    }
    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", userID='" + userID + '\'' +
                ", userPic=" + userPic +
                '}';
    }

    public Uri getUriPic() {
        return uriPic;
    }
    public void setUriPic(Uri uriPic) {
        this.uriPic = uriPic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.email);
        dest.writeString(this.userID);
        dest.writeString(this.userPic);
    }

    protected User(Parcel in) {
        this.username = in.readString();
        this.email = in.readString();
        this.userID = in.readString();
        this.userPic = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
