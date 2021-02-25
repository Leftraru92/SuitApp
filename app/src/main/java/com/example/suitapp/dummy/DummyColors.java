package com.example.suitapp.dummy;


import com.example.suitapp.model.DialogSelectItemList;
import com.example.suitapp.model.Item;
import com.example.suitapp.model.Variant.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyColors implements DialogSelectItemList {

    public static final List<Item> ITEMS = new ArrayList<>();

    public DummyColors(){
        addItems();
    }

    private static void addItems() {
        int i = 0;
        ITEMS.add(new Color(++i, "Blanco", "#FFFFFF"));
        ITEMS.add(new Color(++i, "Gris", "#808080"));
        ITEMS.add(new Color(++i, "Negro", "#000000"));
        ITEMS.add(new Color(++i, "Rojo", "#FF0000"));
        ITEMS.add(new Color(++i, "Granate", "#800000"));
        ITEMS.add(new Color(++i, "Amarillo", "#FFFF00"));
        ITEMS.add(new Color(++i, "Aceituna", "#808000"));
        ITEMS.add(new Color(++i, "Lima", "#00FF00"));
        ITEMS.add(new Color(++i, "Verde", "#008000"));
        ITEMS.add(new Color(++i, "Verde azulado", "#008080"));
        ITEMS.add(new Color(++i, "Cian", "#00FFFF"));
        ITEMS.add(new Color(++i, "Cian Oscuro", "#008B8B"));
        ITEMS.add(new Color(++i, "Azul", "#0000FF"));
        ITEMS.add(new Color(++i, "Armada", "#000080"));
        ITEMS.add(new Color(++i, "Fucsia", "#FF00FF"));
        ITEMS.add(new Color(++i, "Púrpura", "#800080"));
        ITEMS.add(new Color(++i, "Marrón", "#A52A2A"));
        ITEMS.add(new Color(++i, "Otro", "#FFFFFF"));
    }

    public List<Item> getItems(){ return ITEMS; }

    public static Color getColor(int id) {
        if (ITEMS.size() == 0)
            addItems();

        for (Item color : ITEMS) {
            if (id == color.getId())
                return (Color) color;
        }
        return null;
    }


    @Override
    public Map<Integer, String> getList() {
        Map<Integer, String> map = new HashMap<>();
        for (Item color : ITEMS) {
            map.put(color.getId(), color.getName());
        }
        return map;
    }
}