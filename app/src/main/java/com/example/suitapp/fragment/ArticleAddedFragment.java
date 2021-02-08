package com.example.suitapp.fragment;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.suitapp.R;

public class ArticleAddedFragment extends Fragment {


    public ArticleAddedFragment() {
        // Required empty public constructor
    }

    public static ArticleAddedFragment newInstance(String param1, String param2) {
        ArticleAddedFragment fragment = new ArticleAddedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_article_added, container, false);

        return root;
    }
}