package sp.udaan.HelperClasses;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import sp.udaan.R;

/**
 * Created by Tejas on 15-01-2018.
 */

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {

    ArrayList<LeaderboardUser> arrayList;
    String user_name;
    Context context;

    public LeaderboardAdapter(ArrayList<LeaderboardUser> arrayList,Context context,String user_name)
    {
        this.arrayList=arrayList;
        this.context=context;
        this.user_name=user_name;
    }

    @Override
    public LeaderboardAdapter.LeaderboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.leaderboard_item, parent, false);
        return new LeaderboardAdapter.LeaderboardViewHolder(view);

    }

    @Override
    public void onBindViewHolder(LeaderboardAdapter.LeaderboardViewHolder holder, int position) {

        if ((arrayList.get(position).name).equals(user_name))
        {
            int theme= Color.parseColor("#FF1919");
            int theme2 = Color.parseColor("#F6F6F6");
            holder.cardView.setCardBackgroundColor(theme);
            holder.name.setTextColor(theme2);
            holder.score.setTextColor(theme2);
            holder.postion_t.setTextColor(theme2);
        }

        holder.postion_t.setText(String.valueOf(position+1));
        holder.score.setText(arrayList.get(position).score);
        holder.name.setText(arrayList.get(position).name);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class LeaderboardViewHolder extends RecyclerView.ViewHolder {

        TextView postion_t,name,score;
        CardView cardView;
        public LeaderboardViewHolder(View itemView) {
            super(itemView);

            cardView=(CardView)itemView.findViewById(R.id.leaderboard_item_card);
            postion_t=(TextView)itemView.findViewById(R.id.leaderboard_item_position);
            name=(TextView)itemView.findViewById(R.id.leaderboard_item_name);
            score=(TextView)itemView.findViewById(R.id.leaderboard_item_score);

        }
    }
}
