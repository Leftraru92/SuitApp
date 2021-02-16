package com.example.suitapp.fragment.addStore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.suitapp.AddStoreActivity;
import com.example.suitapp.R;
import com.example.suitapp.model.ProvinceList;
import com.example.suitapp.listener.OclSelectDialog;
import com.example.suitapp.model.Province;
import com.example.suitapp.util.Constants;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoreAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreAddressFragment extends Fragment {
    private AddStoreViewModel mViewModel;
    private final List<Province> provinces = ProvinceList.ITEMS;
    View root;
    TextInputLayout tilProvince, tilCity, tilStreet, tilNumber, tilFloor, tilApartment;
    TextInputEditText tietProvince, tietCity, tietStreet, tietNumber, tietFloor, tietApartment;
    AddStoreActivity activity;
    boolean fromReview;

    public static StoreAddressFragment newInstance() {
        return new StoreAddressFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_store_address, container, false);
        init();
        return root;
    }

    private void init() {
        activity = ((AddStoreActivity) getActivity());
        mViewModel = new ViewModelProvider(getActivity()).get(AddStoreViewModel.class);
        fromReview = false;
        if (getArguments() != null)
            fromReview = StoreNameFragmentArgs.fromBundle(getArguments()).getFromReview();

        //bind
        tilProvince = root.findViewById(R.id.tilProvince);
        tilCity = root.findViewById(R.id.tilCity);
        tilStreet = root.findViewById(R.id.tilStreet);
        tilNumber = root.findViewById(R.id.tilNumber);
        tilFloor = root.findViewById(R.id.tilFloor);
        tilApartment = root.findViewById(R.id.tilApartment);

        tietProvince = root.findViewById(R.id.tietProvince);
        tietCity = root.findViewById(R.id.tietCity);
        tietStreet = root.findViewById(R.id.tietStreet);
        tietNumber = root.findViewById(R.id.tietNumber);
        tietFloor = root.findViewById(R.id.tietFloor);
        tietApartment = root.findViewById(R.id.tietApartment);

        //listener
        OclSelectDialog oclSelectDialog = new OclSelectDialog(getContext(), mViewModel, (new ProvinceList()), Constants.SELECT_PROVINCE);
        tietProvince.setOnClickListener(oclSelectDialog);
        ((Button) root.findViewById(R.id.btContinue)).setOnClickListener(v -> next());
        tilProvince.setEndIconOnClickListener(v -> mViewModel.selectValue(0, "", Constants.SELECT_PROVINCE));

        //viewmodel
        mViewModel.getProvince().observe(getViewLifecycleOwner(), s -> tietProvince.setText(s));
        mViewModel.getCity().observe(getViewLifecycleOwner(), s -> tietCity.setText(s));
        mViewModel.getStreet().observe(getViewLifecycleOwner(), s -> tietStreet.setText(s));
        mViewModel.getNumber().observe(getViewLifecycleOwner(), s -> tietNumber.setText(s));
        mViewModel.getFloor().observe(getViewLifecycleOwner(), s -> tietFloor.setText(s));
        mViewModel.getApartment().observe(getViewLifecycleOwner(), s -> tietApartment.setText(s));

    }

    private void next() {
        if (validate()) {
            mViewModel.setCity(tietCity.getText().toString());
            mViewModel.setStreet(tietStreet.getText().toString());
            mViewModel.setNumber(tietNumber.getText().toString());
            mViewModel.setFloor(tietFloor.getText().toString());
            mViewModel.setApartment(tietApartment.getText().toString());

            if (fromReview)
                Navigation.findNavController(root).navigate(R.id.action_nav_address_store_to_nav_store_review);
            else
                Navigation.findNavController(root).navigate(R.id.action_nav_address_store_to_imageStoreFragment);
        } else {
            activity.mostrarMensaje("Complete el formulario para continuar");
        }
    }

    private boolean validate() {
        int error = 0;
        error += activity.validateItem(tilProvince, tietProvince, true, false);
        error += activity.validateItem(tilCity, tietCity, true, true);
        error += activity.validateItem(tilStreet, tietStreet, (!tietNumber.getText().toString().isEmpty() || !tietFloor.getText().toString().isEmpty() || !tietApartment.getText().toString().isEmpty()), true); //mandatorio si se ingresó el número
        error += activity.validateItem(tilNumber, tietNumber, (!tietStreet.getText().toString().isEmpty()), true); //mandatorio si se ingresó la calle
        error += activity.validateItem(tilFloor, tietFloor, (!tietApartment.getText().toString().isEmpty()), true); //mandatorio si se ingresó el depto
        error += activity.validateItem(tilApartment, tietApartment, (!tietFloor.getText().toString().isEmpty()), true); //mandatorio si se ingresó el piso

        return (error == 0);
    }

}