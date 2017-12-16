package sp.udaan.HelperClasses;

/**
 * Created by USER on 30-08-2017.
 */

public class MainSponsor {
    private String  picUrl ,Url, type;

    public MainSponsor(String picUrl, String url, String type) {
        this.picUrl = picUrl;
        Url = url;
        this.type = type;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public String getUrl() {
        return Url;
    }

    public String getType() {
        return type;
    }
}
