package com.jiangxue.arcgisforandroid.data.xml.shpconfig

/**
 * Created by Jinyu Zhang on 2017/4/28.
 */
enum class FiedSearchType(private val type: Int) {
    NONE(0), INPUT(1), LIST(2), SCOPE(3);

    companion object {
        fun getFiedSearchType(type: String): FiedSearchType {
            for (fiedSearchType in values()) {
                if (fiedSearchType.type == type.toInt()) {
                    return fiedSearchType
                }
            }
            return NONE
        }
    }
}