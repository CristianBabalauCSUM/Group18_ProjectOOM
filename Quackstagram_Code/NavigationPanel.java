import java.awt.Color;
import java.awt.Frame;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;



public class NavigationPanel extends JPanel{

    private Frame frame;
    private static final int NAV_ICON_SIZE = 20; // Size for navigation icons

    private JButton createIconButton(String iconPath, String buttonType) {
        ImageIcon iconOriginal = new ImageIcon(iconPath);
        Image iconScaled = iconOriginal.getImage().getScaledInstance(NAV_ICON_SIZE, NAV_ICON_SIZE, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(iconScaled));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
    
        // Define actions based on button type
        if ("home".equals(buttonType)) {
            button.addActionListener(e -> openHomeUI());
        } else if ("profile".equals(buttonType)) {
            button.addActionListener(e -> openProfileUI());
        } else if ("notification".equals(buttonType)) {
            button.addActionListener(e -> notificationsUI());
        } else if ("explore".equals(buttonType)) {
            button.addActionListener(e -> exploreUI());
        } else if ("add".equals(buttonType)) {
            button.addActionListener(e -> ImageUploadUI());
        }
        return button;
    
        
    }

    public  NavigationPanel(Frame frame){
        this.frame = frame;

        this.setBackground(new Color(249, 249, 249));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    
        this.add(createIconButton("img/icons/home.png", "home"));
        this.add(Box.createHorizontalGlue());
        this.add(createIconButton("img/icons/search.png","explore"));
        this.add(Box.createHorizontalGlue());
        this.add(createIconButton("img/icons/add.png","add"));
        this.add(Box.createHorizontalGlue());
        this.add(createIconButton("img/icons/heart.png","notification"));
        this.add(Box.createHorizontalGlue());
        this.add(createIconButton("img/icons/profile.png", "profile"));
    }      
    

    private void openProfileUI() {
       // Open InstagramProfileUI frame
        frame.dispose();
        String loggedInUsername = "";

        // Read the logged-in user's username from users.txt
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "users.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                loggedInUsername = line.split(":")[0].trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        User user = new User(loggedInUsername);
        InstagramProfileUI profileUI = new InstagramProfileUI(user);
        profileUI.setVisible(true);
    }
 
    private void notificationsUI() {
        // Open InstagramProfileUI frame
        frame.dispose();
        NotificationsUI notificationsUI = new NotificationsUI();
        notificationsUI.setVisible(true);
    }
 
    private void openHomeUI() {
        // Open InstagramProfileUI frame
        frame.dispose();
        QuakstagramHomeUI homeUI = new QuakstagramHomeUI();
        homeUI.setVisible(true);
    }

    private void exploreUI() {
        // Open InstagramProfileUI frame
        frame.dispose();
        ExploreUI explore = new ExploreUI();
        explore.setVisible(true);
    }

    private void ImageUploadUI() {
        // Open InstagramProfileUI frame
        frame.dispose();
        ImageUploadUI upload = new ImageUploadUI();
        upload.setVisible(true);
    }

}
