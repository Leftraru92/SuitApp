package com.example.suitapp.util;

import android.animation.ObjectAnimator;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import java.util.concurrent.Callable;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

public class ExpandLayout {
    private Button btDesplegar;
    private ConstraintLayout layoutExpand, clExpandir;
    private SparseBooleanArray expandState = new SparseBooleanArray();
    Callable<Void> callBackExpand;

    public ExpandLayout(Button btDesplegar, ConstraintLayout layoutExpand, ConstraintLayout clExpandir) {
        this.btDesplegar = btDesplegar;
        this.layoutExpand = layoutExpand;
        this.clExpandir = clExpandir;
        this.callBackExpand = null;

        setExpandState();
    }

    public ExpandLayout(Button btDesplegar, ConstraintLayout layoutExpand, ConstraintLayout clExpandir, Callable<Void> callBackOnExpand) {
        this.btDesplegar = btDesplegar;
        this.layoutExpand = layoutExpand;
        this.clExpandir = clExpandir;
        this.callBackExpand = callBackOnExpand;

        setExpandState();
    }


    public Button getBtDesplegar() {
        return btDesplegar;
    }

    public ConstraintLayout getLayoutExpand() {
        return layoutExpand;
    }

    public ConstraintLayout getClExpandir() {
        return clExpandir;
    }

    public boolean getExpandState() {
        return expandState.get(btDesplegar.getId());
    }


    private void setExpandState() {
        final boolean isExpanded = (layoutExpand.getVisibility() == View.VISIBLE) ? true : false;

        expandState.append(btDesplegar.getId(), isExpanded);
        btDesplegar.setRotation(expandState.get(btDesplegar.getId()) ? 180f : 0f);
        if (clExpandir != null) {
            clExpandir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    onClickContraint();
                }
            });
        } else {
            btDesplegar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    onClickContraint();
                }
            });
        }
    }

    public void onClickContraint() {
        View view;

        if (clExpandir != null) {
            view = clExpandir;
        }else{
            view = btDesplegar;
        }

        if (layoutExpand.getVisibility() == View.VISIBLE) {
            //TransitionManager.beginDelayedTransition((ViewGroup) view, new AutoTransition());
            createRotateAnimator(btDesplegar, 180f, 0f).start();
            expandState.put(btDesplegar.getId(), false);
            layoutExpand.setVisibility(View.GONE);

        } else {
            TransitionManager.beginDelayedTransition((ViewGroup) view.getParent(), new AutoTransition());
            createRotateAnimator(btDesplegar, 0f, 180f).start();
            layoutExpand.setVisibility(View.VISIBLE);
            expandState.put(btDesplegar.getId(), true);
        }

        if (callBackExpand != null) {
            try {
                callBackExpand.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(250);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }
}
