package com.yetnt.methods;

/**
 * Enum representing the two common byte orderings: Little-endian and Big-endian.
 * This is used to specify how multi-byte data types are stored in memory or transmitted.
 * @author Lehlogonolo Poole
 */
public enum Endianess {
    /**
     * Little-endian byte order.
     * Where given the input {@code 1011 0110} it will be read as {@code 0110 1011}
     */
    LITTLE,
    /**
     * Big-endian byte order.
     * Where given the input {@code 1011 0110} it will be read as {@code 1011 0110}
     */
    BIG;
}
