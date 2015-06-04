package com.yattatech.magicsquare.adapter;

import android.view.animation.Animation;

/**
 * Adapter class for receiving animation state events. All
 * methods are empty and it just exists for convenience for
 * creating listener objects with no need to implement all
 * methods whenever create
 *
 * @author Adriano Braga Alencar (adrianobragaalencar@gmail.com)
 */
public class AnimationAdapter implements Animation.AnimationListener {
    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
}
