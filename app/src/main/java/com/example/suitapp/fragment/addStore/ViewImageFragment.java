package com.example.suitapp.fragment.addStore;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.suitapp.R;
import com.example.suitapp.util.Constants;
import com.example.suitapp.viewmodel.AddArticleViewModel;
import com.example.suitapp.viewmodel.AddStoreViewModel;
import com.example.suitapp.viewmodel.ArticleDetailViewModel;
import com.example.suitapp.viewmodel.CaptureImageViewModel;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ViewImageFragment extends Fragment {

    private static final String ARG_IMAGE = "imageView";
    private static final String ARG_ID = "imageID";

    View root;
    private CaptureImageViewModel mViewModel;
    FloatingActionButton fabRotateL, fabRotateR;
    PhotoView photoView;
    int requestId;
    int imageArticle;
    Bitmap bitmap;
    boolean editMode;

    public static ViewImageFragment newInstance(int requestId, int imageArticle) {
        ViewImageFragment fragment = new ViewImageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE, requestId);
        args.putInt(ARG_ID, imageArticle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            requestId = getArguments().getInt(ARG_IMAGE);
            imageArticle = getArguments().getInt(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_view_image, container, false);
        init();
        return root;
    }

    private void init() {

        //set
        switch (requestId) {
            case Constants.IMAGE_LOGO:
            case Constants.IMAGE_PORTADA:
                mViewModel = new ViewModelProvider(getActivity()).get(AddStoreViewModel.class);
                imageArticle = requestId;
                editMode = true;
                break;
            case Constants.IMAGE_ARTICLE:
                mViewModel = new ViewModelProvider(getActivity()).get(AddArticleViewModel.class);
                editMode = true;
                break;
            case Constants.IMAGE_ARTICLE_DETAIL:
                mViewModel = new ViewModelProvider(getActivity()).get(ArticleDetailViewModel.class);
                editMode = false;
                break;
        }

        //bind
        photoView = root.findViewById(R.id.imageView);
        fabRotateL = root.findViewById(R.id.fabRotateL);
        fabRotateR = root.findViewById(R.id.fabRotateR);
        FloatingActionButton fabClose = root.findViewById(R.id.fabClose);

        //listener
        fabRotateL.setOnClickListener(view -> {
            photoView.setRotation(photoView.getRotation() - 90);
            saveChange();
        });
        fabRotateR.setOnClickListener(view -> {
            photoView.setRotation(photoView.getRotation() + 90);
            saveChange();
        });
        fabClose.setOnClickListener(view -> Navigation.findNavController(root).navigateUp());

        //viewmodel
        mViewModel.getImage(requestId, imageArticle).observe(getViewLifecycleOwner(), bm -> {
            photoView.setImageBitmap(bm);
            bitmap = bm;
        });

        //if articles
        if (requestId == Constants.IMAGE_ARTICLE) {
            root.findViewById(R.id.ccBackground).setBackgroundColor(getResources().getColor(R.color.white));
            fabClose.setVisibility(View.GONE);
        }

        fabRotateL.setVisibility((editMode) ? View.VISIBLE : View.GONE);
        fabRotateR.setVisibility((editMode) ? View.VISIBLE : View.GONE);

    }

    public void saveChange() {
        if (bitmap != null) {
            Matrix matrix = new Matrix();
            matrix.postRotate(photoView.getRotation());
            //Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, photoView.getWidth(), photoView.getHeight(), true);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            mViewModel.updateImage(rotatedBitmap, imageArticle);
        }
    }
}