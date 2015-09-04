package sg.edu.nus.iss.smartpantry.application.util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.controller.ControlFactory;

/**
 * Created by sambhav on 9/3/15.
 */
public class EditItemDialog extends Dialog{

    private Activity parentActivity;

    private Product selProd;
    private CardDetailAdapter adapt;
    public EditItemDialog(Activity parentActivity,Product prod,CardDetailAdapter adapt) {
        super(parentActivity);
        this.parentActivity=parentActivity;
        this.selProd=prod;
        this.adapt=adapt;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Edit Item ");
        setContentView(R.layout.dialog_edit_item);


        final EditText expDate = (EditText)findViewById(R.id.editItemExpDate);
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
        final EditText purDate = (EditText)findViewById(R.id.editItemPurDate);
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

//                {
//                    try {
//                        Date expiryDate = null;
//                        if(!expDate.getText().toString().trim().equals("")) {
//                            expiryDate = new SimpleDateFormat("dd-MM-yyyy").parse(expDate.getText().toString());
//                        }
//                        ControlFactory.getInstance().getItemController().addItem(parentActivity
//                                        .getApplicationContext(), selProd.getCategoryName(), selProd.getProductName(),
//                                selProd.getProdImage(),expiryDate,selProd.getThreshold(),0);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                }
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


