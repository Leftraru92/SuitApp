package com.example.suitapp.fragment.addStore;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.example.suitapp.AddStoreActivity;
import com.example.suitapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class StoreNameFragment extends Fragment {

    private AddStoreViewModel mViewModel;
    private TextInputLayout tilStoreName, tilStoreDesc;
    private TextInputEditText tietStoreName, tietStoreDesc;
    View root;
    AddStoreActivity activity;
    boolean fromReview;

    public static StoreNameFragment newInstance() {
        return new StoreNameFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_store_name, container, false);
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
        tilStoreName = root.findViewById(R.id.tilStoreName);
        tilStoreDesc = root.findViewById(R.id.tilStoreDesc);
        tietStoreName = root.findViewById(R.id.tietStoreName);
        tietStoreDesc = root.findViewById(R.id.tietStoreDesc);

        //show keyboard
        //tietStoreName.requestFocus();
        //InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        //listener
        ((Button) root.findViewById(R.id.btContinue)).setOnClickListener(v -> next());

        //complete data
        tietStoreName.setText(mViewModel.getName().getValue());
        tietStoreDesc.setText(mViewModel.getDesc().getValue());
    }

    private void next() {
        if (validate()) {
            mViewModel.setName(tietStoreName.getText().toString());
            mViewModel.setDesc(tietStoreDesc.getText().toString());
            if (fromReview)
                Navigation.findNavController(root).navigate(R.id.action_nav_store_name_to_nav_store_review);
            else
                Navigation.findNavController(root).navigate(R.id.action_nav_store_name_to_nav_address_store);
        } else {
            activity.mostrarMensaje(getString(R.string.error_complete_form));
        }
    }

    private boolean validate() {
        int error = 0;

        error += activity.validateItem(tilStoreName, tietStoreName, true, true);
        error += activity.validateItem(tilStoreDesc, tietStoreDesc, false, true);

        return error == 0;
    }

}