package com.example.suitapp.listener;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.suitapp.R;
import com.example.suitapp.database.AccessDataDb;
import com.example.suitapp.model.Category;
import com.example.suitapp.model.Gender;
import com.example.suitapp.model.Item;
import com.example.suitapp.model.Variant;
import com.example.suitapp.util.Constants;
import com.example.suitapp.util.ExpandLayout;
import com.example.suitapp.viewmodel.SearchViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

public class OclArticlesFilter implements View.OnClickListener {

    SearchViewModel searchViewModel;
    private Context context;
    View viewInflater;
    AlertDialog dialog;
    TextInputEditText tietPriceMax, tietPriceMin;
    Button btnLimpiar, btDesplegar, btDesplegarCat, btDesplegarGenre, btDesplegarColor, btDesplegarSize, btDesplegarPrice;
    ConstraintLayout layoutExpandOrdenar, layoutExpandCat, layoutExpandGenre, layoutExpandColor, layoutExpandSize, layoutExpandPrice;
    ConstraintLayout clOrdenarPor, clFiltroCat, clFiltrarGenre, clFiltrarColor, clFiltroSize, clFiltroPrice;
    TextView lbOrdenarPorSelec, lbCatSelect, lbGenreSelect, lbColorSelec, lbSizeSelec,
            lbPriceSelect;
    RadioGroup rgOrdenarPor, rgCategory, rgGenre, rgColor, rgSize;
    RadioButton orden1, orden2, orden3;
    ExpandLayout elOrdenar, elCategory, elGenre, elColor, elSize, elPrice;

    List<Item> categoryList, genderList, colorList, sizeList;

    public OclArticlesFilter(Context context) {
        this.context = context;
        getData();
        init();
    }

    private void init() {
        //set
        searchViewModel =
                new ViewModelProvider((ViewModelStoreOwner) context).get(SearchViewModel.class);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        viewInflater = inflater.inflate(R.layout.dialog_article_filter, null);

        //Bind
        //Textos
        lbOrdenarPorSelec = viewInflater.findViewById(R.id.lbOrdenarPorSelec);
        lbCatSelect = viewInflater.findViewById(R.id.lbCatSelect);
        lbGenreSelect = viewInflater.findViewById(R.id.lbGenreSelect);
        lbColorSelec = viewInflater.findViewById(R.id.lbColorSelec);
        lbSizeSelec = viewInflater.findViewById(R.id.lbSizeSelec);
        lbPriceSelect = viewInflater.findViewById(R.id.lbPriceSelect);
        tietPriceMin = viewInflater.findViewById(R.id.tietPriceMin);
        tietPriceMax = viewInflater.findViewById(R.id.tietPriceMax);

        //Radiogroup
        rgOrdenarPor = viewInflater.findViewById(R.id.rgOrdenarPor);
        rgCategory = viewInflater.findViewById(R.id.rgCategory);
        rgGenre = viewInflater.findViewById(R.id.rgGenre);
        rgColor = viewInflater.findViewById(R.id.rgColor);
        rgSize = viewInflater.findViewById(R.id.rgSize);
        orden1 = viewInflater.findViewById(R.id.orden1);
        orden2 = viewInflater.findViewById(R.id.orden2);
        orden3 = viewInflater.findViewById(R.id.orden3);

        //Botones
        btnLimpiar = viewInflater.findViewById(R.id.btnLimpiar);
        btDesplegar = viewInflater.findViewById(R.id.btDesplegar);
        btDesplegarCat = viewInflater.findViewById(R.id.btDesplegarCat);
        btDesplegarGenre = viewInflater.findViewById(R.id.btDesplegarGenre);
        btDesplegarColor = viewInflater.findViewById(R.id.btDesplegarColor);
        btDesplegarSize = viewInflater.findViewById(R.id.btDesplegarSize);
        btDesplegarPrice = viewInflater.findViewById(R.id.btDesplegarPrice);

        //Layout
        layoutExpandOrdenar = viewInflater.findViewById(R.id.layoutExpandOrdenar);
        layoutExpandCat = viewInflater.findViewById(R.id.layoutExpandCat);
        layoutExpandGenre = viewInflater.findViewById(R.id.layoutExpandGenre);
        layoutExpandColor = viewInflater.findViewById(R.id.layoutExpandColor);
        layoutExpandSize = viewInflater.findViewById(R.id.layoutExpandSize);
        layoutExpandPrice = viewInflater.findViewById(R.id.layoutExpandPrice);

        clOrdenarPor = viewInflater.findViewById(R.id.clOrdenarPor);
        clFiltroCat = viewInflater.findViewById(R.id.clFiltroCat);
        clFiltrarGenre = viewInflater.findViewById(R.id.clFiltrarGenre);
        clFiltrarColor = viewInflater.findViewById(R.id.clFiltrarColor);
        clFiltroSize = viewInflater.findViewById(R.id.clFiltroSize);
        clFiltroPrice = viewInflater.findViewById(R.id.clFiltroPrice);

        elOrdenar = new ExpandLayout(btDesplegar, layoutExpandOrdenar, clOrdenarPor);
        elCategory = new ExpandLayout(btDesplegarCat, layoutExpandCat, clFiltroCat);
        elGenre = new ExpandLayout(btDesplegarGenre, layoutExpandGenre, clFiltrarGenre);
        elColor = new ExpandLayout(btDesplegarColor, layoutExpandColor, clFiltrarColor);
        elSize = new ExpandLayout(btDesplegarSize, layoutExpandSize, clFiltroSize);
        elPrice = new ExpandLayout(btDesplegarPrice, layoutExpandPrice, clFiltroPrice);

        generateRadioGroups();

        //Listener
        btnLimpiar.setOnClickListener(view1 -> limpiarFiltros());

        rgOrdenarPor.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checkedRadioButton = group.findViewById(checkedId);
            elOrdenar.closeCard();
            boolean isChecked = checkedRadioButton.isChecked();
            if (isChecked) {
                setTextSelected(lbOrdenarPorSelec, checkedRadioButton.getText(), checkedRadioButton.getTag().toString(), rgOrdenarPor);
                searchViewModel.setOrder(Integer.valueOf(checkedRadioButton.getTag().toString()));
            }
        });

        rgCategory.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checkedRadioButton = group.findViewById(checkedId);
            elCategory.closeCard();
            boolean isChecked = checkedRadioButton.isChecked();
            if (isChecked) {
                setTextSelected(lbCatSelect, checkedRadioButton.getText(), checkedRadioButton.getText().toString(), rgCategory);
                if (checkedRadioButton.getText().equals(Constants.TODOS))
                    searchViewModel.setCategoryTemp(null);
                else
                    searchViewModel.setCategoryTemp(new Category(checkedRadioButton.getId(), checkedRadioButton.getText().toString(), 0));
            }
        });

        rgGenre.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checkedRadioButton = group.findViewById(checkedId);
            elGenre.closeCard();
            boolean isChecked = checkedRadioButton.isChecked();
            if (isChecked) {
                setTextSelected(lbGenreSelect, checkedRadioButton.getText(), checkedRadioButton.getText().toString(), rgGenre);
                if (checkedRadioButton.getText().equals(Constants.TODOS))
                    searchViewModel.setGenreTemp(null);
                else
                    searchViewModel.setGenreTemp(new Gender(checkedRadioButton.getId(), checkedRadioButton.getText().toString(), 0));
            }
        });

        rgColor.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checkedRadioButton = group.findViewById(checkedId);
            elColor.closeCard();
            boolean isChecked = checkedRadioButton.isChecked();
            if (isChecked) {
                setTextSelected(lbColorSelec, checkedRadioButton.getText(), checkedRadioButton.getText().toString(), rgColor);
                if (checkedRadioButton.getText().equals(Constants.TODOS))
                    searchViewModel.setColorTemp(null);
                else
                    searchViewModel.setColorTemp(new Variant.Color(checkedRadioButton.getId(), checkedRadioButton.getText().toString(), ""));
            }
        });

        rgSize.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checkedRadioButton = group.findViewById(checkedId);
            elSize.closeCard();
            boolean isChecked = checkedRadioButton.isChecked();
            if (isChecked) {
                setTextSelected(lbSizeSelec, checkedRadioButton.getText(), checkedRadioButton.getText().toString(), rgSize);
                if (checkedRadioButton.getText().equals(Constants.TODOS))
                    searchViewModel.setSizeTemp(null);
                else
                    searchViewModel.setSizeTemp(new Variant.Size(checkedRadioButton.getId(), checkedRadioButton.getText().toString()));
            }
        });

        //viewmodel
        searchViewModel.getCategoryTemp().observe((LifecycleOwner) context, s -> {
            if (s == null)
                setTextSelected(lbCatSelect, Constants.TODOS, Constants.TODOS, rgCategory);
            else
                setTextSelected(lbCatSelect, s.getName(), s.getName(), rgCategory);
        });

        searchViewModel.getGenreTemp().observe((LifecycleOwner) context, s -> {
            if (s == null)
                setTextSelected(lbGenreSelect, Constants.TODOS, Constants.TODOS, rgGenre);
            else
                setTextSelected(lbGenreSelect, s.getName(), s.getName(), rgGenre);
        });

        searchViewModel.getColorTemp().observe((LifecycleOwner) context, s -> {
            if (s == null)
                setTextSelected(lbColorSelec, Constants.TODOS, Constants.TODOS, rgColor);
            else
                setTextSelected(lbColorSelec, s.getName(), s.getName(), rgColor);
        });

        searchViewModel.getSizeTemp().observe((LifecycleOwner) context, s -> {
            if (s == null)
                setTextSelected(lbSizeSelec, Constants.TODOS, Constants.TODOS, rgSize);
            else
                setTextSelected(lbSizeSelec, s.getName(), s.getName(), rgSize);
        });

    }

    private void getData() {
        AccessDataDb accessDataDba = AccessDataDb.getInstance(context);

        genderList = accessDataDba.getGendersItems();
        categoryList = accessDataDba.getCategories();
        colorList = accessDataDba.getColorsItems();
        sizeList = accessDataDba.getSizes();
    }

    @Override
    public void onClick(View view) {

        setFilterSelected();
        searchViewModel.setFilterTemp();

        // Genero el dialogo

        if (dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setView(viewInflater)
                    .setTitle("Filtrar")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> processFilter())
                    .setNegativeButton(android.R.string.no, (dialog, which) -> searchViewModel.cleanTemp());
            dialog = builder.create();
        }
        dialog.show();
    }

    private void processFilter() {
        searchViewModel.setMaxPriceTemp(tietPriceMax.getText().toString());
        searchViewModel.setMinPriceTemp(tietPriceMin.getText().toString());
        searchViewModel.setFilter();
    }

    private void setFilterSelected() {
        lbOrdenarPorSelec.setText(searchViewModel.getOrderString());
        switch (searchViewModel.getOrder().getValue()){
            case 1:
                orden1.setChecked(true);
                break;
            case 2:
                orden3.setChecked(true);
                break;
            case 3:
                orden2.setChecked(true);
                break;
        }
    }

    private void limpiarFiltros() {
        setTextSelected(lbCatSelect, Constants.TODOS, Constants.TODOS, rgCategory);
        setTextSelected(lbGenreSelect, Constants.TODOS, Constants.TODOS, rgGenre);
        setTextSelected(lbColorSelec, Constants.TODOS, Constants.TODOS, rgColor);
        setTextSelected(lbSizeSelec, Constants.TODOS, Constants.TODOS, rgSize);
        tietPriceMin.setText("");
        tietPriceMax.setText("");
        lbPriceSelect.setText("");
        searchViewModel.clean();

        elCategory.closeCard();
        elGenre.closeCard();
        elColor.closeCard();
        elSize.closeCard();
        elPrice.closeCard();
    }

    private void setTextSelected(TextView tv, CharSequence valor, String tag, RadioGroup radioGroup) {
        if (valor.equals(Constants.TODOS)) {
            tv.setVisibility(View.GONE);
            tv.setText("");
            ((RadioButton)radioGroup.findViewWithTag(Constants.TODOS)).setChecked(true);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(valor);
            ((RadioButton)radioGroup.findViewWithTag(tag)).setChecked(true);
        }
    }

    private void generateRadioGroups() {
        generateOptions(categoryList, rgCategory);
        generateOptions(genderList, rgGenre);
        generateOptions(colorList, rgColor);
        generateOptions(sizeList, rgSize);
    }

    private void generateOptions(List<Item> lista, RadioGroup radioGroup) {
        int index = 0;
        LayoutInflater inflater = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));

        //Agrego la opcion todos

        radioGroup.removeAllViews();
        RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, (int) context.getResources().getDimension(R.dimen.button_height));
        lp.setMarginStart((int) context.getResources().getDimension(R.dimen.alto_filtro));

        for (Item item : lista) {
            RadioButton rdbtn = (RadioButton) inflater.inflate(R.layout.component_radio_button, null);
            index++;
            rdbtn.setId(index);
            rdbtn.setText(item.getName());
            rdbtn.setTag(item.getName());
            rdbtn.setLayoutParams(lp);
            radioGroup.addView(rdbtn);
        }

        //Agrego opción Todos
        RadioButton rdbtn = (RadioButton) inflater.inflate(R.layout.component_radio_button, null);
        rdbtn.setId(0);
        rdbtn.setText(Constants.TODOS);
        rdbtn.setTag(Constants.TODOS);
        rdbtn.setLayoutParams(lp);
        rdbtn.setChecked(true);
        radioGroup.addView(rdbtn, 0);
    }

}
