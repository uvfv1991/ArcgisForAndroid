package com.jiangxue.arcgisforandroid.helper

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by Jinyu Zhang on 2017/5/2.
 * 异步监听类
 */
abstract class AsyncObserver<T> : Observer<T> {
    override fun onSubscribe(d: Disposable) {}
    override fun onNext(t: T) {
        onSuccess(t)
    }

    override fun onError(e: Throwable) {}
    override fun onComplete() {}
    abstract fun onSuccess(t: T)
}