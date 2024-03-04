import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import javax.swing.SwingConstants;

public class HeaderPanelProfileUI extends JPanel{
    private static final int PROFILE_IMAGE_SIZE = 80; // Adjusted size for the profile image to match UI
    private User user;
    private String loggedInUser;
    private boolean isCurrentUserLogged;


    private void handleFollowAction(String usernameToFollow) {
        Path followingFilePath = Paths.get("data", "following.txt");
        Path usersFilePath = Paths.get("data", "users.txt");
        String currentUserUsername = "";

        try {
            // Read the current user's username from users.txt
            try (BufferedReader reader = Files.newBufferedReader(usersFilePath)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                currentUserUsername = parts[0];
                }
            }

            System.out.println("Real user is "+currentUserUsername);
            // If currentUserUsername is not empty, process following.txt
            if (!currentUserUsername.isEmpty()) {
                boolean found = false;
                StringBuilder newContent = new StringBuilder();

                // Read and process following.txt
                if (Files.exists(followingFilePath)) {
                    try (BufferedReader reader = Files.newBufferedReader(followingFilePath)) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.split(":");
                            if (parts[0].trim().equals(currentUserUsername)) {
                                found = true;
                                if (!line.contains(usernameToFollow)) {
                                    line = line.concat(line.endsWith(":") ? "" : "; ").concat(usernameToFollow);
                                }
                            }
                            newContent.append(line).append("\n");
                        }
                    }
                }

                // If the current user was not found in following.txt, add them
                if (!found) {
                    newContent.append(currentUserUsername).append(": ").append(usernameToFollow).append("\n");
                }

                // Write the updated content back to following.txt
                try (BufferedWriter writer = Files.newBufferedWriter(followingFilePath)) {
                    writer.write(newContent.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private JButton generateFollowButton(){

        JButton followButton;

        if (this.isCurrentUserLogged) {
            followButton = new JButton("Edit Profile");
        } else {
            followButton = new JButton("Follow");
    
            // Check if the current user is already being followed by the logged-in user
            Path followingFilePath = Paths.get("data", "following.txt");
    
            try (BufferedReader reader = Files.newBufferedReader(followingFilePath)) {
                
                String line;

                while ((line = reader.readLine()) != null) {

                    String[] parts = line.split(":");

                    if (parts[0].trim().equals(this.loggedInUser)) {
                        String[] followedUsers = parts[1].split(";");
                        for (String followedUser : followedUsers) {

                            if (followedUser.trim().equals(this.user.getUsername())) {
                                followButton.setText("Following");
                                break;
                            }

                        }
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            followButton.addActionListener(e -> {
                handleFollowAction(this.user.getUsername());
                followButton.setText("Following");
            });
        }
        
            followButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            followButton.setFont(new Font("Arial", Font.BOLD, 12));
            followButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, followButton.getMinimumSize().height)); // Make the button fill the horizontal space
            followButton.setBackground(new Color(225, 228, 232)); // A soft, appealing color that complements the UI
            followButton.setForeground(Color.BLACK);
            followButton.setOpaque(true);
            followButton.setBorderPainted(false);
            followButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add some vertical padding
        
            return followButton;
    }

    private JPanel getStatsPanel(){
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        statsPanel.setBackground(new Color(249, 249, 249));
        System.out.println("Number of posts for this user"+this.user.getPostsCount());
        statsPanel.add(createStatLabel(Integer.toString(this.user.getPostsCount()) , "Posts"));
        statsPanel.add(createStatLabel(Integer.toString(this.user.getFollowersCount()), "Followers"));
        statsPanel.add(createStatLabel(Integer.toString(this.user.getFollowingCount()), "Following"));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0)); // Add some vertical padding
        return statsPanel;
    }
    
    private JLabel getProfileIcon(){
        ImageIcon profileIcon = new ImageIcon(new ImageIcon("img/storage/profile/"+this.user.getUsername()+".png").getImage().getScaledInstance(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE, Image.SCALE_SMOOTH));
        JLabel profileImage = new JLabel(profileIcon);
        profileImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return profileImage;
    }
    
    private JLabel createStatLabel(String number, String text) {
        JLabel label = new JLabel("<html><div style='text-align: center;'>" + number + "<br/>" + text + "</div></html>", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Color.BLACK);
        return label;
    }
    
    private void setLoggedUser(){
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "users.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                this.loggedInUser = line.split(":")[0].trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setCurrentUserPage(){

        boolean isCurrentUser = false;
        String loggedInUsername = "";

        // Read the logged-in user's username from users.txt
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "users.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                loggedInUsername = line.split(":")[0].trim();
                isCurrentUser = loggedInUsername.equals(this.user.getUsername());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.isCurrentUserLogged = isCurrentUser;
    }

     public HeaderPanelProfileUI(User user){
        this.user = user;
        setLoggedUser();


        setCurrentUserPage();
    
    
    
       // Header Panel
        /* 
        try (Stream<String> lines = Files.lines(Paths.get("data", "users.txt"))) {
            isCurrentUser = lines.anyMatch(line -> line.startsWith(currentUser.getUsername() + ":"));
        } catch (IOException e) {
            e.printStackTrace();  // Log or handle the exception as appropriate
        }
        */

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.GRAY);
        
        // Top Part of the Header (Profile Image, Stats, Follow Button)

        JPanel topHeaderPanel = new JPanel(new BorderLayout(10, 0));
        topHeaderPanel.setBackground(new Color(249, 249, 249));

        // Profile image
        topHeaderPanel.add(getProfileIcon(), BorderLayout.WEST);

        // Stats Panel
        JPanel statsPanel = getStatsPanel();
        
        // Follow Button
        // Follow or Edit Profile Button
        // followButton.addActionListener(e -> handleFollowAction(currentUser.getUsername()));
        JButton followButton = generateFollowButton();
        
        // Add Stats and Follow Button to a combined Panel
        JPanel statsFollowPanel = new JPanel();
        statsFollowPanel.setLayout(new BoxLayout(statsFollowPanel, BoxLayout.Y_AXIS));
        statsFollowPanel.add(statsPanel);
        statsFollowPanel.add(followButton);
        topHeaderPanel.add(statsFollowPanel, BorderLayout.CENTER);

        this.add(topHeaderPanel);

     // Profile Name and Bio Panel
        JPanel profileNameAndBioPanel = new JPanel();
        profileNameAndBioPanel.setLayout(new BorderLayout());
        profileNameAndBioPanel.setBackground(new Color(249, 249, 249));

        JLabel profileNameLabel = new JLabel(this.user.getUsername());
        profileNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        profileNameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); // Padding on the sides

        JTextArea profileBio = new JTextArea(this.user.getBio());
        System.out.println("This is the bio "+this.user.getUsername());
        profileBio.setEditable(false);
        profileBio.setFont(new Font("Arial", Font.PLAIN, 12));
        profileBio.setBackground(new Color(249, 249, 249));
        profileBio.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10)); // Padding on the sides

        profileNameAndBioPanel.add(profileNameLabel, BorderLayout.NORTH);
        profileNameAndBioPanel.add(profileBio, BorderLayout.CENTER);

        this.add(profileNameAndBioPanel);
    }
}
