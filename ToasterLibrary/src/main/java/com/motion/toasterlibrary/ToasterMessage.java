package com.motion.toasterlibrary;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class ToasterMessage  extends Toast{
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public ToasterMessage(Context context) {
        super(context);
    }

    public static void s(Context c, String message ){
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
    }
}
