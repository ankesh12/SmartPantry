package sg.edu.nus.iss.smartpantry.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.Item;
import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.application.util.XMLUtil;
import sg.edu.nus.iss.smartpantry.dao.DAOFactory;
import sg.edu.nus.iss.smartpantry.dao.daoClass.ItemDao;
import sg.edu.nus.iss.smartpantry.dao.daoClass.ProductDao;
import sg.edu.nus.iss.smartpantry.views.activity.SPApp;
import sg.edu.nus.iss.smartpantry.views.activity.ShopCreateActivity;

public class NotificationService extends Service {
    public NotificationService() {
    }

    private PowerManager.WakeLock mWakeLock;

    /**
     * Simply return null, since our Service will not be communicating with
     * any other components. It just does its work silently.
     */
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * This is where we initialize. We call this when onStart/onStartCommand is
     * called by the system. We won't do anything with the intent here, and you
     * probably won't, either.
     */
    private void handleIntent(Intent intent) {
        // obtain the wake lock
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Wake Up");
        mWakeLock.acquire();

        // check the global background data setting
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (!cm.getBackgroundDataSetting()) {
            stopSelf();
            return;
        }

        // do the actual work, in a separate thread
        new PollTask().execute();
    }

    private class PollTask extends AsyncTask<Void, Void, List<Item>> {
        /**
         * This is where YOU do YOUR work. There's nothing for me to write here
         * you have to fill this in. Make your HTTP request(s) or whatever it is
         * you have to do to get your updates in here, because this is run in a
         * separate thread
         */
        @Override
        protected List<Item> doInBackground(Void... params) {
            ProductDao prodDao = DAOFactory.getProductDao(getApplicationContext());
            ItemDao itemDao = DAOFactory.getItemDao(getApplicationContext());
            List<Item> itemList = itemDao.getAllItems();
            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());
            List<Item> resultItem = new ArrayList<>();
            for(Item item: itemList){
                Date expiryDate = item.getExpiryDate();

                if(expiryDate!=null){
                    try {
                        //int diff = expiryDate.compareTo(df.parse(formattedDate));
                        System.out.println("System Date: " + df.parse(formattedDate));
                        System.out.println("DB date: " + expiryDate);
                        //System.out.println("Difference" + diff);
                        int diff = (int) ((expiryDate.getTime() - df.parse(formattedDate).getTime())/ (1000 * 60 * 60 * 24));
                        System.out.println("Days: " + diff);
                        int num_days_diff= Integer.parseInt(new XMLUtil().getElementText("NOTIFICATION_NUM_DAYS_DIFF",getApplicationContext().getResources().openRawResource(R.raw.app_settings)));
                        if(diff <=num_days_diff){

                             resultItem.add(item);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
            return resultItem;
        }

        /**
         * In here you should interpret whatever you fetched in doInBackground
         * and push any notifications you need to the status bar, using the
         * NotificationManager. I will not cover this here, go check the docs on
         * NotificationManager.
         *
         * What you HAVE to do is call stopSelf() after you've pushed your
         * notification(s). This will:
         * 1) Kill the service so it doesn't waste precious resources
         * 2) Call onDestroy() which will release the wake lock, so the device
         *    can go to sleep again and save precious battery.
         */
        @Override
        protected void onPostExecute(List<Item> result) {
            // handle your data
            int id =1;
            for(Item item: result) {
                Product product = item.getProduct();
                Bitmap bitmap = product.getProdImage();
                Intent intent = new Intent(getBaseContext   (), ShopCreateActivity.class);
                intent.putExtra("fragment", "WatchList");
                PendingIntent pIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, 0);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Expiry Notification")
                        .setContentIntent(pIntent)
                        .setAutoCancel(true)
                        .setContentText("Item: " + item.getProduct().getProductName() + " will expire on " +
                                item.getExpiryDate() + ". \n");
                //Intent intent = new Intent(getApplicationContext(), SPApp.class);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.

                mNotificationManager.notify(id++, mBuilder.build());

            }
            stopSelf();
        }
    }

    /**
     * This is deprecated, but you have to implement it if you're planning on
     * supporting devices with an API level lower than 5 (Android 2.0).
     */
    @Override
    public void onStart(Intent intent, int startId) {
        handleIntent(intent);
    }

    /**
     * This is called on 2.0+ (API level 5 or higher). Returning
     * START_NOT_STICKY tells the system to not restart the service if it is
     * killed because of poor resource (memory/cpu) conditions.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIntent(intent);
        return START_NOT_STICKY;
    }

    /**
     * In onDestroy() we release our wake lock. This ensures that whenever the
     * Service stops (killed for resources, stopSelf() called, etc.), the wake
     * lock will be released.
     */
    public void onDestroy() {
        super.onDestroy();
        mWakeLock.release();
    }
}
