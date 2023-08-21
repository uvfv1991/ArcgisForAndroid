package com.jiangxue.arcgisforandroid.adapter

import android.util.Xml
import com.jiangxue.arcgisforandroid.helper.AsyncObserver
import io.reactivex.ObservableEmitter
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

/**
 * Created by Jinyu Zhang on 2017/5/2.
 */
class XmlParser<T> {
    private val xmlPullParser: XmlPullParser?
    private var xmlBaseAdapter: XmlBaseAdapter<T>? = null

    init {
        xmlPullParser = Xml.newPullParser()
    }

    fun setAdapter(adapter: XmlBaseAdapter<*>?) {
        xmlBaseAdapter = adapter
    }

    private fun asyncParse(inputStream: InputStream, asyncObserver: AsyncObserver<*>) {
        if (xmlPullParser == null) {
            return
        }
        val tAsync: Async<T> = Async(asyncObserver)
        tAsync.execute(object : AsyncTask() {
            fun async(e: ObservableEmitter<*>) {
                //设置流和字符集
                try {
                    xmlPullParser.setInput(inputStream, "utf-8")
                    e.onNext(xmlBaseAdapter!!.getXmlData(xmlPullParser))
                } catch (e1: XmlPullParserException) {
                    e1.printStackTrace()
                    e.onError(Throwable(if (Check.isEmpty(e1.message)) "Xml pull parser exception." else e1.message))
                } catch (e1: IOException) {
                    e1.printStackTrace()
                    e.onError(Throwable(if (Check.isEmpty(e1.message)) "io exception." else e1.message))
                }
                e.onComplete()
            }
        })
    }

    @Throws(FileNotFoundException::class)
    fun asyncParse(filePath: String?, asyncObserver: AsyncObserver<*>) {
        this.asyncParse(File(filePath), asyncObserver)
    }

    @Throws(FileNotFoundException::class)
    private fun asyncParse(file: File, asyncObserver: AsyncObserver<*>) {
        this.asyncParse(FileInputStream(file), asyncObserver)
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun parse(inputStream: InputStream): T {
        xmlPullParser!!.setInput(inputStream, "utf-8")
        return xmlBaseAdapter!!.getXmlData(xmlPullParser)
    }

    @Throws(IOException::class, XmlPullParserException::class)
    fun parse(filePath: String?): T {
        return this.parse(File(filePath))
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun parse(file: File): T {
        return this.parse(FileInputStream(file))
    }
}