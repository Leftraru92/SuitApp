package com.example.suitapp.dummy;

import com.example.suitapp.R;
import com.example.suitapp.model.Category;
import com.example.suitapp.model.Genre;

import java.util.ArrayList;
import java.util.List;

public class DummyGenres {

    public static final List<Genre> ITEMS = new ArrayList<Genre>();

    static {
            addItems();
    }

    private static void addItems() {
        int i = 0;
        ITEMS.add(new Genre(++i, "Mujeres", R.drawable.card_mujer, 0));
        ITEMS.add(new Genre(++i, "Hombres", R.drawable.card_hombre, 0));
        ITEMS.add(new Genre(++i, "Niños", R.drawable.card_ninos, 0));
    }


}