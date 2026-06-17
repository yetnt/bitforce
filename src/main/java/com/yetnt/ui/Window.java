/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.yetnt.ui;

import com.yetnt.JLabelRichText;
import com.yetnt.api.Cursor;
import com.yetnt.api.Grouping;
import com.yetnt.api.View;
import com.yetnt.api.Converter;
import com.yetnt.methods.Endianess;
import com.yetnt.methods.InterpMethod;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

    /**
     * Creates new form Window
     */
    public Window() {
        initComponents();
        this.bytes = new byte[0];
        start = new Cursor(0, curs1IndexSpinner);
        end = new Cursor(0, curs2IndexSpinner);
        spinnerEvents();
        print();
    }

    public Window(byte[] bytes) {
        initComponents();
        this.bytes = bytes;
        start = new Cursor(bytes.length, curs1IndexSpinner);
        end = new Cursor(bytes.length, curs2IndexSpinner);
        spinnerEvents();
        print();
    }

    private void interpMethod() {
        String str =
                interpMethod.interpret(
                        selectedBytes(), endianess);
        interpretedLabel.setText(str);
        interpLabelFrame.setString(str);
    }

    private void spinnerEvents() {
        curs1IndexSpinner.addChangeListener(
                e -> {
                    start.setByteIndex((Integer) curs1IndexSpinner.getValue());
                    print();
                    interpMethod();
                }
        );
        curs2IndexSpinner.addChangeListener(
                e -> {
                    end.setByteIndex((Integer) curs2IndexSpinner.getValue());
                    print();
                    interpMethod();
                }
        );
        maxByteIndexSpinner.setModel(
                new SpinnerNumberModel(
                        converter.getMaxByteIndex(),
                        0,
                        bytes.length,
                        1
                )
        );
        maxByteViewableSpinner.setModel(
                new SpinnerNumberModel(
                        converter.getMaxViewIndex(),
                        2,
                        bytes.length,
                        1
                )
        );
        maxByteIndexSpinner.addChangeListener(
                e -> {
                    converter.setMaxByteIndex((Integer) maxByteIndexSpinner.getValue());
                    print();
                }
        );
        maxByteViewableSpinner.addChangeListener(
                e -> {
                    converter.setMaxViewIndex((Integer) maxByteViewableSpinner.getValue());
                    print();
                }
        );

        interpretedLabel.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        interpLabelFrame.setVisible(true);
                        System.out.println("h");
                    }
                }
        );

        interpMethod = methods.getFirst();
        methods.getFirst().getRadioButton().setSelected(true);
        for (InterpMethod method : methods) {
            interpMethodsRadioPanel.add(method.getRadioButton());
            interpMethodBtnGroup.add(method.getRadioButton());
            method.getRadioButton().addActionListener(
                    e -> {
                        interpretedLabel.setText(
                                method.interpret(selectedBytes(), endianess)
                        );
                        interpMethod = method;
                    }
            );
        }
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
        jSeparator1 = new javax.swing.JSeparator();
        interpretedLabel = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        maxByteViewableSpinner = new javax.swing.JSpinner();
        methodPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        interpMethodsRadioPanel = new javax.swing.JPanel();
        littleEndianRadio = new javax.swing.JRadioButton();
        bigEdianRadio = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        fileViewScrollPane = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        bitViewRadio = new javax.swing.JRadioButton();
        byteViewRadio1 = new javax.swing.JRadioButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        singularRadio = new javax.swing.JRadioButton();
        group2Radio = new javax.swing.JRadioButton();
        group4Radio = new javax.swing.JRadioButton();
        jLabel8 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        viewerLabel.setFont(new java.awt.Font("Fira Code Medium", 0, 18)); // NOI18N
        viewerLabel.setText("Our very funny colour coded via cursor and byte seperated input will go into here.");
        viewerScrollpane.setViewportView(viewerLabel);

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator1.setOpaque(true);

        interpretedLabel.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        interpretedLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        interpretedLabel.setText("jLabel4");

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator2.setOpaque(true);

        nextByteBtn.setText("next byte");

        previousByteBtn.setText("pre byte");

        next2nBtn.setText("+2n");

        previous2nBtn.setText("-2n");

        cursorBtnGroup.add(cursor1);
        cursor1.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        cursor1.setSelected(true);
        cursor1.setText("Cursor 1");

        cursorBtnGroup.add(cursor2);
        cursor2.setFont(cursor1.getFont());
        cursor2.setText("Cursor 2");

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

        jLabel1.setText("Max Index");

        jLabel2.setText("Max Viewable");

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
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                        .addComponent(maxByteViewableSpinner))
                    .addGroup(mainControlsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        mainControlsPanelLayout.setVerticalGroup(
            mainControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainControlsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainControlsPanelLayout.createSequentialGroup()
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
                            .addGroup(mainControlsPanelLayout.createSequentialGroup()
                                .addComponent(interpretedLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(mainControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(saveInterpBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(listInterpBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(loadInterpBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ponderInterpBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainControlsPanelLayout.createSequentialGroup()
                        .addGroup(mainControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainControlsPanelLayout.createSequentialGroup()
                                .addGap(0, 7, Short.MAX_VALUE)
                                .addGroup(mainControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(nextByteBtn)
                                    .addComponent(next2nBtn)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(mainControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(previousByteBtn)
                                    .addComponent(previous2nBtn)
                                    .addComponent(maxByteIndexSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(maxByteViewableSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15, 15, 15)))
                        .addContainerGap())))
        );

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Interp Methods");

        jTextField1.setText("(Search for a method)");
        jTextField1.setToolTipText("(Search for a method)");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        interpMethodsRadioPanel.setLayout(new java.awt.GridLayout(0, 1));
        jScrollPane2.setViewportView(interpMethodsRadioPanel);

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

        jLabel5.setText("Endianess:");

        javax.swing.GroupLayout methodPanelLayout = new javax.swing.GroupLayout(methodPanel);
        methodPanel.setLayout(methodPanelLayout);
        methodPanelLayout.setHorizontalGroup(
            methodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(methodPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(methodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
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
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(methodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(littleEndianRadio)
                    .addComponent(bigEdianRadio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        fileViewScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        jLabel7.setText("File View");

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        fileViewBtnGrp.add(bitViewRadio);
        bitViewRadio.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        bitViewRadio.setSelected(true);
        bitViewRadio.setText("binary");
        bitViewRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bitViewRadioActionPerformed(evt);
            }
        });

        fileViewBtnGrp.add(byteViewRadio1);
        byteViewRadio1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        byteViewRadio1.setText("hex");
        byteViewRadio1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                byteViewRadio1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bitViewRadio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(byteViewRadio1, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bitViewRadio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(byteViewRadio1)
                .addContainerGap(413, Short.MAX_VALUE))
        );

        jScrollPane4.setViewportView(jPanel4);

        jScrollPane5.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane5.setToolTipText("");

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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(singularRadio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(group2Radio, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(group4Radio, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(singularRadio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(group2Radio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(group4Radio)
                .addContainerGap(386, Short.MAX_VALUE))
        );

        jScrollPane5.setViewportView(jPanel5);

        jLabel8.setText("Character Grouping");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                .addContainerGap())
        );

        fileViewScrollPane.setViewportView(jPanel3);

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(mainControlsPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(viewerScrollpane, javax.swing.GroupLayout.Alignment.LEADING))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(viewerScrollpane, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileViewScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
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

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void bitViewRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bitViewRadioActionPerformed
        converter.view = View.BIN;
        print();
    }//GEN-LAST:event_bitViewRadioActionPerformed

    private void byteViewRadio1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_byteViewRadio1ActionPerformed
        converter.view = View.HEX;
        print();
    }//GEN-LAST:event_byteViewRadio1ActionPerformed

    private void singularRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_singularRadioActionPerformed
        converter.grouping = Grouping.SINGULAR;
        print();
    }//GEN-LAST:event_singularRadioActionPerformed

    private void group2RadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_group2RadioActionPerformed
        converter.grouping = Grouping.DOUBLE;
        print();
    }//GEN-LAST:event_group2RadioActionPerformed

    private void group4RadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_group4RadioActionPerformed
        converter.grouping = Grouping.QUAD;
        print();
    }//GEN-LAST:event_group4RadioActionPerformed

    private void byteIndexCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_byteIndexCheckBoxActionPerformed
        converter.showByteIndexes = byteIndexCheckBox.isSelected();
        print();
    }//GEN-LAST:event_byteIndexCheckBoxActionPerformed

    private void selectionCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectionCheckBoxActionPerformed
        converter.showSelection = selectionCheckBox.isSelected();
        print();
    }//GEN-LAST:event_selectionCheckBoxActionPerformed

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
    private javax.swing.JRadioButton bitViewRadio;
    private javax.swing.JCheckBox byteIndexCheckBox;
    private javax.swing.JRadioButton byteViewRadio1;
    private javax.swing.JSpinner curs1IndexSpinner;
    private javax.swing.JSpinner curs2IndexSpinner;
    private javax.swing.JRadioButton cursor1;
    private javax.swing.JRadioButton cursor2;
    private javax.swing.ButtonGroup cursorBtnGroup;
    private javax.swing.ButtonGroup endianessBtnGroup;
    private javax.swing.ButtonGroup fileViewBtnGrp;
    private javax.swing.JScrollPane fileViewScrollPane;
    private javax.swing.JRadioButton group2Radio;
    private javax.swing.JRadioButton group4Radio;
    private javax.swing.ButtonGroup groupingBtnGrp;
    private javax.swing.ButtonGroup interpMethodBtnGroup;
    private javax.swing.JPanel interpMethodsRadioPanel;
    private javax.swing.JLabel interpretedLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton listInterpBtn;
    private javax.swing.JRadioButton littleEndianRadio;
    private javax.swing.JButton loadInterpBtn;
    private javax.swing.JPanel mainControlsPanel;
    private javax.swing.JSpinner maxByteIndexSpinner;
    private javax.swing.JSpinner maxByteViewableSpinner;
    private javax.swing.JPanel methodPanel;
    private javax.swing.JButton next2nBtn;
    private javax.swing.JButton nextByteBtn;
    private javax.swing.JButton ponderInterpBtn;
    private javax.swing.JButton previous2nBtn;
    private javax.swing.JButton previousByteBtn;
    private javax.swing.JButton saveInterpBtn;
    private javax.swing.JCheckBox selectionCheckBox;
    private javax.swing.JRadioButton singularRadio;
    private javax.swing.JLabel viewerLabel;
    private javax.swing.JScrollPane viewerScrollpane;
    // End of variables declaration//GEN-END:variables
}
