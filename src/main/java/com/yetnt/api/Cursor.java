package com.yetnt.api;

import javax.swing.*;

public class Cursor {
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

    public Cursor setByteIndex(int byteIndex) {
        if (byteIndex >= 0 && byteIndex < maxValue) {
            this.byteIndex = byteIndex;
            spinner.setValue(byteIndex);
        }
        return this;
    }

    public int getByteIndex() {
        return byteIndex;
    }

    public Cursor incrementByte() {
        if (byteIndex != maxValue) {
            byteIndex++;
            spinner.setValue(byteIndex);
        }
        return this;
    }

    public Cursor decrementByte() {
        if (byteIndex != 0) {
            byteIndex--;
            spinner.setValue(byteIndex);
        }
        return this;
    }

    public void snap2nUp(int p) {
        int newIndex =
                (int) Math.ceil((double) byteIndex/p ) * p;
        setByteIndex(newIndex);
    }

    public void snap2nDown(int p) {
        int newIndex =
                (int) Math.floor( (double) byteIndex/p ) * p;
        setByteIndex(newIndex);
    }

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
}
