package sp.udaan.HelperClasses;

/**
 * Created by Tejas on 15-01-2018.
 */

public class LeaderboardUser {

    String name;
    String email;
    public String score;
    String position;
    public LeaderboardUser(String position,String name,String email,String score)
    {
        this.position=position;
        this.name=name;
        this.email=email;
        this.score=score;
    }

}
