package com.jiangxue.arcgisforandroid.data.xml.shpproperties

/**
 * Created by Jinyu Zhang on 2017/5/13.
 */
enum class ShpType(private val type: Int) {
    SHP_TYPR(0), GEO_DATABASE(1);

    companion object {
        fun getShpTypr(type: Int): ShpType {
            for (shpType in values()) {
                if (shpType.type == type) {
                    return shpType
                }
            }
            return SHP_TYPR
        }
    }
}