package com.example.suitapp.listener;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.suitapp.R;
import com.example.suitapp.util.Constants;
import com.example.suitapp.viewmodel.ArticleDetailViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

public class OclQtySelector implements View.OnClickListener, OnQtyListener {
    ArticleDetailViewModel adViewModel;
    private Context context;
    View viewInflater;
    AlertDialog dialog;
    AlertDialog dialogSelect;
    ConstraintLayout clQty1, clQty2, clQty3, clQty4, clQty5, clQty6;
    int maxqty;
    int selected;

    public OclQtySelector(Context context, int qty, ArticleDetailViewModel adViewModel) {
        this.context = context;
        this.maxqty = qty;
        this.adViewModel = adViewModel;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        viewInflater = inflater.inflate(R.layout.dialog_qty_select, null);

        //Layout
        clQty1 = viewInflater.findViewById(R.id.clQty1);
        clQty2 = viewInflater.findViewById(R.id.clQty2);
        clQty3 = viewInflater.findViewById(R.id.clQty3);
        clQty4 = viewInflater.findViewById(R.id.clQty4);
        clQty5 = viewInflater.findViewById(R.id.clQty5);
        clQty6 = viewInflater.findViewById(R.id.clQty6);

        clQty1.setOnClickListener(v -> onClickQty(1));
        clQty2.setOnClickListener(v -> onClickQty(2));
        clQty3.setOnClickListener(v -> onClickQty(3));
        clQty4.setOnClickListener(v -> onClickQty(4));
        clQty5.setOnClickListener(v -> onClickQty(5));
        clQty6.setOnClickListener(v -> onClickQty(6));

        if(qty<2)
            clQty2.setVisibility(View.GONE);
        if(qty<3)
            clQty3.setVisibility(View.GONE);
        if(qty<4)
            clQty4.setVisibility(View.GONE);
        if(qty<5)
            clQty5.setVisibility(View.GONE);
        if(qty<6)
            clQty6.setVisibility(View.GONE);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(viewInflater)
                .setTitle("Elegí cantidad")
                .setNegativeButton(android.R.string.no, null);

        dialog = builder.create();
    }

    @Override
    public void onClick(View view) {
        //setColors(adViewModel.getQty().getValue());
        dialog.show();
    }

    private void setColors(int qty) {
        if(qty>5) qty = 6;
        ConstraintLayout clQty = viewInflater.findViewById(context.getResources().getIdentifier("clQty" + qty, "id", context.getPackageName()));
        TextView lbQty = clQty.findViewById(context.getResources().getIdentifier("lbQty" + qty, "id", context.getPackageName()));
        clQty.setBackgroundColor(context.getResources().getColor(R.color.cian_400));
        lbQty.setTextColor(context.getResources().getColor(R.color.white));
        lbQty.setTypeface(null, Typeface.BOLD);

        //Previa
        if(selected>0) {
            clQty = viewInflater.findViewById(context.getResources().getIdentifier("clQty" + selected, "id", context.getPackageName()));
            lbQty = clQty.findViewById(context.getResources().getIdentifier("lbQty" + selected, "id", context.getPackageName()));
            clQty.setBackgroundColor(context.getResources().getColor(R.color.white));
            lbQty.setTextColor(context.getResources().getColor(R.color.gray_700));
            lbQty.setTypeface(null, Typeface.NORMAL);
        }
        selected = qty;
    }

    @Override
    public void onClickQty(int qty) {
        Log.d(Constants.LOG, "Se seleccionó " + qty);
        if(qty > 5)
            onSelectMoreThanFive();
        else {
            setColors(qty);
            dialog.cancel();
            adViewModel.setQty(qty);
        }
    }

    private void onSelectMoreThanFive() {
        AlertDialog.Builder builderSelect = new AlertDialog.Builder(context);
        View viewInputQty = ((LayoutInflater) ((Activity) context).getLayoutInflater()).inflate(R.layout.dialog_insert_qty, null);
        TextView lbStock = viewInputQty.findViewById(R.id.lbStock);
        lbStock.setText("Cantidad disponible " + maxqty);

        builderSelect.setView(viewInputQty)
                .setTitle("Ingresá cantidad")
                .setPositiveButton(android.R.string.yes, null)
                .setNegativeButton(android.R.string.no, null);

        dialogSelect = builderSelect.create();
        dialogSelect.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                TextInputEditText tietQty = viewInputQty.findViewById(R.id.tietQty);
                tietQty.requestFocus();
                //TODO: show keyboard

                Button b = dialogSelect.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(view -> {
                    validateInsertQty(viewInputQty);
                });
            }
        });
        dialogSelect.show();
    }

    private void validateInsertQty(View viewInputQty) {
        TextInputLayout tilQty = viewInputQty.findViewById(R.id.tilQty);
        TextInputEditText tietQty = viewInputQty.findViewById(R.id.tietQty);
        if(tietQty.length()==0)
            tilQty.setError(context.getResources().getString(R.string.mandatory_field));
        else if(Integer.valueOf(tietQty.getText().toString())> maxqty)
            tilQty.setError(context.getResources().getString(R.string.max_stock));
        else{
            setColors(Integer.valueOf(tietQty.getText().toString()));
            dialog.dismiss();
            dialogSelect.cancel();
            adViewModel.setQty(Integer.valueOf(tietQty.getText().toString()));
        }
    }

}

interface OnQtyListener{
    void onClickQty(int qty);
}
