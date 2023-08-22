package com.jiangxue.arcgisforandroid.helper

import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.annotations.Nullable
import io.reactivex.disposables.Disposable

/**
 * 异步监听类
 */
abstract class AsyncObserver<T> : Observer<T> {
    override fun onSubscribe(d: Disposable) {}
    override fun onNext(@NonNull t: T) {
        onSuccess(t)
    }

    override fun onError(e: Throwable) {}
    override fun onComplete() {}
    abstract fun onSuccess(t: T)
}