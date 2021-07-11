package com.seluhadu.shchat.animation;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.seluhadu.shchat.R;

public class AnimationActivity extends AppCompatActivity {
    private static final String TAG = "AnimationActivity";
    private final int delay = 1000;
    LinearLayout layout;
    RelativeLayout touchBg;
    private int viewHeight;
    private int viewWidth;
    private boolean isKnown = true;
    private ResizeAnimation resizeAnimation;
    private boolean isExpanded = false;
    public static final int toHeight = 500;
    public static final int toWidth = 400;
    private int widthDisplay;
    private int heightDisplay;
    private int cxWidth;
    private int cyHeight;
    private Handler handler = new Handler();
    private Runnable runnable;
    private float currentTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_activity);
        touchBg = findViewById(R.id.bg_touch);
        layout = findViewById(R.id.view_to_animate);
        layout.setOnClickListener(v -> {
            if (isKnown) {
                if (isExpanded) {
                    collapse();
                    touchBg.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    expand();
                    touchBg.setBackgroundColor(getResources().getColor(R.color.touch_bg));
                    touchBg.getBackground().setAlpha(160);
                }
                resizeAnimation.translate(cxWidth, cyHeight, isExpanded);
                isExpanded = !isExpanded;
            }
        });

        touchBg.setOnClickListener(v -> {

        });
        Button start = findViewById(R.id.start);
        touchBg.setBackgroundColor(Color.TRANSPARENT);
        resizeAnimation = new ResizeAnimation(layout);
//        BounceInterpolator bounceInterpolator = new BounceInterpolator(.7, 5);
//        resizeAnimation.setInterpolator(bounceInterpolator);
        getObserver();
        getDisplay();
        if (isKnown) {
            cxWidth = (widthDisplay / 2) - (toWidth / 2);
            cyHeight = (heightDisplay / 2) - (toHeight / 2);
        }
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isKnown) {
                    if (isExpanded) {
                        collapse();
                        touchBg.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        expand();
                        touchBg.setBackgroundColor(getResources().getColor(R.color.touch_bg));
                        touchBg.getBackground().setAlpha(160);
                    }
                    resizeAnimation.translate(cxWidth, cyHeight, isExpanded);
                    isExpanded = !isExpanded;
                }
            }
        });
    }

    private void getObserver() {
        ViewTreeObserver observer = layout.getViewTreeObserver();
        if (observer.isAlive()) {
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    viewWidth = layout.getWidth();
                    viewHeight = layout.getHeight();
                    isKnown = true;
                }
            });
        }
    }

    private void collapse() {
//        cxWidth = (widthDisplay / 2) - (toWidth / 2);
        cyHeight = (heightDisplay / 2) - (toHeight / 2);
        resizeAnimation.translate(cxWidth, cyHeight, isExpanded);
        resizeAnimation.setWidths(viewWidth, viewWidth);
        resizeAnimation.setHeights(viewHeight, viewHeight);
        resizeAnimation.setDuration(200);
        layout.startAnimation(resizeAnimation);
        handler.removeCallbacks(runnable);
    }

    private void expand() {
        currentTime = System.currentTimeMillis();
        resizeAnimation.setWidths(viewWidth, toWidth);
        resizeAnimation.setHeights(viewHeight, toHeight);
        resizeAnimation.setDuration(200);
        layout.startAnimation(resizeAnimation);
        handler.postDelayed(runnable = () -> {
                    cxWidth = (widthDisplay / 2) - (toWidth / 2);
            cyHeight = 0;
            resizeAnimation.translate(cxWidth, cyHeight, isExpanded);
            resizeAnimation.setWidths(toWidth, toWidth);
            resizeAnimation.setHeights(toHeight, heightDisplay);
            resizeAnimation.setDuration(200);
            layout.startAnimation(resizeAnimation);
        }, delay);
    }

    void expandMore() {
        if (System.currentTimeMillis() - currentTime > delay) {
            currentTime = System.currentTimeMillis();
            resizeAnimation.setWidths(viewWidth, toWidth);
            resizeAnimation.setHeights(viewHeight, toHeight);
            resizeAnimation.setDuration(200);
            layout.startAnimation(resizeAnimation);
        }
    }

    private void getDisplay() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        widthDisplay = displayMetrics.widthPixels;
        heightDisplay = displayMetrics.heightPixels;
    }

}
