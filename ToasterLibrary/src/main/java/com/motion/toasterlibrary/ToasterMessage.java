package com.motion.toasterlibrary;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

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

    public ToasterMessage(Context context) {
        super(context);
        toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
    }

    public static void s(Context c, String message) {
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
    }


    public void showSuccessToast(Context context, String customToast) {

        toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        converview = LayoutInflater.from(context).inflate(R.layout.toast_sample, null);
        cardView = converview.findViewById(R.id.toastCard);

        toasti = converview.findViewById(R.id.customToast);

        cardView.setCardBackgroundColor(context.getResources().getColor(R.color.toastSuccess));
        toast.setView(converview);
        toasti.setText(customToast);
        toast.show();
    }
}
