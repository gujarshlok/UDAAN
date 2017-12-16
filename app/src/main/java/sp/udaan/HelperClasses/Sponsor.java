package sp.udaan.HelperClasses;

/**
 * Created by USER on 30-08-2017.
 */

public class Sponsor {

    private String  picUrl ,url;

    public Sponsor(String picUrl, String url) {
        this.picUrl = picUrl;
        this.url = url;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public String getUrl() {
        return url;
    }
}
