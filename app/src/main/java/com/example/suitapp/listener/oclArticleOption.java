package com.example.suitapp.listener;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.suitapp.R;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import static android.content.Context.VIBRATOR_SERVICE;

public class oclArticleOption implements View.OnClickListener {

    Context context;
    final Vibrator vibrator;
    Fragment fragment;

    public oclArticleOption(Context context) {

        this.context = context;
        vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        this.fragment = fragment;
    }

    @Override
    public void onClick(final View view) {
        vibrator.vibrate(100);
        PopupMenu popup = new PopupMenu(context, view);
        popup.getMenuInflater().inflate(R.menu.menu_article_item, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {
                case R.id.action_delete:
                    return true;
                case R.id.action_edit:

                    return true;
                default:
                    return true;
            }
        });
        popup.show();
    }

}
