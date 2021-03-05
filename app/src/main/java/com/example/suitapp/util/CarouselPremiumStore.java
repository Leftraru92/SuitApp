package com.example.suitapp.util;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.suitapp.R;
import com.example.suitapp.adapter.PremiumStoreAdapter;
import com.example.suitapp.dummy.DummyPremiumStores;
import com.example.suitapp.model.Store;
import com.example.suitapp.viewmodel.SearchViewModel;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

public class CarouselPremiumStore implements PremiumStoreAdapter.OnPremiumStoreListener {
    Context context;
    View root;
    ViewPager2 viewPager;
    LinearLayout llIndicator;
    private static int currentPage = 0;
    int size = 0;
    SearchViewModel searchViewModel;

    public CarouselPremiumStore(View root, SearchViewModel searchViewModel, List<Store> storeList, boolean setCallback) {
        this.root = root;
        this.searchViewModel = searchViewModel;
        this.context = root.getContext();
        viewPager = root.findViewById(R.id.viewPager);
        viewPager.setAdapter(new PremiumStoreAdapter(storeList, this));
        llIndicator = (LinearLayout) root.findViewById(R.id.llIndicator);
        llIndicator.removeAllViews();
        size = storeList.size();

        setupIndicator();
        setupCurrentIndicator(0);
        viewPager.registerOnPageChangeCallback(new PageChangeCallback());

        if(setCallback) {
            final Handler handler = new Handler();
            final Runnable Update = () -> {
                if (currentPage == size) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            };
            Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 0, 3000);
        }
    }

    private void setupIndicator() {
        ImageView[] indicator = new ImageView[size];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(12, 0, 12, 0);
        for (int i = 0; i < indicator.length; i++) {
            indicator[i] = new ImageView(context);
            indicator[i].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.indicator_inactive));
            indicator[i].setLayoutParams(layoutParams);
            llIndicator.addView(indicator[i]);
        }
    }

    private void setupCurrentIndicator(int index) {
        int itemcildcount = llIndicator.getChildCount();
        for (int i = 0; i < itemcildcount; i++) {
            ImageView imageView = (ImageView) llIndicator.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.indicator_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.indicator_inactive));
            }
        }
    }

    @Override
    public void onPremiumStoreClick(int storeId) {
        Bundle bundle = new Bundle();
        bundle.putInt("storeId", storeId);
        Navigation.findNavController(root).navigate(R.id.action_move_to_articles, bundle);
    }

    private class PageChangeCallback extends ViewPager2.OnPageChangeCallback {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            setupCurrentIndicator(position);
        }

    }
}
