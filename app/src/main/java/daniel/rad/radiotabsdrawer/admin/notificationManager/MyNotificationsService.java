package daniel.rad.radiotabsdrawer.admin.notificationManager;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import daniel.rad.radiotabsdrawer.MainActivity;
import daniel.rad.radiotabsdrawer.R;

//like service but works Async
public class MyNotificationsService extends IntentService {
    boolean isShowed;

    private DatabaseReference notifications =
            FirebaseDatabase.getInstance()
                    .getReference("Notifications");

    int argNum;
    String title;
    String text;

    public MyNotificationsService() {
        super("MyNotificationsService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        notifications.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                argNum = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (argNum == 0){
                        text = (String) child.getValue();
                    }
                    else if (argNum ==1){
                        title = (String) child.getValue();
                    }
                    argNum++;
                }

                //in order to send a Push - we must first define our apps channels:
                registerChannels();
                if (!isShowed) {
                    showNotification(title,text);
                    isShowed = true;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showNotification(String title, String text) {
        Intent intent = new Intent(MyNotificationsService.this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(MyNotificationsService.this,42,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(MyNotificationsService.this, CHANNEL_ID).
                setContentTitle(title).
                setContentText(text).
                setSmallIcon(R.drawable.ic_app_mini_icon).
                setAutoCancel(true).
                setContentIntent(pendingIntent).
                build();

//        foregroundService(notification);


        NotificationManagerCompat.from(MyNotificationsService.this).notify(123,notification);

    }

    private void foregroundService(Notification notification) {
        startForeground(123,notification);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, MyNotificationsService.class));
        }
    }

    public static final String CHANNEL_ID = "kol.etzion.MyNotificationsService.Channel";

    private void registerChannels(){
        //if less than Oreo -> no channels
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return;
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String channelName = getResources().getString(R.string.notifications);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,channelName,NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);
    }
}
