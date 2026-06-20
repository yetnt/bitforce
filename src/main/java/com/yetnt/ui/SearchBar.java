package com.yetnt.ui;

import com.yetnt.methods.InterpMethod;

import javax.swing.*;
import java.util.ArrayList;

public class SearchBar {
    private ArrayList<InterpMethod> methods = new ArrayList<>();
    private JPanel btnPanel =  new JPanel();

    public SearchBar(ArrayList<InterpMethod> methods, JPanel btnPanel) {
        this.methods = methods;
        this.btnPanel = btnPanel;
    }

    public void search(String input) {
        ArrayList<InterpMethod> found = new ArrayList<>();
        clearBtnPanel();
        input = input.trim();
        if (input.isBlank()) {
            methods.forEach(this::add);
            btnRepaintReval();
            return;
        }

        final String finalInput = input;
        methods.forEach(method -> {
            if (interpNameMatches(finalInput, method)) found.add(method);
        });
        if (found.isEmpty()) return;
        found.forEach(this::add);
        btnRepaintReval();
    }

    private void btnRepaintReval() {
        btnPanel.repaint();
        btnPanel.revalidate();
    }

    private boolean interpNameMatches(String input, InterpMethod interpMethod) {
        return interpMethod.getName().contains(input);
    }

    private void clearBtnPanel() {
        btnPanel.removeAll();
        btnRepaintReval();
    }

    private void add(InterpMethod interpMethod) {
        btnPanel.add(interpMethod.getRadioButton());
    }
}
