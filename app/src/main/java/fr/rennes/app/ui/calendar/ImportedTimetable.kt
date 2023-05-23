package fr.rennes.app.ui.calendar

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import biweekly.ICalendar
import com.islandparadise14.mintable.model.ScheduleEntity

import fr.rennes.app.ui.calendar.utils.updateCalendar
import fr.rennes.app.ui.calendar.utils.calendarToTimetable
import kotlinx.coroutines.runBlocking
import okhttp3.Cache
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

@RequiresApi(Build.VERSION_CODES.O)
class ImportedTimetable(val name: String, private val url: String) {

    private val TAG = "ImportedTimetable"

    private var icalList = listOf<ICalendar>()
    private var timetableList = mapOf<String, List<ScheduleEntity>>()

    init {
        update()
        timetableList = calendarToTimetable(icalList)
        for (key in timetableList.keys) {
            Log.i("ImportTimetable", "key: $key")
        }
    }


    fun update() {
        // https://planning.univ-rennes1.fr/jsp/custom/modules/plannings/KW71yaYM.shu
        runBlocking {
            icalList = updateCalendar(url)
        }
    }


    /** Get timetable in format of a list of ScheduleEntity
     * @param key is the concatenation of year and week number
     *  (for example the key 202215 is for the fifteenth week of the 2022)
     * @return the a list of ScheduleEntity (or null)
     */
    fun getTimetable(key: String): List<ScheduleEntity>? {
        return timetableList[key]
    }


    /**
     *
     */
    fun update(context : Context) {
        val file = context.getDir("service_api_cache", Context.MODE_PRIVATE)
        println("\ncreated : " + file.createNewFile())
        println("absolutePath : " + file.absolutePath)

        val maxSize = 50L * 1024L * 1024L // 50 MiB

        val cache = Cache(file, maxSize)

        val client: OkHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                // println("networkResponse : ${response.networkResponse?.code}")
                // println("cacheResponse : ${response.cacheResponse?.code}\n\n")
                val code = response.cacheResponse?.code
                if (code != 200) {
                    /** The cache is empty => udapte calendar */
                    Log.i(TAG,"cacheResponse $code")
                    var respBody: String? = response.body.toString()
                    response.body.close() // necessary to cache response

                } else {
                    // nothing to do, according to the cache the calendar is up to date
                }


            }
        })


    }


}