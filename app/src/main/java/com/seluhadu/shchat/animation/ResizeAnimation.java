package com.seluhadu.shchat.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ResizeAnimation extends Animation {
    private int startHeight;
    private int deltaHeight;

    private int startWidth;
    private int deltaWidth;

    private View view;

    public ResizeAnimation(View view) {
        this.view = view;
    }

    public void setHeights(int start, int end) {
        this.startHeight = start;
        this.deltaHeight = end - this.startHeight;
    }

    public void setWidths(int start, int end) {
        this.startWidth = start;
        this.deltaWidth = end - this.startWidth;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        if (startHeight != 0) {
            if (deltaHeight > 0) {
                view.getLayoutParams().height = (int) (startHeight + deltaHeight * interpolatedTime);
            } else {
                view.getLayoutParams().height = (int) (startHeight - Math.abs(deltaHeight) * interpolatedTime);
            }
        }

        if (startWidth != 0) {
            if (deltaWidth > 0) {
                view.getLayoutParams().width = (int) (startWidth + deltaWidth * interpolatedTime);
            } else {
                view.getLayoutParams().width = (int) (startWidth - Math.abs(deltaWidth) * interpolatedTime);
            }
        }

        view.requestLayout();
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }


    public void translate(float x, float y, boolean isExpanded) {
        if (isExpanded) {
            view.animate().translationX(0).translationY(0).setDuration(100);
        } else
            view.animate().translationX(x).translationY(y).setDuration(100);
    }
}
