package sg.edu.nus.iss.smartpantry.views.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import sg.edu.nus.iss.smartpantry.controller.ControlFactory;
import sg.edu.nus.iss.smartpantry.controller.DAOFactory;
import sg.edu.nus.iss.smartpantry.dao.CategoryDao;


public class AddItemConfirm extends Fragment {
    private static int pick=1;
    private EditText prodDescText;
    private ImageView prodImage;
    List<String> lables = new ArrayList<>();
    private TextView tvDisplayDate;
    private DatePicker dpResult;
    private Button btnChangeDate;
    EditText expDate;

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
        final Spinner catList = (Spinner)view.findViewById(R.id.spinner);
        loadSpinnerData(view, catList);
        ImageButton addItemToDB = (ImageButton)view.findViewById(R.id.addButton);
        final EditText quantity = (EditText)view.findViewById(R.id.prodQty);
        addItemToDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText prodDesc = (EditText) getActivity().findViewById(R.id.prodDescText);
                if(quantity.getText() == null){
                    quantity.setText(0);
                }
                int qtyEntered =Integer.valueOf(quantity.getText().toString());
                try {
                    if(qtyEntered<=0){
                        Toast.makeText(getActivity().getApplicationContext(),"Quantity cannot be less than 1", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Date expiryDate = null;
                    if(!expDate.getText().toString().trim().equals("")) {
                        expiryDate = new SimpleDateFormat("dd-MM-yyyy").parse(expDate.getText().toString());
                    }
                    Product product = DAOFactory.getProductDao(getActivity().getApplicationContext()).getProduct(catList.getSelectedItem().toString(),prodDesc.getText().toString());
                    if(product==null){
                        show(qtyEntered,prodDesc.getText().toString(),catList.getSelectedItem().toString(),bitmap,expiryDate);
                    }else{

                        for(int i=0;i < qtyEntered;i++) {
                            ControlFactory.getInstance().getItemController().addItem(getActivity().getApplicationContext(), catList.getSelectedItem().toString(), prodDesc.getText().toString(), bitmap, expiryDate,0);
                        }
                        getActivity().onBackPressed();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        ImageButton removeBtn = (ImageButton)view.findViewById(R.id.removeButton);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        expDate = (EditText)view.findViewById(R.id.prodExpDate);
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
        return view;
    }

    /**
     * Showing google speech input dialog
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
//        List<String> lables = new ArrayList<>();

        List<Category> refCatList = categoryDao.getAllCategories();

        if (refCatList.size() == 0)
        {
            Category c = new Category();
            c.setCategoryName("MISC");
            categoryDao.addCategory(c);
            c.setCategoryName("BOOKS");
            categoryDao.addCategory(c);
        }

        for(Category category : categoryDao.getAllCategories()){
            lables.add(category.getCategoryName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catList.setAdapter(dataAdapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    public void show(final int qty, final String productName, final String categoryName, final Bitmap image, final Date expDate) {
//        final int pick;
        final Dialog d = new Dialog(getActivity());
        d.setTitle("Set Threshold");
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
                pick = np.getValue();
                System.out.println("Threshold Value: " + pick);
                try {
                    for(int i=0;i<qty;i++)
                        ControlFactory.getInstance().getItemController().addItem(getActivity().getApplicationContext(), categoryName, productName, image, expDate, pick);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                d.dismiss();
                getActivity().onBackPressed();
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
}
