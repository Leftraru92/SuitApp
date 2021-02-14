package com.example.suitapp.fragment.addStore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.suitapp.AddStoreActivity;
import com.example.suitapp.R;
import com.example.suitapp.listener.OclAddImage;
import com.example.suitapp.util.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ImageStoreFragment extends Fragment {

    private AddStoreViewModel mViewModel;
    private OclAddImage selectImageLogo, selectImagePortada;
    private View root;
    AddStoreActivity activity;

    private int LOGO = 1, PORTADA = 2;


    public static ImageStoreFragment newInstance() {
        return new ImageStoreFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_image_store, container, false);
        init();

        return root;
    }

    private void init() {
        activity = ((AddStoreActivity) getActivity());
        mViewModel = new ViewModelProvider(activity).get(AddStoreViewModel.class);
        selectImageLogo = new OclAddImage(getContext(), LOGO, false, mViewModel, Constants.IMAGE_LOGO);
        selectImagePortada = new OclAddImage(getContext(), PORTADA, false, mViewModel, Constants.IMAGE_PORTADA);

        //bind
        FloatingActionButton btnCameraLogo = root.findViewById(R.id.btnCameraLogo);
        FloatingActionButton btnCameraPortada = root.findViewById(R.id.btnCameraPortada);
        ImageView ivLogo = root.findViewById(R.id.ivLogo);
        ImageView ivPortada = root.findViewById(R.id.ivPortada);
        Button btContinue = root.findViewById(R.id.btContinue);

        //listeners
        btnCameraLogo.setOnClickListener(selectImageLogo);
        btnCameraPortada.setOnClickListener(selectImagePortada);
        btContinue.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_image_store_to_nav_shipping_store));

        //viewmodel
        mViewModel.getImageLogo().observe(getViewLifecycleOwner(), s -> {
            ivLogo.setImageBitmap(s);
        });

        mViewModel.getImagePortada().observe(getViewLifecycleOwner(), s -> {
            ivPortada.setImageBitmap(s);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(Constants.LOG, "resultado " + requestCode);
        if (requestCode == LOGO)
            selectImageLogo.processData(requestCode, resultCode, data);
        else if (requestCode == PORTADA)
            selectImagePortada.processData(requestCode, resultCode, data);
    }
}