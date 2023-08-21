package com.example.perfectplayer.event

/**
 *  author : jiangxue
 *  date : 2023/8/1 13:20
 *  description :
 */

class MessageEvent(type: Int, message: String) {
    private var type: Int
    private var message: String

    init {
        this.type = type
        this.message = message
    }

    override fun toString(): String {
        return "type=$type--message= $message"
    }

    fun getType(): Int {
        return type
    }

    fun setType(type: Int) {
        this.type = type
    }

    fun getMessage(): String {
        return message
    }

    fun setMessage(message: String) {
        this.message = message
    }
}
