package sg.edu.nus.iss.smartpantry.views.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.adapters.CustomDrawerListAdapter;
import sg.edu.nus.iss.smartpantry.views.fragments.ShopListFragment;
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




        ListView list;
        String[] drawerTextItems = {
                "Home",
                "Watch List",
                "Shopping Cart"
        } ;

        Integer[] imageId = {
                R.mipmap.home_drawer,
                R.mipmap.watchlist_drawer,
                R.mipmap.shop_cart_drawer
        };




        Bundle extras = getIntent().getExtras();
        String fragName = extras.getString("fragment");
        System.out.println(fragName);

        if (savedInstanceState == null) {
            if(fragName.equals("WatchList"))
            {
                WatchListFragment watchListFragment= WatchListFragment.newInstance(false,R.mipmap.cart_icon);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.shop_container, watchListFragment,"WatchListWithoutCheckBox");
                //fragmentTransaction.addToBackStack("WatchListWithoutCheckBox");
                fragmentTransaction.commit();
            }
            else if (fragName.equals("shopList"))
            {
                ShopListFragment shopListFragment= new ShopListFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.shop_container, shopListFragment);
                //fragmentTransaction.addToBackStack("WatchListWithoutCheckBox");
                fragmentTransaction.commit();

            }
        }

        CustomDrawerListAdapter drawerListAdapter = new CustomDrawerListAdapter(ShopCreateActivity.this, drawerTextItems, imageId);
        list=(ListView)findViewById(R.id.shopNavList);
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
                    intent.putExtra("fragment","WatchList");
                    startActivity(intent);
                }
                else if(position==2)
                {
                    Intent intent =  new Intent(getApplicationContext(), ShopCreateActivity.class);
                    intent.putExtra("fragment","shopList");
                    startActivity(intent);

                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }
}
