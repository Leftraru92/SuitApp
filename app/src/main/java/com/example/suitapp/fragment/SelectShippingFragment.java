package com.example.suitapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.suitapp.R;
import com.example.suitapp.adapter.PackagesAdapter;
import com.example.suitapp.dummy.DummyPackage;

public class SelectShippingFragment extends Fragment {
    View root;
    PackagesAdapter packagesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root =  inflater.inflate(R.layout.fragment_select_shipping, container, false);
        init();
        return root;
    }

    private void init() {
        setHasOptionsMenu(true);

        RecyclerView listPackages = root.findViewById(R.id.listPackages);
        packagesAdapter = new PackagesAdapter(DummyPackage.ITEMS);
        listPackages.setAdapter(packagesAdapter);
    }
}