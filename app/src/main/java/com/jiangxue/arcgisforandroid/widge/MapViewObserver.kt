package com.jiangxue.arcgisforandroid.widge

import androidx.annotation.NonNull
import com.esri.arcgisruntime.mapping.view.MapView
import com.jiangxue.arcgisforandr.LayerControlAdapter
import com.jiangxue.arcgisforandr.LayerControlAdapter.ActionGuide
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


/**
 * 地图观察者
 */
abstract class MapViewObserver(mapView: MapView?) : MapViewHelpers(mapView),
    Observer<LayerControlAdapter.ActionGuide?> {
   /* 方法不想被子类重写，需在方法前用 final 修饰方法。
    如果在Kotlin 中类和方法想被继承和重写，需添加open 关键字修饰。*/

    override fun onSubscribe(d: Disposable) {
    }

    override fun onError(e: Throwable) {

    }

    override fun onNext(t: ActionGuide) {
        layerControl(t)
    }


    override fun onComplete() {}
    abstract fun layerControl(actionGuide: LayerControlAdapter.ActionGuide?)
}