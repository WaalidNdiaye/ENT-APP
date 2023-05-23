package fr.rennes.app.ui.calendar

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.islandparadise14.mintable.model.ScheduleEntity
import fr.rennes.app.R
import fr.rennes.app.ui.calendar.utils.test

class CalendarFragment : Fragment() {

    private val day = arrayOf("Mon", "Tue", "Wen", "Thu", "Fri")
    private val scheduleList: ArrayList<ScheduleEntity> = ArrayList()

    private val importedCalendar : List<ImportedTimetable> = mutableListOf()
    private val localCalendar : List<LocalTimetable> = mutableListOf()


    companion object {
        fun newInstance() = CalendarFragment()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_calendar, container, false)



        // val importedTimetable = ImportedTimetable("cours fac" , "https://planning.univ-rennes1.fr/jsp/custom/modules/plannings/KW71yaYM.shu")



        /** MinTimeTable
        val table = view.findViewById<MinTimeTableView>(R.id.MinTimeTableView)
        table.baseSetting(20,30,40)
        table.initTable(day)
        table.updateSchedules(scheduleList)

        /** Default calendar */
        val simpleCalendarView = view.findViewById<CalendarView>(R.id.default_calendar)
        simpleCalendarView.setOnDateChangeListener{ _, year, month, dayOfMonth ->
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            val msg = "Selected date is " + dayOfMonth + "/" + (month + 1) + "/" + year
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()

            val weekNumber = LocalDate.of(year, month,dayOfMonth).get(ChronoField.ALIGNED_WEEK_OF_YEAR)
            scheduleList.clear()

            val timetable = importedTimetable.getTimetable("$year$weekNumber")
            if (timetable != null) {
                scheduleList.addAll(timetable)
                Log.i("Calendar Fragment","Number Schedule add : ${timetable.size}")
            } else Log.i("Calendar Fragment","Nothing add the timetable is null")

            table.updateSchedules(scheduleList)
        }

        */

        /** Test */
        activity?.let {
            test("https://planning.univ-rennes1.fr/jsp/custom/modules/plannings/KW71yaYM.shu", it)
        }


        return view
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun sync() {
        for(cal in importedCalendar) {
            try {
                cal.update()
            } catch (e: Exception) {
                Toast.makeText(activity, "Echec de la synchronisation de l'edt", Toast.LENGTH_SHORT).show()
                Log.i("CalendarFragment","Error during timetable sync : $e")
            }
        }
    }




}
