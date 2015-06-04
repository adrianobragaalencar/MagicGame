package com.yattatech.magicsquare.util;

import com.yattatech.magicsquare.domain.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.yattatech.magicsquare.app.Constants.TILE_MIN_VALUE;
import static com.yattatech.magicsquare.app.Constants.TILE_MAX_VALUE;

/**
 * Utility class responsible for shuffling a number list
 * returning a semi random array
 *
 * @author Adriano Braga Alencar (adrianobragaalencar@gmail.com)
 *
 */
public final class ShufflerNumber {

    private static final List<Tile> TILES               = new ArrayList<Tile>();
    private static final Tile.TileComparator COMPARATOR = new Tile.TileComparator();
    static {
        for (int i = TILE_MIN_VALUE; i <= TILE_MAX_VALUE; ++i) {
            TILES.add(new Tile(i));
        }
    }

    private ShufflerNumber() {
        throw new AssertionError();
    }

    public static List<Tile> getShuffledTiles() {
        Collections.shuffle(TILES);
        return TILES;
    }

    public static List<Tile> getOrderedTiles() {
        Collections.sort(TILES, COMPARATOR);
        return TILES;
    }
}
