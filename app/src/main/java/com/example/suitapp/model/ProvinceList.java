package com.example.suitapp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProvinceList implements DialogSelectItemList {

    public static final List<Province> ITEMS = new ArrayList<>();

    public ProvinceList(){
        addItems();
    }

    private static void addItems() {
        int i = 1;
        ITEMS.add(new Province(i++, "Buenos Aires"));
        ITEMS.add(new Province(i++, "Buenos Aires Capital"));
        ITEMS.add(new Province(i++, "Catamarca"));
        ITEMS.add(new Province(i++, "Chaco"));
        ITEMS.add(new Province(i++, "Chubut"));
        ITEMS.add(new Province(i++, "Cordoba"));
        ITEMS.add(new Province(i++, "Corrientes"));
        ITEMS.add(new Province(i++, "Entre Rios"));
        ITEMS.add(new Province(i++, "Formosa"));
        ITEMS.add(new Province(i++, "Jujuy"));
        ITEMS.add(new Province(i++, "La Pampa"));
        ITEMS.add(new Province(i++, "La Rioja"));
        ITEMS.add(new Province(i++, "Mendoza"));
        ITEMS.add(new Province(i++, "Misiones"));
        ITEMS.add(new Province(i++, "Neuquen"));
        ITEMS.add(new Province(i++, "Rio Negro"));
        ITEMS.add(new Province(i++, "Salta"));
        ITEMS.add(new Province(i++, "San Juan"));
        ITEMS.add(new Province(i++, "San Luis"));
        ITEMS.add(new Province(i++, "Santa Cruz"));
        ITEMS.add(new Province(i++, "Santa Fe"));
        ITEMS.add(new Province(i++, "Santiago del Estero"));
        ITEMS.add(new Province(i++, "Tierra del Fuego"));
        ITEMS.add(new Province(i++, "Tucuman"));
    }

    @Override
    public Map<Integer, String> getList() {
        Map<Integer, String> map = new HashMap<>();
        for (Province province : ITEMS) {
            map.put(province.getId(), province.getName());
        }
        return map;
    }
}