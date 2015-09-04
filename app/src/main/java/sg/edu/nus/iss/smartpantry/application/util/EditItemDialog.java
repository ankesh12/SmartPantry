package sg.edu.nus.iss.smartpantry.application.util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sg.edu.nus.iss.smartpantry.Entity.Item;

import sg.edu.nus.iss.smartpantry.R;
;
import sg.edu.nus.iss.smartpantry.dao.sqlite.ItemDaoImpl;

/**
 * Created by sambhav on 9/3/15.
 */
public class EditItemDialog extends Dialog{

    private Activity parentActivity;
    private EditText purDate;
    private EditText expDate;
    private EditText price;

    private Item item;
    private CardDetailAdapter adapt;
    public EditItemDialog(Activity parentActivity,Item item,CardDetailAdapter adapt) {
        super(parentActivity);
        this.parentActivity=parentActivity;
        this.item=item;
        this.adapt=adapt;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Edit Item ");
        setContentView(R.layout.dialog_edit_item);
        purDate = (EditText) findViewById(R.id.editItemPurDate);
        expDate = (EditText) findViewById(R.id.editItemExpDate);
        price = (EditText) findViewById(R.id.priceEditItem);

        purDate.setText(item.getDop().toString());
        expDate.setText(item.getExpiryDate()== null ? " " : item.getExpiryDate().toString());

        price.setText(String.valueOf(item.getPrice()));


        //final EditText expDate = (EditText)findViewById(R.id.editItemExpDate);

        expDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(parentActivity,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                expDate.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        //final EditText purDate = (EditText)findViewById(R.id.editItemPurDate);
        purDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(parentActivity,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                purDate.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });

        Button confirmButton =(Button)findViewById(R.id.confirmEditButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date expiryDate = null;
                Date purchaseDate=null;
               try{
                if(!expDate.getText().toString().trim().equals("")) {
                    expiryDate = new SimpleDateFormat("dd-MM-yyyy").parse(expDate.getText().toString());

                                        }}
               catch(Exception e ){}

                try{
                    if(!purDate.getText().toString().trim().equals("")) {
                        purchaseDate = new SimpleDateFormat("dd-MM-yyyy").parse(purDate.getText().toString());

                    }}
                catch(Exception e ){}



                item.setDop(new java.sql.Date(purchaseDate.getTime()));
                item.setExpiryDate(new java.sql.Date(expiryDate.getTime()));
                item.setPrice(Double.valueOf(price.getText().toString()));
                ItemDaoImpl impl= new ItemDaoImpl(getContext());
                impl.updateItem(item);





                adapt.refreshData();
                Toast.makeText(parentActivity.getApplicationContext(), " Item edited Successfully", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        Button removeBtn = (Button)findViewById(R.id.cancelEditButton);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}


