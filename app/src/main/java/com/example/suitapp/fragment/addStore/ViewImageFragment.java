package com.example.suitapp.fragment.addStore;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.suitapp.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ViewImageFragment extends Fragment {
    View root;
    private AddStoreViewModel mViewModel;
    FloatingActionButton fabRotateL, fabRotateR;
    PhotoView photoView;
    int image;
    Bitmap bitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_view_image, container, false);
        init();
        return root;
    }

    private void init() {

        //set
        mViewModel = new ViewModelProvider(getActivity()).get(AddStoreViewModel.class);
        image = getArguments().getInt("imageView");

        //bind
        photoView = root.findViewById(R.id.imageView);
        fabRotateL = root.findViewById(R.id.fabRotateL);
        fabRotateR = root.findViewById(R.id.fabRotateR);
        FloatingActionButton fabClose = root.findViewById(R.id.fabClose);

        //listener
        fabRotateL.setOnClickListener(view -> photoView.setRotation(photoView.getRotation() - 90));
        fabRotateR.setOnClickListener(view -> photoView.setRotation(photoView.getRotation() + 90));
        fabClose.setOnClickListener(view -> Navigation.findNavController(root).navigateUp());

        //viewmodel
        mViewModel.getImage(image).observe(getViewLifecycleOwner(), bm -> {
            photoView.setImageBitmap(bm);
            bitmap = bm;
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (bitmap != null) {
            Matrix matrix = new Matrix();
            matrix.postRotate(photoView.getRotation());
            //Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, photoView.getWidth(), photoView.getHeight(), true);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            mViewModel.setImage(rotatedBitmap, image);
        }
    }
}