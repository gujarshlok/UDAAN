package sp.udaan.HelperClasses;

import com.google.firebase.database.ServerValue;

import java.util.Map;

/**
 * Created by Tejas on 21-12-2017.
 */

public class QuizResponse {

    String name,email,score;
    Map<String,String> time;

    public QuizResponse(String name, String email, String score, Map<String, String> time)
    {
        this.name=name;
        this.email=email;
        this.score=score;
        this.time=time;
    }

}
