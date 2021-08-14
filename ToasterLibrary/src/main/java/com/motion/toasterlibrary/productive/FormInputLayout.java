package com.motion.toasterlibrary.productive;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.motion.toasterlibrary.R;
import com.motion.toasterlibrary.productive.DataContracts;
import com.motion.toasterlibrary.productive.Validators;

import static com.motion.toasterlibrary.productive.Validators.isValidNumber;

public class FormInputLayout extends FrameLayout {

    public String TAG ="FormInputLayout";
    public static final int INPUT_TYPE_TEXT = 0;
    public static final int INPUT_TYPE_PERSON_NAME = 1;
    public static final int INPUT_TYPE_ADDRESS = 3;
    public static final int INPUT_TYPE_NUMBER = 4;
    public static final int INPUT_TYPE_PRICE = 5;
    public static final int INPUT_TYPE_ALPHA_NUMERIC = 6;
    public static final int INPUT_TYPE_DROPDOWN = 7;
    public static final int INPUT_TYPE_URL = 8;
    public static final int INPUT_TYPE_MOBILE_NUMBER = 9;
    public static final int INPUT_TYPE_LANDLINE_NUMBER = 10;
    public static final int INPUT_TYPE_EMAIL = 11;
    public static final int INPUT_TYPE_PHONE = 12;
    public static final int INPUT_TYPE_WARD = 13;
    public static final int INPUT_TYPE_PASSWORD = 14;

    private boolean required;
    private int underlineColor = Color.argb(255, 121, 121, 121);
    TextInputLayout textInputLayout;
    TextInputEditText editText;
    private String hint, error;
    private int inputType;
    private int minValue, maxValue;
    private String validCharset;
    private int maxLength;

    public Context context;

    public FormInputLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FormInputLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FormInputLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public FormInputLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FormInputView, 0, 0);
        required = a.getBoolean(R.styleable.FormInputView_required, false);
        underlineColor = a.getColor(R.styleable.FormInputView_underlineColor, underlineColor);
        inputType = a.getColor(R.styleable.FormInputView_inputType, 0);
        hint = a.getString(R.styleable.FormInputView_hint);
        error = a.getString(R.styleable.FormInputView_errorText);

        minValue = a.getInteger(R.styleable.FormInputView_min, -999999999);
        maxValue = a.getInteger(R.styleable.FormInputView_max, 999999999);


        if (error == null) error = "Invalid";
        a.recycle();
      /*  textInputLayout = new TextInputLayout(new ContextThemeWrapper(context, R.style.InputFieldLayout));
        editText = new TextInputEditText(new ContextThemeWrapper(context, R.style.InputFieldLayout));
*/
        View root = View.inflate(context, R.layout.form_layout, this);

        textInputLayout = root.findViewById(R.id.textLayout);
        editText = root.findViewById(R.id.text);
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setHintTextAppearance(R.style.InputFieldLayout);
        textInputLayout.setErrorTextAppearance(R.style.InputFieldError);

      //  editText.setMaxLines(1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editText.setBackgroundTintList(ColorStateList.valueOf(underlineColor));
        }

        updateHint();
        updateInputType();
        //   textInputLayout.addView(editText);
        //   addView(textInputLayout);
        editText.setOnFocusChangeListener(handleFocusChange);
        editText.addTextChangedListener(handleTextChange);

    }

    private void updateHint() {
        if (hint == null) return;
        if (required)
            textInputLayout.setHint(hint + "*");
        else
            textInputLayout.setHint(hint + " (optional)");
    }

    private void updateInputType() {

        switch (inputType) {

            case INPUT_TYPE_PERSON_NAME:
                validCharset = Validators.ValidCharset.NAME;
                break;

            case INPUT_TYPE_NUMBER:
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;

            case INPUT_TYPE_WARD:
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;

            case INPUT_TYPE_PRICE:
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DataContracts.AMOUNT_LIMIT)});
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;

            case INPUT_TYPE_ADDRESS:
                validCharset = Validators.ValidCharset.ADDRESS;
                editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                break;

            case INPUT_TYPE_ALPHA_NUMERIC:
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                break;

            case INPUT_TYPE_DROPDOWN:
                editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
                //  setDrawableRightExtra(android.R.drawable.arrow_down_float,18);
                editText.setFocusable(false);
                break;

            case INPUT_TYPE_URL:
                validCharset = Validators.ValidCharset.YOUTUUBE;
                break;

            case INPUT_TYPE_MOBILE_NUMBER:
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DataContracts.MOBILE_NO_LIMIT)});
                break;
            case INPUT_TYPE_LANDLINE_NUMBER:
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DataContracts.LANDLINE_NO_LIMIT)});
                break;
            case INPUT_TYPE_EMAIL:
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                break;
            case INPUT_TYPE_PHONE:
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case INPUT_TYPE_PASSWORD:
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
             default:
                editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                break;



        }
    }

    public void setStyle() {
        this.textInputLayout = new TextInputLayout(new ContextThemeWrapper(context, R.style.InputFieldLayout));
        this.editText = new TextInputEditText(new ContextThemeWrapper(context, R.style.InputFieldLayout));
     /*   textInputLayout.addView(editText);
        addView(textInputLayout);*/

    }

    public void setMaxLength(int lenth) {
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(lenth)});
    }

    public void setHint(String hint) {
        if (hint == null) return;
        if (required)
            textInputLayout.setHint(hint + "*");
        else
            textInputLayout.setHint(hint + " (optional)");
    }

    public void setRequired(boolean required) {
        this.required = required;
        updateHint();
    }

    OnFocusChangeListener handleFocusChange = (v, hasFocus) -> {
        if (!hasFocus) {
            isValid();
        }
    };

    String temp;

    /**
     * Remove Error When User is Typing
     */

    TextWatcher handleTextChange = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(s))
                textInputLayout.setError("");

            if (containsValidCharset(s.toString())) {
                temp = s.toString();
            } else {
                editText.setText(temp);
                editText.setSelection(temp.length());
            }
        }
    };

    private boolean containsValidCharset(String s) {
        if (validCharset == null) return true;
        for (int i = 0; i < s.length(); i++) {
            if (!validCharset.contains(String.valueOf(s.charAt(i))))
                return false;
        }
        return true;
    }

    public boolean isValid() {
        if (isEmpty()) {
            if (required) {
                if (hint == null) {
                    textInputLayout.setError("Required");
                } else {
                    textInputLayout.setError(hint + " " + getResources().getString(R.string.is_required));
                }
                return false;
            } else {
                textInputLayout.setError("");
                return true;
            }
        } else {
            if (isValidInput(getText())) {
                textInputLayout.setError("");
                return true;
            } else {
                textInputLayout.setError(error);
                return false;
            }
        }
    }

    private boolean isValidInput(String text) {
        switch (inputType) {

            case INPUT_TYPE_TEXT:
                // Assuming every input is true for text
                return true;

            case INPUT_TYPE_ADDRESS:
                return Validators.isValidAddress(text);

            case INPUT_TYPE_NUMBER:
            case INPUT_TYPE_WARD:
                return isValidNumber(text, minValue, maxValue);

            case INPUT_TYPE_PRICE:
                return Validators.isValidPrice(text);

            case INPUT_TYPE_ALPHA_NUMERIC:
                return Validators.isValidIdNumber(text);

            case INPUT_TYPE_URL:
                return Validators.isValidURL(text);
            case INPUT_TYPE_MOBILE_NUMBER:
                return Validators.isValidPhone(text);
            case INPUT_TYPE_LANDLINE_NUMBER:
                return Validators.isValidLandline(text);
            case INPUT_TYPE_EMAIL:
                return Validators.isValidEmail(text);
            default:
                return true;
        }
    }

    public boolean isEmpty() {
        return editText.getText() == null || TextUtils.isEmpty(getText());
    }

    public String getText() {
        return editText.getText().toString().trim();
    }

    public void setText(String text) {
        editText.setText(text);
    }

    public void setEnabled(boolean enabled) {
        editText.setEnabled(enabled);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
        editText.setOnClickListener(l);
    }

    public void setDrawableLeft(int drawableLeft, int drawablePadding) {
        editText.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, 0, 0, 0);
        editText.setCompoundDrawablePadding(drawablePadding);
    }

    public void setDrawableLeftExtra(int drawableLeft, int drawablePadding) {
        textInputLayout.setStartIconDrawable(drawableLeft);
        editText.setCompoundDrawablePadding(drawablePadding);

    }

    public void setDrawableRight(int drawableRight, int drawablePadding) {
        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableRight, 0);
        editText.setCompoundDrawablePadding(drawablePadding);
    }

    public void setDrawableTop(int drawableTop, int drawablePadding) {
        editText.setCompoundDrawablesWithIntrinsicBounds(0, drawableTop, 0, 0);
        editText.setCompoundDrawablePadding(drawablePadding);
    }

    public void setDrawableBottom(int drawableBottom, int drawablePadding) {
        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, drawableBottom);
        editText.setCompoundDrawablePadding(drawablePadding);
    }

    public void setPaddingOutside(int left, int top, int right, int bottom) {
        textInputLayout.setPadding(left, top, right, bottom);
    }

    public void setPaddingInside(int left, int top, int right, int bottom) {
        editText.setPadding(left, top, right, bottom);
    }

    public void setMarginOutside(int left, int top, int right, int bottom) {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(left, top, right, bottom);
        textInputLayout.setLayoutParams(params);
    }

    public void visibilityPassword(boolean value) {
        textInputLayout.setPasswordVisibilityToggleEnabled(value);

    }

    public void setHelperMessage(String helpText) {
        textInputLayout.setHelperText(helpText);
    }

    public void setCounter(int max) {
        textInputLayout.setCounterEnabled(true);
        textInputLayout.setCounterMaxLength(max);
    }

    public void setMaxLines(int max) {
          editText.setMaxLines(max);
    }

}
