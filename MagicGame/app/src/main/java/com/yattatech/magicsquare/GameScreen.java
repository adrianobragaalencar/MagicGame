package com.yattatech.magicsquare;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Chronometer;

import com.yattatech.magicsquare.app.Constants;
import com.yattatech.magicsquare.domain.Tile;
import com.yattatech.magicsquare.fragment.MagicSquareFragment;

import java.util.List;

/**
 * Activity that presents to user the MagicSquare
 * game
 *
 * @author Adriano Braga Alencar (adrianobragaalencar@gmail.com)
 *
 */
public final class GameScreen extends FragmentActivity implements MagicSquareFragment.OnGameInteractionListener {

    private SharedPreferences mPrefs;
    private MagicSquareFragment mMagicSquareFrag;
    private PowerManager.WakeLock mWakeLock;
    private Chronometer mChronometer;
    private boolean mRunning;
    private boolean mRules = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);
        mPrefs                       = getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        final PowerManager pm        = (PowerManager) getSystemService(Context.POWER_SERVICE);
        final FragmentManager fm     = getSupportFragmentManager();
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mMagicSquareFrag             = (MagicSquareFragment)fm.findFragmentById(R.id.magicSquareFrag);
        mChronometer                 = (Chronometer)findViewById(R.id.chronometer);
        mWakeLock                    = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "MagicSquareLock");
        ft.show(mMagicSquareFrag);
        ft.commit();
        if (savedInstanceState == null) {
            startNewGame();
        } else {
            mRunning          = savedInstanceState.getBoolean(Constants.RUN_KEY);
            mRules            = savedInstanceState.getBoolean(Constants.RULES_KEY);
            long timeBase     = savedInstanceState.getLong(Constants.CHRONO_KEY);
            int[] tilesNumber = savedInstanceState.getIntArray(Constants.TILES_KEY);
            byte[] gridState  = savedInstanceState.getByteArray(Constants.GRID_KEY);
            mChronometer.setBase(timeBase);
            mMagicSquareFrag.resumeGame(tilesNumber, gridState);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRunning) {
            mChronometer.start();
        }
        mWakeLock.acquire();
        if (mRules) {
            showMessageRules();
            mRules = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mRunning) {
            mChronometer.stop();
        }
        mWakeLock.release();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game_item:
                if (mRunning) {
                    mChronometer.stop();
                }
                mChronometer.setBase(SystemClock.elapsedRealtime());
                startNewGame();
                break;
            case R.id.exit_game_item:
                // as equals as press back button
                mRunning = false;
                finish();
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        final List<Tile> tiles  = mMagicSquareFrag.getTilesSource();
        final int size          = tiles.size();
        final int[] tilesNumber = new int[size];
        for (int i = 0; i < size; ++i) {
            tilesNumber[i] = tiles.get(i).mNumber;
        }
        savedState.putBoolean(Constants.RUN_KEY,    mRunning);
        savedState.putBoolean(Constants.RULES_KEY,  mRules);
        savedState.putIntArray(Constants.TILES_KEY, tilesNumber);
        savedState.putLong(Constants.CHRONO_KEY,    mChronometer.getBase());
        savedState.putByteArray(Constants.GRID_KEY, mMagicSquareFrag.getGridViewState());
    }

    private void startNewGame() {
        mChronometer.start();
        mRunning = true;
        mMagicSquareFrag.startGame();
    }

    private void showMessageRules() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.new_game)
                .setMessage(R.string.new_game_message)
                .setCancelable(true)
                .setPositiveButton(R.string.ok, null)
                .create()
                .show();
    }

    @Override
    public void gameStarted() {
    }

    @Override
    public void gameFinished() {
        mRunning = false;
        mChronometer.stop();
        new AlertDialog.Builder(this)
                .setTitle(R.string.game_over)
                .setMessage(R.string.game_over_message)
                .setCancelable(true)
                .setPositiveButton(R.string.ok, null)
                .create()
                .show();
    }
}
