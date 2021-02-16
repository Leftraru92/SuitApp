package com.example.suitapp.listener;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.suitapp.R;
import com.example.suitapp.model.DialogSelectItemList;
import com.example.suitapp.util.Constants;
import com.example.suitapp.viewmodel.DialogSelectItemViewModel;

import java.util.Map;

import androidx.appcompat.app.AlertDialog;

public class OclSelectDialog implements View.OnClickListener {

    private Context context;
    private DialogSelectItemViewModel viewModel;
    private DialogSelectItemList listItems;
    private int requestId;

    private View viewInflater;
    private LayoutInflater inflater;
    AlertDialog dialog;

    public OclSelectDialog(Context context, DialogSelectItemViewModel viewModel, DialogSelectItemList listItems, int requestId) {
        this.context = context;
        this.viewModel = viewModel;
        this.listItems = listItems;
        this.requestId = requestId;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewInflater = inflater.inflate(R.layout.dialog_select_province, null);
    }

    @Override
    public void onClick(View v) {

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
        openDialog();
    }

    private void openDialog() {
        if(dialog==null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Seleccione una opción")
                    .setView(viewInflater)
                    .setNegativeButton(android.R.string.no, null);

            dialog = builder.create();
        }

        dialog.show();
    }

    private void generateRadioButtons(RadioGroup rgProvinces, CharSequence charSequence, LayoutInflater inflater) {

        rgProvinces.removeAllViews();
        rgProvinces.clearCheck();

        int itemSelected = viewModel.getSelectValue(requestId);

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
            viewModel.selectValue(selectedItemId, listItems.getList().get(selectedItemId), requestId);
            dialog.dismiss();
        }
    }

}
