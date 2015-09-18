package sg.edu.nus.iss.smartpantry.views.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.smartpantry.Entity.Category;
import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.adapters.CardDetailAdapter;
import sg.edu.nus.iss.smartpantry.dao.DAOFactory;
import sg.edu.nus.iss.smartpantry.dao.daoClass.CategoryDao;
import sg.edu.nus.iss.smartpantry.dao.daoClass.ProductDao;

/**
 * Created by A0134493A on 4/9/2015.
 */
public class EditProductDialog extends Dialog {

    Product prod;
    EditText prodName;
    EditText prodThreshQty;
    Button confirmButton;
    Button rejectButton;
    Spinner catList;
    Context context;
    private CardDetailAdapter adapt;
    //CardDetailFragment frag;
    View frag;
    List<String> lables = new ArrayList<>();

    public EditProductDialog(Context context,Product prod, CardDetailAdapter adapt, View frag) {

        super(context);
        this.context = context;
        this.prod = prod;
        this.adapt = adapt;
        this.frag = frag;
        //this.view = view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_edit_product);
        prodName = (EditText) findViewById(R.id.DescText);
        prodThreshQty = (EditText) findViewById(R.id.productThreshQty);
        confirmButton = (Button) findViewById(R.id.confirmButton);
        rejectButton = (Button) findViewById(R.id.rejectButton);
        catList = (Spinner) findViewById(R.id.spinner1);
        loadSpinnerData(catList);

        prodName.setText(prod.getProductName().toString());
        prodThreshQty.setText(String.valueOf(prod.getThreshold()));

        prodThreshQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValueFromNumPicker("Threshold", prodThreshQty);
            }
        });

        catList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView selectedText = (TextView) adapterView.getChildAt(0);
                if (selectedText != null) {
                    selectedText.setTextColor(Color.BLUE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prodDName = prod.getProductName();
                String catName = prod.getCategoryName();
                prod.setProductName(prodName.getText().toString());

                Toast.makeText(context,prodName.getText().toString(),Toast.LENGTH_SHORT).show();
                prod.setCategoryName(catList.getSelectedItem().toString());
                prod.setThreshold(Integer.valueOf(prodThreshQty.getText().toString()));
                ProductDao productDao = DAOFactory.getProductDao(context);
                productDao.updateProduct(prod);
                adapt.refreshData();
                //frag.refreshData(prodDName,catName);
                TextView thresh = (TextView) frag.findViewById(R.id.threshold_card);
                TextView prodname = (TextView) frag.findViewById(R.id.Itemname_card);
                prodname.setText(prodName.getText().toString());
                thresh.setText(prodThreshQty.getText().toString());
                dismiss();
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    public void setValueFromNumPicker(String Title,EditText editObj){
        final EditText editText = editObj;
        final Dialog d = new Dialog(context);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.setBtn);
        Button b2 = (Button) d.findViewById(R.id.cancelBtn);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(50);
        np.setMinValue(1);
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(String.valueOf(np.getValue()));
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    public void loadSpinnerData(Spinner catList){
        CategoryDao categoryDao = DAOFactory.getCategoryDao(context.getApplicationContext());
        List<Category> refCatList = categoryDao.getAllCategories();

        if (refCatList.size() == 0)
        {
            Category c = new Category();
            c.setCategoryName("MISC");
            categoryDao.addCategory(c);
            c.setCategoryName("BOOKS");
            categoryDao.addCategory(c);
            c.setCategoryName("GROC");
            categoryDao.addCategory(c);
            c.setCategoryName("MEDS");
            categoryDao.addCategory(c);
            c.setCategoryName("TOOL");
            categoryDao.addCategory(c);
            c.setCategoryName("TOIL");
            categoryDao.addCategory(c);
            c.setCategoryName("ELEC");
            categoryDao.addCategory(c);
        }

        for(Category category : categoryDao.getAllCategories()){
            lables.add(category.getCategoryName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context.getApplicationContext(),android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(R.layout.dropdown_layout);
        catList.setAdapter(dataAdapter);
    }
}
