package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.motion.toasterlibrary.CustomAlertDialog;
import com.motion.toasterlibrary.CustomProgressDialog;
import com.motion.toasterlibrary.ToasterMessage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class MainActivity extends AppCompatActivity {

    ToasterMessage toasterMessage;
    String TAG = "MainActivity";
    String tag = "MainActivity MainActivity MainActivity MainActivity MainActivity MainActivity MainActivity MainActivity MainActivity";
    Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        c = MainActivity.this;

        toasterMessage =  new ToasterMessage(MainActivity.this);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             // customAlertDialogs();
                progressDialogss();
              //  customToasts();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void customToasts(){
         /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
        toasterMessage.simpleToast(getApplicationContext(),TAG);
        toasterMessage.simpleToast(getApplicationContext(),tag);
        toasterMessage.showUserChoiceBackground(c,tag,R.drawable.ic_launcher_foreground);
        toasterMessage.showSuccessToast(getApplicationContext(),tag);
        toasterMessage.setGravity(Gravity.TOP,0,0);
        toasterMessage.setDuration(Toast.LENGTH_LONG);
        toasterMessage.showErrorToast(getApplicationContext(),TAG);
        toasterMessage.setGravity(Gravity.RIGHT,0,150);
        toasterMessage.setDuration(Toast.LENGTH_SHORT);
        toasterMessage.showWarningToast(getApplicationContext(),tag);
        toasterMessage.setGravity(Gravity.LEFT,100,0);
        toasterMessage.showUserChoice(c,tag,getResources().getColor(R.color.activetext));
        toasterMessage.setGravity(Gravity.CENTER,0,0);
        toasterMessage.showUserChoice(c,tag,getResources().getColor(R.color.inactive),getResources().getColor(R.color.backgrey));
        toasterMessage.showUserChoice(c,tag,getResources().getColor(R.color.colorAccent), getResources().getDrawable(R.drawable.ic_launcher_background),getResources().getColor(R.color.backgrey));

    }

    public void customAlertDialogs(){
        new CustomAlertDialog.Builder(this)
                .setTitle("Test")
                .setCancelable(false)
                .setPositiveButton("OK",(dialog, which) -> dialog.dismiss())
                .setNegativeButton("CANCEL",(dialog, which) -> dialog.dismiss())
                .setHighlightButtonColorNegative(getResources().getColor(R.color.bus_selected))
                .setHighlightButtonColorPositive(getResources().getColor(R.color.material_on_surface_disabled))
                .setAlertImage(getResources().getDrawable(android.R.drawable.btn_dialog),getResources().getColor(R.color.colorAccent))
             //   .setAlertImage(getResources().getDrawable(android.R.drawable.btn_dialog))
                .show();
    }

    public void progressDialogss(){
       /* CustomProgressDialog.Builder buid  = new CustomProgressDialog.Builder(this);
               buid.setCancelable(false);
               buid.show();*/


  /*   new  CustomProgressDialog(this)
             .setTitle("Success")
             .setMessage("Test is Successful, Enjoy")
             .isCancelable(false)
             .show();*/
        CustomProgressDialog sp = new CustomProgressDialog(this);
        sp.setTitle("ProgressDialog");
       sp.setMessage("Loading..");
        sp.isCancelable(false);
       sp.setBackgroundColor(getResources().getColor(R.color.bus_selected));
   //     sp.setBackground(getResources().getColor(R.color.bus_selected));
     //  sp.setIconGifWide();
     //  sp.setwideStyleColor(getResources().getColor(R.color.white));
       //sp.setBoth();
      // sp.setIcon(getResources().getDrawable(android.R.drawable.arrow_up_float));
        sp.show();

        CountDownTimer timer = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                sp.dismiss();
            }
        }.start();
    }
}