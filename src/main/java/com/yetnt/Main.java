package com.yetnt;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        Path filePath = Paths.get("C:\\Users\\ACER\\Documents\\code\\bitforce\\target\\classes\\com\\yetnt\\Main.class");
        try {
            byte[] fileBytes = Files.readAllBytes(filePath);
            for (byte b : fileBytes) {
                // print in 2 bit pairs, e.g. 01 10 11 00
                System.out.println(format(b));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String format(byte b) {
        return
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0').replaceAll("(.{2})", "$1 ").trim() + " "
        // print as hex
        + " - " + String.format("%02X ", b) +
        // print as UTF-8

        " - " + new String(new byte[]{b}, StandardCharsets.UTF_8);
    }
}