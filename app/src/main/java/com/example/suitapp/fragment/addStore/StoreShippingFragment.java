package com.example.suitapp.fragment.addStore;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton;
import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.suitapp.AddStoreActivity;
import com.example.suitapp.R;
import com.example.suitapp.adapter.ShippingPriceAdapter;
import com.example.suitapp.listener.OclShippingPrice;
import com.example.suitapp.model.ProvinceList;
import com.example.suitapp.util.Constants;

public class StoreShippingFragment extends Fragment implements ShippingPriceAdapter.OnShippingPriceListener {
    private AddStoreViewModel mViewModel;
    View root;
    ThemedButton btShipping, btPersonal;
    RecyclerView recyclerListShipping;
    ShippingPriceAdapter shippingPriceAdapter;
    OclShippingPrice oclShippingPrice;
    AddStoreActivity activity;
    ThemedToggleButtonGroup tgShippingType;

    public static StoreShippingFragment newInstance() {
        return new StoreShippingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_store_shipping, container, false);
        init();
        return root;
    }

    private void init() {
        //set
        activity = ((AddStoreActivity) getActivity());
        mViewModel = new ViewModelProvider(getActivity()).get(AddStoreViewModel.class);
        recyclerListShipping = root.findViewById(R.id.listShipping);
        shippingPriceAdapter = new ShippingPriceAdapter(mViewModel.getShippingPrice().getValue(), this, R.layout.card_shipping_price);
        recyclerListShipping.setAdapter(shippingPriceAdapter);

        //bind
        ConstraintLayout ccShipping = root.findViewById(R.id.ccShipping);
        tgShippingType = root.findViewById(R.id.tgShippingType);
        btShipping = root.findViewById(R.id.btShipping);
        btPersonal = root.findViewById(R.id.btPersonal);
        Button btContinue = root.findViewById(R.id.btContinue);
        Button btAddShipping = root.findViewById(R.id.btAddShipping);

        //Listener
        btContinue.setOnClickListener(v -> next());
        oclShippingPrice = new OclShippingPrice(getContext(), mViewModel, (new ProvinceList()), Constants.SELECT_SHIPPING);
        btAddShipping.setOnClickListener(v -> oclShippingPrice.onClick(v));

        tgShippingType.setOnSelectListener((ThemedButton btn) -> {
            if (btn.getId() == R.id.btShipping) {
                if (btShipping.isSelected())
                    ccShipping.setVisibility(View.VISIBLE);
                else
                    ccShipping.setVisibility(View.GONE);
            }
            return kotlin.Unit.INSTANCE;
        });

        //viewmodel
        mViewModel.getMailShipping().observe(getViewLifecycleOwner(), s -> {
            if (s)
                tgShippingType.selectButton(btShipping.getId());
        });
        mViewModel.getPersonalShipping().observe(getViewLifecycleOwner(), s -> {
            if (s)
                tgShippingType.selectButton(btPersonal.getId());
        });
        mViewModel.getShippingPrice().observe(getViewLifecycleOwner(), s -> shippingPriceAdapter.notifyDataSetChanged());
    }

    private void next() {
        if(validate()) {
            mViewModel.setMailShipping(btShipping.isSelected());
            mViewModel.setPersonalShipping(btPersonal.isSelected());
            Navigation.findNavController(root).navigate(R.id.action_nav_shipping_store_to_nav_store_review);
        }
    }

    private boolean validate() {
        int error = 0;
        if(tgShippingType.getSelectedButtons().size() == 0) {
            error++;
            activity.mostrarMensaje(getString(R.string.error_shipping));
        }
        if(btShipping.isSelected() && mViewModel.getShippingPrice().getValue().size() == 0) {
            error++;
            activity.mostrarMensaje(getString(R.string.error_shipping_prov));
        }
        return error == 0;
    }

    @Override
    public void onShippingPriceClick(int position, int requestId) {
        if (requestId == Constants.IMAGE_PORTADA)
            mViewModel.deleteShippingPrice(position);
        if(requestId == Constants.IMAGE_LOGO) {
            mViewModel.setEditShippingPrice(position);
            oclShippingPrice.onClick(root);
        }
    }
}