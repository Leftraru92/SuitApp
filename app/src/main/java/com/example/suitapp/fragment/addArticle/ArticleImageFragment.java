package com.example.suitapp.fragment.addArticle;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.suitapp.R;
import com.example.suitapp.adapter.AddArticleImagesAdapter;
import com.example.suitapp.fragment.addStore.ViewImageFragment;
import com.example.suitapp.listener.OclAddImage;
import com.example.suitapp.util.Constants;
import com.example.suitapp.viewmodel.AddArticleViewModel;

public class ArticleImageFragment extends Fragment implements AddArticleImagesAdapter.OnImageListener {
    View root;
    private OclAddImage selectImages;
    private AddArticleViewModel mViewModel;
    RecyclerView recyclerImages;
    AddArticleImagesAdapter addArticleImagesAdapter;
    int position;
    ConstraintLayout ccImageDefault, ccContent;
    boolean fromReview;

    public static ArticleImageFragment newInstance() {
        return new ArticleImageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_article_image, container, false);
        init();
        return root;
    }

    private void init() {
        //set
        mViewModel = new ViewModelProvider(getActivity()).get(AddArticleViewModel.class);
        selectImages = new OclAddImage(getContext(), Constants.IMAGE_ARTICLE, true, mViewModel);
        recyclerImages = root.findViewById(R.id.list_images);
        addArticleImagesAdapter = new AddArticleImagesAdapter(mViewModel.getImages().getValue(), this, true);
        recyclerImages.setAdapter(addArticleImagesAdapter);
        fromReview = false;
        if (getArguments() != null)
            fromReview = ArticleNameFragmentArgs.fromBundle(getArguments()).getFromReview();


        //bind
        Button btAddImage = root.findViewById(R.id.btAddImage);
        Button btContinue = root.findViewById(R.id.btContinue);
        ccImageDefault = root.findViewById(R.id.ccImageDefault);
        ccContent = root.findViewById(R.id.ccContent);

        //listener
        btAddImage.setOnClickListener(selectImages);
        btContinue.setOnClickListener(v -> next());

        //viewmodel
        mViewModel.getImages().observe(getViewLifecycleOwner(), s -> {
            addArticleImagesAdapter.notifyDataSetChanged();
            if (s.size() > 0) {
                viewImageInFragment(position);
                ccImageDefault.setVisibility(View.INVISIBLE);
                ccContent.setBackgroundColor(getResources().getColor(R.color.gray_200));
            }else{
                ccImageDefault.setVisibility(View.VISIBLE);
                ccContent.setBackgroundColor(getResources().getColor(R.color.cian_400));
            }
        });
    }

    private void next() {
        if (fromReview)
            Navigation.findNavController(root).navigate(R.id.action_nav_article_images_to_nav_article_review);
        else
            Navigation.findNavController(root).navigate(R.id.action_nav_article_images_to_nav_article_price);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(Constants.LOG, "resultado " + requestCode);
        if (requestCode == Constants.IMAGE_ARTICLE) {
            selectImages.processData(requestCode, resultCode, data);
            //ivLogo.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_image_store_to_nav_view_image, bundleLogo));
        }
    }

    private void viewImageInFragment(int imageId) {
        Fragment childFragment = (new ViewImageFragment().newInstance(Constants.IMAGE_ARTICLE, imageId));
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.image_view, childFragment).commit();
    }

    @Override
    public void onImageClick(int position) {
        viewImageInFragment(position);
        this.position = position;
    }

    @Override
    public void onImageDelete(int position) {
        this.position = position;

        AlertDialog dialogo = new AlertDialog
                .Builder(getContext()) // NombreDeTuActividad.this, o getActivity() si es dentro de un fragmento
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteImage();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setTitle("Confirmar") // El título
                .setMessage("¿Deseas eliminar la imagen?") // El mensaje
                .create();
        dialogo.show();


    }

    private void deleteImage() {
        mViewModel.deleteImage(position);
        position = 0;
    }
}