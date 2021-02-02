package com.example.suitapp.dummy;

import com.example.suitapp.model.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyCategories {

    public static final List<Category> ITEMS = new ArrayList<Category>();

    static {
            addItems();
    }

    private static void addItems() {
        int i = 0;
        ITEMS.add(new Category(++i, "Accesorios de Moda", 10));
        ITEMS.add(new Category(++i, "Bermudas y Shorts", 10));
        ITEMS.add(new Category(++i, "Buzos y Hoodies", 10));
        ITEMS.add(new Category(++i, "Calzado", 10));
        ITEMS.add(new Category(++i, "Calzas", 10));
        ITEMS.add(new Category(++i, "Camisas", 10));
        ITEMS.add(new Category(++i, "Camperas y Tapados", 10));
        ITEMS.add(new Category(++i, "Enteritos", 10));
        ITEMS.add(new Category(++i, "Equipaje, Bolsos y Carteras", 10));
        ITEMS.add(new Category(++i, "Pantalones, Jeans y Joggings ", 10));
        ITEMS.add(new Category(++i, "Polleras", 10));
        ITEMS.add(new Category(++i, "Remeras, Musculosas y Chombas", 10));
        ITEMS.add(new Category(++i, "Saquitos, Sweaters y Chalecos", 10));
        ITEMS.add(new Category(++i, "Trajes", 10));
        ITEMS.add(new Category(++i, "Trajes de baño", 10));
        ITEMS.add(new Category(++i, "Uniformes y Ropa de Trabajo", 10));
        ITEMS.add(new Category(++i, "Vestidos", 10));
        ITEMS.add(new Category(++i, "Otros", 10));
    }


}