package com.example.suitapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.suitapp.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vinaygaba.creditcardview.CreditCardView;

public class CreditCardFragment extends Fragment {
    View root;
    CreditCardView creditCardView;
    TextInputLayout tilCcv;
    TextInputEditText tietCcv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_credit_card, container, false);
        init();
        return root;
    }

    private void init() {
        //bind
        Button btContinue = root.findViewById(R.id.btContinue);
        creditCardView = root.findViewById(R.id.creditCard);
        tietCcv = root.findViewById(R.id.tietCcv);

        //listener
        btContinue.setOnClickListener(v -> next());
    }

    private void next() {
        if (validate())
            Navigation.findNavController(root).navigate(R.id.action_nav_card_credit_to_nav_shop_confirm);
        else
            Snackbar.make(root, "Datos incompletos o inválidos", BaseTransientBottomBar.LENGTH_LONG).show();
    }

    private boolean validate() {
        if (creditCardView.getCardName() == null)
            return false;
        if (creditCardView.getCardNumber() == null)
            return false;
        if (creditCardView.getExpiryDate().equals(""))
            return false;
        if (tietCcv.length() != 3)
            return false;

        return true;
    }

}