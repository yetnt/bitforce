package com.yetnt.api;

import com.yetnt.JLabelRichText;

import java.awt.*;

public class Converter {

    public Grouping grouping = Grouping.SINGULAR;
    public View view = View.BIN;
    public boolean showSelection = true;
    public boolean showByteIndexes = true;

    private int maxByteIndex = 300;
    private int minByteIndex = 0;
    public int maxViewIndex = 300;

    public Converter() {}

    public void setMaxByteIndex(int maxByteIndex) {
        if (maxByteIndex < 0) return;
        this.maxByteIndex = maxByteIndex;
        this.minByteIndex = Math.max(maxByteIndex - maxViewIndex - 1, 0);
    }

    public void setMaxViewIndex(int maxViewIndex) {
        if (maxViewIndex <= 1) return;
        this.maxViewIndex = maxViewIndex;
        this.minByteIndex = Math.max(maxByteIndex - maxViewIndex - 1, 0);
    }

    public int getMaxByteIndex() {
        return maxByteIndex;
    }

    public int getMaxViewIndex() {
        return maxViewIndex;
    }

    public byte[] bytesSlice(byte[] bytes) {
        if (bytes.length <= maxByteIndex) return bytes;
        byte[] subArray = new byte[Math.max(maxByteIndex, maxViewIndex)+1 - minByteIndex];
        System.arraycopy(bytes, minByteIndex, subArray, 0, subArray.length);
        return subArray;
    }

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
                        new JLabelRichText("(" + (minByteIndex + i) + ".)")
                                .font(Color.GRAY, "3")
                );
                charLength += 2;
            }
            JLabelRichText inner =
                    new JLabelRichText(applyGrouping(switch (view) {
                        case HEX -> String.format("%02X", b).toCharArray();
                        case BIN -> String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0').toCharArray();
                    }));
            if (showSelection && byteSelected(bytes, i + minByteIndex, start, end))
                inner.font(Color.RED, "6", Color.GREEN);
            else
                inner.font(Color.RED, "6");
            bView.append(inner.bold().italic());

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

    public boolean byteSelected(byte[] bytes, int index, Cursor start, Cursor end) {
        // start is inclusive, end is exclusive.
        return start.getByteIndex() <= index && index < end.getByteIndex();
    }

    public String applyGrouping(char[] chars) {
        return switch (grouping) {
            case SINGULAR, DOUBLE, QUAD -> applyGroupingInternal(chars, grouping.getN());
        };
    }

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
