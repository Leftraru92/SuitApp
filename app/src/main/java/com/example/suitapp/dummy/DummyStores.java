package com.example.suitapp.dummy;

import com.example.suitapp.R;
import com.example.suitapp.model.Store;

import java.util.ArrayList;
import java.util.List;

public class DummyStores {

    public static final List<Store> ITEMS = new ArrayList<Store>();

    static {
            addItems();
    }

    private static void addItems() {
        int i = 0;
        ITEMS.add(new Store(++i, "8 galones", R.drawable.logo8galones, 0));
        ITEMS.add(new Store(++i, "Floater", R.drawable.card_hombre, 0));
        ITEMS.add(new Store(++i, "Cherie Indumentaria", R.drawable.card_ninios, 0));
        ITEMS.add(new Store(++i, "Pilchas", R.drawable.logo8galones, 0));
        ITEMS.add(new Store(++i, "Varsovia", R.drawable.buzo, 0));
        ITEMS.add(new Store(++i, "Al Don Pirulero", R.drawable.card_mujer, 0));
        ITEMS.add(new Store(++i, "Ulala", R.drawable.card_hombre, 0));
        ITEMS.add(new Store(++i, "LeUtte", R.drawable.card_ninios, 0));

    }


}