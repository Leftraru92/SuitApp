package com.example.suitapp.fragment;

import android.content.Context;
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
import android.widget.Toast;

import com.example.suitapp.adapter.FavsRecyclerViewAdapter;
import com.example.suitapp.R;
import com.example.suitapp.dummy.DummyArticles;
import com.example.suitapp.dummy.DummyContent;

/**
 * A fragment representing a list of Items.
 */
public class FavsFragment extends Fragment implements FavsRecyclerViewAdapter.OnFavListener, FavsRecyclerViewAdapter.OnAddToCartListener {

    View root;
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FavsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FavsFragment newInstance(int columnCount) {
        FavsFragment fragment = new FavsFragment();
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
        root = inflater.inflate(R.layout.fragment_favs_list, container, false);

        // Set the adapter
        if (root instanceof RecyclerView) {
            Context context = root.getContext();
            RecyclerView recyclerView = (RecyclerView) root;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new FavsRecyclerViewAdapter(DummyArticles.ITEMS, this, this));
        }
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_favs, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                Navigation.findNavController(root).navigate(R.id.action_nav_favs_to_nav_cart);
                return true;
            case R.id.action_search:
                Navigation.findNavController(root).navigate(R.id.action_nav_favs_to_nav_search);
                return true;
            default:
                return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(root));
        }
    }

    @Override
    public void onFavClick(int position) {
        Navigation.findNavController(root).navigate(R.id.action_nav_favs_to_nav_article_detail);
    }

    @Override
    public void OnAddToCartClick(int position) {
        Navigation.findNavController(root).navigate(R.id.action_nav_favs_to_nav_article_added);
    }
}