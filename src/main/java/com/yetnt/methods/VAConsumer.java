package com.yetnt.methods;

/**
 * A functional interface representing a consumer that processes a byte array and an endianness,
 * returning a String.
 */
@FunctionalInterface
public interface VAConsumer {
    /**
     * Consumes the given byte array and endianness, returning a String representation.
     * @param stuff The byte array to consume.
     * @param endianess The endianness to use for processing the byte array.
     * @return A String representation of the consumed data.
     */
    String consume(byte[] stuff, Endianess endianess);
}
