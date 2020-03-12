package com.example.top10downloader

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.lang.Exception

class ParseApplication {
    private val TAG= "Parse Application"
    val applications= ArrayList<FeedEntry>()


    fun parse(xmlData: String): Boolean {
        Log.d(TAG, "parse called with $xmlData")

        var status =true
        var inEntry= false
        var textValue= ""

        try {
            val factory= XmlPullParserFactory.newInstance()
            factory.isNamespaceAware= true
            val xpp= factory.newPullParser()
            xpp.setInput(xmlData.reader())
            var eventType= xpp.eventType
            var currentRecord= FeedEntry()
            while (eventType !=XmlPullParser.END_DOCUMENT){
                val tagName= xpp.name?.toLowerCase()
                when (eventType){
                    XmlPullParser.START_TAG ->{
                        Log.d(TAG, "parse: Ending Tag for " + tagName)
                        if (inEntry){
                            when (tagName){
                                "name"->{
                                    applications.add(currentRecord)
                                    inEntry= false
                                    currentRecord= FeedEntry()     //creating a new record
                                }
                                "name" -> currentRecord.name= textValue
                                "artist" -> currentRecord.artist= textValue
                                "releaseDaate" -> currentRecord.releaseDate= textValue
                                "summary" -> currentRecord.summary= textValue
                                "image" -> currentRecord.imageURL= textValue
                            }
                        }

                    }
                }
//                nothing else to do
                eventType= xpp.next()
            }
            for (app in applications){
                Log.d(TAG, "*********")
                Log.d(TAG, app.toString())
            }
        }catch (e: Exception){
            e.printStackTrace()
            status= false
        }
        return status
    }



}