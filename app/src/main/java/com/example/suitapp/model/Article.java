package com.example.suitapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Article implements Parcelable {
    private int id;
    private int storeId;
    private String name;
    private float price;
    private String description;
    private int image;
    private String articleImage;
    private int colorsQty;
    private List<Variant> variantList;
    private float ranking;
    private int commentsQty;
    private int quantity;
    private int colorId;
    private int sizeId;
    private String buyDate, arriveDate;

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

        if (dataItem.has("quantity") && dataItem.getString("quantity") != null && !dataItem.getString("quantity").equals("null"))
            quantity = dataItem.getInt("quantity");

        if (dataItem.has("colorId") && dataItem.getString("colorId") != null && !dataItem.getString("colorId").equals("null"))
            colorId = dataItem.getInt("colorId");

        if (dataItem.has("sizeId") && dataItem.getString("sizeId") != null && !dataItem.getString("sizeId").equals("null"))
            sizeId = dataItem.getInt("sizeId");

        if (dataItem.has("storeId") && dataItem.getString("storeId") != null && !dataItem.getString("storeId").equals("null"))
            storeId = dataItem.getInt("storeId");

        if (dataItem.has("buyDate") && dataItem.getString("buyDate") != null && !dataItem.getString("buyDate").equals("null"))
            buyDate = dataItem.getString("buyDate");

        if (dataItem.has("arriveDate") && dataItem.getString("arriveDate") != null && !dataItem.getString("arriveDate").equals("null"))
            arriveDate = dataItem.getString("arriveDate");
    }

    protected Article(Parcel in) {
        id = in.readInt();
        storeId = in.readInt();
        name = in.readString();
        price = in.readFloat();
        description = in.readString();
        image = in.readInt();
        articleImage = in.readString();
        colorsQty = in.readInt();
        ranking = in.readFloat();
        commentsQty = in.readInt();
        quantity = in.readInt();
        colorId = in.readInt();
        sizeId = in.readInt();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getPrice(){
        return price;
    }

    public String getPriceEnter() {
        DecimalFormat formatea = new DecimalFormat("###,###", DecimalFormatSymbols.getInstance(Locale.ITALY));
        return formatea.format(price);
    }

    public String getPriceDecimal() {
        DecimalFormat format = new DecimalFormat("#.00");
        String result = format.format(price);
        int indexOfDecimal = (result.indexOf(",")==-1) ? result.indexOf(".") : result.indexOf(",");

        return result.substring(indexOfDecimal + 1);
    }

    public String getPriceFormated() {
        DecimalFormat formatea = new DecimalFormat("$ ###,###", DecimalFormatSymbols.getInstance(Locale.ITALY));
        return formatea.format(price);
    }

    public String getPriceFormated(int qty) {
        price = price * qty;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public int getSizeId() {
        return sizeId;
    }

    public void setSizeId(int sizeId) {
        this.sizeId = sizeId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public String getArriveDate() {
        return arriveDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(storeId);
        dest.writeString(name);
        dest.writeFloat(price);
        dest.writeString(description);
        dest.writeInt(image);
        dest.writeString(articleImage);
        dest.writeInt(colorsQty);
        dest.writeFloat(ranking);
        dest.writeInt(commentsQty);
        dest.writeInt(quantity);
        dest.writeInt(colorId);
        dest.writeInt(sizeId);
    }
}
