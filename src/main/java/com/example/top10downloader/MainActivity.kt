package com.example.top10downloader

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL
import kotlin.coroutines.coroutineContext
import kotlin.properties.Delegates

class FeedEntry {
    var name: String = ""
    var artist: String = ""
    var releaseDate = ""
    var summary: String = ""
    var imageURL: String = ""
//    override fun toString(): String {
//        return """
//            name= $name
//            artist=$artist
//            releaseDate=$releaseDate
//            summary=$summary
//            imageURL=$imageURL
//            """.trimIndent()
//    }
}

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val downloadData = DownloadData(this, xmlListView)
        downloadData.execute("http://ax.itunes.apple.com/webObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml")
        Log.d(TAG, "Oncreate method Called")
    }

    companion object {
        private class DownloadData(context: Context, listView: ListView) : AsyncTask<String, Void, String>() {
            private val TAG = "DownloadData"

            var propContext: Context by Delegates.notNull()
            var propListView: ListView by Delegates.notNull()

            init {
                propContext = context
                propListView = listView
            }


            override fun onPostExecute(result: String) {
                super.onPostExecute(result)
                val parseApplication = ParseApplication()
                parseApplication.parse(result)


                val arrayAdapter =
                    ArrayAdapter<FeedEntry>(propContext, R.layout.list_item, parseApplication.applications)
                propListView.adapter = arrayAdapter


            }

            override fun doInBackground(vararg url: String?): String {
                Log.d(TAG, "doInBackground: starts with ${url[0]}")
                val rssFeed = downloadXML(url[0])
                if (rssFeed.isEmpty()) {
                    Log.e(TAG, "doInBackground: error downloading")
                }
                return rssFeed
            }

            private fun downloadXML(urlPath: String?): String {
                return URL(urlPath).readText()

            }
        }
    }
}
