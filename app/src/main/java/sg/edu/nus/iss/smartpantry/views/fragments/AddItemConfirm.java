package sg.edu.nus.iss.smartpantry.views.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sg.edu.nus.iss.smartpantry.Entity.Category;
import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.adapters.RecyclerAdapter;
import sg.edu.nus.iss.smartpantry.controller.ControlFactory;
import sg.edu.nus.iss.smartpantry.dao.DAOFactory;
import sg.edu.nus.iss.smartpantry.dao.daoClass.CategoryDao;
import sg.edu.nus.iss.smartpantry.dto.ItemDetailDTO;


public class AddItemConfirm extends Fragment {
    private static int pick=1;
    private EditText prodDescText;
    private ImageView prodImage;
    List<String> lables = new ArrayList<>();
    private TextView tvDisplayDate;
    private DatePicker dpResult;
    private Button btnChangeDate;
    private ItemDetailDTO itemDetailDTO;
    EditText expDate,thresholdQty, price, quantity ;
    Spinner catList;
    Button addBtn,cancelBtn;
    private RecyclerAdapter recyclerAdapter;

    private int year;
    private int month;
    private int day;

    static final int DATE_DIALOG_ID = 999;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private OnFragmentInteractionListener mListener;

    public static AddItemConfirm newInstance(String param1, String param2) {
        AddItemConfirm fragment = new AddItemConfirm();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AddItemConfirm() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_item_confirm, container, false);

        Bundle bundle = this.getArguments();
        prodDescText= (EditText)view.findViewById(R.id.prodDescText);
        prodDescText.setText(bundle.getString("PRODUCT_NAME"));
        prodImage = (ImageView)view.findViewById(R.id.prodImage);
        final Bitmap bitmap = (Bitmap)bundle.getParcelable("PRODUCT_IMG");
        Drawable d = new BitmapDrawable(getResources(),bitmap);
        prodImage.setImageDrawable(d);

        addBtn = (Button)view.findViewById(R.id.addButton);
        cancelBtn = (Button)view.findViewById(R.id.removeButton);

        quantity = (EditText)view.findViewById(R.id.prodQty);
        thresholdQty = (EditText)view.findViewById(R.id.prodThreshQty);

        price = (EditText)view.findViewById(R.id.prodPrice);
        expDate = (EditText)view.findViewById(R.id.prodExpDate);
        catList = (Spinner)view.findViewById(R.id.spinner);
        loadSpinnerData(view, catList);
        quantity.setText("1");
        price.setText("0.0");

        prodDescText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setThreshold();
            }
        });

        catList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView selectedText = (TextView) adapterView.getChildAt(0);
                if (selectedText != null) {
                    selectedText.setTextColor(Color.BLUE);
                    setThreshold();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing
            }
        });

        expDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
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

        ImageButton micButton = (ImageButton)view.findViewById(R.id.micButton);
        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();

            }
        });

        quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValueFromNumPicker("Quantity", quantity);
            }
        });

        thresholdQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValueFromNumPicker("Threshold", thresholdQty);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText prodDesc = (EditText) getActivity().findViewById(R.id.prodDescText);
                if(prodDesc.getText().toString().equals("")){
                    Toast.makeText(getActivity().getApplicationContext(),"Product name cannot be blank",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(quantity.getText().toString().equals("")){
                    Toast.makeText(getActivity().getApplicationContext(),"Quantity cannot be blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(thresholdQty.getText().toString().equals("")){
                    Toast.makeText(getActivity().getApplicationContext(),"Threshold cannot be blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(price.getText().toString().equals("")) {
                    price.setText("0.0");
                }
                try {
                    Date expiryDate = null;
                    if(!expDate.getText().toString().trim().equals("")) {
                        expiryDate = new SimpleDateFormat("dd-MM-yyyy").parse(expDate.getText().toString());
                    }

//                    for(int i=0;i < Integer.valueOf(quantity.getText().toString());i++) {
                    itemDetailDTO = new ItemDetailDTO(catList.getSelectedItem().toString(), prodDesc.getText().toString(), bitmap, expiryDate,Integer.valueOf(thresholdQty.getText().toString()), Double.valueOf(price.getText().toString()),Integer.valueOf(quantity.getText().toString()));
                    //ControlFactory.getInstance().getItemController().addItem(getActivity().getApplicationContext(), catList.getSelectedItem().toString(), prodDesc.getText().toString(), bitmap, expiryDate,Integer.valueOf(thresholdQty.getText().toString()), Double.valueOf(price.getText().toString()),Integer.valueOf(quantity.getText().toString()));
                    ControlFactory.getInstance().getItemController().addItem(getActivity().getApplicationContext(),itemDetailDTO);
//                    }
                            //recyclerAdapter.refreshData();
                            getActivity().onBackPressed();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    /**
     * Show google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity().getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == getActivity().RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    prodDescText.setText(result.get(0));
                }
                break;
            }

        }
    }

    public void loadSpinnerData(View view, Spinner catList){
        CategoryDao categoryDao = DAOFactory.getCategoryDao(getActivity().getApplicationContext());
        List<Category> refCatList = categoryDao.getAllCategories();

        if (refCatList.size() == 0)
        {
            Category c = new Category(categoryDao.generateCategoryId());
            c.setCategoryName("Groceries");
            categoryDao.addCategory(c);
            c = new Category(categoryDao.generateCategoryId());
            c.setCategoryName("Beverages");
            categoryDao.addCategory(c);
            c = new Category(categoryDao.generateCategoryId());
            c.setCategoryName("Fruits");
            categoryDao.addCategory(c);
            c = new Category(categoryDao.generateCategoryId());
            c.setCategoryName("Vegetable");
            categoryDao.addCategory(c);
        }

        for(Category category : categoryDao.getAllCategories()){
            lables.add(category.getCategoryName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(R.layout.dropdown_layout);
        catList.setAdapter(dataAdapter);
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(getActivity().getApplicationContext(), datePickerListener,year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // set selected date into textview
            tvDisplayDate.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));

            // set selected date into datepicker also
            dpResult.init(year, month, day, null);

        }
    };

    public void setValueFromNumPicker(String Title,EditText editObj){
        final EditText editText = editObj;
        final Dialog d = new Dialog(getActivity());
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialog);
        if(Title.equals("Quantity")){
            TextView header = (TextView)d.findViewById(R.id.header);
            header.setText("Quantity");
        }
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

    private void setThreshold(){
        Product product = DAOFactory.getProductDao(getActivity().getApplicationContext()).getProductByCategoryNameAndProdName(catList.getSelectedItem().toString(),prodDescText.getText().toString());
        if(product!=null)
            thresholdQty.setText(String.valueOf(product.getThreshold()));
        else
            thresholdQty.setText("");

    }
}
