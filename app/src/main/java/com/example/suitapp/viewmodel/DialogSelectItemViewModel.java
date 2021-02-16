package com.example.suitapp.viewmodel;

public interface DialogSelectItemViewModel {
    void selectValue(int id, String value, int requestId);
    int getSelectValue(int requestId);
}
