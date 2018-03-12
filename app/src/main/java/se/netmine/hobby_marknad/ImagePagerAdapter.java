package se.netmine.hobby_marknad;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by jusuf on 2018-03-04.
 */

public class ImagePagerAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    ArrayList<String> arrayList;

    public ImagePagerAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        if(arrayList != null){
            return arrayList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = layoutInflater.inflate(R.layout.camping_image, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewCamping);

        Picasso.with(context).load(arrayList.get(position))
//                .placeholder(R.drawable.image_uploading)
//                .error(R.drawable.image_not_found)
        .into(imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

}
