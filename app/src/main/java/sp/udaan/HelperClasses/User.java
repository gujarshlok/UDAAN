package sp.udaan.HelperClasses;


public class User {
    private String name,email,profile,UID,type;
    private String from;
    private String phone;


    public User(String name, String email, String profile, String UID, String type, String from,String phone) {
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.UID = UID;
        this.type = type;
        this.from = from;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProfile() {
        return profile;
    }

    public String getUID() {
        return UID;
    }

    public String getType() {
        return type;
    }

    public String getFrom() {
        return from;
    }

    public String getPhone() {
        return phone;
    }
}
