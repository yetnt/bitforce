package com.yetnt.ut;

public abstract class MathUtil {
    /**
     * Validates whether the given input is a valid power of 2 by using bit magician.
     * <p>
     *     It works as follows
     * </p>
     * <ol>
     *     <li>Take the input say {@code 4} ({@code 100} in binary)</li>
     *     <li>Subtract 1 | {@code 3} ({@code 011})</li>
     *     <li>AND the original and new value together ({@code 100 & 011 = 000})
     *     <p>
     *         Only inputs which are valid 2^n (these inputs are 1 followed by however many zeros)
     *         will have a final value of 0 after the AND operation.
     *     </p>
     *     </li>
     * </ol>
     * @param n The input to validate
     * @return {@code true} if the input is a valid power of 2, {@code false} otherwise
     */
    public static boolean isValid2nInput(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    /**
     * Snaps the given input to either the low or high boundary given.
     * @implSpec The given {@code n} parameter is expected to be a power of 2.
     * Use {{@link #isValid2nInput(int)}} to validate.
     * @param n The power of 2 to snap to
     * @param boundary The boundary to snap to
     * @param byteIndex The index to snap
     * @return The snapped index
     */
    public static int snapToBoundary(int n, Boundary boundary, int byteIndex) {
        return boundary == Boundary.HIGH ?
                (int) Math.ceil((double) byteIndex/n) * n :
                (int) Math.floor( (double) byteIndex/n) * n;
    }

    /**
     * Boundary enum used by {@link #snapToBoundary(int, Boundary, int)}
     */
    public enum Boundary {
        /**
         * The low boundary
         */
        LOW,
        /**
         * The high boundary
         */
        HIGH
    }
}
