package com.vatsalkhanka.mednames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Window extends JFrame {

    //Define GUI elements
    JTextField inputArea;
    JPanel panel;
    JTextArea results;
    JScrollPane scrollPane;
    Timer debounceTimer;
    JButton addMedicine, searchMode;

    public Window() {
        initGUI();
    }

    private void initGUI() {
        //JFrame setup
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0,0,711,400);
        setTitle("MedNames - by Vatsal Khanka");
        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("icon.png")).getImage());

        //Setting up GUI elements
        inputArea = new JTextField();
        inputArea.setVisible(true);
        inputArea.setColumns(30);
        getContentPane().add(inputArea);

        results = new JTextArea("Results display here!");
        results.setColumns(50);
        results.setRows(20);
        results.setEditable(false);
        results.setOpaque(false);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bgImage = new ImageIcon(getClass().getClassLoader().getResource("bg.png"));
                g.drawImage(bgImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        addMedicine = new JButton();
        addMedicine.setText("Add new");
        addMedicine.setSize(100, 25);
        addMedicine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField med = new JTextField();
                JTextField salt = new JTextField();
                JLabel info = new JLabel("If there are multiple salts in the medicine, please separate them by commas");
                Object[] message = {
                        "Brand Name:", med,
                        "Generic Salts:", salt, info
                };

                int option = JOptionPane.showConfirmDialog(null, message, "Enter the brand and generic names", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    MedNames.addMed(med.getText(), salt.getText());
                }
            }
        });

        panel.add(addMedicine, FlowLayout.LEFT);

        panel.add(inputArea);

        searchMode = new JButton();
        searchMode.setText("Generic");
        searchMode.setSize(100, 25);
        searchMode.setAlignmentY(0);
        panel.add(searchMode);

        searchMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch(MedNames.SEARCH_MODE){
                    case MedNames.BRAND_TO_GEN:
                        MedNames.SEARCH_MODE = MedNames.GEN_TO_BRAND;
                        searchMode.setText("Branded");
                        break;

                    case MedNames.GEN_TO_BRAND:
                        MedNames.SEARCH_MODE = MedNames.BRAND_TO_GEN;
                        searchMode.setText("Generic");
                        break;
                }
            }
        });

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        scrollPane = new JScrollPane(results);
        getContentPane().add(scrollPane);
        results.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        panel.add(scrollPane);

        ((FlowLayout) panel.getLayout()).setHgap(65);

        add(panel);
        setVisible(true);

        debounceTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int caretPosition = results.getCaretPosition();
                if(!inputArea.getText().isEmpty()) {
                    results.setText(MedNames.searchMedicine(inputArea.getText()));
                    results.setCaretPosition(caretPosition);
                } else results.setText("Results display here!");
                stopDebounce();
            }
        });

        // Reset the timer on every keystroke
        inputArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                debounceTimer.restart();  // Restart the timer every time a key is typed
            }
        });
    }

    void stopDebounce() {
        debounceTimer.stop();
    }

}
