package com.jiangxue.arcgisforandroid.widge

import com.esri.arcgisruntime.mapping.view.MapView

/**
 * Created by Jinyu Zhang on 2017/5/6.
 * 地图观察者
 */
abstract class MapViewObserver(mapView: MapView?) : MapViewHelpers(mapView),
    Observer<ActionGuide?> {
    fun onSubscribe(@NonNull d: Disposable?) {}
    fun onNext(@NonNull actionGuide: ActionGuide?) {
        layerControl(actionGuide)
    }

    fun onError(@NonNull e: Throwable?) {}
    fun onComplete() {}
    abstract fun layerControl(actionGuide: ActionGuide?)
}