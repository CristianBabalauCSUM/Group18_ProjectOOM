import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import UI_Components.HeaderPanel;

public class ImageDisplayUI extends JFrame {
    
    private JPanel headerPanel = new HeaderPanel("QuackStackGram");
    private JPanel navigationPanel = new NavigationPanel(this);

    private JPanel backButtonPanel;
    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;



    private static final Color LIKE_BUTTON_COLOR_INACTIVE = new Color(100,100,100);
    private static final Color LIKE_BUTTON_COLOR_ACTIVE = new Color(200,40,30);

    public ImageDisplayUI(ImageIcon image, String imagePath) {
        setTitle("Display Image");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        createBackButton();
        initializeUI(image, imagePath);
    }

    public ImageDisplayUI(ImageIcon image, String imagePath, User user) {
        setTitle("Display Image");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        createBackButton(user);
        initializeUI(image, imagePath);
    }


    // Setting the back button in case we want to return to the user page
    private void createBackButton(){
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Back to the page");

        // Make the button take up the full width
        backButton.setPreferredSize(new Dimension(WIDTH-20, backButton.getPreferredSize().height));

        backButtonPanel.add(backButton);

        backButton.addActionListener(e -> {
            this.dispose();
            ExploreUI explore = new ExploreUI();
            explore.setVisible(true);
        });

        this.backButtonPanel = backButtonPanel;


    }


    //setting the back button in case we want to return to the explore page
    private void createBackButton(User user){
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Back to profile");

        // Make the button take up the full width
        backButton.setPreferredSize(new Dimension(WIDTH-20, backButton.getPreferredSize().height));

        backButtonPanel.add(backButton);

        backButton.addActionListener(e -> {
            this.dispose();
            InstagramProfileUI profile = new InstagramProfileUI(user);
            profile.setVisible(true);
        });

        this.backButtonPanel = backButtonPanel;

    }
    private String getTime(String timeStampString){
        String timeSincePosting = "Unknown";
        if (!timeStampString.isEmpty()) {

            LocalDateTime timestamp = LocalDateTime.parse(timeStampString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime now = LocalDateTime.now();
            long days = ChronoUnit.DAYS.between(timestamp, now);
            timeSincePosting = days + " day" + (days != 1 ? "s" : "") + " ago";

        }
        return timeSincePosting;
    }

    private HashMap<String, String> getDetails(String imagePath){
        HashMap<String, String> details = new HashMap<>();

        String imageId = new File(imagePath).getName().split("\\.")[0];
        Path detailsPath = Paths.get("img", "image_details.txt");
        try (Stream<String> lines = Files.lines(detailsPath)) {
            String detailsString = lines.filter(line -> line.contains("ImageID: " + imageId)).findFirst().orElse("");
            if (!detailsString.isEmpty()) {
                String[] parts = detailsString.split(", ");
                details.put("username", parts[1].split(": ")[1]);
                details.put("bio", parts[2].split(": ")[1]);
                details.put("timestamp", parts[3].split(": ")[1]);
                details.put("likes", parts[4].split(": ")[1]);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("The image details file could not be read");
            details.put("username", "Unknown");
            details.put("bio", "Unknown");
            details.put("timestamp", "Unknown");
            details.put("likes", "Unknown");
        }

        return details;
    }

    private void initializeUI(ImageIcon image, String imagePath){
        System.out.println("Started initializing the image");
        
        getContentPane().removeAll();
        this.setLayout(new BorderLayout());

        // Add the header and navigation panels back

        JPanel imageViewerPanel = new JPanel(new BorderLayout());

        // Extract image ID from the imagePath
        String imageId = new File(imagePath).getName().split("\\.")[0];
        
        // Read image details
        HashMap<String, String> imageDetails = getDetails(imagePath);

        // Calculate time since posting
        String timeSincePosting = getTime(imageDetails.get("timestamp"));

        // Top panel for username and time since posting
        JPanel topPanel = new JPanel(new BorderLayout());

        JButton usernameLabel = new JButton(imageDetails.get("username"));
        JLabel timeLabel = new JLabel(timeSincePosting);

        timeLabel.setHorizontalAlignment(JLabel.RIGHT);
        topPanel.add(usernameLabel, BorderLayout.WEST);
        topPanel.add(timeLabel, BorderLayout.EAST);


        // Prepare the image for display
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        try {
            imageLabel.setIcon(image);
        } catch (Exception ex) {
            ex.fillInStackTrace();
            imageLabel.setText("Image not found");
        }

        // Bottom panel for bio and likes (Like button)

        JButton likeButton = new JButton("❤");
        likeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        likeButton.setSize(new Dimension(70,30));
        likeButton.setBackground(LIKE_BUTTON_COLOR_INACTIVE); // Set the background color for the like button
        likeButton.setOpaque(true);
        likeButton.setBorderPainted(false); // Remove border

        /*
        likeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLikeAction(imageId, likesLabel);
            }
        });

         */

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JTextArea bioTextArea = new JTextArea(imageDetails.get("bio")); 
        bioTextArea.setEditable(false);
        JLabel likesLabel = new JLabel("Likes: " + imageDetails.get("likes"));

        bottomPanel.add(likeButton, BorderLayout.NORTH);
        bottomPanel.add(bioTextArea, BorderLayout.CENTER);
        bottomPanel.add(likesLabel, BorderLayout.SOUTH);

        // Adding the components to the frame
        add(topPanel, BorderLayout.NORTH);
        add(imageLabel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Re-add the header and navigation panels

        add(headerPanel, BorderLayout.NORTH);
        add(navigationPanel, BorderLayout.SOUTH);

        // Panel for the back button


        usernameLabel.addActionListener(e -> {
            User user = new User(imageDetails.get("username")); //Assuming User class has a constructor that takes a username
            InstagramProfileUI profileUI = new InstagramProfileUI(user);
            profileUI.setVisible(true);
            dispose(); // Close the current frame
        });

        // Container panel for image and details
        JPanel containerPanel = new JPanel(new BorderLayout());

        containerPanel.add(topPanel, BorderLayout.NORTH);
        containerPanel.add(imageLabel, BorderLayout.CENTER);
        containerPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add the container panel and back button panel to the frame
        this.add(this.backButtonPanel, BorderLayout.NORTH);
        this.add(containerPanel, BorderLayout.CENTER);

        revalidate();
        repaint();

    }
}
