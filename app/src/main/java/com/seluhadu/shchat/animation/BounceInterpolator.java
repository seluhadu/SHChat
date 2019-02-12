package com.seluhadu.shchat.animation;

import android.view.animation.Interpolator;

public class BounceInterpolator implements Interpolator {
    private double mAmplitude = 1;
    private double mFrequency = 10;

    public BounceInterpolator(double amplitude, double frequency) {
        this.mAmplitude = amplitude;
        this.mFrequency = frequency;
    }

    @Override
    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time / mAmplitude) *
                Math.cos(mFrequency * time) + 1);
    }
}
