package com.motion.toasterlibrary;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CustomProgressDialog implements DialogInterface {
    private Activity activity;
    private String title, message;
    private TextView tvTitle, tvMessage;
    //    private LinearLayout imageLayout;
    private ImageView alertImage;
    private boolean cancellable;

    private View divider;
    private ConstraintLayout background;
    private Drawable alertDrawable;

    int primaryColor;

    private LayoutInflater inflater;
    Dialog dialog;

    public CustomProgressDialog(Activity activity) {
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_progress_dialog, null);
        dialog = new Dialog(this.activity);

        tvTitle = view.findViewById(R.id.cp_title);
        tvMessage = view.findViewById(R.id.cp_messge);
        background = view.findViewById(R.id.cp_view);
        divider = view.findViewById(R.id.v1);
        //   imageLayout = view.findViewById(R.id.cp_pbar);
        alertImage = view.findViewById(R.id.cp_pbar);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Glide.with(activity).load(R.raw.loadingc).into(alertImage);
    }

    public CustomProgressDialog setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
            divider.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public CustomProgressDialog setMessage(String title) {
        if (!TextUtils.isEmpty(title)) {
            tvMessage.setVisibility(View.VISIBLE);
            tvMessage.setText(title);
            divider.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public CustomProgressDialog setIcon(Drawable drawable) {
        Glide.with(activity).load(drawable).into(alertImage);
        return this;
    }

    public CustomProgressDialog setBackgroundColor(int color) {
        background.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        return this;
    }

    public CustomProgressDialog titleColor(int color) {
        tvTitle.setTextColor(color);
        return this;
    }

    public CustomProgressDialog messageColor(int color) {
        tvMessage.setTextColor(color);
        return this;
    }

    public CustomProgressDialog setIconGif(int drawable) {

        Glide.with(activity).load(drawable).into(alertImage);
        return this;
    }

    public CustomProgressDialog show() {
        dialog.show();
        return this;
    }

    public CustomProgressDialog isCancelable(boolean cancellable) {
        this.cancellable = cancellable;
        dialog.setCanceledOnTouchOutside(cancellable);
        return this;
    }


    @Override
    public void cancel() {
        dialog.dismiss();
    }

    @Override
    public void dismiss() {
        dialog.dismiss();
    }


}
