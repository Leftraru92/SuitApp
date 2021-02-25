package com.example.suitapp.dummy;

import com.example.suitapp.R;
import com.example.suitapp.model.PremiumStore;

import java.util.ArrayList;
import java.util.List;

public class DummyPremiumStores {
    public static final List<PremiumStore> ITEMS = new ArrayList<PremiumStore>();

    static {
        addItems();
    }

    private static void addItems() {
        int i = 0;
        ITEMS.add(new PremiumStore(++i, R.drawable.card_mujer, "Floater", "Otra descripción"));
        ITEMS.add(new PremiumStore(++i, R.drawable.card_hombre, "Cherie Indumentaria", "descripción"));
        ITEMS.add(new PremiumStore(++i, R.drawable.card_ninios, "Pilchas", "hola"));
        ITEMS.add(new PremiumStore(++i, R.drawable.buzo, "Varsovia", "Otra cosa"));
    }
}
