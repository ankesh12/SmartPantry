package sg.edu.nus.iss.smartpantry.views.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import sg.edu.nus.iss.smartpantry.Entity.Category;
import sg.edu.nus.iss.smartpantry.Entity.Item;
import sg.edu.nus.iss.smartpantry.Entity.Product;
import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.controller.ControlFactory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddItemConfirm.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddItemConfirm#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddItemConfirm extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText prodDescText;
    private ImageView prodImage;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddItemConfirm.
     */
    // TODO: Rename and change types and number of parameters
    public static AddItemConfirm newInstance(String param1, String param2) {
        AddItemConfirm fragment = new AddItemConfirm();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AddItemConfirm() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_item_confirm, container, false);

        Bundle bundle = this.getArguments();
        prodDescText= (EditText)view.findViewById(R.id.prodDescText);
        prodDescText.setText(bundle.getString("PRODUCT_NAME"));
        prodImage = (ImageView)view.findViewById(R.id.prodImage);
        Bitmap bitmap = (Bitmap)bundle.getParcelable("PRODUCT_IMG");
        Drawable d = new BitmapDrawable(getResources(),bitmap);
        prodImage.setImageDrawable(d);

//        NumberPicker s = (NumberPicker) view.findViewById(R.id.quantPick);
//        s.setMaxValue(10);
//        s.setMinValue(1);
//        s.setValue(5);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_spinner_item, arraySpinner);
//        s.setAdapter(adapter);

        ImageButton addItemToDB = (ImageButton)view.findViewById(R.id.addButton);
        addItemToDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText prodDesc = (EditText) getActivity().findViewById(R.id.prodDescText);
                Category category = new Category();
                category.setCategoryId("GUM");
                category.setName("Fevicol");
                Product product = new Product("GUM","GLU");
                System.out.println(prodDesc.getText());
                product.setProductName(prodDesc.getText().toString());
                product.setQuantity(1);
                product.setThreshold(2);
                Item item = new Item("GLU",2);
                item.setPrice(10.00);
                ControlFactory.getInstance().getItemController().addItem(getActivity().getApplicationContext(), category, product, item);

            }
        });
        ImageButton removeBtn = (ImageButton)view.findViewById(R.id.removeButton);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return view;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
