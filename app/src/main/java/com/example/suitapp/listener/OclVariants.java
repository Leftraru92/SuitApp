package com.example.suitapp.listener;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.suitapp.R;
import com.example.suitapp.model.DialogSelectItemList;
import com.example.suitapp.model.ProvinceList;
import com.example.suitapp.util.Constants;
import com.example.suitapp.viewmodel.AddArticleViewModel;
import com.example.suitapp.viewmodel.AddStoreViewModel;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;

public class OclVariants implements View.OnClickListener {

    private Context context;
    private AddArticleViewModel viewModel;
    private DialogSelectItemList listItems;

    private View viewInflater, message;
    private LayoutInflater inflater;
    AlertDialog dialog;
    TextInputEditText tietColor, tietSize, tietQty;


    public OclVariants(Context context, AddArticleViewModel viewModel, DialogSelectItemList listItems) {
        this.context = context;
        this.viewModel = viewModel;
        this.listItems = listItems;

        inflater = ((Activity) context).getLayoutInflater();
        viewInflater = inflater.inflate(R.layout.dialog_variants, null);
    }

    @Override
    public void onClick(View v) {

        tietColor = viewInflater.findViewById(R.id.tietColor);
        tietSize = viewInflater.findViewById(R.id.tietSize);
        tietQty = viewInflater.findViewById(R.id.tietQty);
        message = viewInflater.findViewById(R.id.lbDesc);

        OclSelectDialog oclSelectDialog = new OclSelectDialog(context, viewModel, listItems, 0);
        tietColor.setOnClickListener(vi -> oclSelectDialog.onClick(vi));
        viewModel.getEditVariant().observe((LifecycleOwner) context, s -> {
            if (s != null) {
                tietColor.setText(s.getColor().getName());
                if (s.getSize() != null)
                    tietSize.setText(String.valueOf(s.getSize()));
                tietQty.setText(String.valueOf(s.getStock()));
            } else {
                tietColor.setText("");
                tietSize.setText("");
                tietQty.setText("");
            }
        });
        openDialog();
    }

    private void openDialog() {
        if (dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Variante de artículo")
                    .setView(viewInflater)
                    .setNegativeButton(android.R.string.no, (dialog, which) -> {})
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {});
            dialog = builder.create();
        }

        dialog.setOnShowListener(dialog2 -> {

            Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            b.setOnClickListener(view -> saveVariant());

            Button bc = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            bc.setOnClickListener(view -> cancelDialog());
        });

        dialog.show();
    }

    private void cancelDialog() {
        tietSize.setText("");
        tietQty.setText("");
        viewModel.setEditVariant(-1);

        dialog.dismiss();
    }

    private void saveVariant() {
        if (validate()) {
            String size = tietSize.getText().toString();
            int qty = Integer.valueOf(tietQty.getText().toString());
            viewModel.addVariant(size, qty);
            tietSize.setText("");
            tietQty.setText("");
            message.setVisibility(View.GONE);

            dialog.dismiss();
        } else {
            message.setVisibility(View.VISIBLE);
        }
    }

    private boolean validate() {
        if (tietColor.length() == 0 || tietSize.length() == 0 || tietQty.length() == 0)
            return false;
        else
            return true;

    }

}
