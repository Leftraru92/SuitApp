package com.example.suitapp.fragment.addStore;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

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
    boolean fromReview;
    Bundle bundlePort, bundleLogo;
    ImageView ivLogo, ivPortada;

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

        //set
        activity = ((AddStoreActivity) getActivity());
        mViewModel = new ViewModelProvider(activity).get(AddStoreViewModel.class);
        selectImageLogo = new OclAddImage(getContext(), Constants.IMAGE_LOGO, false, mViewModel, Constants.IMAGE_LOGO);
        selectImagePortada = new OclAddImage(getContext(), Constants.IMAGE_PORTADA, false, mViewModel, Constants.IMAGE_PORTADA);
        fromReview = false;
        if (getArguments() != null)
            fromReview = ImageStoreFragmentArgs.fromBundle(getArguments()).getFromReview();
        bundlePort = new Bundle();
        bundlePort.putInt("imageView", Constants.IMAGE_PORTADA);
        bundleLogo = new Bundle();
        bundleLogo.putInt("imageView", Constants.IMAGE_LOGO);

        //bind
        FloatingActionButton btnCameraLogo = root.findViewById(R.id.btnCameraLogo);
        FloatingActionButton btnCameraPortada = root.findViewById(R.id.btnCameraPortada);
        ivLogo = root.findViewById(R.id.ivLogo);
        ivPortada = root.findViewById(R.id.ivPortada);
        Button btContinue = root.findViewById(R.id.btContinue);

        //listeners
        if (mViewModel.getImage(Constants.IMAGE_PORTADA).getValue() != null)
            ivPortada.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_image_store_to_nav_view_image, bundlePort));
        if (mViewModel.getImage(Constants.IMAGE_LOGO).getValue() != null)
        ivLogo.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_image_store_to_nav_view_image, bundleLogo));
        btnCameraLogo.setOnClickListener(selectImageLogo);
        btnCameraPortada.setOnClickListener(selectImagePortada);
        btContinue.setOnClickListener(v -> {
                    if (fromReview)
                        Navigation.findNavController(root).navigate(R.id.action_nav_image_store_to_nav_store_review);
                    else
                        Navigation.findNavController(root).navigate(R.id.action_nav_image_store_to_nav_shipping_store);
                }
        );

        //viewmodel
        mViewModel.getImageLogo().observe(getViewLifecycleOwner(), s -> ivLogo.setImageBitmap(s));
        mViewModel.getImagePortada().observe(getViewLifecycleOwner(), s -> ivPortada.setImageBitmap(s));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(Constants.LOG, "resultado " + requestCode);
        if (requestCode == Constants.IMAGE_LOGO) {
            selectImageLogo.processData(requestCode, resultCode, data);
            ivLogo.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_image_store_to_nav_view_image, bundleLogo));
        }else if (requestCode == Constants.IMAGE_PORTADA) {
            selectImagePortada.processData(requestCode, resultCode, data);
            ivPortada.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_image_store_to_nav_view_image, bundlePort));
        }
    }
}