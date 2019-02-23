package com.seluhadu.shchat.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

public class ImageUtils {
    private ImageUtils() {
    }

    public static void displayImageFromUrl(final Context context, final ImageView imageView, final String url, Drawable placeHolder, RequestListener requestListener) {
        RequestOptions requestOptions = new RequestOptions()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(placeHolder);
        if (requestListener != null) {
            Glide.with(context)
                    .load(url)
                    .apply(requestOptions)
                    .listener(requestListener)
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(url)
                    .apply(requestOptions)
                    .into(imageView);
        }
    }
    public static void displayImageFromUrlWithPlaceHolder(final Context context, final ImageView imageView, final String url, int placeHolder) {
        RequestOptions requestOptions = new RequestOptions()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(placeHolder);
            Glide.with(context)
                    .load(url)
                    .apply(requestOptions)
                    .into(imageView);
    }

    public static void displayGifImageFromUrl(final Context context, final ImageView imageView, final String url, Drawable placeHolder, RequestListener requestListener) {
        RequestOptions requestOptions = new RequestOptions()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(placeHolder);

        if (requestListener != null) {
            Glide.with(context)
                    .asGif()
                    .load(url)
                    .apply(requestOptions)
                    .listener(requestListener)
                    .into(imageView);
        } else {
            Glide.with(context)
                    .asGif()
                    .load(url)
                    .apply(requestOptions)
                    .into(imageView);
        }
    }
    public static void displayGifImageFromUrlWithThumbnail(final Context context, final ImageView imageView, final String url, String thumbnailUrl, Drawable placeHolder) {
        RequestOptions requestOptions = new RequestOptions()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(placeHolder);

        if (thumbnailUrl != null) {
            Glide.with(context)
                    .asGif()
                    .load(url)
                    .apply(requestOptions)
                    .thumbnail(Glide.with(context).asGif().load(thumbnailUrl))
                    .into(imageView);
        } else {
            Glide.with(context)
                    .asGif()
                    .load(url)
                    .apply(requestOptions)
                    .into(imageView);
        }
    }
}
