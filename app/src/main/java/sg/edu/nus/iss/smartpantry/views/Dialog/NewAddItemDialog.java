package sg.edu.nus.iss.smartpantry.views.Dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.adapters.CardDetailAdapter;
import sg.edu.nus.iss.smartpantry.controller.ControlFactory;
import sg.edu.nus.iss.smartpantry.dto.ItemDetailDTO;

/**
 * Created by ankesh on 8/20/15.
 */
public class NewAddItemDialog extends Dialog{

    private Activity parentActivity;
    private NumberPicker numPicker;
    private Product selProd;
    private CardDetailAdapter adapt;
    private ItemDetailDTO itemDetailDTO;
    public NewAddItemDialog(Activity parentActivity,Product prod,CardDetailAdapter adapt) {
        super(parentActivity);
        this.parentActivity=parentActivity;
        this.selProd=prod;
        this.adapt=adapt;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_item);
        numPicker = (NumberPicker)findViewById(R.id.AddItmNumPicker);
        numPicker.setMinValue(1);
        numPicker.setMaxValue(100);
        numPicker.setWrapSelectorWheel(false);
        numPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        final EditText expDate = (EditText)findViewById(R.id.AddItmExpDate);
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

        Button addItemToDB = (Button)findViewById(R.id.AddItmAddBtn);
        addItemToDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qtyEntered = numPicker.getValue();
//                for(int i=0;i < qtyEntered;i++) {
                    try {
                        Date expiryDate = null;
                        if(!expDate.getText().toString().trim().equals("")) {
                            expiryDate = new SimpleDateFormat("dd-MM-yyyy").parse(expDate.getText().toString());
                        }
                        itemDetailDTO = new ItemDetailDTO(selProd.getCategory().getCategoryName(), selProd.getProductName(),
                                selProd.getProdImage(),expiryDate,selProd.getThreshold(),0, qtyEntered);
                        /*ControlFactory.getInstance().getItemController().addItem(parentActivity
                                        .getApplicationContext(), selProd.getCategoryName(), selProd.getProductName(),
                                selProd.getProdImage(),expiryDate,selProd.getThreshold(),0, qtyEntered);*/
                        ControlFactory.getInstance().getItemController().addItem(parentActivity.getApplicationContext(),
                                itemDetailDTO);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
//                }
                adapt.refreshData();
                Toast.makeText(parentActivity.getApplicationContext(), qtyEntered + " items added " +
                        "successfully", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        Button removeBtn = (Button)findViewById(R.id.AddItmCancelBtn);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
