package sg.edu.nus.iss.smartpantry.application;

import android.app.AlarmManager;
import android.app.ExpandableListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.Item;
import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.application.util.CustomAdapter;
import sg.edu.nus.iss.smartpantry.application.util.NotificationService;
import sg.edu.nus.iss.smartpantry.controller.ControlFactory;
import sg.edu.nus.iss.smartpantry.controller.MainController;
import sg.edu.nus.iss.smartpantry.views.ItemDetails;


public class SPApp extends ExpandableListActivity{
    private MainController mainController;
    List<Product> productList = new ArrayList<Product>();
    List<Item> prodItemList = new ArrayList<Item>();
    List<List<Item>> itemList = new ArrayList<List<Item>>();
    ExpandableListView expListView;
    CustomAdapter customAdapter;
    private static final int CAMERA_REQUEST = 1888;
    int lastExpandedGroupPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spapp);
        setActivityBackgroundColor("#455A64");

        lastExpandedGroupPosition=-1;

        //Get objects for controller
        mainController = ControlFactory.getInstance().getMainController();

        //Add item functionality
        Button addItemBtn = (Button) findViewById(R.id.addItem_btn);
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainController.addItem(SPApp.this);
            }
        });
        Button camButton = (Button)findViewById(R.id.addItem_cam);
        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        //Expandable List View
        expListView = (ExpandableListView) findViewById(android.R.id.list);
        customAdapter = new CustomAdapter(this,productList,itemList,getApplicationContext());
        expListView.setAdapter(customAdapter);
        //Create the list view for products
        customAdapter.refreshData();

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

                if(groupPosition != lastExpandedGroupPosition )
                    expListView.collapseGroup(lastExpandedGroupPosition );
                lastExpandedGroupPosition = groupPosition;
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            Intent intent = new Intent(getApplicationContext(), ItemDetails.class);
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
        getMenuInflater().inflate(R.menu.menu_spapp, menu);

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        customAdapter.refreshData();

        //Notification set up
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        System.out.print("Hours: " + c.getTime().getHours());
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, NotificationService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        am.cancel(pi);


        // by my own convention, minutes <= 0 means notifications are disabled
        if (c.getTime().getHours() >= 14 ) {
            System.out.println("Main andar Hun!");
            int minutes = 60*24*60;
            am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 30*1000,
                    AlarmManager.INTERVAL_FIFTEEN_MINUTES, pi);
        }
        startService(i);
    }

    @Override
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
    public void setActivityBackgroundColor(String color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.parseColor(color));
    }
    
}
