package sg.edu.nus.iss.smartpantry.views.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.views.fragments.ExpiringItemFragment;

public class ExpiringItems extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expiring_items);
        setActivityBackgroundColor("#3B5998");
        if (savedInstanceState == null) {
            ExpiringItemFragment expiringItemFragment= new ExpiringItemFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.relExpiringLayout, expiringItemFragment, "ExpireList");
            fragmentTransaction.commit();
        }
        /*lastExpandedGroupPosition=-1;

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
        });*/
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            Intent intent =  new Intent(getApplicationContext(), SPApp.class);
            startActivity(intent);
        }
        if(id == R.id.action_watch_list){
            Intent intent =  new Intent(getApplicationContext(), ShopCreateActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void setActivityBackgroundColor(String color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.parseColor(color));
    }
}
