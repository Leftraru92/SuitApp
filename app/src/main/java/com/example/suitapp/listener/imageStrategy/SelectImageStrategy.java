package com.example.suitapp.listener.imageStrategy;

import android.content.Intent;

import com.example.suitapp.viewmodel.CaptureImageViewModel;

public interface SelectImageStrategy {
    boolean runIntent();
    boolean processData(int requestCode, int resultCode, Intent data, CaptureImageViewModel mViewModel);
}
