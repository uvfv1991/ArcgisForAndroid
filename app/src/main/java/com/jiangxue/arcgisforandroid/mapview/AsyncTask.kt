package com.jiangxue.arcgisforandroid.mapview

import io.reactivex.ObservableEmitter
import io.reactivex.Observer
import io.reactivex.annotations.NonNull

abstract class AsyncTask<T> {
    abstract fun async(@NonNull e: Observer<T>)
}