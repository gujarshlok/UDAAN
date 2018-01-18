package sp.udaan.HelperClasses;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import sp.udaan.R;

/**
 * Created by madhura on 23/12/17.
 */

public class SetCrescentoImage extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private int[] mResources = {
//            R.drawable.viewpager_1,
//            R.drawable.viewpager_22,
//            R.drawable.viewpager_33
    };

    public SetCrescentoImage(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.crescento_image, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.crescentoContainer);
        Glide.with(mContext).load(mResources[position]).fitCenter().into(imageView);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((RelativeLayout) object);
    }
}
