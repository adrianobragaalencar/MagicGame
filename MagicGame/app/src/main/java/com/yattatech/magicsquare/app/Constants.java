package com.yattatech.magicsquare.app;

/**
 * Utility class responsible for shuffling a number list
 * returning a semi random array
 *
 * @author Adriano Braga Alencar (adrianobragaalencar@gmail.com)
 *
 */
public final class Constants {

    public static final String PREFS_NAME  = "magicSquarePrefs";
    public static final String RUN_KEY     = "runningKey";
    public static final String RULES_KEY   = "rulesKey";
    public static final String TILES_KEY   = "tilesKey";
    public static final String CHRONO_KEY  = "chronoKey";
    public static final String GRID_KEY    = "gridKey";
    public static final int TILE_MIN_VALUE = 1;
    public static final int TILE_MAX_VALUE = 9;

    private Constants() {
        throw new AssertionError();
    }
}
