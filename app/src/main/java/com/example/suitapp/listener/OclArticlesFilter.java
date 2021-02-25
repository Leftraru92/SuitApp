package com.example.suitapp.listener;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.suitapp.R;
import com.example.suitapp.adapter.CategoriesAdapter;
import com.example.suitapp.adapter.GenderAdapter;
import com.example.suitapp.database.AccessDataDb;
import com.example.suitapp.dummy.DummyColors;
import com.example.suitapp.model.Gender;
import com.example.suitapp.model.Item;
import com.example.suitapp.util.Constants;
import com.example.suitapp.util.ExpandLayout;
import com.example.suitapp.viewmodel.SearchViewModel;

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
    Button btnLimpiar, btDesplegar, btDesplegarCat, btDesplegarGenre, btDesplegarColor, btDesplegarSize, btDesplegarPrice;
    ConstraintLayout layoutExpandOrdenar, layoutExpandCat, layoutExpandGenre, layoutExpandColor, layoutExpandSize, layoutExpandPrice;
    ConstraintLayout clOrdenarPor, clFiltroCat, clFiltrarGenre, clFiltrarColor, clFiltroSize, clFiltroPrice;
    TextView lbOrdenarPorSelec, lbCatSelect, lbGenreSelect, lbColorSelec, lbSizeSelec,
            lbPriceSelect;
    RadioGroup rgOrdenarPor, rgCategory, rgGenre, rgColor, rgSize;
    ExpandLayout elOrdenar, elCategory, elGenre, elColor, elSize, elPrice;

    List<Item> categoryList;
    List<Item> genderList;

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

        //Radiogroup
        rgOrdenarPor = viewInflater.findViewById(R.id.rgOrdenarPor);
        rgCategory = viewInflater.findViewById(R.id.rgCategory);
        rgGenre = viewInflater.findViewById(R.id.rgGenre);
        rgColor = viewInflater.findViewById(R.id.rgColor);
        rgSize = viewInflater.findViewById(R.id.rgSize);

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
            boolean isChecked = checkedRadioButton.isChecked();
            if (isChecked) {
                setTextSelected(lbOrdenarPorSelec, checkedRadioButton.getText());
            }
        });

        rgCategory.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checkedRadioButton = group.findViewById(checkedId);
            elCategory.onClickContraint();
            boolean isChecked = checkedRadioButton.isChecked();
            if (isChecked) {
                setTextSelected(lbCatSelect, checkedRadioButton.getText());
            }
        });

        rgGenre.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checkedRadioButton = group.findViewById(checkedId);
            elGenre.onClickContraint();
            boolean isChecked = checkedRadioButton.isChecked();
            if (isChecked) {
                setTextSelected(lbGenreSelect, checkedRadioButton.getText());
            }
        });

        rgColor.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checkedRadioButton = group.findViewById(checkedId);
            elColor.onClickContraint();
            boolean isChecked = checkedRadioButton.isChecked();
            if (isChecked) {
                setTextSelected(lbColorSelec, checkedRadioButton.getText());
            }
        });

        rgSize.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checkedRadioButton = group.findViewById(checkedId);
            boolean isChecked = checkedRadioButton.isChecked();
            if (isChecked) {
                setTextSelected(lbSizeSelec, checkedRadioButton.getText());
            }
        });

        //viewmodel
        searchViewModel.getCategory().observe((LifecycleOwner) context, s -> {
            if (s != null) {
                setTextSelected(lbCatSelect, s.getName());
            }
        });

        searchViewModel.getGenre().observe((LifecycleOwner) context, s -> {
            if (s != null) {
                setTextSelected(lbGenreSelect, s.getName());
            }
        });

        searchViewModel.getColor().observe((LifecycleOwner) context, s -> {
            if (s != null) {
                setTextSelected(lbColorSelec, s.getName());
            }
        });

    }

    private void getData() {
        AccessDataDb accessDataDba = AccessDataDb.getInstance(context);

        genderList = accessDataDba.getGenders(true);
        categoryList = accessDataDba.getCategories();

    }

    @Override
    public void onClick(View view) {

/*
        if (rgOrdenarPor.findViewWithTag(ordenSelected) != null)
            ((RadioButton) rgOrdenarPor.findViewWithTag(ordenSelected)).setChecked(true);
        if (numosSelected != null) {
            tvFiltroNumOs.setText(numosSelected);
            lbNumOs.setText(numosSelected);
            lbNumOs.setVisibility(View.VISIBLE);
        }
        if (catSelected != null && rgCategoria.findViewWithTag(catSelected) != null) {
            ((RadioButton) rgCategoria.findViewWithTag(catSelected)).setChecked(true);
        }
        if (estadoSelected != null && rgEstado.findViewWithTag(estadoSelected) != null) {
            ((RadioButton) rgEstado.findViewWithTag(estadoSelected)).setChecked(true);
        }
        if (fechaSelected != null) {
            lbFechaSelec.setText(fechaSelected);
            lbFechaSelec.setVisibility(View.VISIBLE);
        }
*/



        // Genero el dialogo

        if (dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setView(viewInflater)
                    .setTitle("Filtrar")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // processFilter();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null);
            dialog = builder.create();
        }
        dialog.show();
    }
/*
    private void processFilter() {
        Map<String, String> map = new HashMap<>();
        if (!lbOrdenarPorSelec.getText().toString().equals(""))
            map.put(Constantes.FILTRO_ORDENAR, lbOrdenarPorSelec.getText().toString());
        if (!tvFiltroNumOs.getText().toString().equals(""))
            map.put(Constantes.FILTRO_NUMOS, tvFiltroNumOs.getText().toString());
        if (!lbCategoriaSelec.getText().toString().equals(""))
            map.put(Constantes.FILTRO_CAT, lbCategoriaSelec.getText().toString());
        if (!lbEstadoSelec.getText().toString().equals(""))
            map.put(Constantes.FILTRO_ESTADO, lbEstadoSelec.getText().toString());
        if (!lbFechaSelec.getText().toString().equals(""))
            map.put(Constantes.FILTRO_FECHA, lbFechaSelec.getText().toString());
        dmof.setMapFiltro(map);

    }
*/

    private void limpiarFiltros() {
        setTextSelected(lbCatSelect, Constants.TODOS);
        setTextSelected(lbGenreSelect, Constants.TODOS);
        setTextSelected(lbColorSelec, Constants.TODOS);

        lbPriceSelect.setText("");
/*
        if (layoutExpandNumOs.getVisibility() == View.VISIBLE)
            elNumOs.onClickContraint();

        if (layoutExpandCategoria.getVisibility() == View.VISIBLE)
            elCategorias.onClickContraint();

        if (layoutExpandEstado.getVisibility() == View.VISIBLE)
            elEstados.onClickContraint();
*/
    }

    /*
        private void onClickExpandNumOs(ExpandLayout el) {

            if (!el.getExpandState()) {
                lbNumOs.setText(tvFiltroNumOs.getText());
                lbNumOs.setVisibility((tvFiltroNumOs.getText().length() == 0) ? View.GONE : View.VISIBLE);
            } else {
                lbNumOs.setVisibility(View.GONE);
            }
        }
    */
    private void setTextSelected(TextView tv, CharSequence valor) {
        if (valor.equals("Todos")) {
            tv.setVisibility(View.GONE);
            tv.setText("");
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(valor);
        }
    }

    private void generateRadioGroups() {
        generateOptions(categoryList, rgCategory);
        generateOptions(genderList, rgGenre);
        generateOptions(new DummyColors().getItems(), rgColor);
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
            rdbtn.setTag(item.getId());
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
