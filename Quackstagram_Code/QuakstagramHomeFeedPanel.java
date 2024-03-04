import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import java.util.ArrayList;

public class QuakstagramHomeFeedPanel {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;
    private static final int IMAGE_WIDTH = WIDTH - 100; // Width for the image posts
    private static final int IMAGE_HEIGHT = 150; // Height for the image posts

    private JFrame frame;

    private static final Color LIKE_BUTTON_COLOR_INACTIVE = new Color(100,100,100);
    private static final Color LIKE_BUTTON_COLOR_ACTIVE = new Color(200,40,30);
    JScrollPane scrollPane;

    private void fillFeedPage(JPanel panel, User user){
        PostsManager postsManager = new PostsManager();

        try {
            // get the list of all posts from the logged in user
            List<Post> posts = postsManager.getInterestingPostsForUser(user);

            for (Post post:
                 posts) {


                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
                itemPanel.setBackground(Color.WHITE); // Set the background color for the item panel
                itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                itemPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel nameLabel = new JLabel(post.getUsername());
                nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                // Crop the image to the fixed size
                JLabel imageLabel = new JLabel();
                imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                imageLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
                imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border to image label

                try {
                    BufferedImage originalImage = ImageIO.read(new File(post.getPhotoPath()));
                    BufferedImage croppedImage = originalImage.getSubimage(0, 0, Math.min(originalImage.getWidth(), IMAGE_WIDTH), Math.min(originalImage.getHeight(), IMAGE_HEIGHT));
                    ImageIcon imageIcon = new ImageIcon(croppedImage);
                    imageLabel.setIcon(imageIcon);
                } catch (IOException ex) {
                    // Handle exception: Image file not found or reading error
                    imageLabel.setText("Image not found");
                }

                JLabel descriptionLabel = new JLabel(post.getCaption());
                descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel likesLabel = new JLabel(post.getLikeCount().toString());
                likesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JButton likeButton = new JButton("❤");
                likeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
                likeButton.setBackground(LIKE_BUTTON_COLOR_INACTIVE); // Set the background color for the like button
                likeButton.setOpaque(true);
                likeButton.setBorderPainted(false); // Remove border


//                likeButton.addActionListener(new ActionListener() {
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//                        handleLikeAction(imageId, likesLabel);
//                    }
//                });
//
                itemPanel.add(nameLabel);
                itemPanel.add(imageLabel);
                itemPanel.add(descriptionLabel);
                itemPanel.add(likesLabel);
                itemPanel.add(likeButton);

                panel.add(itemPanel);

                System.out.println("Trying to add a post" + post.toString());

                // Make the image clickable

//                imageLabel.addMouseListener(new MouseAdapter() {
//                    @Override
//                    public void mouseClicked(MouseEvent e) {
//                        displayImage(postData); // Call a method to switch to the image view
//                    }
//                });


                // Grey spacing panel
                JPanel spacingPanel = new JPanel();
                spacingPanel.setPreferredSize(new Dimension(WIDTH-10, 5)); // Set the height for spacing
                spacingPanel.setBackground(new Color(230, 230, 230)); // Grey color for spacing
                panel.add(spacingPanel);
            }

        }catch (Exception e){
            System.out.println("Couldn't add a post");
            e.printStackTrace();
        }
        //return panel;
    }
    public QuakstagramHomeFeedPanel(User user, JFrame frame){
        // Content Scroll Panel
        this.frame = frame;

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Vertical box layout
        fillFeedPage(contentPanel, user);

        this.scrollPane = new JScrollPane(contentPanel);
        this.scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Never allow horizontal scrolling

        //this.scrollPane.add(contentPanel);
        // Set up the home panel
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    }

    private void displayImage(ImageIcon imageIcon, String imagePath) {
        //remove the exploreUi and initialize a new one
        this.frame.dispose();
        JFrame displayImage = new ImageDisplayUI(imageIcon, imagePath);
        displayImage.setVisible(true);
    }


    private void handleLikeAction(String imageId, JLabel likesLabel) {
        Path detailsPath = Paths.get("img", "image_details.txt");
        StringBuilder newContent = new StringBuilder();
        boolean updated = false;
        String currentUser = "";
        String imageOwner = "";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Retrieve the current user from users.txt
        try (BufferedReader userReader = Files.newBufferedReader(Paths.get("data", "users.txt"))) {
            String line = userReader.readLine();
            if (line != null) {
                currentUser = line.split(":")[0].trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read and update image_details.txt
        try (BufferedReader reader = Files.newBufferedReader(detailsPath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("ImageID: " + imageId)) {
                    String[] parts = line.split(", ");
                    imageOwner = parts[1].split(": ")[1];
                    int likes = Integer.parseInt(parts[4].split(": ")[1]);
                    likes++; // Increment the likes count
                    parts[4] = "Likes: " + likes;
                    line = String.join(", ", parts);

                    // Update the UI
                    likesLabel.setText("Likes: " + likes);
                    updated = true;
                }
                newContent.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write updated likes back to image_details.txt
        if (updated) {
            try (BufferedWriter writer = Files.newBufferedWriter(detailsPath)) {
                writer.write(newContent.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Record the like in notifications.txt
            String notification = String.format("%s; %s; %s; %s\n", imageOwner, currentUser, imageId, timestamp);
            try (BufferedWriter notificationWriter = Files.newBufferedWriter(Paths.get("data", "notifications.txt"), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                notificationWriter.write(notification);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public JScrollPane getScrollPane() {
        System.out.println(scrollPane);
        return scrollPane;
    }
}
