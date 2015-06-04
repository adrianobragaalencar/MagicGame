package com.yattatech.magicsquare.domain;

import java.util.Comparator;

/**
 * Domain that represents a number tile within
 * our magic square game
 *
 * @author Adriano Braga Alencar (adrianobragaalencar@gmail.com)
 *
 */
public final class Tile {

    public final int mNumber;

    public Tile(int number) {
        mNumber   = number;
    }

    @Override
    public String toString() {
        return "number=" + mNumber;
    }

    public static final class TileComparator implements Comparator<Tile> {

        @Override
        public int compare(Tile lhs, Tile rhs) {
            if (lhs == null) {
                return rhs == null ? 0 : 1;
            } else if (rhs == null) {
                return -1;
            }
            return Integer.signum(lhs.mNumber - rhs.mNumber);
        }
    }
}
