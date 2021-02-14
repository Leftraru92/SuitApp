package com.example.suitapp.fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suitapp.AddStoreActivity;
import com.example.suitapp.adapter.StoresRecyclerViewAdapter;
import com.example.suitapp.dummy.DummyStores;
import com.example.suitapp.model.Store;
import com.example.suitapp.viewmodel.AccountViewModel;
import com.example.suitapp.R;
import com.example.suitapp.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment implements StoresRecyclerViewAdapter.OnStoreListener {

    private SearchViewModel searchViewModel;
    View root;
    List<Store> myStores;

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.fragment_account, container, false);

        Button btAddStore = root.findViewById(R.id.btAddStore);
        btAddStore.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddStoreActivity.class);
            getContext().startActivity(intent);
        });

        searchViewModel = new ViewModelProvider(getActivity()).get(SearchViewModel.class);

        RecyclerView recyclerViewStores = root.findViewById(R.id.list_stores);
        myStores = new ArrayList<>();
        myStores.add(DummyStores.ITEMS.get(0));
        myStores.add(DummyStores.ITEMS.get(1));
        recyclerViewStores.setAdapter(new StoresRecyclerViewAdapter(myStores, R.layout.card_my_store, this));

        return  root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStoreClick(int position) {
        searchViewModel.setStoreName(DummyStores.ITEMS.get(position).getName());
        searchViewModel.setStore(true);
        Navigation.findNavController(root).navigate(R.id.action_nav_account_to_nav_article);
        Toast.makeText(getContext(), "Se tocó la tienda " + myStores.get(position).getName(), Toast.LENGTH_LONG).show();
    }
}