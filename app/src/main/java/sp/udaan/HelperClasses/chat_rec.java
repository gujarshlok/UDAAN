package sp.udaan.HelperClasses;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import sp.udaan.R;


public class chat_rec extends RecyclerView.ViewHolder  {


   public TextView messageTextView;
   public TextView nameTextView;

    public chat_rec(View itemView){
        super(itemView);

        messageTextView = (TextView)itemView.findViewById(R.id.messageTextView);
        nameTextView = (TextView)itemView.findViewById(R.id.nameTextView);


    }
}
