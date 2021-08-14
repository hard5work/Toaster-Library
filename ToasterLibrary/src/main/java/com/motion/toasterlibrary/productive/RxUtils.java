package com.motion.toasterlibrary.productive;

import java.util.Observable;

import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxUtils {

    public static <T> ObservableTransformer<T,T> defaultTransformers(){
        return upstream -> upstream.compose(schedulersTransformer());
    }

    public static <T> ObservableTransformer<T,T> schedulersTransformer(){
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
;    }
}
