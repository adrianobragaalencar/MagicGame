package com.yattatech.magicsquare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ViewSwitcher;

import com.yattatech.magicsquare.R;
import com.yattatech.magicsquare.domain.Tile;
import com.yattatech.magicsquare.fragment.MagicSquareFragment;
import com.yattatech.magicsquare.ui.GameTextSwitcher;
import com.yattatech.magicsquare.ui.SquareTextView;

import java.util.List;

/**
 * A simple adapter to create a 3x3 grid view
 *
 * @author Adriano Braga Alencar (adrianobragaalencar@gmail.com)
 */
public final class GameGridAdapter extends BaseAdapter {

    private static final byte[] WIN          = new byte[1];
    private static byte[] sCellStates;
    private final Context mContext;
    private final int mRows;
    private final ViewFactoryImpl mViewFactory;
    private final LayoutInflater mVi;
    private byte[] mGridState;
    private List<Tile> mTiles;

    public GameGridAdapter(Context context, int rows) {
        mContext     = context;
        mRows        = rows;
        mVi          = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewFactory = new ViewFactoryImpl(mVi, R.layout.tile_view_item);
        if (sCellStates == null) {
            sCellStates = new byte[mRows * mRows];  // square grid
        }

    }

    public void setTiles(List<Tile> tiles) {
        mTiles = tiles;
        notifyDataSetChanged();
    }

    public void setGridState(byte[] gridState) {
        mGridState = gridState;
    }

    public List<Tile> getTiles() {
        return mTiles;
    }

    @Override
    public int getCount() {
        return (mTiles == null) ? 0 : mTiles.size();
    }

    @Override
    public Tile getItem(int position) {
        if ((mTiles != null) &&
           ((position >= 0)  &&
            (position < mTiles.size()))) {
            return mTiles.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final GameTextSwitcher switcher = (GameTextSwitcher)mVi.inflate(R.layout.tile_view, null);
        final Tile tile                 = getItem(position);
        switcher.setFactory(mViewFactory);
        switcher.setText(String.valueOf(tile.mNumber));
        switcher.setTag(tile);
        if (mGridState != null) {
            if (mGridState.length == 1) {
                switcher.setWin(true);
            } else {
                if (mGridState[position] == MagicSquareFragment.SELECTED_STATE) {
                    switcher.setSelected(true);
                } else if (mGridState[position] == MagicSquareFragment.ADJACENT_STATE) {
                    switcher.setAdjacent(true);
                }
            }
        }
        return switcher;
    }

    public void swapTiles(Tile lhs, Tile rhs) {
        // don't use notifyDataSetChanged(); to avoid
        // reload the whole board whenever user change
        // any position
        final int leftIndexOf  = mTiles.indexOf(lhs);
        final int rightIndexOf = mTiles.indexOf(rhs);
        if ((leftIndexOf | rightIndexOf) < 0) {
            return;
        }
        mTiles.set(rightIndexOf, mTiles.set(leftIndexOf, mTiles.get(rightIndexOf)));
    }

    /**
     * Just check if all Tiles elements in already
     * in ascending order, if so that it means there's
     * nothing to do but start a new game
     *
     * @return boolean
     *
     */
    public boolean isGameFinished() {
        final int size = mTiles.size();
        if ((mTiles.isEmpty()) || (size == 1)) {    //sanity check
            return true;
        }
        for (int i = 1; i < size; ++i) {
            if (mTiles.get(i - 1).mNumber > mTiles.get(i).mNumber) {
                return false;
            }
        }
        return true;
    }

    public final class ViewFactoryImpl implements ViewSwitcher.ViewFactory {

        private final LayoutInflater mVi;
        private final int mResId;

        public ViewFactoryImpl(LayoutInflater vi, int resId) {
            mVi    = vi;
            mResId = resId;
        }

        @Override
        public SquareTextView makeView() {
            return (SquareTextView)mVi.inflate(mResId, null);
        }
    }
}
