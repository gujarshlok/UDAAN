package sp.udaan.HelperClasses;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import sp.udaan.R;

/**
 * Created by Tejas on 19-08-2017.
 */

public class RegistrationAdapter extends RecyclerView.Adapter<RegistrationAdapter.RegistrationViewHolder> {

    ArrayList<Registration> regs;
    Context context;

    public RegistrationAdapter(ArrayList<Registration> regs,Context context)
    {
        this.regs=regs;
        this.context=context;
    }

    @Override
    public RegistrationAdapter.RegistrationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.registrationitem, parent, false);
        return new RegistrationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RegistrationAdapter.RegistrationViewHolder holder, int position) {

        //Perform operation here
        //Eg.
        Registration r = regs.get(position);
        holder.nametext.setText(r.getName());
        holder.fromtext.setText(r.getFrom());
        holder.emailtext.setText(r.getEmail());


    }

    @Override
    public int getItemCount() {
        return regs.size();
    }

    static class RegistrationViewHolder extends RecyclerView.ViewHolder{

        TextView nametext,fromtext,emailtext;
        //CardView reviewcontainer;

        public RegistrationViewHolder(View itemView) {
            super(itemView);

            emailtext=(TextView)itemView.findViewById(R.id.reg_email);
            fromtext=(TextView)itemView.findViewById(R.id.reg_from);
            nametext=(TextView)itemView.findViewById(R.id.reg_name);
            //reviewcontainer=(CardView)itemView.findViewById(R.id.review_cardcontainer);
        }
    }
}
