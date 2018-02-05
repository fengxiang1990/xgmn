package com.fxa.xgmn;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;

import java.util.List;

import fxa.com.xgmn.R;

/**
 * Created by 30315 on 2018/2/5.
 */

public class MyImageAdapter extends PagerAdapter {

    private List<ImageResult> imageResults;
    private DetailActivity activity;

    public MyImageAdapter(List<ImageResult> imageResults, DetailActivity activity) {
        this.imageResults = imageResults;
        this.activity = activity;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageResult imageResult = imageResults.get(position);
        final PhotoView photoView = new PhotoView(activity);
        container.addView(photoView);
        photoView.enable();
        Log.e("imageResult", imageResult == null ? "null" : imageResult.toString());
        Glide.with(activity)
                .asBitmap()
                .apply(RequestOptions.placeholderOf(R.mipmap.loading_icon))
                .load(imageResult.url).into(new ImageViewTarget<Bitmap>(photoView) {
            @Override
            protected void setResource(@Nullable Bitmap resource) {
                if (resource != null) {
                    Bitmap bitmap = Bitmap.createBitmap(resource, 0, 0, resource.getWidth(), resource.getHeight() - 100);
                    photoView.setImageBitmap(bitmap);
                }
            }
        });

        return photoView;
    }

    @Override
    public int getCount() {
        return imageResults != null ? imageResults.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}