package sg.edu.nus.iss.smartpantry.views;

import android.app.ExpandableListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.Item;
import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.application.util.ExpItemsAdapter;
import sg.edu.nus.iss.smartpantry.controller.ControlFactory;
import sg.edu.nus.iss.smartpantry.controller.MainController;

public class ExpiringItems extends ExpandableListActivity {
    private MainController mainController;
    List<Product> productList = new ArrayList<Product>();
    List<Item> prodItemList = new ArrayList<Item>();
    List<List<Item>> itemList = new ArrayList<List<Item>>();
    ExpandableListView expListView;
    ExpItemsAdapter customAdapter;
    int lastExpandedGroupPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expiring_items);
        setActivityBackgroundColor("#3B5998");

        lastExpandedGroupPosition=-1;

        mainController = ControlFactory.getInstance().getMainController();
        expListView = (ExpandableListView) findViewById(android.R.id.list);
        customAdapter = new ExpItemsAdapter(this,productList,itemList,getApplicationContext());
        expListView.setAdapter(customAdapter);
        customAdapter.refreshData();

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

                if (groupPosition != lastExpandedGroupPosition)
                    expListView.collapseGroup(lastExpandedGroupPosition);
                lastExpandedGroupPosition = groupPosition;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_expiring_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
