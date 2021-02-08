package com.example.suitapp.listener;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.example.suitapp.R;

import java.util.Calendar;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

public class OclArticlesFilter implements View.OnClickListener {
    private Context context;
   // private DashboardMisOrdenesFragment dmof;
    //ArrayList<MiOrden> misOrdenes;
    View viewInflater;
    AlertDialog dialog;
    Button btnLimpiar, btnFecha, btDesplegar, btDesplegarCat, btDesplegarEstado, btDesplegarOs;
    ConstraintLayout layoutExpandOrdenar, layoutExpandEstado, layoutExpandNumOs, layoutExpandCategoria,
            clOrdenarPor, clFiltroNumOs, clFiltrarCat, clFiltrarEstado;
    TextView lbOrdenarPorSelec, lbNumOs, lbCategoriaSelec, lbEstadoSelec, lbFechaSelec,
            tvFiltroNumOs;
    RadioGroup rgOrdenarPor, rgCategoria, rgEstado;
    //ExpandLayout elOrdenar, elNumOs, elCategorias, elEstados;
    private SparseBooleanArray expandState = new SparseBooleanArray();


    public OclArticlesFilter(Context context) {
        this.context = context;
       // this.dmof = dmof;
    }




    @Override
    public void onClick(View view) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        viewInflater = inflater.inflate(R.layout.dialog_article_filter, null);

        //Textos
        lbOrdenarPorSelec = viewInflater.findViewById(R.id.lbOrdenarPorSelec);
        lbNumOs = viewInflater.findViewById(R.id.lbNumOs);
        lbCategoriaSelec = viewInflater.findViewById(R.id.lbCategoriaSelec);
        lbEstadoSelec = viewInflater.findViewById(R.id.lbEstadoSelec);
        lbFechaSelec = viewInflater.findViewById(R.id.lbFechaSelec);
        tvFiltroNumOs = viewInflater.findViewById(R.id.tvFiltroNumOs);

        //Radiogroup
        rgOrdenarPor = viewInflater.findViewById(R.id.rgOrdenarPor);
        rgCategoria = viewInflater.findViewById(R.id.rgCategorias);
        rgEstado = viewInflater.findViewById(R.id.rgEstado);

        //Botones
        btnLimpiar = viewInflater.findViewById(R.id.btnLimpiar);
        btnFecha = viewInflater.findViewById(R.id.btnFecha);
        btDesplegar = viewInflater.findViewById(R.id.btDesplegar);
        btDesplegarCat = viewInflater.findViewById(R.id.btDesplegarCat);
        btDesplegarOs = viewInflater.findViewById(R.id.btDesplegarOs);
        btDesplegarEstado = viewInflater.findViewById(R.id.btDesplegarEstado);

        //Layout
        layoutExpandOrdenar = viewInflater.findViewById(R.id.layoutExpandOrdenar);
        layoutExpandNumOs = viewInflater.findViewById(R.id.layoutExpandNumOs);
        layoutExpandCategoria = viewInflater.findViewById(R.id.layoutExpandCategoria);
        layoutExpandEstado = viewInflater.findViewById(R.id.layoutExpandEstado);

        clOrdenarPor = viewInflater.findViewById(R.id.clOrdenarPor);
        clFiltroNumOs = viewInflater.findViewById(R.id.clFiltroNumOs);
        clFiltrarCat = viewInflater.findViewById(R.id.clFiltrarCat);
        clFiltrarEstado = viewInflater.findViewById(R.id.clFiltrarEstado);

        rgOrdenarPor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked) {
                    setTextSelected(lbOrdenarPorSelec, checkedRadioButton.getText());
                }
            }
        });

        rgCategoria.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked) {
                    setTextSelected(lbCategoriaSelec, checkedRadioButton.getText());
                }
            }
        });

        rgEstado.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked) {
                    setTextSelected(lbEstadoSelec, checkedRadioButton.getText());
                }
            }
        });
/*
        String ordenSelected = PreferenceManager.getDefaultSharedPreferences(context).getString(Constantes.FILTRO_ORDENAR, context.getString(R.string.orden_estado));
        String numosSelected = PreferenceManager.getDefaultSharedPreferences(context).getString(Constantes.FILTRO_NUMOS, null);
        String catSelected = PreferenceManager.getDefaultSharedPreferences(context).getString(Constantes.FILTRO_CAT, null);
        String estadoSelected = PreferenceManager.getDefaultSharedPreferences(context).getString(Constantes.FILTRO_ESTADO, null);
        String fechaSelected = PreferenceManager.getDefaultSharedPreferences(context).getString(Constantes.FILTRO_FECHA, null);

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
        setTextSelected(lbOrdenarPorSelec, ((RadioButton) rgOrdenarPor.findViewById(rgOrdenarPor.getCheckedRadioButtonId())).getText());
        setTextSelected(lbCategoriaSelec, ((RadioButton) rgCategoria.findViewById(rgCategoria.getCheckedRadioButtonId())).getText());
        setTextSelected(lbEstadoSelec, ((RadioButton) rgEstado.findViewById(rgEstado.getCheckedRadioButtonId())).getText());
/*
        elOrdenar = new ExpandLayout(btDesplegar, layoutExpandOrdenar, clOrdenarPor);
        elNumOs = new ExpandLayout(btDesplegarOs, layoutExpandNumOs, clFiltroNumOs, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                onClickExpandNumOs(elNumOs);
                return null;
            }
        });
        elCategorias = new ExpandLayout(btDesplegarCat, layoutExpandCategoria, clFiltrarCat);
        elEstados = new ExpandLayout(btDesplegarEstado, layoutExpandEstado, clFiltrarEstado);
*/
        //listener
        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiarFiltros();
            }
        });

        // Genero el dialogo

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
    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String sday = (dayOfMonth< 10) ? "0"+dayOfMonth : String.valueOf(dayOfMonth);
                        month++;
                        String smonth = (month< 10) ? "0"+month : String.valueOf(month);
                        String date = sday + "/" + smonth + "/" + year;
                        lbFechaSelec.setText(date);
                        lbFechaSelec.setVisibility(View.VISIBLE);
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void limpiarFiltros() {
        RadioButton catTodos = (RadioButton) viewInflater.findViewById(R.id.catTodos);
        catTodos.setChecked(true);

        RadioButton estadoTodos = (RadioButton) viewInflater.findViewById(R.id.estadoTodos);
        estadoTodos.setChecked(true);

        tvFiltroNumOs.setText("");
        lbFechaSelec.setText("");
        lbNumOs.setText("");
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
        if (valor.equals("TODOS")) {
            tv.setVisibility(View.GONE);
            tv.setText("");
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(valor);
        }
    }

}
