package com.example.suitapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.suitapp.activity.AddArticleActivity;
import com.example.suitapp.adapter.CartItemRecyclerViewAdapter;
import com.example.suitapp.R;
import com.example.suitapp.dummy.DummyArticles;
import com.example.suitapp.dummy.DummyContent;

/**
 * A fragment representing a list of Items.
 */
public class CartFragment extends Fragment {

    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_cart_list, container, false);
        init();
        return root;
    }

    private void init() {
        setHasOptionsMenu(true);

        RecyclerView cartList = root.findViewById(R.id.cartList);
        cartList.setAdapter(new CartItemRecyclerViewAdapter(DummyArticles.ITEMS));

        Button btAddProduct = getActivity().findViewById(R.id.btAddProduct);
        btAddProduct.setVisibility(View.VISIBLE);
        btAddProduct.setText("Continuar compra");
        btAddProduct.setOnClickListener(v -> {
            Navigation.findNavController(root).navigate(R.id.action_nav_cart_to_nav_select_shipping);
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cart, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Navigation.findNavController(root).navigate(R.id.action_nav_cart_to_nav_search);
                return true;
            default:
                return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(root));
        }
    }
}