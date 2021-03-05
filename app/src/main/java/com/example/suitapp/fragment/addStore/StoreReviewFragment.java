package com.example.suitapp.fragment.addStore;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suitapp.R;
import com.example.suitapp.adapter.ShippingPriceAdapter;
import com.example.suitapp.util.SingletonUser;
import com.example.suitapp.viewmodel.AddStoreViewModel;
import com.google.android.material.chip.Chip;

import org.json.JSONObject;

public class StoreReviewFragment extends Fragment {
    View root;
    AddStoreViewModel mViewModel;
    RecyclerView recyclerListShipping;
    ShippingPriceAdapter shippingPriceAdapter;

    public static StoreReviewFragment newInstance() {
        return new StoreReviewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_store_review, container, false);
        init();
        return root;
    }

    private void init() {
        //set
        mViewModel = new ViewModelProvider(getActivity()).get(AddStoreViewModel.class);
        shippingPriceAdapter = new ShippingPriceAdapter(mViewModel.getShippingPrice().getValue(), null, R.layout.card_shipping_price_mini);
        recyclerListShipping = root.findViewById(R.id.listShipping);
        recyclerListShipping.setAdapter(shippingPriceAdapter);

        //bind
        CardView cardName = root.findViewById(R.id.cardName);
        CardView cardAddress = root.findViewById(R.id.cardAddress);
        CardView cardImages = root.findViewById(R.id.cardImages);
        CardView cardShipping = root.findViewById(R.id.cardShipping);

        TextView tvName = root.findViewById(R.id.tvName);
        TextView tvDescription = root.findViewById(R.id.tvDescription);
        TextView tvAddress = root.findViewById(R.id.tvAddress);
        ImageView ivLogo = root.findViewById(R.id.ivLogo);
        ImageView ivPortada = root.findViewById(R.id.ivPortada);
        Chip chipPersonal = root.findViewById(R.id.chipPersonal);
        Chip chipMail = root.findViewById(R.id.chipMail);
        TextView lbShippingPrice = root.findViewById(R.id.lbShippingPrice);
        Button btContinue = root.findViewById(R.id.btContinue);

        //listener
        cardName.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_store_review_to_nav_store_name));
        cardAddress.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_store_review_to_nav_address_store));
        cardImages.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_store_review_to_nav_image_store));
        cardShipping.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_store_review_to_nav_shipping_store));
        btContinue.setOnClickListener(v -> next());

        //viewmodel
        mViewModel.getName().observe(getViewLifecycleOwner(), s -> tvName.setText(s));
        mViewModel.getDesc().observe(getViewLifecycleOwner(), s -> {
            if (s.equals(""))
                tvDescription.setVisibility(View.GONE);
            else
                tvDescription.setText(s);
        });
        mViewModel.getAddress().observe(getViewLifecycleOwner(), s -> tvAddress.setText(s));
        mViewModel.getImageLogo().observe(getViewLifecycleOwner(), s -> ivLogo.setImageBitmap(s));
        mViewModel.getImagePortada().observe(getViewLifecycleOwner(), s -> ivPortada.setImageBitmap(s));
        mViewModel.getPersonalShipping().observe(getViewLifecycleOwner(), s -> chipPersonal.setVisibility((s) ? View.VISIBLE : View.GONE));
        mViewModel.getMailShipping().observe(getViewLifecycleOwner(), s -> {
            chipMail.setVisibility((s) ? View.VISIBLE : View.GONE);
            if(!s)
                lbShippingPrice.setVisibility(View.GONE);
        });
        mViewModel.getShippingPrice().observe(getViewLifecycleOwner(), shippingPrices -> {

        });
    }

    private void next() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isArticle", false);
        Navigation.findNavController(root).navigate(R.id.action_nav_store_review_to_nav_congrats);
    }
}