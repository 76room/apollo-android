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

public class BorderTransform extends BitmapTransformation{
    private int mBorderWidth;
    private int mColor;

    public BorderTransform(Context context){
        this(context, 15, Color.WHITE);
    }

    public BorderTransform(Context context,int borderWidht, int color) {
        super(context);
        mBorderWidth = borderWidht;
        mColor = color;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        int dstBitmapWidth = toTransform.getWidth()+mBorderWidth*2;
        Bitmap dstBitmap = Bitmap.createBitmap(dstBitmapWidth,dstBitmapWidth, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dstBitmap);
        canvas.drawBitmap(toTransform, mBorderWidth, mBorderWidth, null);

        Paint paint = new Paint();
        paint.setColor(mColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mBorderWidth);
        paint.setAntiAlias(true);

        canvas.drawCircle(
                canvas.getWidth() / 2, // cx
                canvas.getWidth() / 2, // cy
                canvas.getWidth()/2 - mBorderWidth / 2, // Radius
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
