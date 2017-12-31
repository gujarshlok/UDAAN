package sp.udaan.HelperClasses;

/**
 * Created by nikhi on 20-12-2017.
 */

public class FriendlyMessage {

    private String text;
    private String name;
    private String photoUrl;
    private String email;
    private String rec_email;
    private String time;
    private String namereceiver;

    public FriendlyMessage() {
    }

    public FriendlyMessage(String text, String name, String email, String photoUrl, String rec_email, String time, String namereceiver) {
        this.text = text;
        this.name = name;
        this.email=email;
        this.photoUrl = photoUrl;
        this.rec_email=rec_email;
        this.time=time;
        this.namereceiver=namereceiver;
    }

    public String getNamereceiver(){return namereceiver;}
    public String getText() {
        return text;
    }

    public String getTime(){return time;}

    public void setText(String text) {
        this.text = text;
    }

    public void setRec_email(String rec_email) {
        this.rec_email = rec_email;
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

    public void setTime(String time){this.time=time;}

    public void setNamereceiver(String namereceiver){this.namereceiver=namereceiver;}

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getRec_email() {
        return rec_email;}

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
