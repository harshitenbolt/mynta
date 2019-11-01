package com.canvascoders.opaper.adapters;

import android.content.Context;
import android.net.Uri;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.utils.Mylogger;

import java.io.File;
import java.util.ArrayList;


public class MyAdapter extends PagerAdapter {

    private ArrayList<String> images = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;

    public MyAdapter(Context context, ArrayList<String> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.image);
        Glide.with(context).load(Uri.fromFile(new File(images.get(position)))).placeholder(R.drawable.placeholder).into(myImage);
        Mylogger.getInstance().Logit("tetete", images.get(position));
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}