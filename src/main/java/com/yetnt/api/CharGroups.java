package com.yetnt.api;

/**
 * Represents character groups with a specific number of characters.
 * Each enum constant holds an integer value 'n' indicating the group size.
 * @author Lehlogonolo Poole
 */
public enum CharGroups {

    /**
     * Each character is spaced.
     * <pre>{@code
     * A F 2 3
     * 1 0 1 1
     * }</pre>
     */
    SINGULAR(1),
    /**
     * Each character is grouped in pairs.
     * <pre>{@code
     * AF 23
     * 10 11
     * }</pre>
     */
    DOUBLE(2),
    /**
     * Each character is grouped in 4 (Except hex, as 2 hex characters is a single byte.)
     * <pre>{@code
     * AF 23 3F E2
     * 1011 1001
     * }</pre>
     */
    QUAD(4);

    private int n;

    CharGroups(int n) {
        this.n = n;
    }

    public int getN() {
        return n;
    }
}
