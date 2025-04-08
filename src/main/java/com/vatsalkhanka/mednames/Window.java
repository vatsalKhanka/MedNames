package com.vatsalkhanka.mednames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Window extends JFrame {

    JTextField inputArea;
    JPanel panel;
    JTextArea results;
    JScrollPane scrollPane;
    Timer debounceTimer;

    public Window() {
        initGUI();
    }

    private void initGUI() {
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0,0,711,400);
        setTitle("MedNames");
        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("icon.png")).getImage());

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

        panel.add(inputArea);

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        scrollPane = new JScrollPane(results);
        getContentPane().add(scrollPane);
        results.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        panel.add(scrollPane);

        add(panel);
        setVisible(true);

        debounceTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int caretPosition = results.getCaretPosition();
                results.setText(MedNames.searchMedicine(inputArea.getText()));
                results.setCaretPosition(caretPosition);
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
