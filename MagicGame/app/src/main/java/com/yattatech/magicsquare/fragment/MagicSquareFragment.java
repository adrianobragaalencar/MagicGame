package com.yattatech.magicsquare.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.yattatech.magicsquare.R;
import com.yattatech.magicsquare.adapter.GameGridAdapter;
import com.yattatech.magicsquare.domain.Tile;
import com.yattatech.magicsquare.ui.GameTextSwitcher;
import com.yattatech.magicsquare.util.ShufflerNumber;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment where all the game logic/drawing happens.
 *
 * @author Adriano Braga Alencar (adrianobragaalencar@gmail.com)
 *
 */
public final class MagicSquareFragment extends Fragment {

    public static final byte SELECTED_STATE = 0x01;
    public static final byte ADJACENT_STATE = 0x02;
    public static final byte WIN_STATE      = 0x04;
    private GridView mGameGridView;
    private GameGridAdapter mAdapter;
    private int mRows;
    private boolean mGameFinished;
    private final AdapterView.OnItemClickListener mGamGridViewListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (!mGameFinished) {
                updateSquare((GameTextSwitcher)view, position);
            }
        }
    };
    private OnGameInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRows = getResources().getInteger(R.integer.game_container_num_columns);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mGameGridView  = (GridView)inflater.inflate(R.layout.fragment_magic_square, container, false);
        mAdapter       = new GameGridAdapter(getActivity(), mRows);
        mGameGridView.setOnItemClickListener(mGamGridViewListener);
        return mGameGridView;
    }

    public void startGame() {
        for (int i = mGameGridView.getChildCount(); --i >= 0; ) {
            final GameTextSwitcher switcher = (GameTextSwitcher)mGameGridView.getChildAt(i);
            switcher.reset();
        }
        mAdapter.setTiles(ShufflerNumber.getShuffledTiles());
        mAdapter.setGridState(null);
        mGameGridView.setAdapter(mAdapter);
        mListener.gameStarted();
        mGameFinished = false;
    }

    public void resumeGame(int[] tilesNumber, byte[] gridState) {
        final ArrayList<Tile> tiles = new ArrayList<Tile>();
        final int length            = tilesNumber.length;
        for (int i = 0; i < length; ++i) {
            tiles.add(new Tile(tilesNumber[i]));
        }
        mAdapter.setTiles(tiles);
        mGameGridView.setAdapter(mAdapter);
        mListener.gameStarted();
        mAdapter.setGridState(gridState);
        if (gridState.length == 1) {
            mGameFinished = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public byte[] getGridViewState() {
        final boolean win = mAdapter.isGameFinished();
        if (win) {
            return new byte[] { WIN_STATE };
        }
        final int count    = mGameGridView.getChildCount();
        final byte[] state = new byte[count];
        for (int i = 0; i < count; ++i) {
            final GameTextSwitcher switcher = (GameTextSwitcher)mGameGridView.getChildAt(i);
            state[i] = (switcher.mSelected) ? SELECTED_STATE :
                        (switcher.mAdjacent) ? ADJACENT_STATE : WIN_STATE;
        }
        return state;
    }

    public List<Tile> getTilesSource() {
        return mAdapter.getTiles();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnGameInteractionListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnGameInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void updateSquare(GameTextSwitcher switcher, int position) {
        if (switcher == null) {
            return;
        }
        if (switcher.mSelected) {
            setSelected(switcher, position, false);
        } else if (switcher.mAdjacent) {
            final GameTextSwitcher selected = getSelectedCell();
            final int selectedPosition      = mGameGridView.indexOfChild(selected);
            setSelected(selected, selectedPosition, false);
            swapTiles(selected, switcher);
            checkGameHasFinished();
        } else {
            checkForPreviousSelectedCell(switcher);
            setSelected(switcher, position, true);
        }
    }

    private GameTextSwitcher getSelectedCell() {
        final int size = mGameGridView.getChildCount();
        for (int i = 0; i < size; ++i) {
            final GameTextSwitcher switcher = (GameTextSwitcher)mGameGridView.getChildAt(i);
            if (switcher.mSelected) {
                return switcher;
            }
        }
        // it should never get that line
        return null;
    }

    private void checkForPreviousSelectedCell(GameTextSwitcher switcher) {
        final int size = mGameGridView.getChildCount();
        for (int i = 0; i < size; ++i) {
            final GameTextSwitcher next = (GameTextSwitcher)mGameGridView.getChildAt(i);
            if ((next.mSelected) && (!next.equals(switcher))) {
                setSelected(next, i, false);
                break;
            }
        }
    }

    private void setSelected(GameTextSwitcher switcher, int position, boolean enabled) {
        switcher.setSelected(enabled);
        setAdjacentCellsEnabled(position, enabled);
    }

    private void setAdjacentCellsEnabled(int position, boolean enabled) {
        final int row    = position / mRows;
        // Depending which cell user has clicked
        // we can get an invalid position for either,
        // left or right
        final int left   = position - 1;
        final int right  = position + 1;
        // always valid position
        final int top    = position - mRows;
        final int bottom = position + mRows;
        if ((left / mRows) == row) {
            setAdjacentCellEnabled(left,  enabled);
        }
        if ((right / mRows) == row) {
            setAdjacentCellEnabled(right, enabled);
        }
        setAdjacentCellEnabled(top,    enabled);
        setAdjacentCellEnabled(bottom, enabled);
    }

    private void setAdjacentCellEnabled(int position, boolean enabled) {
        if ((position >= 0) && (position < mGameGridView.getChildCount())) {
            final GameTextSwitcher switcher = (GameTextSwitcher)mGameGridView.getChildAt(position);
            switcher.setAdjacent(enabled);
        }
    }

    private void swapTiles(GameTextSwitcher lhs, GameTextSwitcher rhs) {
        Tile leftTile  = (Tile)lhs.getTag();
        Tile rightTile = (Tile)rhs.getTag();
        lhs.setTag(rightTile);
        rhs.setTag(leftTile);
        lhs.setText(String.valueOf(rightTile.mNumber));
        rhs.setText(String.valueOf(leftTile.mNumber));
        mAdapter.swapTiles(leftTile, rightTile);
    }

    private void checkGameHasFinished() {
        if (mAdapter.isGameFinished()) {
            mGameFinished = true;
            mListener.gameFinished();
            for (int i = mGameGridView.getChildCount(); --i >= 0; ) {
                final GameTextSwitcher switcher = (GameTextSwitcher)mGameGridView.getChildAt(i);
                switcher.setWin(true);
            }
        }
    }

    public interface OnGameInteractionListener {

        void gameStarted();
        void gameFinished();
    }
}
