package com.mashazavolnyuk.musicwavejava.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;


public class ImageUtil {

    private static final float BITMAP_SCALE = 0.6f;
    private static final float BLUR_RADIUS = 15f;

    public static Bitmap blur(Context context, Bitmap image) {
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);

        ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        intrinsicBlur.setRadius(BLUR_RADIUS);
        intrinsicBlur.setInput(tmpIn);
        intrinsicBlur.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return applyFilter(outputBitmap);
    }

    private static Bitmap applyFilter(Bitmap image) {
        ColorMatrix cm;
        ColorFilter filter;

        float[] cmData = new float[]{
                1, 0, 0, 0, 0,
                0, 1, 0, 0, 0,
                0, 0, 1, 0, 0,
                0, 0, 0, 0.3f, 0};
        cm = new ColorMatrix(cmData);
        filter = new ColorMatrixColorFilter(cm);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        Canvas canvas = new Canvas(image);
//        paint.setColorFilter(filter);
        paint.setColor(Color.WHITE);
        paint.setAlpha(150);
        // Initialize a new Rect object
        Rect rectangle = new Rect(
                0, // Left
                0, // Top
                canvas.getWidth(), // Right
                canvas.getHeight() // Bottom
        );
        canvas.drawRect(rectangle,paint);
        return image;
    }

    public static Bitmap getBitmap(@NonNull Context context, @DrawableRes int id) {
       Drawable drawable = context.getResources().getDrawable(id,context.getTheme());
        return createBitmap(drawable,1);
    }

    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int id) {
        return context.getResources().getDrawable(id,context.getTheme());
    }

    public static Bitmap createBitmap(Drawable drawable, float sizeMultiplier) {
        Bitmap bitmap = Bitmap.createBitmap((int) (drawable.getIntrinsicWidth() * sizeMultiplier), (int) (drawable.getIntrinsicHeight() * sizeMultiplier), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        drawable.setBounds(0, 0, c.getWidth(), c.getHeight());
        drawable.draw(c);
        return bitmap;
    }
}
