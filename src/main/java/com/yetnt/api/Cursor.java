package com.yetnt.api;

import com.yetnt.ut.MathUtil;

import javax.swing.*;

/**
 * A class representing a cursor that points to a specific byte index within a byte array.
 * It manages the byte index, its maximum value, and interacts with a {@link JSpinner} to reflect and update the index in a UI.
 * @author Lehlogonolo Poole
 */
public class Cursor {
    /**
     * The current byte index.
     */
    private int byteIndex = 0;
    private int maxValue;
    private JSpinner spinner;

    public Cursor(){}
    public Cursor(int maxValue, JSpinner spinner) {
        this.maxValue = maxValue;
        // set spinner max and min values.
        SpinnerNumberModel model = new SpinnerNumberModel(0, 0, maxValue, 1);
        spinner.setModel(model);
        spinner.setValue(0);
        this.spinner = spinner;
    }

    /**
     * Sets the current byte index.
     * @param byteIndex The new byte index.
     * @return The Cursor object for chaining.
     */
    public Cursor setByteIndex(int byteIndex) {
        if (byteIndex >= 0 && byteIndex < maxValue) {
            this.byteIndex = byteIndex;
            spinner.setValue(byteIndex);
        }
        return this;
    }

    /**
     * Gets the current byte index.
     * @return The current byte index.
     */
    public int getByteIndex() {
        return byteIndex;
    }

    /**
     * Increments the byte index by one, if it's not already at the maximum value.
     * The spinner value is also updated.
     * @return The Cursor object for chaining.
     */
    public Cursor incrementByte() {
        if (byteIndex != maxValue) {
            byteIndex++;
            spinner.setValue(byteIndex);
        }
        return this;
    }

    /**
     * Decrements the byte index by one, if it's not already at zero.
     * The spinner value is also updated.
     * @return The Cursor object for chaining.
     */
    public Cursor decrementByte() {
        if (byteIndex != 0) {
            byteIndex--;
            spinner.setValue(byteIndex);
        }
        return this;
    }

    /**
     * Snaps the current byte index up to the nearest multiple of 'p'.
     * For example, if byteIndex is 7 and p is 4, newIndex will be 8.
     * @param p The multiple to snap to.
     */
    public void snap2nUp(int p) {
        int q = MathUtil.snapToBoundary(p, MathUtil.Boundary.HIGH, byteIndex);
        setByteIndex(
                byteIndex == q ?
                        MathUtil.snapToBoundary(p, MathUtil.Boundary.HIGH, byteIndex + 1) :
                        q
        );
    }

    /**
     * Snaps the current byte index down to the nearest multiple of 'p'.
     * For example, if byteIndex is 7 and p is 4, newIndex will be 4.
     * @param p The multiple to snap to.
     */
    public void snap2nDown(int p) {
        int q = MathUtil.snapToBoundary(p, MathUtil.Boundary.LOW, byteIndex);
        setByteIndex(
                byteIndex == q ?
                        MathUtil.snapToBoundary(p, MathUtil.Boundary.LOW, byteIndex - 1) :
                        q
        );
    }

    /**
     * Extracts a sub-array of bytes from a given byte array based on the start and end cursor positions.
     * @param start The starting Cursor, inclusive.
     * @param end The ending Cursor, exclusive.
     * @param bytes The original byte array to extract from.
     * @return A new byte array containing the selected bytes, or an empty array if the selection is invalid.
     */
    public static byte[] bytes(Cursor start, Cursor end, byte[] bytes) {
        if (start.getByteIndex() == end.getByteIndex()) {
            // no byte selected
            return new byte[]{};
        } else if (start.getByteIndex() > end.getByteIndex()) {
            // invalid
            return new byte[]{};
        } else {
            // return sub array, start index is inclusive
            // end is exclusive.
            byte[] subArray = new byte[end.getByteIndex() - start.getByteIndex()];
            System.arraycopy(bytes, start.getByteIndex(), subArray, 0, subArray.length);
            return subArray;
        }
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
}
