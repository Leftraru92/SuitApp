package com.example.suitapp.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suitapp.R;
import com.example.suitapp.adapter.ShippingPriceAdapter;
import com.example.suitapp.model.Store;
import com.example.suitapp.util.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.chip.Chip;

public class StoreDetailFragment extends Fragment implements OnMapReadyCallback {
    View root;
    protected static GoogleMap mMap;
    SupportMapFragment mapFragment;
    Store mStore;
    RecyclerView recyclerListShipping;
    ShippingPriceAdapter shippingPriceAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mStore = StoreDetailFragmentArgs.fromBundle(getArguments()).getStore();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        root = inflater.inflate(R.layout.fragment_store_detail, container, false);
        init();
        return root;
    }

    private void init() {
        //set
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);

        //bind
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView tvStoreName = root.findViewById(R.id.tvStoreName);
        TextView tvAddress = root.findViewById(R.id.tvAddress);
        ImageView ivBanner = root.findViewById(R.id.ivBanner);
        ImageView ivLogo = root.findViewById(R.id.ivLogo);
        TextView tvDesc = root.findViewById(R.id.tvDesc);
        Chip chipMail = root.findViewById(R.id.chipMail);
        Chip chipPersonal = root.findViewById(R.id.chipPersonal);

        //set
        toolbar.setTitle(mStore.getName());
        tvStoreName.setText(mStore.getName());
        tvAddress.setText(mStore.getAddress());
        tvDesc.setText(mStore.getDescription());

        if (mStore.getStoreCoverPhoto() != null && !mStore.getStoreCoverPhoto().equals("")) {
            byte[] decodedString = Base64.decode(mStore.getStoreCoverPhoto(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivBanner.setImageBitmap(decodedByte);
        }

        if (mStore.getStoreLogo() != null && !mStore.getStoreLogo().equals("")) {
            byte[] decodedString = Base64.decode(mStore.getStoreLogo(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivLogo.setImageBitmap(decodedByte);
        }

        chipPersonal.setVisibility((mStore.isPhysical_store()) ? View.VISIBLE : View.GONE);
        chipMail.setVisibility((mStore.isMailShipping()) ? View.VISIBLE : View.GONE);

        if(mStore.isMailShipping()) {
            (root.findViewById(R.id.ccShippingPrice)).setVisibility(View.VISIBLE);
            shippingPriceAdapter = new ShippingPriceAdapter(mStore.getShippingPrice(), null, R.layout.card_shipping_price_mini);
            recyclerListShipping = root.findViewById(R.id.listShipping);
            recyclerListShipping.setAdapter(shippingPriceAdapter);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(-32.9439603559106, -60.6621152782223);
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Marcador en tienda"));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
    }
}