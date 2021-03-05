package com.example.suitapp.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogSelect implements DialogSelectItemList {
    List<Item> itemList;

    public DialogSelect(List<Item> itemList){
        this.itemList = itemList;
    }


    @Override
    public Map<Integer, String> getList() {
        Map<Integer, String> map = new HashMap<>();
        for (Item color : itemList) {
            map.put(color.getId(), color.getName());
        }
        return map;
    }
}
