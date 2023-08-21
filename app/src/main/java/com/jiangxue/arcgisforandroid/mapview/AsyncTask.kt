package com.jiangxue.arcgisforandroid.mapview

import io.reactivex.ObservableEmitter

/**
 * Created by Jinyu Zhang on 2017/5/2.
 */
abstract class AsyncTask<T> {
    abstract fun async(e: ObservableEmitter<T>?)
}