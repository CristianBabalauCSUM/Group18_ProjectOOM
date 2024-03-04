package UI_Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class HeaderPanel extends JPanel{
    JPanel panel;
    private static final int WIDTH = 300;


    public HeaderPanel(String label){
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
        JLabel lblRegister = new JLabel(label);
        lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
        lblRegister.setForeground(Color.WHITE); // Set the text color to white
        this.add(lblRegister);
        this.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height

    }


    public JPanel getPanel() {
        return panel;
    }
}
