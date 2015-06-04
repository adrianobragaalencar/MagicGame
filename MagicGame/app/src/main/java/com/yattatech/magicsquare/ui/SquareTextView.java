package com.yattatech.magicsquare.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.yattatech.magicsquare.R;

/**
 * Custom TextView as squared element
 * inside of GridView component.
 *
 * @author Adriano Braga Alencar (adrianobragaalencar@gmail.com)
 *
 */
public final class SquareTextView extends TextView {

    private static final int[] STATE_SELECTED = { R.attr.state_cell_selected };
    private static final int[] STATE_ADJACENT = { R.attr.state_cell_adjacent };
    private static final int[] STATE_WIN      = { R.attr.state_cell_win };
    private boolean mSelected;
    private boolean mAdjacent;
    private boolean mWin;

    public SquareTextView(Context context) {
        super(context);
    }

    public SquareTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SquareTextView);
        mSelected              = array.getBoolean(R.styleable.SquareTextView_state_cell_selected, false);
        mAdjacent              = array.getBoolean(R.styleable.SquareTextView_state_cell_adjacent, false);
    }

    public void reset() {
        mSelected =
        mAdjacent =
        mWin      = false;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
        if (mSelected) {
            mAdjacent = false;
        }
        refreshDrawableState();
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setAdjacent(boolean adjacent) {
        mAdjacent = adjacent;
        if (mAdjacent) {
            mSelected = false;
        }
        refreshDrawableState();
    }

    public boolean isAdjacent() {
        return mAdjacent;
    }

    public void setWin(boolean win) {
        mWin = win;
        if (mWin) {
            mSelected =
            mAdjacent = false;
        }
        refreshDrawableState();
    }

    public boolean isWin() {
        return mWin;
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 3);
        if (mSelected) {
            mergeDrawableStates(drawableState, STATE_SELECTED);
        } else if (mAdjacent) {
            mergeDrawableStates(drawableState, STATE_ADJACENT);
        } else if (mWin) {
            mergeDrawableStates(drawableState, STATE_WIN);
        }
        return drawableState;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // Just ignore heightMeasureSpec and make it has the same
        // value for both using its width parameter
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, width);
    }
}
