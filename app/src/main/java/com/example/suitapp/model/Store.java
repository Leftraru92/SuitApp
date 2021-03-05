package com.example.suitapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Store implements Parcelable {

    private int id;
    private String name;
    private String storeLogo;
    private String storeCoverPhoto;
    private String coord;
    private String description;
    private boolean premium;
    private boolean physical_store;
    private String address;

    public Store(int id, String name, int image, int visits) {
        this.id = id;
        this.name = name;
    }

    public Store(JSONObject dataItem) throws JSONException {
        id = dataItem.getInt("storeId");
        name = dataItem.getString("storeName");

        if (dataItem.has("storeLogo") && dataItem.getString("storeLogo") != null && !dataItem.getString("storeLogo").equals("null"))
            storeLogo = dataItem.getString("storeLogo");
        if (dataItem.has("storeImage") && dataItem.getString("storeImage") != null && !dataItem.getString("storeImage").equals("null"))
            storeLogo = dataItem.getString("storeImage");
        if (dataItem.has("storeCoverPhoto") && dataItem.getString("storeCoverPhoto") != null && !dataItem.getString("storeCoverPhoto").equals("null"))
            storeCoverPhoto = dataItem.getString("storeCoverPhoto");
        if (dataItem.has("coord") && dataItem.getString("coord") != null && !dataItem.getString("coord").equals("null"))
            coord = dataItem.getString("coord");
        if (dataItem.has("description") && dataItem.getString("description") != null && !dataItem.getString("description").equals("null"))
            description = dataItem.getString("description");
        if (dataItem.has("premium") && dataItem.getString("premium") != null && !dataItem.getString("premium").equals("null"))
            premium = dataItem.getBoolean("premium");
        if (dataItem.has("physical_store") && dataItem.getString("physical_store") != null && !dataItem.getString("physical_store").equals("null"))
            physical_store = dataItem.getBoolean("physical_store");
        if (dataItem.has("address") && dataItem.getString("address") != null && !dataItem.getString("address").equals("null"))
            address = dataItem.getString("address");
    }

    protected Store(Parcel in) {
        id = in.readInt();
        name = in.readString();
        storeLogo = in.readString();
        storeCoverPhoto = in.readString();
        coord = in.readString();
        description = in.readString();
        premium = in.readByte() != 0;
        physical_store = in.readByte() != 0;
        address = in.readString();
    }

    public static final Creator<Store> CREATOR = new Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel in) {
            return new Store(in);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStoreLogo() {
        return storeLogo;
    }

    public String getStoreCoverPhoto() {
        return storeCoverPhoto;
    }

    public String getCoord() {
        return coord;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPremium() {
        return premium;
    }

    public boolean isPhysical_store() {
        return physical_store;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(storeLogo);
        dest.writeString(storeCoverPhoto);
        dest.writeString(coord);
        dest.writeString(description);
        dest.writeByte((byte) (premium ? 1 : 0));
        dest.writeByte((byte) (physical_store ? 1 : 0));
        dest.writeString(address);
    }
}
