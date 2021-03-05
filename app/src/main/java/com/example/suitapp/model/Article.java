package com.example.suitapp.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Article {
    private int id;
    private String name;
    private float price;
    private String description;
    private int image;
    private String articleImage;
    private int colorsQty;
    private List<Variant> variantList;
    private float ranking;
    private int commentsQty;

    public Article(int id, String name, float price, int image, int colors) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.colorsQty = colors;
    }

    public Article(JSONObject dataItem) throws JSONException {
        id = dataItem.getInt("articleId");
        name = dataItem.getString("articleName");
        price = (float) dataItem.getDouble("articlePrice");

        if (dataItem.has("articleDescription") && dataItem.getString("articleDescription") != null && !dataItem.getString("articleDescription").equals("null"))
            description = dataItem.getString("articleDescription");

        if (dataItem.getString("articleImage") != null && !dataItem.getString("articleImage").equals("null"))
            articleImage = dataItem.getString("articleImage");

        variantList = new ArrayList<>();
        if (dataItem.has("variants") && dataItem.getJSONArray("variants") != null && dataItem.getJSONArray("variants").length() > 0)
            for (int i = 0; i < dataItem.getJSONArray("variants").length(); i++) {
                JSONObject variant = dataItem.getJSONArray("variants").getJSONObject(i);
                variantList.add(new Variant(variant, i));
            }

        if (dataItem.has("ranking") && dataItem.getString("ranking") != null && !dataItem.getString("ranking").equals("null"))
            ranking = dataItem.getInt("ranking");

        if (dataItem.has("commentsQty") && dataItem.getString("commentsQty") != null && !dataItem.getString("commentsQty").equals("null"))
            commentsQty = dataItem.getInt("commentsQty");

        if (dataItem.has("articleCountColours") && dataItem.getString("articleCountColours") != null && !dataItem.getString("articleCountColours").equals("null"))
            colorsQty = dataItem.getInt("articleCountColours");

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPriceEnter() {
        DecimalFormat formatea = new DecimalFormat("###,###", DecimalFormatSymbols.getInstance(Locale.ITALY));
        return formatea.format(price);
    }

    public String getPriceDecimal() {
        DecimalFormat format = new DecimalFormat("#.00");
        String result = format.format(price);
        int indexOfDecimal = result.indexOf(".");
        return result.substring(indexOfDecimal + 1);
    }

    public String getPriceFormated() {
        DecimalFormat formatea = new DecimalFormat("$ ###,###", DecimalFormatSymbols.getInstance(Locale.ITALY));
        return formatea.format(price);
    }

    public String getDescription() {
        if (description == null || description.equals(""))
            return "El vendedor no incluyó una descripción del producto";
        else
            return description;
    }

    public int getImage() {
        return image;
    }

    public String getArticleImage() {
        return articleImage;
    }

    public int getColorsQty() {
        return colorsQty;
    }

    public List<Variant> getVariantList() {
        return variantList;
    }

    public List<String> getDistinctSizes() {
        List<String> distinctSizes = new ArrayList<>();
        for (Variant variant : variantList) {
            if (!distinctSizes.contains(variant.getSize().getName()))
                distinctSizes.add(variant.getSize().getName());
        }
        return distinctSizes;
    }

    public List<String> getDistinctSizes(String color) {
        List<String> distinctSizes = new ArrayList<>();
        for (Variant variant : variantList) {
            if (!distinctSizes.contains(variant.getSize().getName()) && variant.getColor().getName().equals(color))
                distinctSizes.add(variant.getSize().getName());
        }
        return distinctSizes;
    }

    public List<Variant.Color> getDistinctColors() {
        List<Variant.Color> distinctSizes = new ArrayList<>();
        for (Variant variant : variantList) {
            if (!existColorInList(distinctSizes, variant.getColor()))
                distinctSizes.add(variant.getColor());
        }
        return distinctSizes;
    }

    public List<Variant.Color> getDistinctColors(String size) {
        List<Variant.Color> distinctSizes = new ArrayList<>();
        for (Variant variant : variantList) {
            if (!existColorInList(distinctSizes, variant.getColor()) && variant.getSize().getName().equals(size))
                distinctSizes.add(variant.getColor());
        }
        return distinctSizes;
    }

    private boolean existColorInList(List<Variant.Color> distinctSizes, Variant.Color color) {
        for (Variant.Color col : distinctSizes) {
            if (col.getName().equals(color.getName()))
                return true;
        }
        return false;
    }

    public String getStockFormatted(int stock) {
        String result = (stock == 1) ? "(" + stock + " disponible)" : "(" + stock + " disponibles)";
        return result;
    }

    public int getStock() {
        int stock = 0;
        for (Variant variant : variantList) {
            stock += variant.getStock();
        }
        return stock;
    }

    public int getStock(String color, String size) {
        int stock = 0;
        if (color != null || size != null) {
            for (Variant variant : variantList) {
                if (color != null && size != null) {
                    if (variant.getColor().getName().equals(color) && variant.getSize().getName().equals(size))
                        stock += variant.getStock();
                } else if (color != null) {
                    if (variant.getColor().getName().equals(color))
                        stock += variant.getStock();
                } else if (size != null) {
                    if (variant.getSize().getName().equals(size))
                        stock += variant.getStock();
                }
            }
            return stock;
        } else return getStock();
    }

    public float getRanking() {
        return ranking;
    }

    public int getCommentsQty() {
        return commentsQty;
    }
}
