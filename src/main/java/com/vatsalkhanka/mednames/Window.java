package com.vatsalkhanka.mednames;

import javax.swing.*;

public class Window {

    JFrame frame;
    JTextField inputArea;

    public Window() {
        initGUI();
    }

    private void initGUI() {
        frame = new JFrame();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(0,0,800,450);

        inputArea = new JTextField();
        inputArea.setVisible(true);
        frame.getContentPane().add(inputArea);
    }

    public String getMedicineInput() {
        return inputArea.getText();
    }

}
