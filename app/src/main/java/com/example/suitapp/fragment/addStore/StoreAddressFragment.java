package com.example.suitapp.fragment.addStore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.suitapp.AddStoreActivity;
import com.example.suitapp.R;
import com.example.suitapp.dummy.ProvinceList;
import com.example.suitapp.listener.OclSelectDialog;
import com.example.suitapp.model.Province;
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

    public StoreAddressFragment() {
        // Required empty public constructor
    }

    public static StoreAddressFragment newInstance(String param1, String param2) {
        StoreAddressFragment fragment = new StoreAddressFragment();
        return fragment;
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
        activity =  ((AddStoreActivity) getActivity());
        mViewModel = new ViewModelProvider(getActivity()).get(AddStoreViewModel.class);

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
        OclSelectDialog oclSelectDialog = new OclSelectDialog(getContext(), mViewModel, (new ProvinceList()));
        tietProvince.setOnClickListener(view -> {
            oclSelectDialog.selectItem();
        });
        ((Button) root.findViewById(R.id.btContinue)).setOnClickListener(v -> next());

        //viewmodel
        mViewModel.getProvince().observe(getViewLifecycleOwner(), (Observer<String>) s -> {
            tietProvince.setText(s);
        });
        tilProvince.setEndIconOnClickListener(v -> {
            mViewModel.selectValue(0, "");
        });
    }

    private void next() {
        if (validate()) {
            mViewModel.setCity(tietCity.getText().toString());
            mViewModel.setNumber(tietNumber.getText().toString());
            mViewModel.setFloor(tietFloor.getText().toString());
            mViewModel.setApartment(tietApartment.getText().toString());

            Navigation.findNavController(root).navigate(R.id.action_nav_address_store_to_imageStoreFragment);
        } else {
            activity.mostrarMensaje("Complete el formulario para continuar");
        }
    }

    private boolean validate() {
        int error = 0;
        error += activity.validateItem(tilProvince, tietProvince, true, false);
        error += activity.validateItem(tilCity, tietCity, true, true);
        error += activity.validateItem(tilStreet, tietStreet, false, true);
        error += activity.validateItem(tilNumber, tietNumber, false, true);
        error += activity.validateItem(tilFloor, tietFloor, false, true);
        error += activity.validateItem(tilApartment, tietApartment, false, true);

        return (error==0);
    }

}