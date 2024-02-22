package UI_Components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class RegisterPanel {
    private static JPanel panel;
    private JPanel headerPanel;  
    private JPanel fieldsPanel;
    private JTextField txtUsername;
    private JTextField txtPassword;
    private JTextField txtBio;


    private void onRegisterClicked(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String bio = txtBio.getText();

        if (doesUsernameExist(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists. Please choose a different username.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            saveCredentials(username, password, bio);
            handleProfilePictureUpload();
            dispose();
    
            // Open the SignInUI frame
            SwingUtilities.invokeLater(() -> {
                SignInUI signInFrame = new SignInUI();
                signInFrame.setVisible(true);
            });
        }
    }
    

    public RegisterPanel(JPanel HeaderPanel, JPanel FieldsPanel){
        headerPanel = HeaderPanel;
        fieldsPanel = FieldsPanel;  

        JButton btnRegister = new JButton("Register");
        btnRegister.addActionListener(this::onRegisterClicked);
        btnRegister.setBackground(new Color(255, 90, 95)); // Use a red color that matches the mockup
        btnRegister.setForeground(Color.BLACK); // Set the text color to black
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
        JPanel registerPanel = new JPanel(new BorderLayout()); // Panel to contain the register button
        registerPanel.setBackground(Color.WHITE); // Background for the panel
        registerPanel.add(btnRegister, BorderLayout.CENTER);

        // Adding components to the frame

         // Adding the sign in button to the register panel or another suitable panel

        btnSignIn = new JButton("Already have an account? Sign In");
        btnSignIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSignInUI();
            }
        });
        registerPanel.add(btnSignIn, BorderLayout.SOUTH);
    }
    public JPanel getPanel(){
        return panel;
    }
}
