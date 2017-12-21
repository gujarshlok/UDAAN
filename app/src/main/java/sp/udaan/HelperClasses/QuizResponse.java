package sp.udaan.HelperClasses;

import com.google.firebase.database.ServerValue;

import java.util.Map;

/**
 * Created by Tejas on 21-12-2017.
 */

public class QuizResponse {

    String name,email,correct;
    Map<String,String> time;

    public QuizResponse(String name, String email, String correct, Map<String, String> time)
    {
        this.name=name;
        this.email=email;
        this.correct=correct;
        this.time=time;
    }

}
