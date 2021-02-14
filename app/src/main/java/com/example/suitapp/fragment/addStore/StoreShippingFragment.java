package com.example.suitapp.fragment.addStore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.suitapp.R;

public class StoreShippingFragment extends Fragment {
    View root;

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
        
        root =  inflater.inflate(R.layout.fragment_store_shipping, container, false);
        init();
        return root;
    }

    private void init() {
    }
}