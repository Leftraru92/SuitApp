package com.example.suitapp.dummy;

import com.example.suitapp.R;
import com.example.suitapp.model.Article;
import com.example.suitapp.model.Genre;

import java.util.ArrayList;
import java.util.List;

public class DummyArticles {

    public static final List<Article> ITEMS = new ArrayList<Article>();

    static {
            addItems();
    }

    private static void addItems() {
        int i = 0;
        ITEMS.add(new Article(++i, "Pantalón cargo hombre gabardina", 1950, R.drawable.pantalon, 3));
        ITEMS.add(new Article(++i, "Zapatillas Jaguar Oficial Deportiva Art. # 918 Urbana Hombre Y Mujer", 1988,  R.drawable.zapatilla, 0));
        ITEMS.add(new Article(++i, "Havaianas hojotas color negro", 926.50f, R.drawable.camisa, 10));
        ITEMS.add(new Article(++i, "Pantalon Cargo Gabardina Pre Lavada For Men Hard Work", 3099, R.drawable.pantalon, 4));
        ITEMS.add(new Article(++i, "Buzo canguro capucha slim", 2000,  R.drawable.buzo, 2));
        ITEMS.add(new Article(++i, "Camisa entallada slim hombre varios colores!", 926.50f, R.drawable.camisa, 0));
        ITEMS.add(new Article(++i, "Buzo canguro capucha slim", 1400,  R.drawable.buzo, 0));
        ITEMS.add(new Article(++i, "Zapatilla Hombre Kioshi Bokeh Sin Cordones Ultra Livianas", 2899,  R.drawable.zapatilla2, 0));
    }


}