package fr.rennes.app.ui.calendar.utils

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import biweekly.ICalendar
import biweekly.property.DateEnd
import biweekly.property.DateStart
import com.islandparadise14.mintable.model.ScheduleDay
import com.islandparadise14.mintable.model.ScheduleEntity
import okhttp3.Cache
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.time.LocalDateTime
import java.time.Month
import java.time.temporal.ChronoField


suspend fun updateCalendar(url: String): List<ICalendar> {
    // val response = Fuel.get(url).body
    // return Biweekly.parse(response).all()
    return listOf()
}


/** Convert Icalendar list to timetable represented by a Map,
 * the key is a week and the value is a list a ScheduleEntity this week :
 *   -  the key is the concatenation of year and week number
 *      (for example the key 202215 is for the fifteenth week of the 2022)
 *   -  the value is a mutable list of ScheduleEntity
 */
@RequiresApi(Build.VERSION_CODES.O)
fun calendarToTimetable(
    icalendarList: List<ICalendar>,
    tag: Int = 0
): MutableMap<String, MutableList<ScheduleEntity>> {
    val timetables: MutableMap<String, MutableList<ScheduleEntity>> = mutableMapOf()

    for (icalendar in icalendarList) {
        for (event in icalendar.events) {
            val schedule = ScheduleEntity(
                tag, //originId
                event.summary.value.toString(), //scheduleName
                event.location.value.toString(), //roomInfo
                dateToDay(event.dateStart), //ScheduleDay object (MONDAY ~ SUNDAY)
                dateStartToStarTime(event.dateStart), //startTime format: "HH:mm"
                dateEndToEndTime(event.dateEnd), //endTime  format: "HH:mm"
                // "#000000", //backgroundColor (optional)
                // "#000000" //textcolor (optional)
            )
            val date = dateStartToLocalDateTime(event.dateStart)
            val weekNumber = date.get(ChronoField.ALIGNED_WEEK_OF_YEAR)
            val year = date.year
            val key = "$year$weekNumber"

            if (timetables.containsKey(key)) timetables[key]?.add(schedule)
            else timetables[key] = mutableListOf(schedule)
        }
    }
    return timetables
}


/**  Get start time string (in format 18:15) from a dateEnd
 * @param start (value in format : Thu Nov 10 13:30:00 GMT+01:00 2022)
 * @return string
 */
fun dateStartToStarTime(start: DateStart): String {
    return start.value.toString().substring(11, 16)
}

/**  Get end time string (in format 18:15) from a dateEnd
 * @param end (value in format : Thu Nov 10 13:30:00 GMT+01:00 2022)
 * @return string
 */
fun dateEndToEndTime(end: DateEnd): String {
    return end.value.toString().substring(11, 16)
}

/** Get ScheduleDay from a dateStart
 * @param dateStart (value in format : Thu Nov 10 13:30:00 GMT+01:00 2022)
 * @return ScheduleDay
 */
fun dateToDay(dateStart: DateStart) = when (dateStart.value.toString().take(3)) {
    "Mon" -> ScheduleDay.MONDAY
    "Tue" -> ScheduleDay.TUESDAY
    "Wed" -> ScheduleDay.WEDNESDAY
    "Thu" -> ScheduleDay.THURSDAY
    "Fri" -> ScheduleDay.FRIDAY
    "Sat" -> ScheduleDay.SATURDAY
    "Sun" -> ScheduleDay.SUNDAY
    else -> throw Exception("incorrect day name in dateStart")
}


/** Convert DateStart to LocalDateTime (java8)
 * @param dateStart (value in format : Thu Nov 10 13:30:00 GMT+01:00 2022)
 * @return LocalDateTime
 */
@RequiresApi(Build.VERSION_CODES.O)
fun dateStartToLocalDateTime(dateStart: DateStart): LocalDateTime {
    // Format s : Thu Nov 10 13:30:00 GMT+01:00 2022
    val s = dateStart.value.toString()
    //Log.i("utils sync","Format s : $s")

    val d = s.substring(8, 10).toInt()
    val m = when (s.substring(4, 7)) {
        "Jan" -> Month.JANUARY
        "Feb" -> Month.FEBRUARY
        "Mar" -> Month.MARCH
        "Apr" -> Month.APRIL
        "May" -> Month.MAY
        "Jun" -> Month.JUNE
        "Jul" -> Month.JULY
        "Aug" -> Month.AUGUST
        "Sep" -> Month.SEPTEMBER
        "Oct" -> Month.OCTOBER
        "Nov" -> Month.NOVEMBER
        "Dec" -> Month.DECEMBER
        else -> throw Exception("incorrect month name in dateStart")
    }
    val y = s.substring(30, 34).toInt()
    val h = s.substring(11, 13).toInt()
    val min = s.substring(14, 16).toInt()

    return LocalDateTime.of(y, m, d, h, min)
}


fun test(url: String, context : Context) {
    val file = context.getDir("service_api_cache", Context.MODE_PRIVATE)
    println("\ncreated : " + file.createNewFile())
    println("absolutePath : " + file.absolutePath)

    println("isFile : " + file.isFile)
    val maxSize =  50L * 1024L * 1024L // 50 MiB

    val cache = Cache(file,maxSize)
    println("\ndirectoryPath : " + cache.directoryPath)
    println("directory : " + cache.directory)
    println("is closed : " + cache.isClosed)
    println("requestCount : " + cache.requestCount())



    val client: OkHttpClient = OkHttpClient.Builder()
        .cache(cache)
        .build()

    val request = Request.Builder()
        .url(url)
        .build()


//    val response = client.newCall(request).execute()
//    println(response.body.toString())

    // cache.evictAll();
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        @Throws(IOException::class)
        override fun onResponse(call: Call, response: Response)  {
            if(response.cacheResponse?.code != 200){

            }
            println("networkResponse : ${response.networkResponse?.code}")
            println("cacheResponse : ${response.cacheResponse?.code}\n\n")

            var respBody: String? = null
            if (response.body != null) {
                respBody = response.body.toString()
                response.body.close()
            }
        }
    })


}




