package com.example.suitapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.suitapp.R;
import com.example.suitapp.adapter.PackagesAdapter;
import com.example.suitapp.dummy.DummyPackage;
import com.example.suitapp.viewmodel.ArticleDetailViewModel;
import com.example.suitapp.viewmodel.ShoppingViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class SelectShippingFragment extends Fragment {
    View root;
    PackagesAdapter packagesAdapter;
    ShoppingViewModel shoppingViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_select_shipping, container, false);
        init();
        return root;
    }

    private void init() {
        //set
        setHasOptionsMenu(true);
        shoppingViewModel = new ViewModelProvider(getActivity()).get(ShoppingViewModel.class);
        packagesAdapter = new PackagesAdapter(DummyPackage.ITEMS, shoppingViewModel);
        shoppingViewModel.setPackages(DummyPackage.ITEMS);

        //bind
        Button btContinue = root.findViewById(R.id.btContinue);
        RecyclerView listPackages = root.findViewById(R.id.listPackages);
        listPackages.setAdapter(packagesAdapter);

        //listener
        btContinue.setOnClickListener(v -> next());
    }

    private void next() {
        if (shoppingViewModel.isAllShippingSelected())
            Navigation.findNavController(root).navigate(R.id.action_nav_select_shipping_to_nav_card_credit);
        else
            Snackbar.make(root, "Debe seleccionar el método de envío", BaseTransientBottomBar.LENGTH_LONG).show();
    }
}