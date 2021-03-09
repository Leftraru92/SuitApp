package com.example.suitapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.example.suitapp.R;
import com.example.suitapp.activity.AddStoreActivity;
import com.example.suitapp.activity.MainActivity;
import com.example.suitapp.api.CallWebService;
import com.example.suitapp.api.WebService;
import com.example.suitapp.fragment.addStore.StoreNameFragmentArgs;
import com.example.suitapp.listener.OclSelectDialog;
import com.example.suitapp.model.Province;
import com.example.suitapp.model.ProvinceList;
import com.example.suitapp.util.Constants;
import com.example.suitapp.util.SingletonUser;
import com.example.suitapp.viewmodel.AddStoreViewModel;
import com.example.suitapp.viewmodel.AddressViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

public class UserAddressFragment extends Fragment implements CallWebService {
    private AddressViewModel mViewModel;
    private final List<Province> provinces = ProvinceList.ITEMS;
    View root;
    TextInputLayout tilProvince, tilCity, tilCp, tilStreet, tilNumber, tilFloor, tilApartment, tilDetail;
    TextInputEditText tietProvince, tietCity, tietCp, tietStreet, tietNumber, tietFloor, tietApartment, tietDetail;
    final int RC_ADDRESS = 1;

    public static UserAddressFragment newInstance() {
        return new UserAddressFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_user_address, container, false);
        init();
        return root;
    }

    private void init() {
        mViewModel = new ViewModelProvider(getActivity()).get(AddressViewModel.class);

        //bind
        tilProvince = root.findViewById(R.id.tilProvince);
        tilCity = root.findViewById(R.id.tilCity);
        tilCp = root.findViewById(R.id.tilCp);
        tilStreet = root.findViewById(R.id.tilStreet);
        tilNumber = root.findViewById(R.id.tilNumber);
        tilFloor = root.findViewById(R.id.tilFloor);
        tilApartment = root.findViewById(R.id.tilApartment);
        tilDetail = root.findViewById(R.id.tilDetail);

        tietProvince = root.findViewById(R.id.tietProvince);
        tietCity = root.findViewById(R.id.tietCity);
        tietCp = root.findViewById(R.id.tietCp);
        tietStreet = root.findViewById(R.id.tietStreet);
        tietNumber = root.findViewById(R.id.tietNumber);
        tietFloor = root.findViewById(R.id.tietFloor);
        tietApartment = root.findViewById(R.id.tietApartment);
        tietDetail = root.findViewById(R.id.tietDetail);

        //listener
        OclSelectDialog oclSelectDialog = new OclSelectDialog(getContext(), mViewModel, (new ProvinceList()), Constants.SELECT_PROVINCE);
        tietProvince.setOnClickListener(oclSelectDialog);
        ((Button) root.findViewById(R.id.btContinue)).setOnClickListener(v -> next());
        tilProvince.setEndIconOnClickListener(v -> mViewModel.selectValue(0, "", Constants.SELECT_PROVINCE));

        //viewmodel
        mViewModel.getProvince().observe(getViewLifecycleOwner(), s -> tietProvince.setText(s));
        mViewModel.getCp().observe(getViewLifecycleOwner(), s -> tietCp.setText(String.valueOf(s)));
        mViewModel.getCity().observe(getViewLifecycleOwner(), s -> tietCity.setText(s));
        mViewModel.getStreet().observe(getViewLifecycleOwner(), s -> tietStreet.setText(s));
        mViewModel.getNumber().observe(getViewLifecycleOwner(), s -> tietNumber.setText(s));
        mViewModel.getFloor().observe(getViewLifecycleOwner(), s -> tietFloor.setText(s));
        mViewModel.getApartment().observe(getViewLifecycleOwner(), s -> tietApartment.setText(s));
        mViewModel.getDetail().observe(getViewLifecycleOwner(), s -> tietDetail.setText(s));

    }

    private void next() {
        if (validate()) {
            mViewModel.setCity(tietCity.getText().toString());
            mViewModel.setCp(Integer.parseInt(tietCp.getText().toString()));
            mViewModel.setStreet(tietStreet.getText().toString());
            mViewModel.setNumber(tietNumber.getText().toString());
            mViewModel.setFloor(tietFloor.getText().toString());
            mViewModel.setApartment(tietApartment.getText().toString());
            mViewModel.setDetail(tietDetail.getText().toString());

            callWs();
        } else {
            Snackbar.make(root, "Complete el formulario para continuar", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(getResources().getColor(R.color.black))
                    .setTextColor(getResources().getColor(R.color.white))
                    .show();
        }
    }

    private void callWs() {
        String email = SingletonUser.getInstance(getContext()).getHash();
        JSONObject body = mViewModel.toJSON(email);
        WebService webService = new WebService(getContext(), RC_ADDRESS);
        webService.callService(this, Constants.WS_DOMINIO + Constants.WS_ADDRESS, null, Request.Method.POST, Constants.JSON_TYPE.OBJECT, body);
    }

    private boolean validate() {
        int error = 0;
        error += validateItem(tilProvince, tietProvince, true, false);
        error += validateItem(tilCity, tietCity, true, true);
        error += validateItem(tilCp, tietCp, true, true);
        error += validateItem(tilStreet, tietStreet, true, true); //mandatorio si se ingresó el número
        error += validateItem(tilNumber, tietNumber, true, true); //mandatorio si se ingresó la calle
        error += validateItem(tilFloor, tietFloor, (!tietApartment.getText().toString().isEmpty()), true); //mandatorio si se ingresó el depto
        error += validateItem(tilApartment, tietApartment, (!tietFloor.getText().toString().isEmpty()), true); //mandatorio si se ingresó el piso
        error += validateItem(tilDetail, tietDetail, false, true);

        return (error == 0);
    }

    public int validateItem(TextInputLayout tilItem, TextInputEditText tietItem, boolean mandatory, boolean max) {
        int error = 0;
        if (mandatory && tietItem.length() == 0) {
            tilItem.setError(getResources().getString(R.string.mandatory_field));
            error++;
        } else if (max && tietItem.length() > tilItem.getCounterMaxLength()) {
            tilItem.setError(getResources().getString(R.string.error_max_length));
            error++;
        } else {
            tilItem.setError(null);
        }
        return error;
    }


    @Override
    public void onResult(int requestCode, JSONObject response) {
        mViewModel.clean();
        Navigation.findNavController(root).navigateUp();
    }

    @Override
    public void onResult(int requestCode, JSONArray response) {

    }

    @Override
    public void onError(int requestCode, String message) {
        Snackbar.make(root, "Error: " + message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.black))
                .setTextColor(getResources().getColor(R.color.white))
                .show();
    }
}