/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.yetnt.ui;

import com.yetnt.JLabelRichText;
import com.yetnt.ut.FilesUtility;
import com.yetnt.ut.MathUtil;
import com.yetnt.api.CharGroups;
import com.yetnt.api.Cursor;
import com.yetnt.api.View;
import com.yetnt.api.Converter;
import com.yetnt.methods.Endianess;
import com.yetnt.methods.InterpMethod;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;


/**
 *
 * @author yetnt
 */
public class Window extends javax.swing.JFrame {

    private Cursor start;
    private Cursor end;
    private byte[] bytes;
    private Converter converter = new Converter();
    private Endianess endianess = Endianess.BIG;
    private InterpMethod interpMethod;
    public static ArrayList<InterpMethod> methods  = InterpMethod.getMethods();
    private InterpretedLabel interpLabelFrame = new InterpretedLabel();
    private Cursor selectedCursor;
    private int value2n = 4;
    private SearchBar search;

    /**
     * Creates new form Window
     */
    public Window() {
        initComponents();
        this.bytes = new byte[0];
        start = new Cursor(0, curs1IndexSpinner);
        end = new Cursor(0, curs2IndexSpinner);
        selectedCursor = end;
        init2();
        print();
    }

    public Window(byte[] bytes, String title) {
        this.setTitle(title);
        initComponents();
        this.bytes = bytes;
        start = new Cursor(bytes.length, curs1IndexSpinner);
        end = new Cursor(bytes.length, curs2IndexSpinner);
        selectedCursor = end;
        init2();
        print();
    }

    public void load(File file) {
        try {
            this.setTitle(file.getAbsolutePath());
            start.setByteIndex(0);
            end.setByteIndex(0);
            bytes = Files.readAllBytes(file.toPath());
            start.setMaxValue(bytes.length);
            end.setMaxValue(bytes.length);
            print();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Converter getConverter() {
        return converter;
    }

    private void interpMethod() {
        String str =
                interpMethod.interpret(
                        selectedBytes(), endianess);
        interpretedLabel.setText(str);
        interpLabelFrame.setString(str);
    }

    private void init2() {
        curs1IndexSpinner.addChangeListener(e -> {
            start.setByteIndex((Integer) curs1IndexSpinner.getValue());
            print();
            interpMethod();
        });
        curs2IndexSpinner.addChangeListener(e -> {
            end.setByteIndex((Integer) curs2IndexSpinner.getValue());print();
            interpMethod();
        });
        maxByteIndexSpinner.setModel(new SpinnerNumberModel(
                converter.getMaxByteIndex(),
                0,
                bytes.length,
                1
        ));
        minByteIndexSpinner.setModel(new SpinnerNumberModel(
                converter.getMinByteIndex(),
                0,
                bytes.length,
                1));
        maxByteIndexSpinner.addChangeListener(e -> {
            converter.setMaxByteIndex((Integer) maxByteIndexSpinner.getValue());
            print();
        });
        minByteIndexSpinner.addChangeListener(e -> {
            converter.setMinByteIndex((Integer) minByteIndexSpinner.getValue());
            print();
        });

        interpretedLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                interpLabelFrame.setVisible(true);
            }
        });

        interpMethod = methods.getFirst();
        methods.getFirst().getRadioButton().setSelected(true);
        for (InterpMethod method : methods) {
            interpMethodsRadioPanel.add(method.getRadioButton());
            interpMethodBtnGroup.add(method.getRadioButton());
            methodJMenu.add(method.getMenuItem());
            ActionListener a = e -> {
                String str = method.interpret(selectedBytes(), endianess);
                interpretedLabel.setText(str);
                interpLabelFrame.setString(str);
                interpMethod = method;
                method.getRadioButton().setSelected(true);
                iNote();
            };
            method.getRadioButton().addActionListener(a);
            method.getMenuItem().addActionListener(a);
        }
        interpLabelFrame.setMethods(methods);
        first100BytesMenuItemActionPerformed(null);
        search = new SearchBar(methods, interpMethodsRadioPanel);
        interpMethodsRadioPanel.repaint();
        interpMethodsRadioPanel.revalidate();
        repaint();
        revalidate();
    }

    public byte[] selectedBytes() {
        // start inclusive, end exclusive. use cursors
        return Cursor.bytes(start, end, bytes);
    }

    public void print() {
        setText(
                converter.conv(
                        bytes, start, end
                )
        );
    }

    private void setText(JLabelRichText ...rchTxts) {
        viewerLabel.setText(JLabelRichText.htmlOf(rchTxts));
        viewerScrollpane.repaint();
        viewerScrollpane.revalidate();
    }

    private void note(String txt) {
        noteLabel.setText(txt);
    }
    private void iNote() {
        note("Set the interpretation method to " + interpMethod.getName());
    }

    private boolean _setMinValue(int min) {
        int oldMin = converter.getMinByteIndex();
        converter.setMinByteIndex(min);
        if (oldMin != converter.getMinByteIndex()) {
            minByteIndexSpinner.setValue(min);
            return true;
        }
        return false;
    }
    private boolean _setMaxValue(int max) {
        int oldMax = converter.getMaxByteIndex();
        converter.setMaxByteIndex(max);
        if (oldMax != converter.getMaxByteIndex()) {
            maxByteIndexSpinner.setValue(max);
            return true;
        }
        return false;
    }

    private void _changeview(View view) {
        note("Changed view to " + view.name());
        converter.view = view;
        print();
    }
    private void _changegrouping(CharGroups g) {
        note("Changed charGroups to " + g.name());
        converter.charGroups = g;
        print();
    }
    private void _incrementSelectedCursor() {
        note("Incremented Cursor " + (selectedCursor == start ? 1 : 2) + " to byte " + selectedCursor.getByteIndex());
        selectedCursor.incrementByte();
        print();
    }
    private void _decrementSelectedCursor() {
        note("Decremented Cursor " + (selectedCursor == start ? 1 : 2) + " to byte " + selectedCursor.getByteIndex());
        selectedCursor.decrementByte();
        print();
    }
    private void _selectCursor(Cursor c) {
        note("Selected Cursor " + (c == start ? 1 : 2) + ".");
        selectedCursor = c;
        print();
    }
    private void byteRangeNote() {
        note("Showing byte range: [" + converter.getMinByteIndex() + "; " + converter.getMaxByteIndex() + ")");
    }
    private void setValue2n() {
        String option = JOptionPane.showInputDialog(this,
                JLabelRichText.paragraphWrap(
                        new JLabelRichText("Enter a valid power of 2 (1, 2, 4, 8, 16...) or \"2^n\" like (2^3)"),
                        new JLabelRichText(JLabelRichText.LINE_BREAK + "[Or leave blank to use the old value of " + value2n + "]")
                ).wrapHTML(),
                JOptionPane.INFORMATION_MESSAGE
        );
        if (option == null)
            return;
        option = option.trim();
        if (option.isEmpty())
            return;
        int newNum = 0;
        try {
            newNum = Integer.parseInt(option.startsWith("2^") ?
                    option.substring(2) :  option);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this, "Twin. Put a number twin.",
                    "Error", JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (option.startsWith("2^")) {
            value2n = (int) Math.pow(2, newNum);
            note("Byte Jump set to " + value2n + ".");
            return;
        }

        if (!MathUtil.isValid2nInput(newNum)) {
            JOptionPane.showMessageDialog(
                    this, "Twin. Put thats not a powr of 2 twin.",
                    "Error", JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        value2n = newNum;
        note("Byte Jump set to " + value2n + ".");
    }
    private void _selected2nUp() {
        int oldIndex = selectedCursor.getByteIndex();
        selectedCursor.snap2nUp(value2n);
        note(
                "Cursor " + (selectedCursor == start ? 1 : 2) + " jumped from byte "
                        + oldIndex + " to byte " + selectedCursor.getByteIndex() + "."
                        + (selectedCursor == start ? "(inclusive)" : "(exclusive)")
        );
        print();
    }
    private void _selected2nDown() {
        int oldIndex = selectedCursor.getByteIndex();
        selectedCursor.snap2nDown(value2n);
        note(
                "Cursor " + (selectedCursor == start ? 1 : 2) + " jumped from byte "
                + oldIndex + " to byte " + selectedCursor.getByteIndex() + "."
                + (selectedCursor == start ? "(inclusive)" : "(exclusive)")
        );
        print();
    }

    /**
     * This method is called from within the constructor to initialise the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        endianessBtnGroup = new javax.swing.ButtonGroup();
        cursorBtnGroup = new javax.swing.ButtonGroup();
        fileViewBtnGrp = new javax.swing.ButtonGroup();
        groupingBtnGrp = new javax.swing.ButtonGroup();
        interpMethodBtnGroup = new javax.swing.ButtonGroup();
        viewerScrollpane = new javax.swing.JScrollPane();
        viewerLabel = new javax.swing.JLabel();
        mainControlsPanel = new javax.swing.JPanel();
        curs2IndexSpinner = new javax.swing.JSpinner();
        curs1IndexSpinner = new javax.swing.JSpinner();
        seperatorLeft = new javax.swing.JSeparator();
        interpretedLabel = new javax.swing.JLabel();
        seperatorRight = new javax.swing.JSeparator();
        nextByteBtn = new javax.swing.JButton();
        previousByteBtn = new javax.swing.JButton();
        next2nBtn = new javax.swing.JButton();
        previous2nBtn = new javax.swing.JButton();
        cursor1 = new javax.swing.JRadioButton();
        cursor2 = new javax.swing.JRadioButton();
        byteIndexCheckBox = new javax.swing.JCheckBox();
        selectionCheckBox = new javax.swing.JCheckBox();
        saveInterpBtn = new javax.swing.JButton();
        listInterpBtn = new javax.swing.JButton();
        loadInterpBtn = new javax.swing.JButton();
        ponderInterpBtn = new javax.swing.JButton();
        maxByteIndexSpinner = new javax.swing.JSpinner();
        maxUbdexLabel = new javax.swing.JLabel();
        minIndexLabel = new javax.swing.JLabel();
        minByteIndexSpinner = new javax.swing.JSpinner();
        noteLabel = new javax.swing.JLabel();
        methodPanel = new javax.swing.JPanel();
        interpMethodLabl = new javax.swing.JLabel();
        searchBarJTextField = new javax.swing.JTextField();
        interpMethodScrollPane = new javax.swing.JScrollPane();
        interpMethodsRadioPanel = new javax.swing.JPanel();
        littleEndianRadio = new javax.swing.JRadioButton();
        bigEdianRadio = new javax.swing.JRadioButton();
        endianessLabel = new javax.swing.JLabel();
        fileViewScrollPane = new javax.swing.JScrollPane();
        fileViewPanel = new javax.swing.JPanel();
        fileViewLabel = new javax.swing.JLabel();
        innerViewScrollPane = new javax.swing.JScrollPane();
        innerViewJPanel = new javax.swing.JPanel();
        binViewRadio = new javax.swing.JRadioButton();
        hexViewRadio = new javax.swing.JRadioButton();
        blockViewRadio = new javax.swing.JRadioButton();
        groupingScrollPane = new javax.swing.JScrollPane();
        groupingJPanel = new javax.swing.JPanel();
        singularRadio = new javax.swing.JRadioButton();
        group2Radio = new javax.swing.JRadioButton();
        group4Radio = new javax.swing.JRadioButton();
        characterGroupingLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileJMenu = new javax.swing.JMenu();
        loadFileMenuItem = new javax.swing.JMenuItem();
        editJMenu = new javax.swing.JMenu();
        curorJMenu = new javax.swing.JMenu();
        selectCursor1MenuItem = new javax.swing.JMenuItem();
        selectCursor2MenuItem = new javax.swing.JMenuItem();
        nextByteMenuItem = new javax.swing.JMenuItem();
        previousByteMenuItem = new javax.swing.JMenuItem();
        snap2nByteJMenu = new javax.swing.JMenu();
        set2nValueMenuItem = new javax.swing.JMenuItem();
        nextBytesMenuItem = new javax.swing.JMenuItem();
        previousBytesMenuItem = new javax.swing.JMenuItem();
        teleportMenuItem = new javax.swing.JMenuItem();
        viewJMenu = new javax.swing.JMenu();
        binaryViewMenuItem = new javax.swing.JMenuItem();
        hexViewMenuItem = new javax.swing.JMenuItem();
        charGroupingJMenu = new javax.swing.JMenu();
        singularMenuItem = new javax.swing.JMenuItem();
        grouped2menuItem = new javax.swing.JMenuItem();
        grouped4menuitem = new javax.swing.JMenuItem();
        viewByteJMenu = new javax.swing.JMenu();
        setViewAroundCursorMenuItem = new javax.swing.JMenuItem();
        showAllBytesMenuItem = new javax.swing.JMenuItem();
        first100BytesMenuItem = new javax.swing.JMenuItem();
        last100BytesMenuItem = new javax.swing.JMenuItem();
        plus100BytesMenuItem = new javax.swing.JMenuItem();
        minus100BytesMenuItem = new javax.swing.JMenuItem();
        minIndexJMenu = new javax.swing.JMenu();
        plusMinMenuItem = new javax.swing.JMenuItem();
        minusMinMenuItem = new javax.swing.JMenuItem();
        maxIndexJMenu = new javax.swing.JMenu();
        plusMaxMenuItem = new javax.swing.JMenuItem();
        minusMaxMenuItem = new javax.swing.JMenuItem();
        interpJMenu = new javax.swing.JMenu();
        methodJMenu = new javax.swing.JMenu();
        openInterpWindowMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        viewerLabel.setFont(new java.awt.Font("Fira Code Medium", 0, 18)); // NOI18N
        viewerLabel.setText("Our very funny colour coded via cursor and byte seperated input will go into here.");
        viewerScrollpane.setViewportView(viewerLabel);

        seperatorLeft.setBackground(new java.awt.Color(0, 0, 0));
        seperatorLeft.setForeground(new java.awt.Color(0, 0, 0));
        seperatorLeft.setOpaque(true);

        interpretedLabel.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        interpretedLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        interpretedLabel.setText("jLabel4");

        seperatorRight.setBackground(new java.awt.Color(0, 0, 0));
        seperatorRight.setForeground(new java.awt.Color(0, 0, 0));
        seperatorRight.setOpaque(true);

        nextByteBtn.setText("next byte");
        nextByteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextByteBtnActionPerformed(evt);
            }
        });

        previousByteBtn.setText("pre byte");
        previousByteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousByteBtnActionPerformed(evt);
            }
        });

        next2nBtn.setText("+2n");
        next2nBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                next2nBtnActionPerformed(evt);
            }
        });

        previous2nBtn.setText("-2n");
        previous2nBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previous2nBtnActionPerformed(evt);
            }
        });

        cursorBtnGroup.add(cursor1);
        cursor1.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        cursor1.setText("Cursor 1");
        cursor1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cursor1ActionPerformed(evt);
            }
        });

        cursorBtnGroup.add(cursor2);
        cursor2.setFont(cursor1.getFont());
        cursor2.setSelected(true);
        cursor2.setText("Cursor 2");
        cursor2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cursor2ActionPerformed(evt);
            }
        });

        byteIndexCheckBox.setSelected(true);
        byteIndexCheckBox.setText("byte index");
        byteIndexCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                byteIndexCheckBoxActionPerformed(evt);
            }
        });

        selectionCheckBox.setSelected(true);
        selectionCheckBox.setText("selection");
        selectionCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectionCheckBoxActionPerformed(evt);
            }
        });

        saveInterpBtn.setText("save");

        listInterpBtn.setText("list");

        loadInterpBtn.setText("load");

        ponderInterpBtn.setText("ponder");

        maxUbdexLabel.setText("Max Index");

        minIndexLabel.setText("Min Index");

        noteLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        noteLabel.setText("hallo im a note :)");

        javax.swing.GroupLayout mainControlsPanelLayout = new javax.swing.GroupLayout(mainControlsPanel);
        mainControlsPanel.setLayout(mainControlsPanelLayout);
        mainControlsPanelLayout.setHorizontalGroup(
            mainControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainControlsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cursor1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cursor2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(curs2IndexSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(curs1IndexSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(byteIndexCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectionCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(seperatorLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(interpretedLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(mainControlsPanelLayout.createSequentialGroup()
                            .addGap(6, 6, 6)
                            .addComponent(saveInterpBtn)
                            .addGap(18, 18, 18)
                            .addComponent(listInterpBtn)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(loadInterpBtn)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(ponderInterpBtn)))
                    .addGroup(mainControlsPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(noteLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(seperatorRight, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(nextByteBtn)
                    .addComponent(previousByteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(previous2nBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                    .addComponent(next2nBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainControlsPanelLayout.createSequentialGroup()
                        .addComponent(maxByteIndexSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(minByteIndexSpinner))
                    .addGroup(mainControlsPanelLayout.createSequentialGroup()
                        .addComponent(maxUbdexLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(minIndexLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(12, 12, 12))
        );
        mainControlsPanelLayout.setVerticalGroup(
            mainControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainControlsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainControlsPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(mainControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(curs1IndexSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cursor1)
                            .addComponent(byteIndexCheckBox))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mainControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(curs2IndexSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cursor2)
                            .addComponent(selectionCheckBox)))
                    .addComponent(seperatorLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(mainControlsPanelLayout.createSequentialGroup()
                            .addComponent(interpretedLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(mainControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(saveInterpBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(listInterpBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(loadInterpBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(ponderInterpBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(noteLabel))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(mainControlsPanelLayout.createSequentialGroup()
                                .addGroup(mainControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(nextByteBtn)
                                    .addComponent(next2nBtn)
                                    .addComponent(maxUbdexLabel)
                                    .addComponent(minIndexLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(mainControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(previousByteBtn)
                                    .addComponent(previous2nBtn)
                                    .addComponent(maxByteIndexSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(minByteIndexSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(seperatorRight, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        interpMethodLabl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        interpMethodLabl.setText("Interp Methods");

        searchBarJTextField.setText("(Search for a method)");
        searchBarJTextField.setToolTipText("(Search for a method)");
        searchBarJTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBarJTextFieldActionPerformed(evt);
            }
        });

        interpMethodScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        interpMethodsRadioPanel.setLayout(new java.awt.GridLayout(0, 1));
        interpMethodScrollPane.setViewportView(interpMethodsRadioPanel);

        endianessBtnGroup.add(littleEndianRadio);
        littleEndianRadio.setText("little");
        littleEndianRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                littleEndianRadioActionPerformed(evt);
            }
        });

        endianessBtnGroup.add(bigEdianRadio);
        bigEdianRadio.setSelected(true);
        bigEdianRadio.setText("big");
        bigEdianRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bigEdianRadioActionPerformed(evt);
            }
        });

        endianessLabel.setText("Endianess:");

        javax.swing.GroupLayout methodPanelLayout = new javax.swing.GroupLayout(methodPanel);
        methodPanel.setLayout(methodPanelLayout);
        methodPanelLayout.setHorizontalGroup(
            methodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(methodPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(methodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(endianessLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(interpMethodLabl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchBarJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(interpMethodScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(methodPanelLayout.createSequentialGroup()
                        .addComponent(littleEndianRadio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(bigEdianRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        methodPanelLayout.setVerticalGroup(
            methodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(methodPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(interpMethodLabl, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(endianessLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(methodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(littleEndianRadio)
                    .addComponent(bigEdianRadio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchBarJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(interpMethodScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        fileViewScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        fileViewLabel.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        fileViewLabel.setText("File View");

        innerViewScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        fileViewBtnGrp.add(binViewRadio);
        binViewRadio.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        binViewRadio.setSelected(true);
        binViewRadio.setText("binary");
        binViewRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                binViewRadioActionPerformed(evt);
            }
        });

        fileViewBtnGrp.add(hexViewRadio);
        hexViewRadio.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        hexViewRadio.setText("hex");
        hexViewRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hexViewRadioActionPerformed(evt);
            }
        });

        fileViewBtnGrp.add(blockViewRadio);
        blockViewRadio.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        blockViewRadio.setText("blocks");
        blockViewRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blockViewRadioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout innerViewJPanelLayout = new javax.swing.GroupLayout(innerViewJPanel);
        innerViewJPanel.setLayout(innerViewJPanelLayout);
        innerViewJPanelLayout.setHorizontalGroup(
            innerViewJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(innerViewJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(innerViewJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(binViewRadio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(hexViewRadio, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addGroup(innerViewJPanelLayout.createSequentialGroup()
                        .addComponent(blockViewRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        innerViewJPanelLayout.setVerticalGroup(
            innerViewJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(innerViewJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(binViewRadio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hexViewRadio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(blockViewRadio)
                .addContainerGap(386, Short.MAX_VALUE))
        );

        innerViewScrollPane.setViewportView(innerViewJPanel);

        groupingScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        groupingScrollPane.setToolTipText("");

        groupingBtnGrp.add(singularRadio);
        singularRadio.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        singularRadio.setSelected(true);
        singularRadio.setText("Singular");
        singularRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                singularRadioActionPerformed(evt);
            }
        });

        groupingBtnGrp.add(group2Radio);
        group2Radio.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        group2Radio.setText("Group 2");
        group2Radio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                group2RadioActionPerformed(evt);
            }
        });

        groupingBtnGrp.add(group4Radio);
        group4Radio.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        group4Radio.setText("Grouped 4");
        group4Radio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                group4RadioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout groupingJPanelLayout = new javax.swing.GroupLayout(groupingJPanel);
        groupingJPanel.setLayout(groupingJPanelLayout);
        groupingJPanelLayout.setHorizontalGroup(
            groupingJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(groupingJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(groupingJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(singularRadio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(group2Radio, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(group4Radio, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
                .addContainerGap())
        );
        groupingJPanelLayout.setVerticalGroup(
            groupingJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(groupingJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(singularRadio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(group2Radio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(group4Radio)
                .addContainerGap(386, Short.MAX_VALUE))
        );

        groupingScrollPane.setViewportView(groupingJPanel);

        characterGroupingLabel.setText("Character Grouping");

        javax.swing.GroupLayout fileViewPanelLayout = new javax.swing.GroupLayout(fileViewPanel);
        fileViewPanel.setLayout(fileViewPanelLayout);
        fileViewPanelLayout.setHorizontalGroup(
            fileViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fileViewPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(fileViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileViewLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(innerViewScrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(groupingScrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(characterGroupingLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        fileViewPanelLayout.setVerticalGroup(
            fileViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fileViewPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileViewLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(innerViewScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(characterGroupingLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(groupingScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                .addContainerGap())
        );

        fileViewScrollPane.setViewportView(fileViewPanel);

        fileJMenu.setText("File");

        loadFileMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        loadFileMenuItem.setText("Load FIel");
        loadFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadFileMenuItemActionPerformed(evt);
            }
        });
        fileJMenu.add(loadFileMenuItem);

        jMenuBar1.add(fileJMenu);

        editJMenu.setText("Edit");
        jMenuBar1.add(editJMenu);

        curorJMenu.setText("Cursors");

        selectCursor1MenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, 0));
        selectCursor1MenuItem.setText("Select Cursor 1");
        selectCursor1MenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectCursor1MenuItemActionPerformed(evt);
            }
        });
        curorJMenu.add(selectCursor1MenuItem);

        selectCursor2MenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, 0));
        selectCursor2MenuItem.setText("Select Cursor 2");
        selectCursor2MenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectCursor2MenuItemActionPerformed(evt);
            }
        });
        curorJMenu.add(selectCursor2MenuItem);

        nextByteMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_PERIOD, 0));
        nextByteMenuItem.setText("Next Byte");
        nextByteMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextByteMenuItemActionPerformed(evt);
            }
        });
        curorJMenu.add(nextByteMenuItem);

        previousByteMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_COMMA, 0));
        previousByteMenuItem.setText("Previous Byte");
        previousByteMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousByteMenuItemActionPerformed(evt);
            }
        });
        curorJMenu.add(previousByteMenuItem);

        snap2nByteJMenu.setText("2^nth Byte Snapping");

        set2nValueMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_BACK_SLASH, 0));
        set2nValueMenuItem.setText("Set 2^n Value");
        set2nValueMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                set2nValueMenuItemActionPerformed(evt);
            }
        });
        snap2nByteJMenu.add(set2nValueMenuItem);

        nextBytesMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_CLOSE_BRACKET, 0));
        nextBytesMenuItem.setText("Next Bytes");
        nextBytesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextBytesMenuItemActionPerformed(evt);
            }
        });
        snap2nByteJMenu.add(nextBytesMenuItem);

        previousBytesMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_OPEN_BRACKET, 0));
        previousBytesMenuItem.setText("Previous Bytes");
        previousBytesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousBytesMenuItemActionPerformed(evt);
            }
        });
        snap2nByteJMenu.add(previousBytesMenuItem);

        curorJMenu.add(snap2nByteJMenu);

        teleportMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SLASH, 0));
        teleportMenuItem.setText("Teleport Cursor To Byte");
        teleportMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teleportMenuItemActionPerformed(evt);
            }
        });
        curorJMenu.add(teleportMenuItem);

        jMenuBar1.add(curorJMenu);

        viewJMenu.setText("View");

        binaryViewMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        binaryViewMenuItem.setText("Binary");
        binaryViewMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                binaryViewMenuItemActionPerformed(evt);
            }
        });
        viewJMenu.add(binaryViewMenuItem);

        hexViewMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        hexViewMenuItem.setText("Hex");
        hexViewMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hexViewMenuItemActionPerformed(evt);
            }
        });
        viewJMenu.add(hexViewMenuItem);

        charGroupingJMenu.setText("Character Grouping");

        singularMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        singularMenuItem.setText("Singular");
        singularMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                singularMenuItemActionPerformed(evt);
            }
        });
        charGroupingJMenu.add(singularMenuItem);

        grouped2menuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        grouped2menuItem.setText("Grouped 2");
        grouped2menuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grouped2menuItemActionPerformed(evt);
            }
        });
        charGroupingJMenu.add(grouped2menuItem);

        grouped4menuitem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_4, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        grouped4menuitem.setText("Grouped 4");
        grouped4menuitem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grouped4menuitemActionPerformed(evt);
            }
        });
        charGroupingJMenu.add(grouped4menuitem);

        viewJMenu.add(charGroupingJMenu);

        viewByteJMenu.setText("Viewable Bytes");

        setViewAroundCursorMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SLASH, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        setViewAroundCursorMenuItem.setText("Set View Around Cursor");
        setViewAroundCursorMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setViewAroundCursorMenuItemActionPerformed(evt);
            }
        });
        viewByteJMenu.add(setViewAroundCursorMenuItem);

        showAllBytesMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        showAllBytesMenuItem.setText("Show All Bytes");
        showAllBytesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showAllBytesMenuItemActionPerformed(evt);
            }
        });
        viewByteJMenu.add(showAllBytesMenuItem);

        first100BytesMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_HOME, 0));
        first100BytesMenuItem.setText("First 100 bytes");
        first100BytesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                first100BytesMenuItemActionPerformed(evt);
            }
        });
        viewByteJMenu.add(first100BytesMenuItem);

        last100BytesMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_END, 0));
        last100BytesMenuItem.setText("Last 100 Bytes");
        last100BytesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                last100BytesMenuItemActionPerformed(evt);
            }
        });
        viewByteJMenu.add(last100BytesMenuItem);

        plus100BytesMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_PAGE_DOWN, 0));
        plus100BytesMenuItem.setText("+100 Bytes");
        plus100BytesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plus100BytesMenuItemActionPerformed(evt);
            }
        });
        viewByteJMenu.add(plus100BytesMenuItem);

        minus100BytesMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_PAGE_UP, 0));
        minus100BytesMenuItem.setText("-100 Bytes");
        minus100BytesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minus100BytesMenuItemActionPerformed(evt);
            }
        });
        viewByteJMenu.add(minus100BytesMenuItem);

        minIndexJMenu.setText("Min Index");

        plusMinMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ADD, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        plusMinMenuItem.setText("Increment Min Index");
        plusMinMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plusMinMenuItemActionPerformed(evt);
            }
        });
        minIndexJMenu.add(plusMinMenuItem);

        minusMinMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SUBTRACT, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        minusMinMenuItem.setText("Decrement Min Index");
        minusMinMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minusMinMenuItemActionPerformed(evt);
            }
        });
        minIndexJMenu.add(minusMinMenuItem);

        viewByteJMenu.add(minIndexJMenu);

        maxIndexJMenu.setText("Max Index");

        plusMaxMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ADD, 0));
        plusMaxMenuItem.setText("Increment Max Index");
        plusMaxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plusMaxMenuItemActionPerformed(evt);
            }
        });
        maxIndexJMenu.add(plusMaxMenuItem);

        minusMaxMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SUBTRACT, 0));
        minusMaxMenuItem.setText("Decrement Max Index");
        minusMaxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minusMaxMenuItemActionPerformed(evt);
            }
        });
        maxIndexJMenu.add(minusMaxMenuItem);

        viewByteJMenu.add(maxIndexJMenu);

        viewJMenu.add(viewByteJMenu);

        jMenuBar1.add(viewJMenu);

        interpJMenu.setText("Interpretation");

        methodJMenu.setText("Methods");
        interpJMenu.add(methodJMenu);

        openInterpWindowMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        openInterpWindowMenuItem.setText("Open Window");
        openInterpWindowMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openInterpWindowMenuItemActionPerformed(evt);
            }
        });
        interpJMenu.add(openInterpWindowMenuItem);

        jMenuBar1.add(interpJMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(mainControlsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(viewerScrollpane))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(methodPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fileViewScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(mainControlsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(viewerScrollpane, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileViewScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(methodPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bigEdianRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bigEdianRadioActionPerformed
        endianess = Endianess.BIG;
        interpMethod();
    }//GEN-LAST:event_bigEdianRadioActionPerformed

    private void littleEndianRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_littleEndianRadioActionPerformed
        endianess = Endianess.LITTLE;
        interpMethod();
    }//GEN-LAST:event_littleEndianRadioActionPerformed

    private void searchBarJTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBarJTextFieldActionPerformed
        String input = searchBarJTextField.getText();
        search.search(input);
        this.requestFocus();
    }//GEN-LAST:event_searchBarJTextFieldActionPerformed

    private void binViewRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_binViewRadioActionPerformed
        _changeview(View.BIN);
    }//GEN-LAST:event_binViewRadioActionPerformed

    private void hexViewRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hexViewRadioActionPerformed
        _changeview(View.HEX);
    }//GEN-LAST:event_hexViewRadioActionPerformed

    private void singularRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_singularRadioActionPerformed
        _changegrouping(CharGroups.SINGULAR);
    }//GEN-LAST:event_singularRadioActionPerformed

    private void group2RadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_group2RadioActionPerformed
        _changegrouping(CharGroups.DOUBLE);
    }//GEN-LAST:event_group2RadioActionPerformed

    private void group4RadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_group4RadioActionPerformed
        _changegrouping(CharGroups.QUAD);
    }//GEN-LAST:event_group4RadioActionPerformed

    private void byteIndexCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_byteIndexCheckBoxActionPerformed
        converter.showByteIndexes = byteIndexCheckBox.isSelected();
        print();
    }//GEN-LAST:event_byteIndexCheckBoxActionPerformed

    private void selectionCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectionCheckBoxActionPerformed
        converter.showSelection = selectionCheckBox.isSelected();
        print();
    }//GEN-LAST:event_selectionCheckBoxActionPerformed

    private void next2nBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_next2nBtnActionPerformed
        setValue2n();
        _selected2nUp();
    }//GEN-LAST:event_next2nBtnActionPerformed

    private void previous2nBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previous2nBtnActionPerformed
        setValue2n();
        _selected2nDown();
    }//GEN-LAST:event_previous2nBtnActionPerformed

    private void nextByteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextByteBtnActionPerformed
        _incrementSelectedCursor();
    }//GEN-LAST:event_nextByteBtnActionPerformed

    private void previousByteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousByteBtnActionPerformed
        _decrementSelectedCursor();
    }//GEN-LAST:event_previousByteBtnActionPerformed

    private void cursor1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cursor1ActionPerformed
        _selectCursor(start);
    }//GEN-LAST:event_cursor1ActionPerformed

    private void cursor2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cursor2ActionPerformed
        _selectCursor(end);
    }//GEN-LAST:event_cursor2ActionPerformed

    private void openInterpWindowMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openInterpWindowMenuItemActionPerformed
        interpLabelFrame.setVisible(true);
    }//GEN-LAST:event_openInterpWindowMenuItemActionPerformed

    private void binaryViewMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_binaryViewMenuItemActionPerformed
        _changeview(View.BIN);
        binViewRadio.setSelected(true);
    }//GEN-LAST:event_binaryViewMenuItemActionPerformed

    private void hexViewMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hexViewMenuItemActionPerformed
        _changeview(View.HEX);
        hexViewRadio.setSelected(true);
    }//GEN-LAST:event_hexViewMenuItemActionPerformed

    private void singularMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_singularMenuItemActionPerformed
        _changegrouping(CharGroups.SINGULAR);
        singularRadio.setSelected(true);
    }//GEN-LAST:event_singularMenuItemActionPerformed

    private void previousByteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousByteMenuItemActionPerformed
        _decrementSelectedCursor();
    }//GEN-LAST:event_previousByteMenuItemActionPerformed

    private void selectCursor1MenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectCursor1MenuItemActionPerformed
        _selectCursor(start);
        cursor1.setSelected(true);
    }//GEN-LAST:event_selectCursor1MenuItemActionPerformed

    private void selectCursor2MenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectCursor2MenuItemActionPerformed
        _selectCursor(end);
        cursor2.setSelected(true);
    }//GEN-LAST:event_selectCursor2MenuItemActionPerformed

    private void nextByteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextByteMenuItemActionPerformed
        _incrementSelectedCursor();
    }//GEN-LAST:event_nextByteMenuItemActionPerformed

    private void set2nValueMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_set2nValueMenuItemActionPerformed
        setValue2n();
    }//GEN-LAST:event_set2nValueMenuItemActionPerformed

    private void grouped2menuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grouped2menuItemActionPerformed
        _changegrouping(CharGroups.DOUBLE);
        group2Radio.setSelected(true);
    }//GEN-LAST:event_grouped2menuItemActionPerformed

    private void grouped4menuitemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grouped4menuitemActionPerformed
        _changegrouping(CharGroups.QUAD);
        group4Radio.setSelected(true);
    }//GEN-LAST:event_grouped4menuitemActionPerformed

    private void plus100BytesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plus100BytesMenuItemActionPerformed
        if (_setMaxValue(converter.getMaxByteIndex() + 100)) {
            _setMinValue(converter.getMinByteIndex() + 100);
            print();
            byteRangeNote();
        }
    }//GEN-LAST:event_plus100BytesMenuItemActionPerformed

    private void showAllBytesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showAllBytesMenuItemActionPerformed
        if (bytes.length >= 1000 &&
                JOptionPane.showConfirmDialog(this,
                        "This file is large. (" + bytes.length + " bytes). Temporary UI freezing is expected. Continue?")
                == JOptionPane.NO_OPTION
        )
            return;
        _setMinValue(0);
        _setMaxValue(bytes.length);
        print();
        byteRangeNote();
    }//GEN-LAST:event_showAllBytesMenuItemActionPerformed

    private void first100BytesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_first100BytesMenuItemActionPerformed
        _setMinValue(0);
        _setMaxValue(100);
        print();
        byteRangeNote();
    }//GEN-LAST:event_first100BytesMenuItemActionPerformed

    private void last100BytesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_last100BytesMenuItemActionPerformed
        _setMaxValue(bytes.length - 1);
        _setMinValue(bytes.length - 101);
        print();
        byteRangeNote();
    }//GEN-LAST:event_last100BytesMenuItemActionPerformed

    private void minus100BytesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minus100BytesMenuItemActionPerformed
        if (_setMinValue(converter.getMinByteIndex() - 100)) {
            _setMaxValue(converter.getMaxByteIndex() - 100);
            print();
            byteRangeNote();
        }
    }//GEN-LAST:event_minus100BytesMenuItemActionPerformed

    private void plusMinMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plusMinMenuItemActionPerformed
        _setMinValue(converter.getMinByteIndex() + 1);
        print();
        byteRangeNote();
    }//GEN-LAST:event_plusMinMenuItemActionPerformed

    private void minusMinMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minusMinMenuItemActionPerformed
        _setMinValue(converter.getMinByteIndex() - 1);
        print();
        byteRangeNote();
    }//GEN-LAST:event_minusMinMenuItemActionPerformed

    private void minusMaxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minusMaxMenuItemActionPerformed
        _setMaxValue(converter.getMaxByteIndex() - 1);
        print();
        byteRangeNote();
    }//GEN-LAST:event_minusMaxMenuItemActionPerformed

    private void plusMaxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plusMaxMenuItemActionPerformed
        _setMaxValue(converter.getMaxByteIndex() + 1);
        print();
        byteRangeNote();
    }//GEN-LAST:event_plusMaxMenuItemActionPerformed

    private void nextBytesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextBytesMenuItemActionPerformed
        _selected2nUp();
    }//GEN-LAST:event_nextBytesMenuItemActionPerformed

    private void previousBytesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousBytesMenuItemActionPerformed
        _selected2nDown();
    }//GEN-LAST:event_previousBytesMenuItemActionPerformed

    private void teleportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teleportMenuItemActionPerformed
        String num = JOptionPane.showInputDialog(
                this,
                "Teleport " + (selectedCursor == start ? 1 : 2) + " to byte...",
                "Teleport",
                JOptionPane.QUESTION_MESSAGE
        );
        if (num == null)
            return;
        try {
            int byteIndex = Integer.parseInt(num);
            if (byteIndex < 0 || byteIndex >= bytes.length) {
                JOptionPane.showMessageDialog(this, "Too far man.",
                        "Out of bounds", JOptionPane.ERROR_MESSAGE);
                return;
            }
            selectedCursor.setByteIndex(byteIndex);
            note(
                    "Cursor " + (selectedCursor == start? 1 : 2) + " has been sent to byte " +
                            selectedCursor.getByteIndex() + "."
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Thats just not a number",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
        print();
    }//GEN-LAST:event_teleportMenuItemActionPerformed

    private void setViewAroundCursorMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setViewAroundCursorMenuItemActionPerformed
        int newMin = Math.max(0, selectedCursor.getByteIndex() - 100);
        int newMax = Math.min(bytes.length - 1, selectedCursor.getByteIndex() + 100);
        _setMinValue(newMin);
        _setMaxValue(newMax);
        print();
        byteRangeNote();
    }//GEN-LAST:event_setViewAroundCursorMenuItemActionPerformed

    private void loadFileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadFileMenuItemActionPerformed
        File file = FilesUtility.genericFileChooser(this);
        if (file.isDirectory()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ight so like is a folder a file? don't think so.",
                    "Err",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        note("Loading file MIGHT take time");
        load(file);
        note(file.getName() + " was loaded twin");
    }//GEN-LAST:event_loadFileMenuItemActionPerformed

    private void blockViewRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blockViewRadioActionPerformed
        _changeview(View.BLOCKS);
    }//GEN-LAST:event_blockViewRadioActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Window().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton bigEdianRadio;
    private javax.swing.JRadioButton binViewRadio;
    private javax.swing.JMenuItem binaryViewMenuItem;
    private javax.swing.JRadioButton blockViewRadio;
    private javax.swing.JCheckBox byteIndexCheckBox;
    private javax.swing.JMenu charGroupingJMenu;
    private javax.swing.JLabel characterGroupingLabel;
    private javax.swing.JMenu curorJMenu;
    private javax.swing.JSpinner curs1IndexSpinner;
    private javax.swing.JSpinner curs2IndexSpinner;
    private javax.swing.JRadioButton cursor1;
    private javax.swing.JRadioButton cursor2;
    private javax.swing.ButtonGroup cursorBtnGroup;
    private javax.swing.JMenu editJMenu;
    private javax.swing.ButtonGroup endianessBtnGroup;
    private javax.swing.JLabel endianessLabel;
    private javax.swing.JMenu fileJMenu;
    private javax.swing.ButtonGroup fileViewBtnGrp;
    private javax.swing.JLabel fileViewLabel;
    private javax.swing.JPanel fileViewPanel;
    private javax.swing.JScrollPane fileViewScrollPane;
    private javax.swing.JMenuItem first100BytesMenuItem;
    private javax.swing.JRadioButton group2Radio;
    private javax.swing.JRadioButton group4Radio;
    private javax.swing.JMenuItem grouped2menuItem;
    private javax.swing.JMenuItem grouped4menuitem;
    private javax.swing.ButtonGroup groupingBtnGrp;
    private javax.swing.JPanel groupingJPanel;
    private javax.swing.JScrollPane groupingScrollPane;
    private javax.swing.JMenuItem hexViewMenuItem;
    private javax.swing.JRadioButton hexViewRadio;
    private javax.swing.JPanel innerViewJPanel;
    private javax.swing.JScrollPane innerViewScrollPane;
    private javax.swing.JMenu interpJMenu;
    private javax.swing.ButtonGroup interpMethodBtnGroup;
    private javax.swing.JLabel interpMethodLabl;
    private javax.swing.JScrollPane interpMethodScrollPane;
    private javax.swing.JPanel interpMethodsRadioPanel;
    private javax.swing.JLabel interpretedLabel;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem last100BytesMenuItem;
    private javax.swing.JButton listInterpBtn;
    private javax.swing.JRadioButton littleEndianRadio;
    private javax.swing.JMenuItem loadFileMenuItem;
    private javax.swing.JButton loadInterpBtn;
    private javax.swing.JPanel mainControlsPanel;
    private javax.swing.JSpinner maxByteIndexSpinner;
    private javax.swing.JMenu maxIndexJMenu;
    private javax.swing.JLabel maxUbdexLabel;
    private javax.swing.JMenu methodJMenu;
    private javax.swing.JPanel methodPanel;
    private javax.swing.JSpinner minByteIndexSpinner;
    private javax.swing.JMenu minIndexJMenu;
    private javax.swing.JLabel minIndexLabel;
    private javax.swing.JMenuItem minus100BytesMenuItem;
    private javax.swing.JMenuItem minusMaxMenuItem;
    private javax.swing.JMenuItem minusMinMenuItem;
    private javax.swing.JButton next2nBtn;
    private javax.swing.JButton nextByteBtn;
    private javax.swing.JMenuItem nextByteMenuItem;
    private javax.swing.JMenuItem nextBytesMenuItem;
    private javax.swing.JLabel noteLabel;
    private javax.swing.JMenuItem openInterpWindowMenuItem;
    private javax.swing.JMenuItem plus100BytesMenuItem;
    private javax.swing.JMenuItem plusMaxMenuItem;
    private javax.swing.JMenuItem plusMinMenuItem;
    private javax.swing.JButton ponderInterpBtn;
    private javax.swing.JButton previous2nBtn;
    private javax.swing.JButton previousByteBtn;
    private javax.swing.JMenuItem previousByteMenuItem;
    private javax.swing.JMenuItem previousBytesMenuItem;
    private javax.swing.JButton saveInterpBtn;
    private javax.swing.JTextField searchBarJTextField;
    private javax.swing.JMenuItem selectCursor1MenuItem;
    private javax.swing.JMenuItem selectCursor2MenuItem;
    private javax.swing.JCheckBox selectionCheckBox;
    private javax.swing.JSeparator seperatorLeft;
    private javax.swing.JSeparator seperatorRight;
    private javax.swing.JMenuItem set2nValueMenuItem;
    private javax.swing.JMenuItem setViewAroundCursorMenuItem;
    private javax.swing.JMenuItem showAllBytesMenuItem;
    private javax.swing.JMenuItem singularMenuItem;
    private javax.swing.JRadioButton singularRadio;
    private javax.swing.JMenu snap2nByteJMenu;
    private javax.swing.JMenuItem teleportMenuItem;
    private javax.swing.JMenu viewByteJMenu;
    private javax.swing.JMenu viewJMenu;
    private javax.swing.JLabel viewerLabel;
    private javax.swing.JScrollPane viewerScrollpane;
    // End of variables declaration//GEN-END:variables
}
