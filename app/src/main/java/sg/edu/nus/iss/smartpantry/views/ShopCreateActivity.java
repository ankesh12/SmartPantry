package sg.edu.nus.iss.smartpantry.views;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.application.SPApp;
import sg.edu.nus.iss.smartpantry.views.fragments.WatchListFragment;

public class ShopCreateActivity extends Activity {


    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        int numFragments = fm.getBackStackEntryCount();
        if(numFragments > 1)
            fm.popBackStack();
        else
            super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_create);
        if (savedInstanceState == null) {
            WatchListFragment watchListFragment= WatchListFragment.newInstance(false,R.mipmap.cart_icon);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.shop_container, watchListFragment,"WatchListWithoutCheckBox");
            //fragmentTransaction.addToBackStack("WatchListWithoutCheckBox");
            fragmentTransaction.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shop_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            Intent intent =  new Intent(getApplicationContext(), SPApp.class);
            startActivity(intent);
        }
        else if(id == R.id.action_expiring_items){
            Intent intent =  new Intent(getApplicationContext(), ExpiringItems.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
