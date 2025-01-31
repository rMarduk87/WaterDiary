@file:Suppress("DEPRECATED_IDENTITY_EQUALS")

package rpt.tool.waterdiary.ui.userinfo

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import rpt.tool.waterdiary.DashboardActivity
import rpt.tool.waterdiary.R
import rpt.tool.waterdiary.base.NavBaseFragment
import rpt.tool.waterdiary.base.OnActivityListener
import rpt.tool.waterdiary.databinding.FragmentInitUserInfoBinding
import rpt.tool.waterdiary.receiver.MyAlarmManager.cancelRecurringAlarm
import rpt.tool.waterdiary.receiver.MyAlarmManager.scheduleAutoRecurringAlarm
import rpt.tool.waterdiary.utils.log.d
import rpt.tool.waterdiary.utils.log.e
import rpt.tool.waterdiary.utils.managers.SharedPreferencesManager
import rpt.tool.waterdiary.utils.view.adapters.InitUserInfoPagerAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class InitUserInfoFragment :
    NavBaseFragment<FragmentInitUserInfoBinding>(FragmentInitUserInfoBinding::inflate),OnActivityListener {

    var initUserInfoPagerAdapter: InitUserInfoPagerAdapter? = null
    var current_page_idx: Int = 0
    var max_page: Int = 7

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.navigationBarColor = requireContext().resources.getColor(
            R.color.water_color)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        body()
    }

    private fun body() {
        initUserInfoPagerAdapter = InitUserInfoPagerAdapter(requireActivity().supportFragmentManager, requireContext())
        binding.viewPager.setAdapter(initUserInfoPagerAdapter)
        binding.viewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                current_page_idx = position

                if (position == 0) {
                    binding.btnBack.visibility = View.GONE
                    binding.space.visibility = View.GONE
                } else {
                    binding.btnBack.visibility = View.VISIBLE
                    binding.space.visibility = View.VISIBLE
                }

                if (position == max_page - 1) {
                    binding.lblNext.text = sh!!.get_string(R.string.str_get_started)
                } else {
                    binding.lblNext.text = sh!!.get_string(R.string.str_next)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        binding.viewPager.setOffscreenPageLimit(10)
        binding.dotsIndicator.attachTo(binding.viewPager)

        binding.btnBack.setOnClickListener {
            if (current_page_idx > 0){
                current_page_idx -= 1
                binding.viewPager.setCurrentItem(current_page_idx)
            }
        }
        
        binding.btnNext.setOnClickListener {
            if (current_page_idx == 0) {
                if (sh!!.check_blank_data(SharedPreferencesManager.userName.toString())) {
                    ah!!.customAlert(sh!!.get_string(R.string.str_your_name_validation))
                    return@setOnClickListener
                }

                if (SharedPreferencesManager.userName.toString().length < 3) {
                    ah!!.customAlert(sh!!.get_string(R.string.str_valid_name_validation))
                    return@setOnClickListener
                }
            }
            if (current_page_idx == 1) {
                try {
                    if (sh!!.check_blank_data(SharedPreferencesManager.personHeight.toString())) {
                        ah!!.customAlert(sh!!.get_string(R.string.str_height_validation))
                        return@setOnClickListener
                    }

                    if (sh!!.check_blank_data(SharedPreferencesManager.personWeight)) {
                        ah!!.customAlert(sh!!.get_string(R.string.str_weight_validation))
                        return@setOnClickListener
                    }

                    val `val` = ("" + SharedPreferencesManager.personHeight).toFloat()
                    if (`val` < 2) {
                        ah!!.customAlert(sh!!.get_string(R.string.str_height_validation))
                        return@setOnClickListener
                    }

                    val val2 = ("" + SharedPreferencesManager.personWeight).toFloat()
                    if (val2 < 30) {
                        ah!!.customAlert(sh!!.get_string(R.string.str_weight_validation))
                        return@setOnClickListener
                    }
                } catch (e: Exception) {
                    e.message?.let { it1 -> e(Throwable(e), it1) }
                }
            }

            if (current_page_idx == 5) {
                if (sh!!.check_blank_data(SharedPreferencesManager.wakeUpTime.toString()) || sh!!.check_blank_data(
                        SharedPreferencesManager.bedTime.toString()
                    )
                ) {
                    ah!!.customAlert(sh!!.get_string(R.string.str_from_to_invalid_validation))
                    return@setOnClickListener
                } else if (SharedPreferencesManager.ignoreNextStep) {
                    ah!!.customAlert(sh!!.get_string(R.string.str_from_to_invalid_validation))
                    return@setOnClickListener
                }

            }
            if (current_page_idx < max_page - 1) {
                current_page_idx += 1
                binding.viewPager.setCurrentItem(current_page_idx)
            } else {
                checkStoragePermissions()
            }
        }
    }

    private fun isNextDayEnd(): Boolean {
        val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        var date1: Date? = null
        var date2: Date? = null
        try {
            date1 = SharedPreferencesManager.wakeUpTime?.let { simpleDateFormat.parse(it) }
            date2 = SharedPreferencesManager.bedTime?.let { simpleDateFormat.parse(it) }

            return date1!!.time > date2!!.time
        } catch (e: java.lang.Exception) {
            e.message?.let { e(Throwable(e), it) }
        }

        return false
    }

    fun setAlarm() {
        val minute_interval: Int = SharedPreferencesManager.interval

        if (sh!!.check_blank_data(SharedPreferencesManager.wakeUpTime.toString()) || sh!!.check_blank_data(
                SharedPreferencesManager.bedTime.toString()
            )
        ) {
            return
        } else {
            val startTime: Calendar = Calendar.getInstance(Locale.getDefault())
            startTime.set(Calendar.HOUR_OF_DAY, SharedPreferencesManager.wakeUpTimeHour)
            startTime.set(Calendar.MINUTE, SharedPreferencesManager.wakeUpTimeMinute)
            startTime.set(Calendar.SECOND, 0)

            val endTime: Calendar = Calendar.getInstance(Locale.getDefault())
            endTime.set(Calendar.HOUR_OF_DAY, SharedPreferencesManager.bedTimeHour)
            endTime.set(Calendar.MINUTE, SharedPreferencesManager.bedTimeMinute)
            endTime.set(Calendar.SECOND, 0)

            if (isNextDayEnd()) endTime.add(Calendar.DATE, 1)

            if (startTime.timeInMillis < endTime.timeInMillis) {
                deleteAutoAlarm(true)

                var _id = System.currentTimeMillis().toInt()

                val initialValues = ContentValues()
                initialValues.put(
                    "AlarmTime",
                    ("" + SharedPreferencesManager.wakeUpTime + " - " + SharedPreferencesManager.bedTime
                    )
                )
                initialValues.put("AlarmId", "" + _id)
                initialValues.put("AlarmType", "R")
                initialValues.put("AlarmInterval", "" + minute_interval)
                dh!!.insert("tbl_alarm_details", initialValues)

                val getLastId: String = dh!!.getLastId("tbl_alarm_details")

                while (startTime.timeInMillis <= endTime.timeInMillis) {
                    d(
                        "ALARMTIME  : ",
                        (("" + startTime.get(Calendar.HOUR_OF_DAY)).toString() + ":" + startTime.get(
                            Calendar.MINUTE
                        )).toString() + ":" + startTime.get(Calendar.SECOND)
                    )

                    try {
                        _id = System.currentTimeMillis().toInt()

                        var formatedDate = ""
                        val sdf: SimpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                        val sdfs: SimpleDateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                        var dt: Date
                        var time = ""

                        time =
                            (startTime.get(Calendar.HOUR_OF_DAY).toString() +
                                    ":" + startTime.get(Calendar.MINUTE)) + ":" + startTime.get(
                                Calendar.SECOND
                            )
                        dt = sdf.parse(time)!!
                        formatedDate = sdfs.format(dt)

                        if (!dh!!.isExists(
                                "tbl_alarm_details",
                                "AlarmTime='$formatedDate'"
                            ) && !dh!!.isExists(
                                "tbl_alarm_sub_details",
                                "AlarmTime='$formatedDate'"
                            )
                        ) {
                            scheduleAutoRecurringAlarm(mContext!!, startTime, _id)

                            val initialValues2 = ContentValues()
                            initialValues2.put("AlarmTime", "" + formatedDate)
                            initialValues2.put("AlarmId", "" + _id)
                            initialValues2.put("SuperId", "" + getLastId)
                            dh!!.insert("tbl_alarm_sub_details", initialValues2)

                            val _id_sunday = System.currentTimeMillis().toInt()
                            val _id_monday = System.currentTimeMillis().toInt()
                            val _id_tuesday = System.currentTimeMillis().toInt()
                            val _id_wednesday = System.currentTimeMillis().toInt()
                            val _id_thursday = System.currentTimeMillis().toInt()
                            val _id_friday = System.currentTimeMillis().toInt()
                            val _id_saturday = System.currentTimeMillis().toInt()
                            val initialValues3 = ContentValues()
                            initialValues3.put("AlarmTime", "" + formatedDate)
                            initialValues3.put("AlarmId", "" + _id_sunday)
                            initialValues3.put("SundayAlarmId", "" + _id_sunday)
                            initialValues3.put("MondayAlarmId", "" + _id_monday)
                            initialValues3.put("TuesdayAlarmId", "" + _id_tuesday)
                            initialValues3.put("WednesdayAlarmId", "" + _id_wednesday)
                            initialValues3.put("ThursdayAlarmId", "" + _id_thursday)
                            initialValues3.put("FridayAlarmId", "" + _id_friday)
                            initialValues3.put("SaturdayAlarmId", "" + _id_saturday)
                            initialValues3.put("AlarmType", "M")
                            initialValues3.put("AlarmInterval", "0")
                            dh!!.insert("tbl_alarm_details", initialValues3)
                        }
                    } catch (e: java.lang.Exception) {
                        e.message?.let { e(Throwable(e), it) }
                        e.printStackTrace()
                    }

                    startTime.add(Calendar.MINUTE, minute_interval)
                }
            } else {
                return
            }
        }
    }

    private fun deleteAutoAlarm(alsoData: Boolean) {
        val arr_data: ArrayList<HashMap<String, String>> = dh!!.getdata("tbl_alarm_details")

        for (k in arr_data.indices) {
            cancelRecurringAlarm(mContext!!, arr_data[k]["AlarmId"]!!.toInt())

            val arr_data2: ArrayList<HashMap<String, String>> =
                dh!!.getdata("tbl_alarm_sub_details", "SuperId=" + arr_data[k]["id"])
            for (j in arr_data2.indices) cancelRecurringAlarm(
                mContext!!, arr_data2[j]["AlarmId"]!!
                    .toInt()
            )
        }

        if (alsoData) {
            dh!!.remove("tbl_alarm_details")
            dh!!.remove("tbl_alarm_sub_details")
        }
    }

    private fun checkStoragePermissions() {
        if (ContextCompat.checkSelfPermission(
                act!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                act!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf<String>(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), ALL_PERMISSION
            )
        } else {
            gotoHomeScreen()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ALL_PERMISSION ->
                gotoHomeScreen()

        }
    }

    private fun gotoHomeScreen() {
        SharedPreferencesManager.hideWelcomeScreen = true

        setAlarm()

        intent = Intent(act, DashboardActivity::class.java)
        startActivity(intent!!)
    }

    override fun onBackPressed(): Boolean {
        if (current_page_idx > 0) {
            current_page_idx -= 1
            binding.viewPager.setCurrentItem(current_page_idx)
            return  true
        } else return false
    }

    companion object {
        const val ALL_PERMISSION: Int = 3
    }

}