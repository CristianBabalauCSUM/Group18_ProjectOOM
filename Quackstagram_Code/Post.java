import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Post {
    private User user;

    private String id;
    private Integer likeCount;
    private String photoPath;
    private String caption;

    private String timeStamp;

    private String timeSincePosted;

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


    public Post(User user, String id, Integer likeCount, String photoPath, String caption, String timeStamp){
        this.user = user;
        this.id = id;
        this.likeCount = likeCount;
        this.photoPath = photoPath;
        this.caption = caption;
        this.timeStamp = timeStamp;
        this.timeSincePosted = getTime(timeStamp);
    }

    public User getUser() {
        return user;
    }

    public String getCaption() {
        return caption;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public String getTimeSincePosted() {
        return timeSincePosted;
    }

    public String getId() {
        return id;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getUsername(){
        return this.user.getUsername();
    }

    @Override
    public String toString() {
        return user.toString() + "" + id + likeCount + photoPath + caption + timeStamp + timeSincePosted;
    }
}
