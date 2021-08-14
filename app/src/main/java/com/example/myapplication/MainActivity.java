package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.motion.toasterlibrary.classes.APIContracts;
import com.motion.toasterlibrary.productive.ApiCore;
import com.motion.toasterlibrary.productive.CustomAlertDialog;
import com.motion.toasterlibrary.productive.CustomProgressDialog;
import com.motion.toasterlibrary.productive.JsonUtils;
import com.motion.toasterlibrary.productive.ToasterMessage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;

public class MainActivity extends AppCompatActivity {

    ToasterMessage toasterMessage;
    String TAG = "MainActivity";
    String tag = "MainActivity MainActivity MainActivity MainActivity MainActivity MainActivity MainActivity MainActivity MainActivity";
    Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  Toolbar toolbar = findViewById(R.id.toolbar);
     //   setSupportActionBar(toolbar);
        c = MainActivity.this;

//        APIContracts.baseUrl = "https://stagesys.prabhupay.com/api/";
        APIContracts.baseUrl = "https://api.publicapis.org/";
        Log.e(TAG, "onCreate: " + APIContracts.baseUrl );
/*{
  "username": "string",
  "password": "string",
  "rememberMe": true,
  "uniqueId": "string",
  "apiLevel": "string",
  "appVersion": "string",
  "buildNumber": "string",
  "deviceName": "string",
  "deviceModelName": "string",
  "product": "string",
  "manufacturer": "string",
  "requestFromFlag": 0
}*/
        JsonObject req = new JsonObject();
        req.addProperty("username","9849528642");
        req.addProperty("password","1234");
        req.addProperty("rememberMe",true);
        req.addProperty("uniqueId","");
        req.addProperty("apiLevel","");
        req.addProperty("appVersion","");
        req.addProperty("buildNumber","");
        req.addProperty("deviceName","");
        req.addProperty("deviceModelName","");
        req.addProperty("product","");
        req.addProperty("manufacturer","");
        req.addProperty("requestFromFlag","0");


    /*   login(getApplicationContext(),req).subscribe(new DisposableObserver<JsonObject>() {
           @Override
           public void onNext(@NonNull JsonObject jsonObject) {
               Log.e(TAG, "onNext: " + jsonObject );
           }

           @Override
           public void onError(@NonNull Throwable e) {

               Log.e(TAG, "onError:  "+ e.getMessage() );
           }

           @Override
           public void onComplete() {

           }
       });*/
        test(getApplicationContext(),new JsonObject()).subscribe(new DisposableObserver<JsonObject>() {
            @Override
            public void onNext(@NonNull JsonObject jsonObject) {
                Log.e(TAG, "onNext: " + jsonObject );
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

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

    public static Observable<JsonObject> login(Context context, JsonObject req){
        return ApiCore.send(context, APIContracts.APIName.User.Login,req)
                .map( res -> {
                    Log.e("MainActivityAnish", "login: " + res );
                    if (res.get("success").getAsBoolean()) {
                        return res;
                    } else {
                        throw new Error(res.get("message").getAsString());
                    }
                });
    }
    public static Observable<JsonObject> test(Context context, JsonObject req){
        return ApiCore.send(context, APIContracts.APIName.User.test,req)
                .map( res -> {
                    Log.e("MainActivityAnish", "login: " + res );
                   return res;
                });
    }
}