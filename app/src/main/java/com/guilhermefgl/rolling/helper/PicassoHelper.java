package com.guilhermefgl.rolling.helper;

import android.widget.ImageView;

import com.guilhermefgl.rolling.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class PicassoHelper {

    private PicassoHelper() { }

    public static void loadImage(String url, final ImageView imageView) {
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_connection_placeholder)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() { }

                    @Override
                    public void onError(Exception e) {
                        imageView.setImageResource(R.drawable.ic_connection_error);
                    }
                });
    }
}
