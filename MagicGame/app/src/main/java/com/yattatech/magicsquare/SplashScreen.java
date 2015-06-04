package com.yattatech.magicsquare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.yattatech.magicsquare.adapter.AnimationAdapter;

import com.yattatech.magicsquare.BuildConfig;
import com.yattatech.magicsquare.R;

/**
 * Main Entry point to MagicSquare game
 *
 * @author Adriano Braga Alencar (adrianobragaalencar@gmail.com)
 *
 */
public final class SplashScreen extends Activity {

    // add image animation
    private static final String TAG    = "SplashScreen";
    private static final boolean DEBUG = BuildConfig.DEBUG;
    private Animation mAnim;
    private View mView;
    private volatile boolean mMoveToGame = true;
    private final AnimationAdapter mViewAnimationListener = new AnimationAdapter() {

        @Override
        public void onAnimationEnd(Animation animation) {
            mView.setAnimation(null);
            if (mMoveToGame) {
                final Intent intent = new Intent(getBaseContext(), GameScreen.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
            } else {
                if (DEBUG) {
                    Log.d(TAG, "Game canceled by user on onBackPressed");
                }
            }
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        mView = findViewById(R.id.content);
        mAnim = AnimationUtils.loadAnimation(this, R.anim.spash_open_scale);
        mView.setAnimation(mAnim);
        mAnim.setAnimationListener(mViewAnimationListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mView.startAnimation(mAnim);
    }

    @Override
    public void onBackPressed() {
        // Disable onbackpressed behavior but be aware
        // that if user has request to go out before
        // animation has done, it's going to die as soon
        // as animation  has finished
        mMoveToGame = false;
    }
}
