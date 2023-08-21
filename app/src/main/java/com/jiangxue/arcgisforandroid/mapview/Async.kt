package com.jiangxue.arcgisforandroid.mapview

import haoyuan.com.qianguoqualitysafety.utils.AsyncObserver
import io.reactivex.Observable

/**
 * Created by Jinyu Zhang on 2017/5/2.
 */
class Async<T>(asyncObserver: AsyncObserver) {
    private val asyncObserver: AsyncObserver<T>

    init {
        this.asyncObserver = asyncObserver
    }

    fun execute(asyncTask: AsyncTask<*>) {
        Observable.create<T>(object : ObservableOnSubscribe<T> {
            @Throws(Exception::class)
            override fun subscribe(e: ObservableEmitter<T>) {
                asyncTask.async(e)
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(asyncObserver)
    }
}