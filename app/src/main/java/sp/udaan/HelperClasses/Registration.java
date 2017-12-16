package sp.udaan.HelperClasses;

/**
 * Created by USER on 18-08-2017.
 */

public class Registration {
    private String name,email,from;

    public Registration(String name, String email, String from) {
        this.name = name;
        this.email = email;
        this.from = from;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getFrom() {
        return from;
    }
}
