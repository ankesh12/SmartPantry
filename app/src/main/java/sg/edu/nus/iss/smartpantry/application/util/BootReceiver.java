package sg.edu.nus.iss.smartpantry.application.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import java.util.Calendar;

import sg.edu.nus.iss.smartpantry.service.NotificationService;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Notification set up
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        System.out.print("Hours: " + c.getTime().getHours());
        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent i = new Intent(context, NotificationService.class);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        am.cancel(pi);


        // by my own convention, minutes <= 0 means notifications are disabled
        if (c.getTime().getHours() >= 14 ) {
            int minutes = 60*24*60;
            am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 13*60*60,
                    AlarmManager.INTERVAL_DAY, pi);
        }
    }
}
