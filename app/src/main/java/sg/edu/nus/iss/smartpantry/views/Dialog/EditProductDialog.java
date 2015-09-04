package sg.edu.nus.iss.smartpantry.views.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;

import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.R;

/**
 * Created by A0134493A on 4/9/2015.
 */
public class EditProductDialog extends Dialog {

    Product prod;
    EditText prodName;
    EditText prodThreshQty;

    public EditProductDialog(Context context,Product prod) {

        super(context);
        this.prod = prod;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Edit Product Details");
        setContentView(R.layout.dialog_edit_product);
        prodName = (EditText) findViewById(R.id.DescText);
        prodThreshQty = (EditText) findViewById(R.id.productThreshQty);
        prodName.setText(prod.getProductName().toString());
        prodThreshQty.setText(String.valueOf(prod.getThreshold()));


    }
}
