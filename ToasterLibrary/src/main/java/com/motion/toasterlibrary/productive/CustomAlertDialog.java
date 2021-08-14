package com.motion.toasterlibrary.productive;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.motion.toasterlibrary.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CustomAlertDialog implements DialogInterface {
    private Activity activity;
    private String title, message, positiveText, negativeText;
    private DialogInterface.OnClickListener positiveListener, negativeListener;
    private DialogInterface.OnDismissListener dismissListener;
    private DialogInterface.OnCancelListener cancelListener;
    private int buttonHighlight;
    private int highlightColor, buttonHightLightv2, buttonHightLightv1, highlightColor2;
    private TextView tvTitle, tvMessage, tvPositive, tvNegative;
    private LinearLayout imageLayout;
    private ImageView alertImage;
    private boolean cancellable;

    private Drawable alertDrawable;
    private int alertImageLayoutColor;
    View separator;

    int primaryColor;

    private LayoutInflater inflater;
    Dialog dialog;

    private CustomAlertDialog(Builder builder) {
        this.activity = builder.activity;
        this.title = builder.title;
        this.message = builder.message;
        this.positiveText = builder.positiveText;
        this.negativeText = builder.negativeText;
        this.positiveListener = builder.positiveListener;
        this.negativeListener = builder.negativeListener;
        this.dismissListener = builder.dismissListener;
        this.buttonHighlight = builder.buttonHighlight;
        this.buttonHightLightv2 = builder.buttonHightLightv2;
        this.buttonHightLightv1 = builder.buttonHightLightv1;
        this.highlightColor = builder.highlightColor;
        this.highlightColor2 = builder.highlightColor2;
        this.cancellable = builder.cancellable;
        this.alertDrawable = builder.alertDrawable;
        this.alertImageLayoutColor = builder.alertImageLayoutColor;

        try {
            inflater = (LayoutInflater) builder.activity.getSystemService(LAYOUT_INFLATER_SERVICE);
            primaryColor = this.activity.getResources().getColor(R.color.colorPrimary);
            initDialog();
        } catch (Exception e) {
            Log.e("AlertDialog", "AlertDialog: activity null");
        }

    }

    private void initDialog() {
        View view = inflater.inflate(R.layout.alert_dialog_message, null);
        dialog = new Dialog(this.activity);

        tvTitle = view.findViewById(R.id.alertTitle);
        tvMessage = view.findViewById(R.id.alertMessage);
        tvPositive = view.findViewById(R.id.positive);
        tvNegative = view.findViewById(R.id.negative);
        separator = view.findViewById(R.id.separator);
        imageLayout = view.findViewById(R.id.alertImageLayout);
        alertImage = view.findViewById(R.id.alertImage);

        tvTitle.setText(this.title);
        tvMessage.setText(this.message);
        tvPositive.setText(this.positiveText);
        tvNegative.setText(this.negativeText);

        if (buttonHighlight == BUTTON_POSITIVE) {
            tvPositive.setTextColor(primaryColor);
        } else if (buttonHighlight == BUTTON_NEGATIVE) {
            tvNegative.setTextColor(primaryColor);
        }
        if (buttonHightLightv1 == 1) {
            tvPositive.setTextColor(highlightColor);
        }
        if (buttonHightLightv2 == 2) {
            tvNegative.setTextColor(highlightColor2);
        }


        if (TextUtils.isEmpty(this.title)) {
            tvTitle.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(this.message)) {
            tvMessage.setVisibility(View.GONE);
        }

        if (this.alertDrawable != null) {
            imageLayout.setVisibility(View.VISIBLE);
            Glide.with(activity).load(alertDrawable).into(alertImage);
        }

        if (!TextUtils.isEmpty(String.valueOf(this.alertImageLayoutColor))) {
            imageLayout.getBackground().setColorFilter(alertImageLayoutColor, PorterDuff.Mode.SRC_ATOP);
        }

        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(cancellable);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (positiveListener != null) {
            tvPositive.setOnClickListener(v -> {
                positiveListener.onClick(this, 0);
                dialog.dismiss();
            });
        }

        if (negativeListener != null) {
            tvNegative.setOnClickListener(v -> {
                negativeListener.onClick(this, 0);
                dialog.dismiss();
            });
        } else {
            tvNegative.setOnClickListener(v -> dialog.dismiss());
        }

        if (dismissListener != null) {
            dialog.setOnDismissListener(dismissListener);
            dialog.dismiss();
        }

        if (TextUtils.isEmpty(negativeText)) {
            switchToSingleButtonMode();
        }

        dialog.show();
    }

    private void switchToSingleButtonMode() {
        tvNegative.setVisibility(View.GONE);
        separator.setVisibility(View.GONE);

        if (TextUtils.isEmpty(positiveText)) tvPositive.setText("Ok");
        if (positiveListener == null) tvPositive.setOnClickListener(v -> dialog.dismiss());

    }

    @Override
    public void cancel() {
        dialog.dismiss();
        if (cancelListener != null) cancelListener.onCancel(this);
    }

    @Override
    public void dismiss() {
        if (dismissListener != null) dismissListener.onDismiss(this);
    }

    public static class Builder {

        private Activity activity;
        private String title, message;

        private Drawable alertDrawable;
        private int alertImageLayoutColor;
        private int buttonHighlight;
        private int highlightColor, buttonHightLightv2, buttonHightLightv1, highlightColor2;
        private boolean cancellable = true;
        private String positiveText, negativeText;
        private DialogInterface.OnClickListener positiveListener, negativeListener;
        private DialogInterface.OnDismissListener dismissListener;

        public Builder(Activity activity) {
            this.activity = activity;
        }


        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setPositiveButton(String text, DialogInterface.OnClickListener listener) {
            this.positiveText = text;
            this.positiveListener = listener;
            return this;
        }

        public Builder setNegativeButton(String text, DialogInterface.OnClickListener listener) {
            this.negativeText = text;
            this.negativeListener = listener;
            return this;
        }

        public Builder setOnDismissListener(DialogInterface.OnDismissListener dismissListener) {
            this.dismissListener = dismissListener;
            return this;
        }

        public Builder setButtonHighlight(int highlight) {
            this.buttonHighlight = highlight;
            return this;
        }

        public Builder setHighlightButtonColorPositive(int color) {
            this.highlightColor = color;
            this.buttonHightLightv1 = 1;
            return this;
        }

        public Builder setHighlightButtonColorNegative(int color) {
            this.highlightColor2 = color;
            this.buttonHightLightv2 = 2;
            return this;
        }

        public Builder setAlertImage(Drawable alertImage, int backgroudColor) {
            this.alertImageLayoutColor = backgroudColor;
            this.alertDrawable = alertImage;
            return this;
        }

        public Builder setAlertImage(Drawable alertImage) {
            this.alertDrawable = alertImage;
            return this;
        }

        public CustomAlertDialog show() {
            return new CustomAlertDialog(this);
        }

        public Builder setCancelable(boolean isCancellable) {
            this.cancellable = isCancellable;
            return this;
        }

    }
}
