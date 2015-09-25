package sg.edu.nus.iss.smartpantry.views.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import sg.edu.nus.iss.smartpantry.CustomException.ItemNotFoundException;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.application.network.ItemLookup;
import sg.edu.nus.iss.smartpantry.adapters.CustomDrawerListAdapter;
import sg.edu.nus.iss.smartpantry.dto.ItemDetailDTO;
import sg.edu.nus.iss.smartpantry.service.NotificationService;
import sg.edu.nus.iss.smartpantry.application.util.XMLUtil;
import sg.edu.nus.iss.smartpantry.controller.ControlFactory;
import sg.edu.nus.iss.smartpantry.controller.MainController;
import sg.edu.nus.iss.smartpantry.views.fragments.CardHomeFragment;

public class SPApp extends Activity{

    private MainController mainController;
    private static final int CAMERA_REQUEST = 1888;
    private CardHomeFragment cardHomeFragment;
    protected DrawerLayout drawer;
    private ItemDetailDTO itemDetails;
    //implementing the code for drawerList

    ListView list;
    String[] drawerTextItems = {
           "Home",
            "Watch List",
            "Shopping List"
    } ;

    Integer[] imageId = {
            R.mipmap.home_drawer,
            R.mipmap.watchlist_drawer,
            R.mipmap.shop_cart_drawer

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_scan);

        if (savedInstanceState == null) {
            cardHomeFragment= new CardHomeFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.bluescan, cardHomeFragment, "MatDesign");
            fragmentTransaction.commit();
        }
        //Events for the Image Buttons

        mainController = ControlFactory.getInstance().getMainController();
        ImageButton addItemBtn = (ImageButton) findViewById(R.id.addItem_btn);
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainController.addItem(SPApp.this);
            }
        });


        ImageButton camButton = (ImageButton)findViewById(R.id.addItem_cam);
        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCamButtonClicked();
            }
        });

        ImageButton btrButton = (ImageButton)findViewById(R.id.addItem_bt);
        btrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBTReaderDialog();
            }
        });

        drawer = (DrawerLayout)findViewById(R.id.drawerLayout);
        ImageButton options_btn = (ImageButton) findViewById(R.id.options_btn);
        options_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        initiateDrawer();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            image=Bitmap.createScaledBitmap(image, 150, 150, false);
            Intent intent = new Intent(SPApp.this.getApplicationContext(), ItemDetails.class);
            Bundle b = new Bundle();
            b.putString("PRODUCT_NAME", ""); //Your id
            b.putParcelable("PRODUCT_IMG", image);
            intent.putExtras(b);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Notification set up
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        System.out.print("Hours: " + c.getTime().getHours());
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, NotificationService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        am.cancel(pi);

        int hrs= Integer.parseInt(new XMLUtil().getElementText("NOTIFICATION_TIME_HRS",SPApp.this.getResources().openRawResource(R.raw.app_settings)));
        // by my own convention, minutes <= 0 means notifications are disabled
        if (c.getTime().getHours() >= hrs) {
            int minutes = 60*24*60;
            am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 30*1000,
                    AlarmManager.INTERVAL_FIFTEEN_MINUTES, pi);
        }
        startService(i);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showBTReaderDialog() {
        final Dialog d = new Dialog(SPApp.this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.btrdialog);
        final EditText barcode = (EditText)d.findViewById(R.id.barCodeText);
        barcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {
                    getProductDetailsAndAdd(barcode.getText().toString(), d);
                    barcode.setEnabled(false);
                    return true;
                }
                return false;
            }
        });
        Button doneBtn = (Button)d.findViewById(R.id.doneBtn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
                cardHomeFragment.getAdapter().refreshData();
            }
        });
        d.show();
    }

    public void getProductDetailsAndAdd(String code, Dialog dialog){
        final String barcode = code;
        final Dialog dlg = dialog;
        new AsyncTask<Void, Void, ArrayList<String>>(){
            Drawable d;
            @Override
            protected ArrayList<String> doInBackground(Void... params) {
                ArrayList<String> details = null;
                try {
                    details = new ItemLookup(getApplicationContext()).GetProductDetails(barcode);
                    InputStream is = (InputStream)new URL(details.get(1)).getContent();
                    d = Drawable.createFromStream(is,null);
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (ItemNotFoundException e) {
                    details = null;
                }
                return details;
            }
            @Override
            protected void onPostExecute(ArrayList<String> s) {
                if (s==null){
                    addProduct(null, null, dlg);
                }else {
                    addProduct(s.get(0), d, dlg);
                }
            }
        }.execute();

    }
    private void addProduct(String prodTitle,Drawable image, Dialog dlg) {
        if(prodTitle==null){
            Toast.makeText(SPApp.this, "Product Not Found.", Toast.LENGTH_SHORT).show();
            MediaPlayer player = MediaPlayer.create(getApplicationContext(),R.raw.nfbeep);
            player.start();
        }else{
            Bitmap bitmap = ((BitmapDrawable)image).getBitmap();
            bitmap=Bitmap.createScaledBitmap(bitmap, 150,150,false);
            try {
                itemDetails = new ItemDetailDTO("Groceries", prodTitle, bitmap, null, 1, 0, 1);
                ControlFactory.getInstance().getItemController().addItem(getApplicationContext(), itemDetails);
                MediaPlayer player = MediaPlayer.create(getApplicationContext(),R.raw.beep);
                player.start();
                Toast.makeText(SPApp.this, "Product Added", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        final EditText barcode = (EditText)dlg.findViewById(R.id.barCodeText);
        barcode.setEnabled(true);
        barcode.setText("");
    }

    public void initiateDrawer(){
        CustomDrawerListAdapter drawerListAdapter = new CustomDrawerListAdapter(SPApp.this, drawerTextItems, imageId);
        list=(ListView)findViewById(R.id.navList);
        list.setAdapter(drawerListAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(position==0){
                    Intent intent =  new Intent(getApplicationContext(), SPApp.class);
                    startActivity(intent);

                }
                else if(position==1)
                {
                    Intent intent =  new Intent(getApplicationContext(), ShopCreateActivity.class);
                    intent.putExtra("fragment", "WatchList");
                    drawer.closeDrawer(Gravity.LEFT);
                    startActivity(intent);
                }
                else if(position==2)
                {
                    Intent intent =  new Intent(getApplicationContext(), ShopCreateActivity.class);
                    intent.putExtra("fragment","shopList");
                    drawer.closeDrawer(Gravity.LEFT);
                    startActivity(intent);
                }
            }
        });
    }


    private void onCamButtonClicked()
    {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }
}
