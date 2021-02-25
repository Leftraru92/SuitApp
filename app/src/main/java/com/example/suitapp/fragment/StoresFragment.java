package com.example.suitapp.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.suitapp.R;
import com.example.suitapp.adapter.StoresRecyclerViewAdapter;
import com.example.suitapp.dummy.DummyContent;
import com.example.suitapp.dummy.DummyStores;

/**
 * A fragment representing a list of Items.
 */
public class StoresFragment extends Fragment implements StoresRecyclerViewAdapter.OnStoreListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StoresFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static StoresFragment newInstance(int columnCount) {
        StoresFragment fragment = new StoresFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new StoresRecyclerViewAdapter(DummyStores.ITEMS, R.layout.card_stores, this));
        }
        return view;
    }

    @Override
    public void onStoreClick(int position) {
        Toast.makeText(getContext(), "Se tocó la tienda " + DummyStores.ITEMS.get(position).getName(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStoreEdit(int position) {

    }

    @Override
    public void onStoreDelete(int position) {

    }
}