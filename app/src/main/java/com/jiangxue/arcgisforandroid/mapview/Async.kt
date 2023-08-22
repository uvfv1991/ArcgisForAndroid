package com.jiangxue.arcgisforandroid.mapview

import com.jiangxue.arcgisforandroid.data.xml.properties.Properties
import com.jiangxue.arcgisforandroid.helper.AsyncObserver
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class Async<T : Any>(asyncObserver: AsyncObserver<T>) {
     var asyncObserver: AsyncObserver<T>

    init {
        this.asyncObserver = asyncObserver

    }

    fun execute(asyncTask: AsyncTask<T>) {


        Observable.create(object :ObservableOnSubscribe<T>{
            override fun subscribe(e: ObservableEmitter<T>) {
                asyncTask.async(asyncObserver)
            }

        }).subscribe(asyncObserver)




    }

}