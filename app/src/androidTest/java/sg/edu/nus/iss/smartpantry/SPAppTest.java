package sg.edu.nus.iss.smartpantry;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.NumberPicker;

import com.robotium.solo.Solo;

import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.application.SPApp;

/**
 * Created by CHARAN on 6/4/2015.
 */

public class SPAppTest extends ActivityInstrumentationTestCase2<SPApp> {

    private Solo solo;
    private ExpandableListView expListView;
    private ExpandableListAdapter expListAdapter;

    public SPAppTest() {
        super(SPApp.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
        //Unlock the lock screen
        solo.unlockScreen();
        solo.assertCurrentActivity("Expected SPApp Activity", SPApp.class);
//        expListView=getActivity().getExpandableListView();
//        expListAdapter = expListView.getExpandableListAdapter();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        //solo.finishOpenedActivities();
    }

    public void testAddQty()
    {
        Product prod =  (Product)expListAdapter.getGroup(0);
        int prodQty = 0;
        if (prod != null)
            prodQty = prod.getQuantity();
        solo.clickLongOnView(expListView.getChildAt(0));
        solo.clickOnMenuItem("Add Items");
        assertTrue("Could not find the dialog!", solo.searchText("Add Items by Quantity"));
        //NumberPicker qty = (NumberPicker)getActivity().findViewById(R.id.AddItmNumPicker);
        solo.clickOnEditText(0);
        solo.setDatePicker(0, 2015, 11, 16);
        solo.clickOnText("Set");
        NumberPicker qty = (NumberPicker)solo.getView(R.id.AddItmNumPicker);
        setNumberPicker(qty, 3);
        solo.clickOnImageButton(0);
        solo.waitForDialogToClose(3000);
        expListAdapter = expListView.getExpandableListAdapter();
        prod =  (Product)expListAdapter.getGroup(0);
        assertTrue("Num Picker not set properly!!!",qty.getValue()==3);
        assertTrue("Add Quantity Unsuccessful!!!", prod.getQuantity() == (prodQty+3));
    }
    public void setNumberPicker(final NumberPicker numberPicker, final int value) {
        if (numberPicker != null && getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    numberPicker.setValue(value);
                }
            });
        }
    }
}

