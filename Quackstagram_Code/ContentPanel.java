import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class ContentPanel extends JPanel{

    /*
     * THIS PANEL IS USED IN 
     * ExploreUI
     */

    private static final String pathname = "img/uploaded";
    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;
    private static final int NAV_ICON_SIZE = 20; // Size for navigation icons
    private static final int IMAGE_SIZE = WIDTH / 3; // Size for each image in the grid
    private JFrame frame;

    public ContentPanel(JFrame frame){

        this.frame = frame;

        System.out.println("This content panel was added");
        // Create the main content panel with search and image grid
        // Search bar at the top
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField(" Search Users");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchField.getPreferredSize().height)); // Limit the height
        
        JPanel imageGridPanel = new JPanel(new GridLayout(0, 3, 2, 2)); // 3 columns, auto rows
        // finds and adds all the images into the grid panel
        getImages(imageGridPanel);


        JScrollPane scrollPane = new JScrollPane(imageGridPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Main content panel that holds both the search bar and the image grid

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(searchPanel);
        this.add(scrollPane); // This will stretch to take up remaining space

    }
    
     

    private void getImages(JPanel imageGridPanel){
        // Load images from the uploaded folder
        File imageDir = new File(pathname);
        File[] imageFiles = imageDir.listFiles((dir, name) -> name.matches(".*\\.(png|jpg|jpeg)"));

        if (imageDir.exists() && imageDir.isDirectory()) {
            if (imageFiles != null) {
                for (File imageFile : imageFiles) {

                    ImageIcon imageIcon = new ImageIcon(new ImageIcon(imageFile.getPath()).getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH));
                    JLabel imageLabel = new JLabel(imageIcon);

                    MouseAdapter adapter = new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent event) {
                            System.out.println("this event is triggered");
                            displayImage(imageIcon, imageFile.getPath());
                        }
                    };

                    imageLabel.addMouseListener(adapter);
                    imageGridPanel.add(imageLabel);
                }
            }
        }
    }


    private void displayImage(ImageIcon imageIcon, String imagePath) {
        //remove the exploreUi and initialize a new one
        this.frame.dispose();
        JFrame displayImage = new ImageDisplayUI(imageIcon, imagePath);
        displayImage.setVisible(true);
    }
}
