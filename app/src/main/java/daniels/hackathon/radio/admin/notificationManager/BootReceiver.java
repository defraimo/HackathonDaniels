package daniels.hackathon.radio.admin.notificationManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Service Stops","Service Not Available");
        context.startService(new Intent(context, MyNotificationsService.class));
    }
}