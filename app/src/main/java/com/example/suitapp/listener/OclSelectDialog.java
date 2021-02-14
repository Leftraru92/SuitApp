package com.example.suitapp.listener;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.suitapp.R;
import com.example.suitapp.model.DialogSelectItem;
import com.example.suitapp.viewmodel.DialogSelectViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Map;

import androidx.appcompat.app.AlertDialog;

public class OclSelectDialog {

    private Context context;
    private DialogSelectViewModel viewModel;
    private DialogSelectItem listItems;

    private View viewInflater;
    private LayoutInflater inflater;
    AlertDialog dialog;

    public OclSelectDialog(Context context, DialogSelectViewModel viewModel, DialogSelectItem listItems) {
        this.context = context;
        this.viewModel = viewModel;
        this.listItems = listItems;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewInflater = inflater.inflate(R.layout.dialog_select_province, null);
    }

    public void selectItem() {

        TextView tvProvince = viewInflater.findViewById(R.id.tvProvince);
        final RadioGroup rgProvinces = viewInflater.findViewById(R.id.rgProvinces);

        tvProvince.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                generateRadioButtons(rgProvinces, charSequence, inflater);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        generateRadioButtons(rgProvinces, null, inflater);
        openDialog(rgProvinces);
    }

    private void openDialog(RadioGroup rgProvinces) {
        if(dialog==null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Seleccione Provincia")
                    .setView(viewInflater)
                    .setNegativeButton(android.R.string.no, null);

            dialog = builder.create();
        }

        dialog.show();
    }

    private void generateRadioButtons(RadioGroup rgProvinces, CharSequence charSequence, LayoutInflater inflater) {

        rgProvinces.removeAllViews();
        rgProvinces.clearCheck();

        int itemSelected = viewModel.getSelectValue();

        for (Map.Entry<Integer, String> entry : listItems.getList().entrySet()) {

            if (charSequence == null || entry.getValue().toUpperCase().contains(charSequence.toString().toUpperCase())) {

                RadioButton rdbtn = (RadioButton) inflater.inflate(R.layout.component_radio_button, null);

                rdbtn.setId(entry.getKey());
                rdbtn.setText(entry.getValue());

                if (itemSelected == entry.getKey())
                    rdbtn.setChecked(true);

                rgProvinces.addView(rdbtn);
            }

            rgProvinces.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    saveItemSelected(rgProvinces);
                }
            });
        }
    }

    private void saveItemSelected(RadioGroup rgProvinces) {
        int selectedItemId = rgProvinces.getCheckedRadioButtonId();

        if (selectedItemId > 0) {
            viewModel.selectValue(selectedItemId, listItems.getList().get(selectedItemId));
            dialog.dismiss();
        }
    }
}
