package sp.udaan.Activites;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

import sp.udaan.R;

public class SignInVideo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_video);

        VideoView x = (VideoView) findViewById(R.id.video);
        String y = "android.resource://spit.matrix17/" + R.raw.matrixintro;
        Uri z = Uri.parse(y);

        x.setVideoURI(z);
        x.requestFocus();
        x.start();

        x.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Intent i = new Intent(SignInVideo.this,LoginPage.class);
                i.putExtra("name", getIntent().getStringExtra("name"));
                i.putExtra("email", getIntent().getStringExtra("email"));
                i.putExtra("profile", getIntent().getStringExtra("profile"));
                i.putExtra("uid", getIntent().getStringExtra("uid"));
                i.putExtra("type", getIntent().getStringExtra("type"));
                startActivity(i);

                finish();
            }
        });
    }
}