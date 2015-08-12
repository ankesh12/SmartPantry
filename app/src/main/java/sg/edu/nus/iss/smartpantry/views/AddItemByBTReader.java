package sg.edu.nus.iss.smartpantry.views;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

import sg.edu.nus.iss.smartpantry.CustomException.ItemNotFoundException;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.application.network.ItemLookup;
import sg.edu.nus.iss.smartpantry.controller.ControlFactory;

public class AddItemByBTReader extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_by_btreader);
        final EditText barcode = (EditText)findViewById(R.id.barCodeText);
        barcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Toast.makeText(getApplicationContext(),barcode.getText().toString(),Toast.LENGTH_SHORT).show();
                    addProduct(barcode.getText().toString());
                    barcode.setText("");
                    return true;
                }
                return false;
            }
        });
    }

    public void addProduct(String code){
        final String barcode = code;
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
                    addProduct(null, null);
                }else {
                    System.out.println("PRODUCT XXXXXXXXXXXX" + s.get(0));
                    addProduct(s.get(0), d);
                }
            }
        }.execute();

    }

    private void addProduct(String prodTitle,Drawable image) {
        if(prodTitle==null){
            Toast.makeText(AddItemByBTReader.this,"Product Not Found.", Toast.LENGTH_SHORT).show();
            onResume();
            return;
        }
        Bitmap bitmap = ((BitmapDrawable)image).getBitmap();
//        Product product = DAOFactory.getProductDao(getApplicationContext()).getProduct("MISC", prodTitle);
        try {
//            if(product==null){
                ControlFactory.getInstance().getItemController().addItem(getApplicationContext(), "MISC", prodTitle, bitmap, null, 5);
//            }else{
//                ControlFactory.getInstance().getItemController().addItem(getApplicationContext(), "MISC", prodTitle, bitmap, null,0);
//            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_item_by_btreader, menu);
        return true;
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
}
