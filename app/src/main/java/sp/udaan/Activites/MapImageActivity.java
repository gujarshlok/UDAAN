package sp.udaan.Activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import sp.udaan.R;

public class MapImageActivity extends AppCompatActivity {

    ImageView img1,img2,img3,img4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_image);

        img1=(ImageView)findViewById(R.id.mapimage_1);
        img2=(ImageView)findViewById(R.id.mapimage_2);
        img3=(ImageView)findViewById(R.id.mapimage_3);
        img4=(ImageView)findViewById(R.id.mapimage_4);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MapImageActivity.this,MainActivity.class);
                i.putExtra("EventCategory","Fun Events");
                startActivity(i);
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MapImageActivity.this,MainActivity.class);
                i.putExtra("EventCategory","Performing Arts");
                startActivity(i);
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MapImageActivity.this,MainActivity.class);
                i.putExtra("EventCategory","Literary Arts");
                startActivity(i);
            }
        });

        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MapImageActivity.this,MainActivity.class);
                i.putExtra("EventCategory","Featured");
                startActivity(i);
            }
        });
    }
}
