import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PostsManager {
    private final String directoryPath = "img/uploaded/";

    private List<Post> postsList = new ArrayList<Post>();
    private List<Post> interestingPostsList = new ArrayList<Post>();

    private File findImageFile(String imageName) {
        File directory = new File(this.directoryPath);
        File[] files = directory.listFiles((dir, name) -> name.startsWith(imageName));
        if (files != null && files.length > 0) {
            return files[0]; // Return the first matching file
        }
        return null; // File not found
    }


    private List<Post> getAllPosts(){
        List<Post> postsList = new ArrayList<Post>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get("img", "image_details.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                try {

                    String[] details = line.split(", ");
                    String id = details[0].split(": ")[1];
                    String userOwner = details[1].split(": ")[1];
                    String caption = details[2].split(": ")[1];
                    String timeStamp = details[3].split(": ")[1];
                    Integer likesCounter = Integer.parseInt(details[4].split(": ")[1]);
                    String imagePath = "";
                    File imageFile = findImageFile(id);

                    imagePath = this.directoryPath + imageFile.getName();
                    Post post = new Post(
                            new User(userOwner),
                            id,
                            likesCounter,
                            imagePath,
                            caption,
                            timeStamp
                    );
                    postsList.add(post);

                } catch (Exception e) {
                    System.out.println("Error reading the image file: " + e.getMessage());
                    System.out.println("Post is not loaded");
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return postsList;
    }

    public List<Post> getInterestingPostsForUser(User user){
        List<Post> allInterestingPosts = new ArrayList<>();
        // parse through all objects of type User that are being followed by us to get their posts.
        for (User influencer: user.getFolowedUsers() ) {
            System.out.println(influencer.getUsername());
            List<Post> tempPostsList =
                    postsList.
                            stream().
                            filter(post -> post.getUser().getUsername().equals(influencer.getUsername())).
                            toList();

            for (Post post:
                 tempPostsList) {
                allInterestingPosts.add(post);
            }
        }

        return allInterestingPosts;
    }


    public PostsManager(){
        this.postsList = this.getAllPosts();
    }

    public List<Post> getPostsList() {
        return postsList;
    }

    public List<Post> getInterestingPostsList() {
        return interestingPostsList;
    }

    public static void main(String[] args) {
        PostsManager postsManager = new PostsManager();
        //postsManager.getInterestingPosts(new User("Mystar"));
    }

}
