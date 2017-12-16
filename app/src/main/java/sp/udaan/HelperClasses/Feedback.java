package sp.udaan.HelperClasses;

/**
 * Created by USER on 13-08-2017.
 */

public class Feedback {
    private String feedback,email;
    private String rating;

    public Feedback(String feedback, String rating,String email) {
        this.feedback = feedback;
        this.rating = rating;
        this.email = email;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getEmail() {
        return email;
    }

    public String getRating() {
        return rating;
    }
}
