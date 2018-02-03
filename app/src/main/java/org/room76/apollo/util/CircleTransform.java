package org.room76.apollo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by a.zatsepin on 04/02/2018.
 */

public class CircleTransform extends BitmapTransformation {

    public CircleTransform(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        int squareBitmapWidth = Math.min(toTransform.getWidth(), toTransform.getHeight());

        Bitmap dstBitmap = Bitmap.createBitmap(
                squareBitmapWidth, // Width
                squareBitmapWidth, // Height
                Bitmap.Config.ARGB_8888 // Config
        );

        Canvas canvas = new Canvas(dstBitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Rect rect = new Rect(0, 0, squareBitmapWidth, squareBitmapWidth);

        RectF rectF = new RectF(rect);

        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        float left = (squareBitmapWidth-toTransform.getWidth())/2;
        float top = (squareBitmapWidth-toTransform.getHeight())/2;

        canvas.drawBitmap(toTransform, left, top, paint);

        toTransform.recycle();

        return dstBitmap;
    }

    @Override
    public String getId() {
        return getClass().getSimpleName();
    }
}