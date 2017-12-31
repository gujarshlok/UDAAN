package sp.udaan.HelperClasses;

/**
 * Created by nikhi on 20-12-2017.
 */

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import sp.udaan.R;

public class MessageAdapter extends ArrayAdapter<FriendlyMessage> {
    public MessageAdapter(Context context, int resource, List<FriendlyMessage> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }

        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.photoImageView);
        TextView messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        TextView nametoTextView=(TextView)convertView.findViewById(R.id.nametoTextView);
        TextView to =(TextView)convertView.findViewById(R.id.to);
        TextView emailTextView=(TextView)convertView.findViewById(R.id.emailTextView);
        CardView x = (CardView) convertView.findViewById(R.id.msgCard);

        FriendlyMessage message = getItem(position);

        String w = message.getEmail();
        String z = message.getRec_email();

        x.setBackgroundResource(R.drawable.left_buton);

        boolean isPhoto = message.getPhotoUrl() != null;
        if (isPhoto) {
            messageTextView.setVisibility(View.GONE);
            photoImageView.setVisibility(View.VISIBLE);
            emailTextView.setVisibility(View.GONE);
            Glide.with(photoImageView.getContext())
                    .load(message.getPhotoUrl())
                    .into(photoImageView);
        } else {
            messageTextView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            emailTextView.setVisibility(View.GONE);
            messageTextView.setText(message.getText());
        }
        to.setText("to");
        authorTextView.setText(message.getName());
        emailTextView.setText(message.getEmail());
        nametoTextView.setText(message.getNamereceiver());
        if(!w.equals(z)){
            x.setBackgroundResource(R.drawable.right_buton);
        }


        return convertView;
    }
}

