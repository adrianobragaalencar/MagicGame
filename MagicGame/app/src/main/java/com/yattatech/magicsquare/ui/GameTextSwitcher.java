package com.yattatech.magicsquare.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextSwitcher;

/**
 * A better way to propagate both properties, selected and
 * adjacent to its SquareTextView children. That class'
 * supposed to be used only with SquareTextView, otherwise
 * we get back an error.
 *
 * @author Adriano Braga Alencar (adrianobragaalencar@gmail.com)
 */
public final class GameTextSwitcher extends TextSwitcher {

    public boolean mSelected;
    public boolean mAdjacent;
    public boolean mWin;

    public GameTextSwitcher(Context context) {
        super(context);
    }

    public GameTextSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void reset() {
        mSelected =
        mAdjacent =
        mWin      = false;
        for (int i = getChildCount(); --i >= 0;) {
            SquareTextView textView = (SquareTextView)getChildAt(i);
            textView.reset();
        }
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
        if (mSelected) {
            mAdjacent = false;
        }
        for (int i = getChildCount(); --i >= 0;) {
            SquareTextView textView = (SquareTextView)getChildAt(i);
            textView.setSelected(selected);
        }
        invalidate();
    }

    public void setAdjacent(boolean adjacent) {
        mAdjacent = adjacent;
        if (mAdjacent) {
            mSelected = false;
        }
        for (int i = getChildCount(); --i >= 0;) {
            SquareTextView textView = (SquareTextView)getChildAt(i);
            textView.setAdjacent(adjacent);
        }
        invalidate();
    }

    public void setWin(boolean win) {
        for (int i = getChildCount(); --i >= 0;) {
            SquareTextView textView = (SquareTextView)getChildAt(i);
            textView.setWin(win);
        }
        invalidate();
    }
}
