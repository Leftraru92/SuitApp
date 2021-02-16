package com.example.suitapp.fragment.addStore;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.suitapp.R;

public class CongratsFragment extends Fragment {
    View root;
    ProgressBar progress_circular;
    ConstraintLayout clPublicado;

    public static CongratsFragment newInstance() {
        return new CongratsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_congrats, container, false);
        init();
        return root;
    }

    private void init() {
        Button btAddProduct = root.findViewById(R.id.btAddProduct);
        Button btContinue = root.findViewById(R.id.btContinue);
        progress_circular = root.findViewById(R.id.progress_circular);
        clPublicado = root.findViewById(R.id.clPublicado);

        btAddProduct.setOnClickListener(v -> Toast.makeText(getContext(), "Todavía no programado", Toast.LENGTH_LONG).show());
        btContinue.setOnClickListener(v -> getActivity().finish());

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setResult(true);
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 2000);
    }

    private void setResult(boolean result){
        progress_circular.setVisibility((result) ? View.GONE : View.VISIBLE);
        clPublicado.setVisibility((result) ? View.VISIBLE : View.GONE);
    }
}