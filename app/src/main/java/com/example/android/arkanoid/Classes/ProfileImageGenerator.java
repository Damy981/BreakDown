package com.example.android.arkanoid.Classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.widget.TextView;

import androidx.annotation.NonNull;

// This is a ProfileImageGenerator. It is used to retrieve or generate a drawing to use as a profile picture

public class ProfileImageGenerator
{
    // Context used to perform the functionality of this object
    private final Context context;
    // Drawable loaded (o generated)
    private Drawable resource;

    // Interface used to provide an implementation of the callback method invoked when the resource is ready
    public interface OnImageGeneratedListener
    { void onImageGenerated(Drawable drawable); }

    @SuppressLint("CheckResult")
    public ProfileImageGenerator(@NonNull Context context)
    {
        this.context = context;
    }

    //  Generates an image for the specified name
    @SuppressLint("CheckResult")
    public void fetchImageOf(@NonNull String name, OnImageGeneratedListener onImageGeneratedListener)
    {
        resource = new ProfileImageLetter(name);

        if(onImageGeneratedListener != null)
            onImageGeneratedListener.onImageGenerated(resource);
    }

    public Context getContext()
    {
        return context;
    }

    public Drawable getResource()
    {
        return resource;
    }
}

// Drawable generated from a given string
class ProfileImageLetter extends ShapeDrawable
{
    // Letter to be displayed in the profile picture
    private char letter;

    ProfileImageLetter(String name)
    {
        if( name != null && ! name.isEmpty()) {
            this.letter = name.toUpperCase().charAt(0);
        }
        setShape(new OvalShape());
        setIntrinsicWidth(512);
        setIntrinsicHeight(512);
        setColorFilter( generateColorFor(name) , PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    protected void onDraw(Shape shape, Canvas canvas, Paint paint2)
    {
        super.onDraw(shape, canvas, paint2);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(350);
        paint.setTextAlign(Paint.Align.CENTER);

        canvas.drawText( "" + letter , ((float) getBounds().width()/2),
                ((float) getBounds().height()/2) - ((paint.descent() + paint.ascent()) /2), paint);
    }

    // Generates a color based on the specified string.
    private int generateColorFor(String name)
    {
        if( name == null || name.isEmpty())
            return Color.TRANSPARENT;

        int color = Color.rgb(mod(name.hashCode(), 255),
                mod(name.hashCode() / 255, 255),
                mod(name.hashCode() / (255 * 255), 255));

        return color;
    }

    // Calculation of an equivalent number from x modulo y
    private int mod(int x, int y)
    {
        while(x < 0)
            x += y;

        return x % y;
    }

}
