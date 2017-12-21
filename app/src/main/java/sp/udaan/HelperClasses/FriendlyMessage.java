package sp.udaan.HelperClasses;

/**
 * Created by nikhi on 20-12-2017.
 */

public class FriendlyMessage {

    private String text;
    private String name;
    private String photoUrl;
    private String email;

    public FriendlyMessage() {
    }

    public FriendlyMessage(String text, String name,String email, String photoUrl) {
        this.text = text;
        this.name = name;
        this.email=email;
        this.photoUrl = photoUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
