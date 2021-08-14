package com.motion.toasterlibrary.productive;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.tv.TvInputService;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.motion.toasterlibrary.R;

public class ToasterMessage  extends Toast {
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     * or {@link Activity} object.
     */


    TextView toasti;
    CardView cardView;
    Context context;
    Toast toast;
    View converview;
    ImageView toastImage;

    public ToasterMessage(Context context) {
        super(context);
        toast = new Toast(context);
      //  toast.setDuration(Toast.LENGTH_SHORT);
    }

    /** Default toast */
    public  void simpleToast(Context c, String message) {
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
    }


    /** Default Success Toast */
    public void showSuccessToast(Context context, String customToast) {

        toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 130);
        toast.setDuration(Toast.LENGTH_SHORT);
        converview = LayoutInflater.from(context).inflate(R.layout.toast_sample, null);
        cardView = converview.findViewById(R.id.toastCard);

        toasti = converview.findViewById(R.id.customToast);

        cardView.setCardBackgroundColor(context.getResources().getColor(R.color.toastSuccess));
        toast.setView(converview);
        toasti.setText(customToast);
        toast.show();
    }

    /** Default Error Toast */
    public void showErrorToast( Context context, String customToast) {

        toast = new Toast(context);

        toast.setGravity(Gravity.BOTTOM, 0, 130);
        toast.setDuration(Toast.LENGTH_SHORT);
        converview = LayoutInflater.from(context).inflate(R.layout.toast_sample, null);
        cardView = converview.findViewById(R.id.toastCard);

        toasti = converview.findViewById(R.id.customToast);

        cardView.setCardBackgroundColor(context.getResources().getColor(R.color.toastFailed));
        toast.setView(converview);
        toasti.setText(customToast);
        toast.show();
    }

    /** Default warning Toast */
    public void showWarningToast( Context context, String customToast) {

        toast = new Toast(context);

        toast.setGravity(Gravity.BOTTOM, 0, 130);
        toast.setDuration(Toast.LENGTH_SHORT);
        converview = LayoutInflater.from(context).inflate(R.layout.toast_sample, null);
        cardView = converview.findViewById(R.id.toastCard);

        toasti = converview.findViewById(R.id.customToast);

        cardView.setCardBackgroundColor(context.getResources().getColor(R.color.toastInfo));
        toast.setView(converview);
        toasti.setText(customToast);
        toast.show();
    }

    /** User can send user choice background color */
    public void showUserChoice(Context context, String customToast , int color) {
        toast = new Toast(context);

        toast.setGravity(Gravity.BOTTOM, 0, 130);
        toast.setDuration(Toast.LENGTH_SHORT);
        converview = LayoutInflater.from(context).inflate(R.layout.toast_sample, null);
        cardView = converview.findViewById(R.id.toastCard);

        toasti = converview.findViewById(R.id.customToast);

        cardView.setCardBackgroundColor(color);
        toast.setView(converview);
        toasti.setText(customToast);
        toast.show();
    }

    /** User can send user choice background color */
    public void showUserChoice(Context context, String customToast , int bgColor, int textColor) {
        toast = new Toast(context);

        toast.setGravity(Gravity.BOTTOM, 0, 130);
        toast.setDuration(Toast.LENGTH_SHORT);
        converview = LayoutInflater.from(context).inflate(R.layout.toast_sample, null);
        cardView = converview.findViewById(R.id.toastCard);

        toasti = converview.findViewById(R.id.customToast);

        cardView.setCardBackgroundColor(bgColor);
        toasti.setTextColor(textColor);
        toast.setView(converview);
        toasti.setText(customToast);
        toast.show();
    }

    /** User can send user choiced image and user choice background color */
    public void showUserChoiceBackground(Context context, String customToast , int color, int drawable) {
        toast = new Toast(context);

        toast.setGravity(Gravity.BOTTOM, 0, 130);
        toast.setDuration(Toast.LENGTH_SHORT);
        converview = LayoutInflater.from(context).inflate(R.layout.toast_sample, null);
        cardView = converview.findViewById(R.id.toastCard);
        toastImage = converview.findViewById(R.id.toastImage);
        toastImage.setVisibility(View.VISIBLE);
        toasti = converview.findViewById(R.id.customToast);
        Glide.with(context).load(drawable).into(toastImage);
        cardView.setCardBackgroundColor(color);
        toast.setView(converview);
        toasti.setText(customToast);
        toast.show();
    }


    /** User can send user choiced image and user choice background color */
    public void showUserChoiceBackground(Context context, String customToast , int drawable) {
        toast = new Toast(context);

        toast.setGravity(Gravity.BOTTOM, 0, 130);
        toast.setDuration(Toast.LENGTH_SHORT);
        converview = LayoutInflater.from(context).inflate(R.layout.toast_sample, null);
        cardView = converview.findViewById(R.id.toastCard);
        toastImage = converview.findViewById(R.id.toastImage);
        toastImage.setVisibility(View.VISIBLE);
        toasti = converview.findViewById(R.id.customToast);
        Glide.with(context).load(drawable).into(toastImage);
        cardView.setCardBackgroundColor(context.getResources().getColor(R.color.defaultToast));
        toast.setView(converview);
        toasti.setText(customToast);
        toast.show();
    }

    /** User can send user choiced image and user choice background color , textColor */
    public void showUserChoice(Context context, String customToast , int bgColor, Drawable toastImages, int textColor) {
        toast = new Toast(context);

        toast.setGravity(Gravity.BOTTOM, 0, 130);
        toast.setDuration(Toast.LENGTH_SHORT);
        converview = LayoutInflater.from(context).inflate(R.layout.toast_sample, null);
        cardView = converview.findViewById(R.id.toastCard);
        toastImage = converview.findViewById(R.id.toastImage);
        toastImage.setVisibility(View.VISIBLE);
        toasti = converview.findViewById(R.id.customToast);
        Glide.with(context).load(toastImages).into(toastImage);
        cardView.setCardBackgroundColor(bgColor);
        toasti.setTextColor(textColor);
        toast.setView(converview);
        toasti.setText(customToast);
        toast.show();
    }

    public void setGravity(int gravity , int xOffset, int yOffset){
        this.toast.setGravity(gravity, xOffset, yOffset);
    }

    public void setDuration(int duration){
        this.toast.setDuration(duration);
    }
}
