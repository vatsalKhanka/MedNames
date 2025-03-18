package com.vatsalkhanka.mednames;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Window extends JFrame {

    JTextField inputArea;
    JPanel panel;
    JTextArea results;
    JScrollPane scrollPane;

    public Window() {
        initGUI();
    }

    private void initGUI() {
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0,0,800,450);
        setTitle("MedNames");

        inputArea = new JTextField();
        inputArea.setVisible(true);
        inputArea.setColumns(50);
        getContentPane().add(inputArea);

        results = new JTextArea("Results display here!");
        results.setColumns(75);
        results.setRows(20);
        results.setEditable(false);

        panel = new JPanel();
        panel.add(inputArea);

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        scrollPane = new JScrollPane(results);
        getContentPane().add(scrollPane);
        panel.add(scrollPane);

        add(panel);
        setVisible(true);

        InputMap inputMap = this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getRootPane().getActionMap();

        Action enterAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                results.setText(MedNames.searchMedicine(inputArea.getText()));
            }
        };

        inputMap.put(KeyStroke.getKeyStroke("ENTER"), "enterPressed");
        actionMap.put("enterPressed", enterAction);

    }


}
