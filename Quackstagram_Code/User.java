import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;

// Represents a user on Quackstagram
class User {
    private String username;
    private String bio;
    private String password;
    private int postsCount;
    private int followersCount;
    private int followingCount;
    private List<Picture> pictures;

    private List<User> followedUsers;

    public User(String username, String bio, String password) {
        this.username = username;
        this.bio = bio;
        this.password = password;
        this.pictures = new ArrayList<>();
        // Initialize counts to 0
        this.postsCount = 0;
        this.followersCount = 0;
        this.followingCount = 0;
    }

    public User(String username){
        this.username = username;
        updateUserData();
    }

    public List<User> getFolowedUsers(){
        List<User> followedUsers = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "following.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(this.username + ":")) {
                    for (
                            String followedUserName:
                            line.split(":")[1].trim().split(";")
                        ) {
                        User tempFollower = new User(followedUserName.trim());
                        followedUsers.add(tempFollower);
                    }
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("No User Or Subscribers Found");
            e.printStackTrace();
        }

        return followedUsers;
    }

    /*
    public void setUserPhotos(){
        Path imageDir = Paths.get("img", "uploaded");

        try (Stream<Path> paths = Files.list(imageDir)) {
            paths.filter(path -> path.getFileName().toString().startsWith(this.username + "_"))
                    .forEach(path -> {


                        ImageIcon imageIcon = new ImageIcon(new ImageIcon(path.toString()).getImage().getScaledInstance(GRID_IMAGE_SIZE, GRID_IMAGE_SIZE, Image.SCALE_SMOOTH));
                        JLabel imageLabel = new JLabel(imageIcon);

                    });
        } catch (IOException ex) {
            ex.printStackTrace();
            // Handle exception (e.g., show a message or log)
        }

    }

     */

    public void updateUserData(){

            int imageCount = 0;
            // Step 1: Read image_details.txt to count the number of images posted by the user
            Path imageDetailsFilePath = Paths.get("img", "image_details.txt");
            try (BufferedReader imageDetailsReader = Files.newBufferedReader(imageDetailsFilePath)) {
                String line;
                while ((line = imageDetailsReader.readLine()) != null) {
                    if (line.contains("Username: " + this.username) ) {
                        imageCount++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.postsCount = imageCount;



            // Step 2: Read following.txt to calculate followers and following
            int followingCount = 0;
            int followersCount = 0;


            Path followingFilePath = Paths.get("data", "following.txt");

            try (BufferedReader followingReader = Files.newBufferedReader(followingFilePath)) {
                String line;
                while ((line = followingReader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        String usernameLocal = parts[0].trim();
                        String[] followingUsers = parts[1].split(";");
                        if (usernameLocal.equals(this.username)) {
                            followingCount = followingUsers.length;
                        } else {
                            for (String followingUser : followingUsers) {
                                if (followingUser.trim().equals(this.username)) {
                                    followersCount++;
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.followingCount = followingCount;
            this.followersCount = followersCount;


            // Step 3 : update the bio

            String bio = "";

            Path bioDetailsFilePath = Paths.get("data", "credentials.txt");
            try (BufferedReader bioDetailsReader = Files.newBufferedReader(bioDetailsFilePath)) {
                String line;
                while ((line = bioDetailsReader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts[0].equals(this.username) && parts.length >= 3) {
                        bio = parts[2];
                        break; // Exit the loop once the matching bio is found
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.bio = bio;
    }
    // Add a picture to the user's profile
    public void addPicture(Picture picture) {
        pictures.add(picture);
        postsCount++;
    }

    // Getter methods for user details
    public String getUsername() { return username; }
    public String getBio() { return bio; }
    public void setBio(String bio) {this.bio = bio; }
    public int getPostsCount() {
        return postsCount;
    }
    public int getFollowersCount() {
        return followersCount;
    }
    public int getFollowingCount() {
        return followingCount;
    }
    public List<Picture> getPictures() { return pictures; }

    // Setter methods for followers and following counts
    public void setFollowersCount(int followersCount) { this.followersCount = followersCount; }
   public void setFollowingCount(int followingCount) { this.followingCount = followingCount; }
   public void setPostCount(int postCount) { this.postsCount = postCount;}
    // Implement the toString method for saving user information
    @Override
    public String toString() {
        return username + ":" + bio + ":" + password; // Format as needed
    }

}