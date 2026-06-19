package com.yetnt.api;

import java.util.function.Function;

/**
 * Represents the different view modes for displaying data.
 * @author Lehlogonolo Poole
 */
public enum View {
    /**
     * Displayed as binary. 1s ad 0s. obviously
     */
    BIN(b ->
        String.format("%8s", Integer.toBinaryString(b & 0xFF))
            .replace(' ', '0')
            .toCharArray()
    ),
    /**
     * Displayed as hexadecimal characters.
     */
    HEX( b ->
            String.format("%02X", b).toCharArray()
    ),
    /**
     * Displays binary as ASCII block characters.
     */
    BLOCKS(b -> String.format("%8s", Integer.toBinaryString(b & 0xFF))
            .replace('1', '█')
            .replace(' ', '░')
            .replace('0', '░')
            .toCharArray()
    );

    View(VAFunction function) {
        view = function;
    }

    private final VAFunction view;
    public char[] call(byte daByte) {
        return view.run(daByte);
    }


    /**
     * A functional interface representing a consumer that processes a byte array and an endianness,
     * returning a String.
     */
    @FunctionalInterface
    public interface VAFunction {
        /**
         * Consumes the given byte returning a String representation.
         * @param stuff The byte to consume.
         */
        char[] run(byte stuff);
    }
}
