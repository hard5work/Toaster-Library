package com.motion.toasterlibrary.kotlin
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers

object RxUtils {
    fun <T> defaultTransformers(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream: Observable<T> ->
            upstream.compose(
                schedulersTransformer()
            )
        }
    }

    fun defaultTransformersCompletable(): CompletableTransformer {
        return CompletableTransformer { upstream: Completable ->
            upstream.compose(
                schedulersTransformerCompletable<Any>()
            )
        }
    }

    fun <T> schedulersTransformer(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream: Observable<T> ->
            upstream.subscribeOn(
                Schedulers.io()
            ).observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> schedulersTransformerCompletable(): CompletableTransformer {
        return CompletableTransformer { upstream: Completable ->
            upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }

    //    public static <T> ObservableTransformer<T, T> showLoadingDialog(UIFragment fragment) {
    //        return observable -> observable
    //                .doOnSubscribe(disposable -> /* show dialog */)
    //                .doOnTerminate(() -> /* hide dialog */);
    //    }
    fun onlyCompleteObserver(): DisposableCompletableObserver {
        return object : DisposableCompletableObserver() {
            override fun onComplete() {}
            override fun onError(e: Throwable) {}
        }
    }
}