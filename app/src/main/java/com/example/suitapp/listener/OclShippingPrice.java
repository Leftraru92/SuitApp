package com.example.suitapp.listener;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.suitapp.R;
import com.example.suitapp.viewmodel.AddStoreViewModel;
import com.example.suitapp.model.DialogSelectItemList;
import com.example.suitapp.model.ProvinceList;
import com.example.suitapp.util.Constants;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;

public class OclShippingPrice implements View.OnClickListener {

    private Context context;
    private AddStoreViewModel viewModel;
    private DialogSelectItemList listItems;
    private int requestId;

    private View viewInflater, message;
    private LayoutInflater inflater;
    AlertDialog dialog;
    TextInputEditText tietProvince, tietPrice;


    public OclShippingPrice(Context context, AddStoreViewModel viewModel, DialogSelectItemList listItems, int requestId) {
        this.context = context;
        this.viewModel = viewModel;
        this.listItems = listItems;
        this.requestId = requestId;

        inflater = ((Activity) context).getLayoutInflater();
        viewInflater = inflater.inflate(R.layout.dialog_shipping_price, null);
    }

    @Override
    public void onClick(View v) {

        tietProvince = viewInflater.findViewById(R.id.tietProvince);
        tietPrice = viewInflater.findViewById(R.id.tietPrice);
        message = viewInflater.findViewById(R.id.lbDesc);

        OclSelectDialog oclSelectDialog = new OclSelectDialog(context, viewModel, listItems, requestId);
        tietProvince.setOnClickListener(vi -> oclSelectDialog.onClick(vi));
        viewModel.getEditShippingPrice().observe((LifecycleOwner) context, s -> {
            if (s != null) {
                tietProvince.setText(s.getProvince().getName());
                if (s.getPrice() != -1)
                    tietPrice.setText(String.valueOf(s.getPrice()));
            }else {
                tietProvince.setText("");
                tietPrice.setText("");
            }
        });
        openDialog();
    }

    private void openDialog() {
        if (dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Costo de envío")
                    .setView(viewInflater)
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    });
            dialog = builder.create();
        }

        dialog.setOnShowListener(dialog2 -> {

            Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            b.setOnClickListener(view -> {
                saveShippingPrice();
            });
        });

        dialog.show();
    }

    private void saveShippingPrice() {
        if (validate()) {
            String price = tietPrice.getText().toString();
            viewModel.addShippingPrice(Integer.valueOf(price));
            tietPrice.setText("");
            message.setVisibility(View.GONE);

            dialog.dismiss();
        } else {
            message.setVisibility(View.VISIBLE);
        }
    }

    private boolean validate() {
        if (tietProvince.length() == 0 || tietPrice.length() == 0)
            return false;
        else
            return true;

    }

}
