package com.example.suitapp.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton;
import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suitapp.MainActivity;
import com.example.suitapp.R;
import com.example.suitapp.adapter.ArticleAdapter;
import com.example.suitapp.adapter.ArticlesGroupAdapter;
import com.example.suitapp.adapter.ImageAdapter;
import com.example.suitapp.dummy.DummyArticles;
import com.example.suitapp.dummy.DummyArticlesGroup;
import com.example.suitapp.listener.OclQtySelector;
import com.example.suitapp.util.SingletonUser;
import com.example.suitapp.util.Util;
import com.example.suitapp.viewmodel.ArticleDetailViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ArticleDetailFragment extends Fragment implements ArticlesGroupAdapter.OnGroupListener {

    ArticleDetailViewModel adViewModel;
    View root;
    boolean fav;
    TextView tvSize;
    TextView tvColor;

    public static ArticleDetailFragment newInstance() {
        return new ArticleDetailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_article_detail, container, false);
        ViewPager2 viewPager = root.findViewById(R.id.viewPager);
        Button btAddToCart = root.findViewById(R.id.btAddToCart);
        btAddToCart.setOnClickListener(v -> onAddToCart());

        RecyclerView recyclerGroups = root.findViewById(R.id.article_groups_list);
        recyclerGroups.setAdapter(new ArticlesGroupAdapter(DummyArticlesGroup.ITEMS, this, root, R.layout.card_article_group_horizontal_scroll));

        List<Integer> lImagenes = new ArrayList<>();
        lImagenes.add(R.drawable.buzo);
        lImagenes.add(R.drawable.camisa);
        lImagenes.add(R.drawable.zapatilla);
        lImagenes.add(R.drawable.zapatilla2);
        lImagenes.add(R.drawable.pantalon);
        ImageAdapter adapter = new ImageAdapter(lImagenes);
        viewPager.setAdapter(adapter);

        CardView btQuantity = root.findViewById(R.id.btQuantity);
        TextView tvQuantity = root.findViewById(R.id.tvQuantity);

        adViewModel = new ViewModelProvider(this).get(ArticleDetailViewModel.class);
        adViewModel.getQty().observe(getViewLifecycleOwner(), s -> tvQuantity.setText(String.valueOf(s)));
        btQuantity.setOnClickListener(new OclQtySelector(getActivity(), 7, adViewModel));


        ThemedToggleButtonGroup tbgColor = root.findViewById(R.id.tgColors);
        ThemedToggleButtonGroup tbgSize = root.findViewById(R.id.tgSize);

        int index = 0;
        int pxWidth = Util.getPxByDensity(getResources(), 40);
        int pxMargin = Util.getPxByDensity(getResources(), 3);

        ThemedButton btn1 = (ThemedButton) inflater.inflate(R.layout.component_color_toggle, null);
        ThemedButton btn2 = (ThemedButton) inflater.inflate(R.layout.component_color_toggle, null);
        ThemedButton btn3 = (ThemedButton) inflater.inflate(R.layout.component_color_toggle, null);
        ThemedButton btn4 = (ThemedButton) inflater.inflate(R.layout.component_color_toggle, null);
        ThemedButton btn5 = (ThemedButton) inflater.inflate(R.layout.component_color_toggle, null);

        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
                pxWidth, pxWidth);

        btn1.setTag("Verde Claro");
        btn1.setSelectedTextColor(Color.parseColor("#00FF00"));
        btn1.setTextColor(Color.parseColor("#00FF00"));
        tbgColor.addView(btn1, params2);

        btn2.setTag("Verde Oscuro");
        btn2.setSelectedTextColor(Color.parseColor("#00AA00"));
        btn2.setTextColor(Color.parseColor("#00AA00"));
        tbgColor.addView(btn2, params2);

        btn3.setTag("Azul");
        btn3.setSelectedTextColor(Color.parseColor("#0066AA"));
        btn3.setTextColor(Color.parseColor("#0066AA"));
        tbgColor.addView(btn3, params2);

        btn4.setTag("Rosa");
        btn4.setSelectedTextColor(Color.parseColor("#FF66AA"));
        btn4.setTextColor(Color.parseColor("#FF66AA"));
        tbgColor.addView(btn4, params2);

        btn5.setTag("Gris");
        btn5.setSelectedTextColor(Color.parseColor("#AAAAAA"));
        btn5.setTextColor(Color.parseColor("#AAAAAA"));
        tbgColor.addView(btn5, params2);

        tvColor = root.findViewById(R.id.tvColor);

        tbgColor.setOnSelectListener((ThemedButton btn) -> {
            if (((ThemedToggleButtonGroup) btn.getParent()).getSelectedButtons().size() > 0) {
                tvColor.setText(btn.getTag().toString());
                tvColor.setTextColor(getResources().getColor(R.color.cian_400));
                //TODO: pasar solo id
                adViewModel.setColor(btn.getTag().toString());
            } else {
                tvColor.setText(getResources().getString(R.string.select_variant));
                adViewModel.setColor(null);
            }
            return kotlin.Unit.INSTANCE;
        });


        ThemedButton btnS1 = (ThemedButton) inflater.inflate(R.layout.component_size_toggle, null);
        ThemedButton btnS2 = (ThemedButton) inflater.inflate(R.layout.component_size_toggle, null);
        ThemedButton btnS3 = (ThemedButton) inflater.inflate(R.layout.component_size_toggle, null);
        ThemedButton btnS4 = (ThemedButton) inflater.inflate(R.layout.component_size_toggle, null);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, pxWidth);
        params.setMargins(pxMargin, pxMargin, pxMargin, pxMargin);

        btnS1.setTag("S");
        btnS1.setText("S");
        tbgSize.addView(btnS1, params);

        btnS2.setTag("M");
        btnS2.setText("M");
        tbgSize.addView(btnS2, params);

        btnS3.setTag("L");
        btnS3.setText("L");
        tbgSize.addView(btnS3, params);

        btnS4.setTag("XL");
        btnS4.setText("XL");
        tbgSize.addView(btnS4, params);

        tvSize = root.findViewById(R.id.tvSize);
        tbgSize.setOnSelectListener((ThemedButton btn) -> {
            if (((ThemedToggleButtonGroup) btn.getParent()).getSelectedButtons().size() > 0) {
                tvSize.setText(btn.getTag().toString());
                tvSize.setTextColor(getResources().getColor(R.color.cian_400));
                //TODO: pasar solo id
                adViewModel.setSize(btn.getTag().toString());
            } else {
                tvSize.setText(getResources().getString(R.string.select_variant));
                adViewModel.setSize(null);
            }
            return kotlin.Unit.INSTANCE;
        });

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_article_detail, menu);
        fav = false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                Navigation.findNavController(root).navigate(R.id.action_nav_article_detail_to_nav_cart);
                return true;
            case R.id.action_search:
                Navigation.findNavController(root).navigate(R.id.action_nav_article_detail_to_nav_search);
                return true;
            case R.id.action_fav:
                if (fav)
                    item.setIcon(R.drawable.ic_heart_regular);
                else {
                    Toast.makeText(getContext(), "Se agregó a favoritos", Toast.LENGTH_LONG).show();
                    item.setIcon(R.drawable.ic_heart_solid);
                }
                fav = !fav;
                return true;
            default:
                return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(root));
        }
    }

    private void onAddToCart() {
        if (SingletonUser.getInstance(getContext()).isLogued()) {
            if (validateVariants())
                Navigation.findNavController(root).navigate(R.id.action_nav_article_detail_to_nav_article_added);
            else
                Snackbar.make(tvColor, getResources().getString(R.string.error_select_variant), BaseTransientBottomBar.LENGTH_LONG).show();
        }else {
            ((MainActivity)getActivity()).mostrarMensaje();
        }
    }

    private boolean validateVariants() {
        boolean result = true;
        if (adViewModel.getColor().getValue() == null) {
            tvColor.setTextColor(getResources().getColor(R.color.error));
            result = false;
        }

        if (adViewModel.getSize().getValue() == null) {
            tvSize.setTextColor(getResources().getColor(R.color.error));
            result = false;
        }

        return result;
    }

    @Override
    public void onGroupClick(int position) {

    }
}