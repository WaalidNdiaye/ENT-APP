package fr.rennes.app.ui.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import com.islandparadise14.mintable.MinTimeTableView
import com.islandparadise14.mintable.model.ScheduleDay
import com.islandparadise14.mintable.model.ScheduleEntity
import fr.rennes.app.R

class CalendarFragment : Fragment() {

    private val day = arrayOf("Mon", "Tue", "Wen", "Thu", "Fri")
    private val scheduleList: ArrayList<ScheduleEntity> = ArrayList()


    companion object {
        fun newInstance() = CalendarFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        /** Default calendar */
        val simpleCalendarView = view.findViewById<CalendarView>(R.id.default_calendar)
        simpleCalendarView.setOnDateChangeListener{ _, year, month, dayOfMonth ->
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            val msg = "Selected date is " + dayOfMonth + "/" + (month + 1) + "/" + year
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
        }

        /** MinTimeTable */
        val table = view.findViewById<MinTimeTableView>(R.id.MinTimeTableView)
        val schedule = ScheduleEntity(
            32, //originId
            "Database", //scheduleName
            "IT Building 301", //roomInfo
            ScheduleDay.TUESDAY, //ScheduleDay object (MONDAY ~ SUNDAY)
            "8:20", //startTime format: "HH:mm"
            "10:30", //endTime  format: "HH:mm"
            "#3D0000", //backgroundColor (optional)
            "#EBE5E5" //textcolor (optional)
        )
        val schedule2 = ScheduleEntity(
            32, //originId
            "Database", //scheduleName
            "IT Building 301", //roomInfo
            ScheduleDay.TUESDAY, //ScheduleDay object (MONDAY ~ SUNDAY)
            "11:20", //startTime format: "HH:mm"
            "18:30", //endTime  format: "HH:mm"
            "#7289da", //backgroundColor (optional)
            "#000000" //textcolor (optional)
        )
        scheduleList.add(schedule)
        scheduleList.add(schedule2)
        table.baseSetting(20,30,40)
        table.initTable(day)
        table.updateSchedules(scheduleList)



        return view
    }




}
