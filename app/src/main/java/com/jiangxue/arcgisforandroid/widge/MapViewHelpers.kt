package com.jiangxue.arcgisforandroid.widge

import com.esri.arcgisruntime.mapping.view.MapView

open class MapViewHelpers(mapView: MapView?) {
    var mapView: MapView? = null

    init {
        requireNotNull(mapView) { "mapview == null" }
        this.mapView = mapView
    }
}