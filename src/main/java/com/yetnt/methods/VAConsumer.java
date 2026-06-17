package com.yetnt.methods;

@FunctionalInterface
public interface VAConsumer {
    String consume(byte[] stuff, Endianess endianess);
}
