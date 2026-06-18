package com.yetnt;

import com.yetnt.ui.Window;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    /**
     * The name of this, is intentionally long.
     * My code, my rules.
     */
    public static String HARD_CODED_PATH_TO_MY_TEST_FILE = "C:\\Users\\ACER\\Documents\\code\\bitforce\\src\\main\\resources\\gq33.wav";

    public static void main(String[] args) {
        Path filePath;
        if (args.length == 0) filePath = Paths.get(HARD_CODED_PATH_TO_MY_TEST_FILE);
        else filePath = Paths.get(args[0]);
        if (!filePath.toFile().exists()) {
            System.out.println("So like i cant find " + filePath.toAbsolutePath());
            return;
        }
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            new Window(Files.readAllBytes(filePath)).setVisible(true);
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public static String format(byte b) {
        return
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0').replaceAll("(.{2})", "$1 ").trim() + " "
        // print as hex
        + " - " + String.format("%02X ", b) +
        // print as UTF-8

        " - " + new String(new byte[]{b}, StandardCharsets.UTF_8)
                + "\n";
    }

    public static String format2(byte b) {
        return
                String.format("%02X", b) + " ";
    }
}