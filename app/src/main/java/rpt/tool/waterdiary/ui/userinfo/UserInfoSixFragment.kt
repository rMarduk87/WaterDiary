package rpt.tool.waterdiary.ui.userinfo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import com.wdullaer.materialdatetimepicker.time.Timepoint
import rpt.tool.waterdiary.AppUtils
import rpt.tool.waterdiary.R
import rpt.tool.waterdiary.base.MasterBaseAppCompatActivity
import rpt.tool.waterdiary.base.NavBaseFragment
import rpt.tool.waterdiary.databinding.FragmentInitUserInfoSixBinding
import rpt.tool.waterdiary.utils.log.e
import rpt.tool.waterdiary.utils.managers.SharedPreferencesManager
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class UserInfoSixFragment :
    NavBaseFragment<FragmentInitUserInfoSixBinding>(FragmentInitUserInfoSixBinding::inflate) {

    var from_hour: Int = 8
    var from_minute: Int = 0
    var to_hour: Int = 22
    var to_minute: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        body()
        setCount()
    }

    @SuppressLint("SetTextI18n")
    private fun body() {
        binding.rdo15.text = "15 " + sh!!.get_string(R.string.str_min)
        binding.rdo30.text = "30 " + sh!!.get_string(R.string.str_min)
        binding.rdo45.text = "45 " + sh!!.get_string(R.string.str_min)
        binding.rdo60.text = "1 " + sh!!.get_string(R.string.str_hour)

        binding.txtWakeupTime.setOnClickListener {
            openAutoTimePicker(binding.txtWakeupTime, true)
        }

        binding.txtBedTime.setOnClickListener {
            openAutoTimePicker(binding.txtBedTime, false)
        }

        binding.rdo15.setOnClickListener { setCount() }
        binding.rdo30.setOnClickListener { setCount() }
        binding.rdo45.setOnClickListener { setCount() }
        binding.rdo60.setOnClickListener { setCount() }

    }

    private fun setCount() {
        val startTime: Calendar = Calendar.getInstance(Locale.getDefault())
        startTime.set(Calendar.HOUR_OF_DAY, from_hour)
        startTime.set(Calendar.MINUTE, from_minute)
        startTime.set(Calendar.SECOND, 0)

        val endTime: Calendar = Calendar.getInstance(Locale.getDefault())
        endTime.set(Calendar.HOUR_OF_DAY, to_hour)
        endTime.set(Calendar.MINUTE, to_minute)
        endTime.set(Calendar.SECOND, 0)

        // @@@@@
        if (isNextDayEnd()) endTime.add(Calendar.DATE, 1)

        val mills: Long = endTime.timeInMillis - startTime.timeInMillis

        val hours = (mills / (1000 * 60 * 60)).toInt()
        val mins = ((mills / (1000 * 60)) % 60).toInt()
        val total_minutes = ((hours * 60) + mins)

        val interval =
            if (binding.rdo15.isChecked) 15 else if (binding.rdo30.isChecked) 30 else if
                    (binding.rdo45.isChecked) 45 else 60

        var consume = 0 // @@@@@
        if (total_minutes > 0) consume =
            Math.round(AppUtils.DAILY_WATER_VALUE / (total_minutes / interval)).toInt()

        val unit = if (SharedPreferencesManager.personWeightUnit) "ml" else "fl oz"

        binding.lblMessage.text = sh!!.get_string(R.string.str_goal_consume).replace(
            "$1",
            "$consume $unit"
        ).replace("$2", "" + AppUtils.DAILY_WATER_VALUE + " " + unit)

        SharedPreferencesManager.wakeUpTime = binding.txtWakeupTime.getText().toString().trim()
        SharedPreferencesManager.wakeUpTimeHour = from_hour
        SharedPreferencesManager.wakeUpTimeMinute = from_minute

        SharedPreferencesManager.bedTime = binding.txtBedTime.text.toString().trim()
        SharedPreferencesManager.bedTimeHour = to_hour
        SharedPreferencesManager.bedTimeMinute = to_minute

        SharedPreferencesManager.interval = interval

        if (consume > AppUtils.DAILY_WATER_VALUE)
            SharedPreferencesManager.ignoreNextStep = true
        else if (consume == 0) SharedPreferencesManager.ignoreNextStep = true
        else SharedPreferencesManager.ignoreNextStep = false
    }

    private fun isNextDayEnd(): Boolean {
        val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        var date1: Date? = null
        var date2: Date? = null
        try {
            date1 = simpleDateFormat.parse(binding.txtWakeupTime.getText().toString().trim())
            date2 = simpleDateFormat.parse(binding.txtBedTime.getText().toString().trim())

            return date1.time > date2.time
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e), it) }
        }

        return false
    }

    @SuppressLint("SetTextI18n")
    fun openAutoTimePicker(appCompatTextView: AppCompatTextView, isFrom: Boolean) {
        val onTimeSetListener: TimePickerDialog.OnTimeSetListener =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute, second ->
                var formatedDate = ""
                val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val sdfs = SimpleDateFormat("hh:mm a", Locale.getDefault())
                val dt: Date
                var time = ""

                try {
                    if (isFrom) {
                        from_hour = hourOfDay
                        from_minute = minute
                    } else {
                        to_hour = hourOfDay
                        to_minute = minute
                    }

                    time = "$hourOfDay:$minute:00"
                    dt = sdf.parse(time)!!
                    formatedDate = sdfs.format(dt)
                    appCompatTextView.text = "" + formatedDate

                    setCount()
                } catch (e: ParseException) {
                    e.message?.let { e(Throwable(e), it) }
                    e.printStackTrace()
                }
            }

        val now = Calendar.getInstance(Locale.getDefault())

        if (isFrom) {
            now[Calendar.HOUR_OF_DAY] = from_hour
            now[Calendar.MINUTE] = from_minute
        } else {
            now[Calendar.HOUR_OF_DAY] = to_hour
            now[Calendar.MINUTE] = to_minute
        }
        val tpd: TimePickerDialog
        if (!android.text.format.DateFormat.is24HourFormat(act)) {
            //12 hrs format
            tpd = TimePickerDialog.newInstance(
                onTimeSetListener,
                now[Calendar.HOUR_OF_DAY],
                now[Calendar.MINUTE], false
            )

            tpd.setSelectableTimes(generateTimepoints(23.50, 30))

            tpd.setMaxTime(23, 30, 0)
        } else {
            //24 hrs format
            tpd = TimePickerDialog.newInstance(
                onTimeSetListener,
                now[Calendar.HOUR_OF_DAY],
                now[Calendar.MINUTE], true
            )

            tpd.setSelectableTimes(generateTimepoints(23.50, 30))

            tpd.setMaxTime(23, 30, 0)
        }


        tpd.accentColor = MasterBaseAppCompatActivity.getThemeColor(mContext!!)
        tpd.show(act!!.fragmentManager, "Datepickerdialog")
        tpd.accentColor = MasterBaseAppCompatActivity.getThemeColor(mContext!!)
    }

    private fun generateTimepoints(maxHour: Double, minutesInterval: Int): Array<Timepoint> {
        val lastValue = (maxHour * 60).toInt()

        val timepoints: MutableList<Timepoint> = ArrayList()

        var minute = 0
        while (minute <= lastValue) {
            val currentHour = minute / 60
            val currentMinute = minute - (if (currentHour > 0) (currentHour * 60) else 0)
            if (currentHour == 24) {
                minute += minutesInterval
                continue
            }
            timepoints.add(Timepoint(currentHour, currentMinute))
            minute += minutesInterval
        }
        return timepoints.toTypedArray<Timepoint>()
    }
}