package com.example.mati.chatappjava8.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.example.mati.chatappjava8.profile.ImagesUtils;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;


/**
 * Created by Matias Furszyfer
 */
public class BitmapWorkerTask extends AsyncTask<byte[], Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;
    private final Resources res;
    private final int resImageInCaseOfError;
    private boolean isCircle = false;

    public BitmapWorkerTask(ImageView imageView, Resources res, @Nullable int resImageInCaseOfError, boolean isCircle) {
        this.res = res;
        this.isCircle = isCircle;
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);
        this.resImageInCaseOfError = resImageInCaseOfError;
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(byte[]... params) {
        byte[] data = params[0];
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        final ImageView imageView = imageViewReference.get();
        if (bitmap != null) {
            //if (imageView != null) {
            //imageView.setImageDrawable(ImagesUtils.getRoundedBitmap(res,bitmap));
            imageView.setImageDrawable((isCircle) ? ImagesUtils.getRoundedBitmap(res, bitmap) : new BitmapDrawable(res, bitmap));
            //}
        } else {
            if (isCircle)
                Picasso.with(imageView.getContext()).load(resImageInCaseOfError).transform(new CircleTransform()).into(imageView);
            else
                Picasso.with(imageView.getContext()).load(resImageInCaseOfError).into(imageView);
        }
    }


}