package org.room76.apollo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by a.zatsepin on 04/02/2018.
 */

public class ShadowTransform extends BitmapTransformation {
    private int mShadowWidth;
    private int mColor;

    public ShadowTransform(Context context){
        this(context, 4, Color.LTGRAY);
    }

    public ShadowTransform(Context context,int shadowWidht, int color) {
        super(context);
        mShadowWidth = shadowWidht;
        mColor = color;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        int dstBitmapWidth = toTransform.getWidth()+mShadowWidth*2;
        Bitmap dstBitmap = Bitmap.createBitmap(dstBitmapWidth,dstBitmapWidth, Bitmap.Config.ARGB_8888);

        // Initialize a new Canvas instance
        Canvas canvas = new Canvas(dstBitmap);
        canvas.drawBitmap(toTransform, mShadowWidth, mShadowWidth, null);

        // Paint to draw circular bitmap shadow
        Paint paint = new Paint();
        paint.setColor(mColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mShadowWidth);
        paint.setAntiAlias(true);

        // Draw the shadow around circular bitmap
        canvas.drawCircle(
                dstBitmapWidth / 2, // cx
                dstBitmapWidth / 2, // cy
                dstBitmapWidth / 2 - mShadowWidth / 2, // Radius
                paint // Paint
        );

        toTransform.recycle();
        return dstBitmap;
    }

    @Override
    public String getId() {
        return getClass().getSimpleName();
    }
}
