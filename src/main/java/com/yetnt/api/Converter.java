package com.yetnt.api;

import com.yetnt.JLabelRichText;

import java.awt.*;

/**
 * The Converter class is responsible for converting byte arrays into a displayable format,
 * typically for a rich text label, with various customisation options.
 * @author Lehlogonolo Poole
 */
public class Converter {

    public CharGroups charGroups = CharGroups.SINGULAR;
    public View view = View.BIN;
    public boolean showSelection = true;
    public boolean showByteIndexes = true;

    private int maxByteIndex = 1;
    private int minByteIndex = 0;

    /**
     * Constructs a new Converter with default settings.
     */
    public Converter() {}

    /**
     * Sets the maximum byte index for the slice.
     * The maximum byte index must be non-negative and not less than the minimum byte index.
     *
     * @param maxByteIndex The new maximum byte index.
     */
    public void setMaxByteIndex(int maxByteIndex) {
        if (maxByteIndex < 0 || maxByteIndex < minByteIndex) return;
        this.maxByteIndex = maxByteIndex;
    }

    /**
     * Sets the minimum byte index for the slice.
     * The minimum byte index must be non-negative and not greater than the maximum byte index.
     *
     * @param minByteIndex The new minimum byte index.
     */
    public void setMinByteIndex(int minByteIndex) {
        if (minByteIndex < 0 || minByteIndex > maxByteIndex) return;
        this.minByteIndex = minByteIndex;
    }

    /**
     * Returns the current maximum byte index.
     * @return The maximum byte index.
     */
    public int getMaxByteIndex() {
        return maxByteIndex;
    }

    /**
     * Returns the current minimum byte index.
     * @return The minimum byte index.
     */
    public int getMinByteIndex() {
        return minByteIndex;
    }

    /**
     * Slices a byte array based on the current {@code minByteIndex} and {@code maxByteIndex}.
     * If the array length is less than or equal to {@code maxByteIndex}, the original array is returned.
     *
     * @param bytes The input byte array.
     * @return A new byte array representing the slice.
     */
    public byte[] bytesSlice(byte[] bytes) {
        if (bytes.length <= maxByteIndex) return bytes;
        byte[] subArray = new byte[maxByteIndex - minByteIndex];
        System.arraycopy(bytes, minByteIndex, subArray, 0, subArray.length);
        return subArray;
    }

    /**
     * Converts a byte array into an array of {@link JLabelRichText} objects,
     * applying various formatting and display options.
     *
     * @param bytes The byte array to convert.
     * @param start The starting cursor for selection highlighting.
     * @param end The ending cursor for selection highlighting.
     * @return An array of {@link JLabelRichText} objects representing the formatted bytes.
     */
    public JLabelRichText[] conv(byte[] bytes, Cursor start, Cursor end) {
        byte[] sliced = bytesSlice(bytes);
        JLabelRichText []txts = new JLabelRichText[sliced.length];
        int maxCharLength = 50;
        int charsInLn = 0;
        for (int i = 0; i < sliced.length; i++) {
            byte b = sliced[i];
            int charLength = 0;

            StringBuilder bView = new StringBuilder();
            if (showByteIndexes) {
                bView.append(
                        new JLabelRichText((minByteIndex + i) + ".")
                                .font(Color.GRAY, "3")
                );
                charLength += 2;
            }
            JLabelRichText inner =
                    new JLabelRichText(applyGrouping(view.call(b)));
            if (showSelection && byteSelected(bytes, i + minByteIndex, start, end))
                inner.font(Color.RED, "6", new Color(201, 232, 177)).italic();
            else
                inner.font(Color.BLACK, "6");
            bView.append(inner.bold());

            charLength += inner.getRawContent().length();
            charsInLn += charLength;

            if (charsInLn >= maxCharLength) {
                charsInLn = 0;
                bView.append("<br>");
            } else {
                bView.append(" ");
            }

            txts[i] = new JLabelRichText(bView.toString());
        }
        return txts;
    }

    /**
     * Checks if a specific byte at a given index is within the selected range.
     * The start cursor is inclusive, and the end cursor is exclusive.
     *
     * @param bytes The byte array (unused in the current implementation, but kept for context).
     * @param index The index of the byte to check.
     * @param start The starting cursor of the selection.
     * @param end The ending cursor of the selection.
     * @return {@code true} if the byte is selected, {@code false} otherwise.
     */
    public boolean byteSelected(byte[] bytes, int index, Cursor start, Cursor end) {
        // start is inclusive, end is exclusive.
        return start.getByteIndex() <= index && index < end.getByteIndex();
    }

    /**
     * Applies charGroups to a character array based on the current {@code charGroups} setting.
     * @param chars The character array to group.
     * @return A string with the characters grouped.
     */
    public String applyGrouping(char[] chars) {
        return switch (charGroups) {
            case SINGULAR, DOUBLE, QUAD -> applyGroupingInternal(chars, charGroups.getN());
        };
    }

    /**
     * Internal helper method to apply charGroups to a character array.
     *
     * @param chars The character array to group.
     * @param groupSize The size of each group.
     * @return A string with the characters grouped and separated by spaces.
     */
    private String applyGroupingInternal(char[] chars, int groupSize) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < chars.length; i += groupSize) {
            for (int j = 0; j < groupSize && (i + j) < chars.length; j++) {
                output.append(chars[i + j]);
            }
            if (i != chars.length - 1)
                output.append(" ");
        }
        return output.toString();
    }
}
