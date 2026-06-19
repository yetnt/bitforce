package com.yetnt.ut;

import javax.swing.*;
import java.io.*;
import java.util.function.Consumer;

/**
 * Stolen from my other project <a href="https://github.com/yetnt/j3engine">J3Engine</a>
 * like {@link com.yetnt.JLabelRichText} however, this is only for the file and folder chooser. the other methods like
 * file binary writing using {@link DataInputStream} or {@link DataOutputStream} were removed.
 * @author Lehlogonolo Poole
 */
public class FilesUtility {

    /**
     * Shows a file chooser dialogue and returns the selected file's absolute path.
     * @param chooserConfigure A consumer that configures the file chooser.
     * @return The absolute path of the selected file, or null if no file is selected.
     */
    public static File fileChooser(Consumer<JFileChooser> chooserConfigure, JFrame frameParent) {
        JFileChooser chooser = new JFileChooser();
        chooserConfigure.accept(chooser);

        int result = chooser.showOpenDialog(frameParent);
        return result == JFileChooser.APPROVE_OPTION ? chooser.getSelectedFile(): null;
    }

    public static File genericFileChooser(JFrame frameParent) {
        return fileChooser(e -> {},frameParent);
    }

    /**
     * Shows a folder chooser dialogue and returns the selected folder's absolute path.
     * @return The absolute path of the selected folder, or null if no folder is selected.
     */
    public static File folderChooser(JFrame frameParent) {
        return fileChooser(chooser -> {
            chooser.setDialogTitle("Select Folder Big Dawgg");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
        }, frameParent);
    }
}