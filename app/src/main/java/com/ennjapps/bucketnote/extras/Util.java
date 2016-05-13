package com.ennjapps.bucketnote.extras;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by haider on 07-05-2016.
 */
public class Util {

    public static Typeface loadRalewayThin(Context context) {


        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/raleway-regular.ttf");
        return customFont;
    }



    public static Typeface loadRalewayRegular(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-Bold.ttf");
        return customFont;
    }

    public static boolean isJellyBeanOrMore() {
        return Build.VERSION.SDK_INT > 16;
    }

    public static boolean isLollipopOrMore() {
        return Build.VERSION.SDK_INT > 20;
    }

    public static String getFormattedDate(long milliseconds) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy, hh:mm a");
        String outputDate = format.format(new Date(milliseconds));
        return outputDate;
    }

    public static Drawable getDrawable(Context context, int resourceId) {
        Drawable drawable;
        if (isLollipopOrMore()) {
            drawable = context.getDrawable(resourceId);
        } else {
            drawable = context.getResources().getDrawable(resourceId);
        }
        return drawable;
    }

    public static void setBackgroundDrawable(View view, int resourceId) {
        Drawable drawable = getDrawable(view.getContext(), resourceId);
        if (isJellyBeanOrMore()) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public static void setImageDrawable(ImageView imageView, int resourceId) {
        Drawable drawable = getDrawable(imageView.getContext(), resourceId);
        imageView.setImageDrawable(drawable);
    }

    public static void showViews(List<View> views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void hideViews(List<View> views) {
        for (View view : views) {
            view.setVisibility(View.GONE);
        }
    }

}
