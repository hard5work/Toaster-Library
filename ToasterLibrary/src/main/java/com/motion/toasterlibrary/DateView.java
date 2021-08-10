package com.motion.toasterlibrary;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hornet.dateconverter.DateConverter;
import com.hornet.dateconverter.Model;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static java.lang.Integer.parseInt;


/**
 * Modified Date : 28/07/2021
 * Modified By: Anish Maharjan
 * <p>
 * Changes -
 * Validation in dates
 * Fixes on date min and max date
 * Fixes on UI on AD BS
 */

/**
 If issued is occured Please Use this in Implementation

 configurations {
 cleanedAnnotations
 compile.exclude group: 'org.jetbrains' , module:'annotations'
 }


 * */
public class DateView extends LinearLayout {

    private static final String TAG = DateView.class.getSimpleName();
    private static final int MAX_DATE = 32;
    private static final int MAX_MONTH = 12;
    public static final String BS = "BS";
    public static final String AD = "AD";


    private static long minDate = 0, maxDate = 0;
    // private static long enterdDate = 0;

    String mode = AD;

    private TextView dateFormat;
    private TextInputLayout dateYearLayout;
    private TextInputEditText dateYear;
    private TextInputLayout dateMonthLayout;
    private TextInputEditText dateMonth;
    private TextInputLayout dateDayLayout;
    private TextInputEditText dateDay;
    private TextView title;

    private String _defaultYear;
    private String _defaultMonth;
    private String _defaultDay;
    private int _defaultFormat;
    private boolean _fixedDateFormat;
    private int focusedView;

    public static String minDay, minMonth, minYear;
    public static String maxDay, maxMonth, maxYear;
    public static int minDayi, minMonthi, minYeari;
    public static int maxDayi, maxMonthi, maxYeari;

    public DateView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public DateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public DateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStylesRes) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DateView, defStyleAttr, defStylesRes);

        _defaultYear = a.getString(R.styleable.DateView_default_year);
        _defaultMonth = a.getString(R.styleable.DateView_default_month);
        _defaultDay = a.getString(R.styleable.DateView_default_day);
        _defaultFormat = a.getInteger(R.styleable.DateView_default_format, 0);
        _fixedDateFormat = a.getBoolean(R.styleable.DateView_fixed_format, false);

        a.recycle();

        View root = View.inflate(context, R.layout.date_view, this);
        dateFormat = root.findViewById(R.id.date_format);
        dateYearLayout = root.findViewById(R.id.date_year_layout);
        dateYear = root.findViewById(R.id.date_year);
        dateMonthLayout = root.findViewById(R.id.date_month_layout);
        dateMonth = root.findViewById(R.id.date_month);
        dateDayLayout = root.findViewById(R.id.date_day_layout);
        dateDay = root.findViewById(R.id.date_day);
        title = root.findViewById(R.id.title);


        dateYearLayout.setErrorTextAppearance(R.style.InputFieldError);
        dateMonthLayout.setErrorTextAppearance(R.style.InputFieldError);
        dateDayLayout.setErrorTextAppearance(R.style.InputFieldError);

        dateFormat.setText("AD");
        this.mode = AD;

        initValidations();
        // setting default values
    }

    public String getTextMain() {
      /*  try {
            getDatesEntered();
        }catch (ParseException e){
            Log.e(TAG, "getTextMain: " + e.getMessage() );
        }
*/
        return dateYear.getText().toString() + "-" + dateMonth.getText().toString() + "-" + dateDay.getText().toString();
    }

    public void updateDateMode(String newMode) {
        //  Log.e(TAG, "updateDateMode: " + getTextMain() + " " + this.mode + " " + newMode);
        if (this.mode.equals(AD) && newMode.equals(BS)) {
            setText(getDate(BS));
        } else if (this.mode.equals(BS) && newMode.equals(AD)) {
            setText(getDate(AD));
        }
        this.mode = newMode;
    }

    public void setText(String s) {
        //   Log.e(TAG, "setText: String I got " + s);
        if (!TextUtils.isEmpty(s)) {
            dateDayLayout.setErrorEnabled(false);
            dateMonthLayout.setErrorEnabled(false);
            dateYearLayout.setErrorEnabled(false);
        }
        try {
            if (s != null) {
                //      Log.e(TAG, "setText: " + s);
                String[] vals = s.split("-");

                Log.e(TAG, "setText: " + vals[0] + "-" + vals[1] + "-" + vals[2]);
             /*   Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {*/
                // Do something after 5s = 5000ms
                dateYear.setText(vals[0]);
                dateMonth.setText(vals[1]);
                dateDay.setText(vals[2]);
                dateYear.clearFocus();
                dateMonth.clearFocus();
                dateDay.clearFocus();
                dateFormat.setClickable(true);
         /*           }
                }, 500);*/


            }
        } catch (Exception e) {

            dateFormat.setClickable(true);
            Log.e(TAG, "setText: " + e.getMessage());
            if (!isEmpty())
                Toast.makeText(getContext(), "Sorry! Invalid Date Found. Please Try Again.", Toast.LENGTH_SHORT).show();
            dateYear.setText("");
            dateMonth.setText("");
            dateDay.setText("");

            dateYear.clearFocus();
            dateMonth.clearFocus();
            dateDay.clearFocus();
        }
    }

    public String getDate(String mode) {

        try {
            if (mode.equals(BS))
                return getDateBS();
            else
                return getDateAD();
        } catch (Exception e) {
            return "";
        }
    }

    public String getFinalDate() {
        if (mode.equals(AD)) {
            return getTextMain();
        } else
            return getADforFinal();
    }

    private String getDateBS() throws Exception {
        if (isEmpty()) return "";

        if (this.mode.equals(BS)) {
            return getTextMain();
        }

        Log.e(TAG, "getDateBS: to change  " + getTextMain());

        DateConverter dc = new DateConverter();

        String[] engDate = getTextMain().split("-");
        if (engDate.length != 3) return "";

        Model dcOutput = dc.getNepaliDate(parseInt(engDate[0]), parseInt(engDate[1]), parseInt(engDate[2]));
        String npDate = setFormattedDate(dcOutput.getYear(), dcOutput.getMonth() + 1, dcOutput.getDay());

        return npDate;
    }

    private String getADforFinal() {

        if (isEmpty()) return "";

        if (this.mode.equals(AD)) {
            return getTextMain();
        }

        Log.e(TAG, "This is for final Value " + getTextMain());
        DateConverter dc = new DateConverter();

        String[] npDate = getTextMain().split("-");
        if (npDate.length != 3) return "";

        Model dcOutput = dc.getEnglishDate(parseInt(npDate[0]), parseInt(npDate[1]), parseInt(npDate[2]));
        String engDate = setFormattedDate(dcOutput.getYear(), dcOutput.getMonth() + 1, dcOutput.getDay());
        return engDate;
    }


    private String getDateAD() throws Exception {

        if (isEmpty()) return "";

        if (this.mode.equals(AD)) {
            return getTextMain();
        }

        Log.e(TAG, "getDateAD:  to change " + getTextMain());
        DateConverter dc = new DateConverter();

        String[] npDate = getTextMain().split("-");
        if (npDate.length != 3) return "";

        Model dcOutput = dc.getEnglishDate(parseInt(npDate[0]), parseInt(npDate[1]), parseInt(npDate[2]));
        String engDate = setFormattedDate(dcOutput.getYear(), dcOutput.getMonth() + 1, dcOutput.getDay());
        return engDate;
    }

    private String setFormattedDate(int year, int monthOfYear, int dayOfMonth) {
        return String.format(Locale.ENGLISH, "%s-%02d-%02d", year, monthOfYear, dayOfMonth);
    }


    private boolean isValidDate(String input) {

        if (mode.equals(AD)) {


            String formatString = "yyyy-MM-dd";

            try {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(formatString);
                format.setLenient(false);
                format.parse(input);
                return true;
            } catch (ParseException e) {
                dateDayLayout.setError("Invalid");
                return false;
            } catch (IllegalArgumentException e) {
                dateDayLayout.setError("Invalid");
                return false;
            }
        } else if (mode.equals(BS)) {
            return isValidDateBS(input);
        }

        return false;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // setFormat(Mode.values()[_defaultFormat], false);
        // setDefaultDate();

        // if (!_fixedDateFormat)
        dateFormat.setOnClickListener(v -> {

            //   Mode nextFormat = Mode.nextFormat(_defaultFormat);
            dateFormat.setClickable(false);
         /*   if(dateDayLayout.isErrorEnabled() || dateYearLayout.isErrorEnabled() || dateMonthLayout.isErrorEnabled() ){
                Log.e(TAG, "onFinishInflate: Error Has Occured"  );
            }
            else{
                Log.e(TAG, "onFinishInflate: I am Goooooood"  );
            }*/
            //  setFormat(nextFormat, true);

            if (this.mode.equals("AD")) {
                dateFormat.setText(BS);
                // callChanger(BS);
                updateDateMode(BS);
                Log.e(TAG, "onFinishInflate: AD");

            } else if (this.mode.equals("BS")) {
                dateFormat.setText(AD);
                updateDateMode(AD);
                //callChanger(AD);
                Log.e(TAG, "onFinishInflate: BS");

            }
            if (maxDate != 0 && minDate != 0) {
                Log.e(TAG, "onFinishInflate: " + " checked frm button dear");
                if (!isEmpty())
                    checkMinDates(getInteger(getYear()), getInteger(getMonth()), getInteger(getDay()));
            }

        });


    }

    public void callChanger(String date) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    updateDateMode(date);
                } catch (Exception e) {
                    Log.e(TAG, "run: " + e.getMessage());
                }
            }
        }).start();
    }

    /**
     * Set today's date.
     */
    public void setDefaultDate() {
        // TODO: 1/6/20 get current  year and month and date
        setYear(_defaultYear == null || _defaultYear.isEmpty() ? "2020" : _defaultYear);
        setMonth(_defaultMonth == null || _defaultMonth.isEmpty() ? "01" : _defaultMonth);
        setDay(_defaultDay == null || _defaultDay.isEmpty() ? "01" : _defaultDay);
    }


    public void setYear(String year) {
        // TODO validate the year
        dateYear.setText(year);
    }

    public void setDay(String day) {
        dateDay.setText(day);
    }

    public String getDay() {
        return dateDay.getText().toString();
    }

    public String getYear() {
        return dateYear.getText().toString();
    }

    public void setMonth(String month) {
        dateMonth.setText(month);
    }

    public String getMonth() {
        return dateMonth.getText().toString();
    }



    public String getFormat() {
        return dateFormat.getText().toString();
    }

    public void clearDates() {
        dateYear.setText("");
        dateMonth.setText("");
        dateDay.setText("");
    }

    public void initValidations() {
        dateDay.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        dateDay.setMaxEms(2);
        dateDayLayout.setCounterMaxLength(2);
        dateDay.addTextChangedListener(dayChangeListener);
        dateDay.setOnFocusChangeListener(focusChangeListener);

        dateMonth.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        dateMonth.setMaxEms(2);
        dateMonthLayout.setCounterMaxLength(2);
        dateMonth.addTextChangedListener(monthChangeListener);
        dateMonth.setOnFocusChangeListener(focusChangeListener);

        dateYear.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        dateYear.setMaxEms(4);
        dateYearLayout.setCounterMaxLength(2);
        dateYear.addTextChangedListener(yearChangeListener);
        dateYear.setOnFocusChangeListener(focusChangeListener);
    }

    /**
     * Formats the current field to whatever is necessary. Currently adds only zeros where necessary.
     */
    private void finishCurrentField(Editable editable) {
        if (focusedView == R.id.date_day) {//  enableTextChangeListener = false;
            if (editable.length() == 1) {
                // adapt but using editable
                editable.insert(0, "0");
                // day = "0" + day;
            } else if (editable.length() == 2) {
                // correct highlighting maybe?!
            } else {
                editable.clear();
                editable.insert(0, "??");
                // day = "??";
                dateDayLayout.setError("Error");
            }
        } else if (focusedView == R.id.date_month) {//  enableTextChangeListener = false;
            // String month = dateMonth.getText().toString();
            if (editable.length() == 1) {
                // adapt but using editable
                editable.insert(0, "0");
            } else if (editable.length() == 2) {
                // correct highlighting maybe?!
            } else {
                editable.clear();
                editable.insert(0, "??");
                dateMonthLayout.setError("Error");
            }
        } else if (focusedView == R.id.date_year) {// enableTextChangeListener = false;

            if (editable.length() <= 3) {
                editable.clear();
                // adapt but using editable
                editable.insert(0, insetMask(editable.toString(), "2010"));
            } else if (editable.length() == 4) {
                // correct highlighting maybe?!
            } else {
                editable.clear();
                editable.insert(0, "????");
                dateYearLayout.setError("Error");
            }
        } else {
            Log.e(TAG, "finishCurrentField: unknown view id");
        }
    }

    /**
     * Focus to next field. Focus priority is given to one which does not have input value yet or simply
     * to the field to the right.
     */
    private void focusNextField() {
        int nextField = -1;

        if (focusedView == R.id.date_day) {// meaning check other empty fields
            if (validateDay(false)) {
                nextField = findInvalidField();
            } else {
                dateDay.selectAll();
            }
        } else if (focusedView == R.id.date_month) {
            if (validateMonth(false)) {
                if (validateDay(false)) {
                    nextField = findInvalidField(); // most likely year
                } else {
                    dateDay.requestFocus();
                }
            } else {
                dateMonth.selectAll();
            }
        } else if (focusedView == R.id.date_year) {
            if (validateYear(false)) {
                if (validateMonth(false)) {
                    if (validateDay(false)) {
                        nextField = -1;
                    } else {
                        dateDay.requestFocus();
                    }
                } else {
                    dateMonth.requestFocus();
                }
            } else {
                dateYear.selectAll();
            }
        }
        if (nextField != -1) {
            findViewById(nextField).requestFocus();
        }
    }

    private int findInvalidField() {
        if (!validateDay(false)) return R.id.date_day;
        else if (!validateMonth(false)) return R.id.date_month;
        else if (!validateYear(false)) return R.id.date_year;
        else return -1;
    }

    private boolean validateDay(boolean adapt) {
        // TODO: 1/6/20 validate with current date format
        int date = TestUtils.isInteger(dateDay.getText().toString(), -1);
        if (date == -1 || date > MAX_DATE) {
            return false;
        }

        if (date < 10 && adapt) { // only imperfect but valid date should be adapted else unnecessary process is executed
            //    enableTextChangeListener = false;
            setDay(insetMask(dateDay.getText().toString(), "01"));
        }
        return true;
    }

    private boolean validateMonth(boolean adapt) {
        // TODO: 1/6/20 validate with current date format
        int month = TestUtils.isInteger(dateMonth.getText().toString(), -1);
        if (month == -1) {
            return false;
        }

        // if invalid then return
        if (!(month <= MAX_MONTH && month > 0)) {
            return false;
        }

        if (month < 10 && adapt) { // only imperfect but valid month should be adapted else unnecessary process is executed
            //    enableTextChangeListener = false;
            setMonth(insetMask(dateMonth.getText().toString(), "01"));
        }

        return true;
    }

    private boolean validateYear(boolean adapt) {
        // TODO: 1/6/20 validate with current date format
        int year = TestUtils.isInteger(dateYear.getText().toString(), -1);
        if (year == -1) {
            return false;
        }

        if (!(year <= 9999 && year > 0)) {
            return false;
        }

        if (year < 1000 && adapt) {
            //  enableTextChangeListener = false;
            setYear(insetMask(dateYear.getText().toString(), "2000"));
        }
        return !dateYear.getText().toString().isEmpty();
    }

    private OnFocusChangeListener focusChangeListener = (v, hasFocus) -> {
        if (!hasFocus) {
            int id = v.getId();
            if (id == R.id.date_year) {
                validateYear(true);
                if (dateYear.length() == 4 && dateMonth.length() == 2 && dateDay.length() == 2) {
                    if (maxDate != 0 && minDate != 0) {
                        if (!isEmpty()) {
                            checkMinDates(getInteger(getYear()), getInteger(getMonth()), getInteger(getDay()));
                        }
                    }
                }
            } else if (id == R.id.date_month) {
                validateMonth(true);
                if (getMonth().equals("00")) {
                    dateMonth.setText("");
                }
                if (dateYear.length() == 4 && dateMonth.length() == 2 && dateDay.length() == 2) {
                    if (maxDate != 0 && minDate != 0) {
                        if (!isEmpty()) {
                            checkMinDates(getInteger(getYear()), getInteger(getMonth()), getInteger(getDay()));
                        }
                    }
                }
            } else if (id == R.id.date_day) {
                validateDay(true);
                if (getDay().equals("00")) {
                    dateDay.setText("");
                }
                if (dateYear.length() == 4 && dateMonth.length() == 2 && dateDay.length() == 2) {
                    if (maxDate != 0 && minDate != 0) {
                        if (!isEmpty()) {
                            checkMinDates(getInteger(getYear()), getInteger(getMonth()), getInteger(getDay()));
                        }
                    }
                }
            } else {
                Log.w(TAG, "focusChangeListener: Unknown id");
            }
        }
        if (hasFocus) focusedView = v.getId();
/*
        if (dateYear.length() == 4 && dateMonth.length() == 2 && dateDay.length() == 2) {
            if (maxDate != 0 && minDate != 0) {
                if (!isEmpty()) {
                    Log.e(TAG, ": I am calllllllleeeeeeeeeeeed");
                    checkMinDates(getInteger(getYear()), getInteger(getMonth()), getInteger(getDay()));
                }
            }
        }*/

      /*  if (!isEmpty()){
            isValidDate(getTextMain());

        }
*/
    /*    if (dateYear.length() == 4 && dateMonth.length()==2 && dateDay.length()==2){
            dateDay.clearFocus();
            dateMonth.clearFocus();
            dateYear.clearFocus();
        }*/


    };

    public int getInteger(String dates) {
        return parseInt(dates);
    }

    /**
     * Toggle the change listener. Trying to change the text using Editable or directly assigning the values will cause recursive call to TextWatcher.
     */
    private boolean enableTextChangeListener = true;

    public TextWatcher dayChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
           /* if (!enableTextChangeListener) {
                enableTextChangeListener = true;
                return;
            }*/

            int day = 1;
            try {
                day = parseInt(s.toString());
            } catch (NumberFormatException ignored) {
            }

            // set max length 2
            if (s.length() == 2) {
                finishCurrentField(s);
               /* if (maxDate != 0 && minDate != 0){
                    Log.e(TAG, "afterTextChanged: " + checkMinDates(parseInt(s.toString()),"DAY"));
                    if (!checkMinDates(parseInt(s.toString()),"DAY")){
                        dateDayLayout.setError("Invalid");
                    }
                    else
                        dateDayLayout.setError("");
                }*/
                //   focusNextField();
            }

       /*     if (s.length() > 2) { // copy/paste situation
             //   enableTextChangeListener = false;
                s.delete(2, s.length());
                finishCurrentField(s);
                focusNextField();
            }*/

            if (s.toString().length() == 2) {
                if (dateMonth.getText().toString().isEmpty()) {
                    dateMonth.requestFocus();
                }
            }

          /*  if (s.toString().length() ==1 && String.valueOf(dateDay.getText().toString().charAt(0)).equals("0")){
               dateDay.setKeyListener(DigitsKeyListener.getInstance("123456789"));
            }*/
            if (s.toString().length() == 1 && day > 4) { // trying to type '4' or more, means the second letter is not required.
                finishCurrentField(s);
                //  focusNextField();
            }

            if (day > 32) { // TODO: 1/6/20 check with max day for current format & with max date setting
                //   enableTextChangeListener = false;
                // delete that very last character
                s.delete(s.length() - 1, s.length());
                // finish because the user has probably meant to finish
                finishCurrentField(s);
                //  focusNextField();
            }

        }
    };

    public TextWatcher monthChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
           /* if (!enableTextChangeListener) {
                enableTextChangeListener = true;
                return;
            }*/

            int month = 1;
            try {
                month = parseInt(s.toString());
            } catch (NumberFormatException ignored) {
            }
            if (s.length() == 0) return;

            // set max length 2
         /*   if (s.length() > 2) {
           //     enableTextChangeListener = false;
                s.delete(2, s.length());
                finishCurrentField(s);
                focusNextField();
            }*/

            // check for 1 char or 2 chars

            if (s.toString().length() == 1 && month > 1) { // trying to type '2' or more, means the second letter is not required
                finishCurrentField(s);
                //2 focusNextField();
            }
            /*if (s.toString().length() ==1 && String.valueOf(dateMonth.getText().toString().charAt(0)).equals("0")){
                dateMonth.setKeyListener(DigitsKeyListener.getInstance("123456789"));
            }*/
            if (s.length() == 2) {
                finishCurrentField(s);
                dateDay.requestFocus();
              /*  if (maxDate != 0 && minDate != 0){

                    Log.e(TAG, "afterTextChanged: " + checkMinDates(parseInt(s.toString()),"MONTH"));
                    if (!checkMinDates(parseInt(s.toString()),"MONTH")){
                        dateMonthLayout.setError("Invalid");
                    }
                    else
                        dateMonthLayout.setError("");
                }*/
                //   focusNextField();
            }

/*
            if (month >= 10 &&  month  <= 12){
                finishCurrentField(s);
                //  focusNextField();
            }*/
            if (month > 12) {
                // delete that very last character
                s.delete(s.length() - 1, s.length());
            } else if (month >= 10) { // && <= 12
                finishCurrentField(s);
                // focusNextField();
            }
        }
    };

    public TextWatcher yearChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
          /*  if (!enableTextChangeListener) {
                enableTextChangeListener = true;
                return;
            }*/

            if (s.length() == 4) { // check if within range
                // finishCurrentField(s);
                // focusNextField();
                dateMonth.requestFocus();
            }

           /* if (s.length() == 4) { // check if within range
                // finishCurrentField(s);
                // focusNextField();
                //  dateMonth.requestFocus();

                if (maxDate != 0 && minDate != 0) {

                    Log.e(TAG, "afterTextChanged: " + checkMinDates(parseInt(s.toString()), "YEAR"));
                    if (!checkMinDates(parseInt(s.toString()), "YEAR")) {
                        dateYearLayout.setError("Invalid");
                    }
                }
            }*/


            // set max length 4, delete the last char
         /*   if (s.length() > 4) {
              //  enableTextChangeListener = false;
                s.delete(4, s.length() + 1);
                finishCurrentField(s);
                focusNextField();
            }*/

            // int year = 2000; // get default year
            // try {
            //     year = Integer.parseInt(s.toString());
            // } catch (NumberFormatException ignored) { }
            // if (s.length() == 0) return;
        }
    };

    /**
     * if mask = "2019" and val is "3" then your final result is "2013". This is useful if user just wants
     * to type "20" to get "2020".
     *
     * @param val  actual value
     * @param mask masking value
     * @return masked value
     */
    private String insetMask(String val, String mask) {
        // val.length should not be greater than mask.length
        if (val.length() > mask.length()) return val;
        int charsUnmasked = mask.length() - val.length();

        String first = "";
        if (charsUnmasked != 0)
            first = mask.substring(0, charsUnmasked);

        return first + val;
    }


    boolean year = true, month = true, day = true;

    public boolean checkMinDates(int values, String tag) {

        if (tag.equals("YEAR")) {

            if (mode.equals("AD")) {
                if (values <= maxYeari && minYeari <= values) {
                    year = false;
                    return true;
                }
            } else {
                try {
                    String changer = String.valueOf(values);
                    changer += "-01-01";
                    String changed = changeBStoAD(changer).split("-")[0];
                    values = parseInt(changed);
                    if (values <= maxYeari && minYeari <= values) {
                        year = false;
                        return true;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "checkMinDates: " + e.getMessage());
                }


            }

        }

        return false;
    }

    public String changeBStoAD(String changeThis) {
        Log.e(TAG, "Change BS TO AD:  to change " + changeThis);
        DateConverter dc = new DateConverter();
        String engDate = "";
        String[] npDate = changeThis.split("-");

        try {
            Model dcOutput = dc.getEnglishDate(parseInt(npDate[0]), parseInt(npDate[1]), parseInt(npDate[2]));
            engDate = setFormattedDate(dcOutput.getYear(), dcOutput.getMonth() + 1, dcOutput.getDay());

            Log.e(TAG, "changeBStoAD: " + engDate);
        } catch (Exception e) {
            Log.e(TAG, "changeBStoAD: " + e.getMessage());
        }
        return engDate;
    }

    public void checkMinDates(int year, int month, int day) {
        if (mode.equals(BS)) {
            if (getTextMain().isEmpty()) return;
            try {
                String[] enDate = changeBStoAD(getTextMain()).split("-");
                year = parseInt(enDate[0]);
                month = parseInt(enDate[1]);
                day = parseInt(enDate[2]);
            } catch (Exception e) {
                Log.e(TAG, "checkMinDates: " + e.getMessage());
            }
        }

        if (year < maxYeari && minYeari < year) {
            dateYearLayout.setError("");
            dateMonthLayout.setError("");
            dateDayLayout.setError("");
        } else if (year == minYeari) {
            dateYearLayout.setError("");
            if (month >= minMonthi) {
                dateMonthLayout.setError("");
                dateDayLayout.setError("");
/*
                Log.e(TAG, "checkMinDates: " + enterdDate + " >=" + minDate);
                if (enterdDate >= minDate) {

                    dateDayLayout.setError("");
                } else dateDayLayout.setError("Invalid");*/
            } else {
                dateMonthLayout.setError("Invalid");
                // dateDayLayout.setError("Invalid");

            }
        } else if (year == maxYeari) {
            dateYearLayout.setError("");
            if (month <= maxMonthi) {
                dateMonthLayout.setError("");

                dateDayLayout.setError("");
            /*    Log.e(TAG, "checkMinDates: " + enterdDate + " <=" + maxDate);
                if (enterdDate <= maxDate) {
                    dateDayLayout.setError("");
                } else dateDayLayout.setError("Invalid");*/
            } else {
                dateMonthLayout.setError("Invalid");
                // dateDayLayout.setError("Invalid");

            }
        } else {
            dateYearLayout.setError("Invalid");
            dateMonthLayout.setError("Invalid");
            dateDayLayout.setError("Invalid");

        }
    }

    public boolean checkDateValid() {
        int year = getInteger(getYear());
        int month = getInteger(getMonth());
        int day = getInteger(getDay());

        if (mode.equals(BS)) {
            String[] enDate = changeBStoAD(getTextMain()).split("-");
            Log.e(TAG, "checkDateValid: " + enDate.length );
            if (enDate.length ==3) {
                year = parseInt(enDate[0]);
                month = parseInt(enDate[1]);
                day = parseInt(enDate[2]);
            }
        }

      /*  if (year < maxYeari && minYeari < year) {
            Log.e(TAG, "checkDateValid: year ><" + year);
            return true;
        }*/


        Log.e(TAG, "checkDateValid:  I am checking the valid Date " + mode + " " + year + " " + month + " " + day);
        if (checkLessYear(year)) {
            return true;
        } else if (checkMaxYear(year, month, day)) {
            return true;
        } else if (checkMinYear(year, month, day)) {
            return true;
        } else
            return false;

    }

    /**
     * old codes if new doesnot work
     * if (year == minYeari) {
     * <p>
     * Log.e(TAG, "checkDateValid: year == " + year);
     * if (month >= minMonthi) {
     * <p>
     * if (day >= minDayi) {
     * return true;
     * } else return false;
     * } else {
     * return false;
     * <p>
     * }
     * }
     * if (year == maxYeari) {
     * if (month <= maxMonthi) {
     * if (day <= maxDayi) {
     * return true;
     * } else return false;
     * } else {
     * return false;
     * <p>
     * }
     * }
     */


    public boolean checkLessYear(int year) {
        if (year < maxYeari && minYeari < year) {
            return true;
        } else
            return false;
    }

    public boolean checkMaxYear(int year, int month, int day) {
        if (year == maxYeari) {
            if (month <= maxMonthi) {
                return true;
              /*  if (enterdDate <= maxDate) {
                    return true;
                } else return false;*/
            } else {
                Log.e(TAG, "checkMaxYear:  max month");
                return false;

            }
        } else
            return false;
    }

    public boolean checkMinYear(int year, int month, int day) {
        if (year == minYeari) {

            if (month >= minMonthi) {
                return true;
             /*   if (enterdDate >= minDate) {
                    return true;
                } else return false;*/
            } else {
                return false;

            }
        } else
            return false;
    }

    private boolean isValidDateBS(String input) {
        String engDate = "";
        DateConverter dc = new DateConverter();

        Log.e(TAG, "isValidDateBS: " + input);
        String[] npDate = input.split("-");
        if (npDate.length != 3) return false;
        try {
            Model dcOutput = dc.getEnglishDate(Integer.parseInt(npDate[0]), Integer.parseInt(npDate[1]), Integer.parseInt(npDate[2]));
            engDate = setFormattedDate(dcOutput.getYear(), dcOutput.getMonth() + 1, dcOutput.getDay());
        } catch (Exception e) {
            //   Log.e(TAG, "isValidDateBS: " + e.getMessage());
            dateDayLayout.setError("Invalid");
        }


        DateConverter dc2 = new DateConverter();

        String[] engDate2 = engDate.split("-");
        if (engDate2.length != 3) return false;

        Model dcOutput2 = dc2.getNepaliDate(Integer.parseInt(engDate2[0]), Integer.parseInt(engDate2[1]), Integer.parseInt(engDate2[2]));
        String npDate2 = setFormattedDate(dcOutput2.getYear(), dcOutput2.getMonth() + 1, dcOutput2.getDay());

        Log.e(TAG, "isValidDateBS: the Result" + npDate2);
        Log.e(TAG, "isValidDateBS: " + input);
        if (!npDate2.equals(input)) {
            dateDayLayout.setError("Invalid");
        }
        return true;
    }




    public boolean isValidManual() {
        if (!isEmpty()) {

            //For checking if the date has max date or not
            if (maxDate != 0 && minDate != 0) {
                if (checkDateValid()) {
                    setError("");
                    return isValidDate(getTextMain());
                } else {
                    setError("Invalid");
                    return false;
                }

            } else {
                setError("");
                return isValidDate(getTextMain());
            }
        } else
            setError("Invalid");


        return false;
    }


    public boolean isEmpty() {
        if (!dateYear.getText().toString().isEmpty() && !dateDay.getText().toString().isEmpty() && !dateMonth.getText().toString().isEmpty())
            return false;

        return true;
    }

    public void setTitle(String msg) {
        if (msg == null) title.setVisibility(GONE);
        else {
            title.setVisibility(VISIBLE);
            title.setText(msg);
        }
    }

    public void setError(String msg) {
        if (msg == null) {
            dateYearLayout.setError("");
            dateMonthLayout.setError("");
            dateDayLayout.setError("");
        } else {
            dateYearLayout.setError(msg);
            dateMonthLayout.setError(msg);
            dateDayLayout.setError(msg);
         /*   if (dateYear.getText().toString().isEmpty())
                dateYearLayout.setError(msg);
            else
                dateYearLayout.setError("");
            if (dateMonth.getText().toString().isEmpty())
                dateMonthLayout.setError(msg);
            else
                dateMonthLayout.setError("");

            if (dateDay.getText().toString().isEmpty())
                dateDayLayout.setError(msg);
            else
                dateDayLayout.setError("");*/
        }
    }

    public void setMaxDate(int calendar, int duration) {
        Calendar c = Calendar.getInstance();
        c.add(calendar, duration);
        this.maxDate = c.getTimeInMillis();
        if (this.maxDate != 0) {
            String maxDateFormat = Converter.beautifyLongTimeDate(this.maxDate);
            String[] maxDates = maxDateFormat.split("-");
            this.maxYear = maxDates[0];
            this.maxYeari = parseInt(this.maxYear);
            this.maxMonth = maxDates[1];
            this.maxDay = maxDates[2];
            this.maxMonthi = parseInt(this.maxMonth);
            this.maxDayi = parseInt(this.maxDay);

            Log.e(TAG, "init: max Date" + Converter.beautifyLongTimeDate(this.maxDate));
        }

    }

    public void setMinDate(int calendar, int duration) {
        Calendar c = Calendar.getInstance();
        c.add(calendar, duration);
        this.minDate = c.getTimeInMillis();
        if (this.minDate != 0) {
            String minDateFormat = Converter.beautifyLongTimeDate(this.minDate);
            String[] minDates = minDateFormat.split("-");
            this.minYear = minDates[0];
            this.minMonth = minDates[1];
            this.minDay = minDates[2];
            this.minYeari = parseInt(this.minYear);
            this.minMonthi = parseInt(this.minMonth);
            this.minDayi = parseInt(this.minDay);

            Log.e(TAG, "init: minDate" + Converter.beautifyLongTimeDate(this.minDate));

        }

    }

    public void setMaxDateFormat(String max) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.maxDate = simpleDateFormat.parse(max).getTime();
        if (this.maxDate != 0) {
            String maxDateFormat = Converter.beautifyLongTimeDate(this.maxDate);
            String[] maxDates = maxDateFormat.split("-");
            this.maxYear = maxDates[0];
            this.maxYeari = parseInt(this.maxYear);
            this.maxMonth = maxDates[1];
            this.maxDay = maxDates[2];
            this.maxMonthi = parseInt(this.maxMonth);
            this.maxDayi = parseInt(this.maxDay);

            Log.e(TAG, "init: max Date Format" + Converter.beautifyLongTimeDate(this.maxDate));
        }

    }

/*    public long getDatesEntered() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        enterdDate = simpleDateFormat.parse(getTextMain()).getTime();
        return enterdDate;
    }*/

    public void setMinDateFormat(String min) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.minDate = simpleDateFormat.parse(min).getTime();
        if (this.minDate != 0) {
            String minDateFormat = Converter.beautifyLongTimeDate(this.minDate);
            String[] minDates = minDateFormat.split("-");
            this.minYear = minDates[0];
            this.minMonth = minDates[1];
            this.minDay = minDates[2];
            this.minYeari = parseInt(this.minYear);
            this.minMonthi = parseInt(this.minMonth);
            this.minDayi = parseInt(this.minDay);

            Log.e(TAG, "init: minDateFormat" + Converter.beautifyLongTimeDate(this.minDate));

        }
    }


}


