package rpt.tool.waterdiary

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.appwidget.AppWidgetManager
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface.OnShowListener
import android.content.Intent
import android.database.Cursor
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.InputFilter
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import rpt.tool.waterdiary.base.MasterBaseAppCompatActivity
import rpt.tool.waterdiary.data.model.Container
import rpt.tool.waterdiary.data.model.Menu
import rpt.tool.waterdiary.data.model.NextReminderModel
import rpt.tool.waterdiary.data.model.SoundModel
import rpt.tool.waterdiary.databinding.ActivityDashboardBinding
import rpt.tool.waterdiary.utils.helpers.HeightWeightHelper
import rpt.tool.waterdiary.utils.log.d
import rpt.tool.waterdiary.utils.log.e
import rpt.tool.waterdiary.utils.log.v
import rpt.tool.waterdiary.utils.log.w
import rpt.tool.waterdiary.utils.managers.SharedPreferencesManager
import rpt.tool.waterdiary.utils.view.adapters.ContainerAdapterNew
import rpt.tool.waterdiary.utils.view.adapters.MenuAdapter
import rpt.tool.waterdiary.utils.view.adapters.SoundAdapter
import rpt.tool.waterdiary.utils.view.custom.InputFilterWeightRange
import rpt.tool.waterdiary.utils.view.widget.NewAppWidget
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Random


class DashboardActivity : MasterBaseAppCompatActivity() {
    
    var menu_name: ArrayList<Menu> = ArrayList<Menu>()
    var menuAdapter: MenuAdapter? = null
    var lst_sounds: MutableList<SoundModel> = ArrayList()
    var soundAdapter: SoundAdapter? = null
    var containerArrayList: ArrayList<Container> = ArrayList()
    var adapter: ContainerAdapterNew? = null
    var drink_water: Float = 0f
    var old_drink_water: Float = 0f
    var selected_pos: Int = 0
    var handler: Handler? = null
    var runnable: Runnable? = null
    var handlerReminder: Handler? = null
    var runnableReminder: Runnable? = null
    var max_bottle_height: Int = 870
    var progress_bottle_height: Int = 0
    var cp: Int = 0
    var np: Int = 0
    var ringtone: Ringtone? = null
    var btnclick: Boolean = true
    lateinit var binding:ActivityDashboardBinding
    var bottomSheetDialog: BottomSheetDialog? = null
    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var installStateUpdatedListener: InstallStateUpdatedListener
    private lateinit var activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>
    
    @SuppressLint("BatteryLife")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (SharedPreferencesManager.dailyWater == 0f) {
            AppUtils.DAILY_WATER_VALUE = 2500f
        } else {
            AppUtils.DAILY_WATER_VALUE = SharedPreferencesManager.dailyWater
        }

        if (sh!!.check_blank_data("" + SharedPreferencesManager.waterUnit)) {
            AppUtils.WATER_UNIT_VALUE = "ml"
        } else {
            AppUtils.WATER_UNIT_VALUE = SharedPreferencesManager.waterUnit.toString()
        }

        searchForUpdate()

        setFrame()

        if(SharedPreferencesManager.isBloodDonor &&
            !dh!!.isExists("tbl_blood_donor_details",
                "DonorDate='${dth!!.currentDate}'")){
            binding.calendarDonorBlock.visibility = VISIBLE
            binding.imgCalendarHelper.visibility = GONE
        }
        else if(SharedPreferencesManager.isBloodDonor &&
            dh!!.isExists("tbl_blood_donor_details",
                "DonorDate='${dth!!.currentDate}'")){
            binding.calendarDonorBlock.visibility = VISIBLE
            binding.imgCalendarHelper.visibility =  VISIBLE
        }
        else{
            binding.calendarDonorBlock.visibility = GONE
        }

        ringtone = RingtoneManager.getRingtone(
            mContext,
            Uri.parse(("android.resource://" + mContext!!.packageName) + "/" + R.raw.fill_water_sound)
        )
        ringtone!!.isLooping = false
    }

    private fun searchForUpdate() {
        initInAppUpdate()
    }

    private fun initInAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(baseContext)
        registerInAppUpdateResultWatcher()
        registerInstallStateResultWatcher()
        checkUpdates()
    }

    private fun registerInstallStateResultWatcher() {
        installStateUpdatedListener = InstallStateUpdatedListener { installState ->
            when (installState.installStatus()) {
                InstallStatus.DOWNLOADED -> appUpdateManager.completeUpdate()
                InstallStatus.INSTALLED -> appUpdateManager.unregisterListener(
                    installStateUpdatedListener
                )

                else -> {}
            }
        }
        appUpdateManager.registerListener(installStateUpdatedListener)

    }

    private fun registerInAppUpdateResultWatcher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
                if (it.resultCode != RESULT_OK) {
                    w("Update flow failed! Result code: " + it.resultCode)
                }
            }
    }

    private fun checkUpdates() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {

                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    activityResultLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                )
            }
        }
    }

    private fun loadPhoto() {
        if (SharedPreferencesManager.userPhoto?.let { sh!!.check_blank_data(it) } == true) {
            Glide.with(act!!).load(
                if (SharedPreferencesManager.userGender)
                    R.drawable.female_white
                else
                    R.drawable.male_white
            ).apply(RequestOptions.circleCropTransform())
                .into(binding.imgUser)
        } else {
            var ex = false

            try {
                val f: File? = SharedPreferencesManager.userPhoto?.let { File(it) }
                if (f!!.exists()) ex = true
            } catch (e: Exception) {
                e.message?.let { e(Throwable(e), it) }
            }

            if (ex) {
                Glide.with(act!!).load(SharedPreferencesManager.userPhoto)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.imgUser)
            } else {
                Glide.with(act!!).load(
                    if (SharedPreferencesManager.userGender)
                        R.drawable.female_white
                    else
                        R.drawable.male_white
                ).apply(RequestOptions.circleCropTransform())
                    .into(binding.imgUser)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        activityResultLauncher,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                    )
                }
            }
        
        if (AppUtils.RELOAD_DASHBOARD) {
            init()
        } else {
            AppUtils.RELOAD_DASHBOARD = true
        }
    }

    fun init() {
        initMenuScreen()

        body()

        getAllReminderData()

        fetchNextReminder()
    }


    private fun initMenuScreen() {
        filter_cal = Calendar.getInstance(Locale.getDefault())
        today_cal = Calendar.getInstance(Locale.getDefault())
        yesterday_cal = Calendar.getInstance(Locale.getDefault())
        yesterday_cal!!.add(Calendar.DATE, -1)

        menuBody()
        
        binding.include1.lblToolbarTitle.setOnClickListener {
            filter_cal!!.timeInMillis = today_cal!!.timeInMillis
            binding.include1.lblToolbarTitle.text = sh!!.get_string(R.string.str_today)
            setCustomDate(
                dth!!.getDate(
                    filter_cal!!.timeInMillis,
                    AppUtils.DATE_FORMAT
                )
            )
        }

        binding.lblUserName.text = SharedPreferencesManager.userName
    }

    @SuppressLint("RtlHardcoded")
    private fun menuBody() {

        loadPhoto()

        binding.include1.lblToolbarTitle.text = sh!!.get_string(R.string.str_today)
        binding.lblUserName.text = SharedPreferencesManager.userName


        menu_name.clear()
        menu_name.add(Menu(sh!!.get_string(R.string.str_home), true))
        menu_name.add(Menu(sh!!.get_string(R.string.str_drink_history), false))
        menu_name.add(Menu(sh!!.get_string(R.string.str_drink_report), false))
        menu_name.add(Menu(sh!!.get_string(R.string.str_settings), false))
        menu_name.add(Menu(sh!!.get_string(R.string.str_faqs), false))
        menu_name.add(Menu(sh!!.get_string(R.string.str_privacy_policy), false))
        menu_name.add(Menu(sh!!.get_string(R.string.str_tell_a_friend), false))
        menu_name.add(Menu(sh!!.get_string(R.string.str_container_report), false))

        menuAdapter = act?.let {
            MenuAdapter(it, menu_name, object : MenuAdapter.CallBack {
                @SuppressLint("RtlHardcoded", "UnsafeIntentLaunch")
                override fun onClickSelect(menu: Menu?, position: Int) {
                    binding.drawerLayout.closeDrawer(Gravity.LEFT)

                    intent = Intent(act, MenuActivity::class.java)

                    when (position) {
                        1 -> {
                            intent!!.putExtra("Class",1)
                            startActivity(intent)
                        }
                        2 -> {
                            intent!!.putExtra("Class",2)
                            startActivity(intent)
                        }
                        3 -> {
                            intent!!.putExtra("Class",3)
                            startActivity(intent)
                        }
                        4 -> {
                            intent!!.putExtra("Class",4)
                            startActivity(intent)
                        }
                        5 -> {
                            val i = Intent(Intent.ACTION_VIEW)
                            i.setData(Uri.parse(AppUtils.PRIVACY_POLICY_ULR))
                            startActivity(i)
                        }
                        6 -> {
                            val str = sh!!.get_string(R.string.app_share_txt)
                                .replace("#1", AppUtils.APP_SHARE_URL)

                            ih!!.ShareText(getApplicationName(mContext!!), str)
                        }
                        8 -> {
                            intent!!.putExtra("Class",8)
                            startActivity(intent)
                        }
                    }
                }
            })
        }
        
        binding.btnRateUs.setOnClickListener {
            val appPackageName = packageName
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW, Uri.parse(
                            "market://details?id=$appPackageName"
                        )
                    )
                )
            } catch (anfe: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW, Uri.parse(
                            "https://play.google.com/store/apps/details?id=$appPackageName"
                        )
                    )
                )
            }
        }
        
        binding.btnContactUs.setOnClickListener {
            try {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + getString(R.string.mail)))
                intent.putExtra(Intent.EXTRA_SUBJECT, "")
                intent.putExtra(Intent.EXTRA_TEXT, "")
                startActivity(intent)
            } catch (ex: Exception) {
                ex.message?.let { it1 -> e(Throwable(ex), it1) }
            }
        }

        binding.leftDrawer.setLayoutManager(
            LinearLayoutManager(
                activity,
                LinearLayoutManager.VERTICAL,
                false
            )
        )

        binding.leftDrawer.setAdapter(menuAdapter)

        binding.openProfile.setOnClickListener {
            try {
                if (binding.drawerLayout.isDrawerOpen(Gravity.LEFT)) 
                    binding.drawerLayout.closeDrawer(Gravity.LEFT)
            } catch (e: Exception) {
                e.message?.let { it1 -> e(Throwable(e), it1) }
            }
            val i = Intent(act, MenuActivity::class.java)
            i.putExtra("Class",5)
            startActivity(i)
        }
        
        binding.include1.btnAlarm.setOnClickListener { showReminderDialog() }
        
        binding.include1.btnMenu.setOnClickListener {
            try {
                if (binding.drawerLayout.isDrawerOpen(Gravity.LEFT)) 
                    binding.drawerLayout.closeDrawer(Gravity.LEFT)
                else binding.drawerLayout.openDrawer(Gravity.LEFT)
            } catch (e: Exception) {
                e.message?.let { it1 -> e(Throwable(e), it1) }
            }
        }
        
        binding.include1.imgPre.setOnClickListener {
            filter_cal!!.add(Calendar.DATE, -1)
            if (dth!!.getDate(filter_cal!!.timeInMillis, AppUtils.DATE_FORMAT).equals(
                    dth!!.getDate(
                        yesterday_cal!!.timeInMillis, AppUtils.DATE_FORMAT
                    ),true
                )
            )
                binding.include1.lblToolbarTitle.text = sh!!.get_string(R.string.str_yesterday)
            else binding.include1.lblToolbarTitle.text = dth!!.getDate(
                filter_cal!!.timeInMillis,
                AppUtils.DATE_FORMAT
            )
            setCustomDate(dth!!.getDate(filter_cal!!.timeInMillis, AppUtils.DATE_FORMAT))
        }
        
        binding.include1.imgNext.setOnClickListener {
            filter_cal!!.add(Calendar.DATE, 1)
            if (filter_cal!!.timeInMillis > today_cal!!.timeInMillis) {
                filter_cal!!.add(Calendar.DATE, -1)
                return@setOnClickListener
            }

            if (dth!!.getDate(filter_cal!!.timeInMillis, AppUtils.DATE_FORMAT).equals(
                    dth!!.getDate(
                        today_cal!!.timeInMillis, AppUtils.DATE_FORMAT
                    ),true
                )
            ) binding.include1.lblToolbarTitle.text = sh!!.get_string(R.string.str_today)
            else if (dth!!.getDate(filter_cal!!.timeInMillis, AppUtils.DATE_FORMAT)
                    .equals(
                        dth!!.getDate(
                            yesterday_cal!!.timeInMillis, AppUtils.DATE_FORMAT
                        ),true
                    )
            ) binding.include1.lblToolbarTitle.text = sh!!.get_string(R.string.str_yesterday)
            else binding.include1.lblToolbarTitle.text = dth!!.getDate(
                filter_cal!!.timeInMillis,
                AppUtils.DATE_FORMAT
            )
            setCustomDate(dth!!.getDate(filter_cal!!.timeInMillis, AppUtils.DATE_FORMAT))
        }
    }

    private fun getAllReminderData() {
        val reminder_data: MutableList<NextReminderModel> = ArrayList()

        val arr_data = dh!!.getdata("tbl_alarm_details")

        for (k in arr_data.indices) {
            if (arr_data[k]["AlarmType"].equals("R", ignoreCase = true)) {
                if (!SharedPreferencesManager.isManualReminder) {
                    val arr_data2 =
                        dh!!.getdata("tbl_alarm_sub_details", "SuperId='" + arr_data[k]["id"] + "'")
                    for (j in arr_data2.indices) {
                        reminder_data.add(
                            NextReminderModel(
                                getMillisecond(arr_data2[j]["AlarmTime"]),
                                arr_data2[j]["AlarmTime"]!!
                            )
                        )
                    }
                }
            } else {
                if (SharedPreferencesManager.isManualReminder) {
                    if (arr_data[k]["IsOff"].equals("0", ignoreCase = true)) {
                        reminder_data.add(
                            NextReminderModel(
                                getMillisecond(arr_data[k]["AlarmTime"]),
                                arr_data[k]["AlarmTime"]!!
                            )
                        )
                    }
                }
            }
        }

        val mDate = Date()
        reminder_data.sort()
        var tmp_pos = 0
        for (k in reminder_data.indices) {
            if (reminder_data[k].millesecond > mDate.time) {
                tmp_pos = k
                break
            }
        }

        binding.nextReminderBlock.visibility = View.VISIBLE

        if (reminder_data.size > 0) {

            binding.lblNextReminder.text = sh!!.get_string(R.string.str_next_reminder)
                .replace("$1", reminder_data[tmp_pos].time)
        } else binding.nextReminderBlock.visibility = View.INVISIBLE
    }

    private fun getMillisecond(givenDateString: String?): Long {
        var givenDateString = givenDateString
        var timeInMilliseconds: Long = 0

        givenDateString = dth!!.getFormatDate("yyyy-MM-dd") + " " + givenDateString

        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
        try {
            val mDate = sdf.parse(givenDateString)
            if (mDate != null) {
                timeInMilliseconds = mDate.time
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            ah!!.Show_Alert_Dialog(e.message)
        }

        return timeInMilliseconds
    }

    private fun fetchNextReminder() {
        runnableReminder = Runnable {
            getAllReminderData()
            handlerReminder!!.postDelayed(runnableReminder!!, 1000)
        }
        handlerReminder = Handler()
        handlerReminder!!.postDelayed(runnableReminder!!, 1000)
    }


    private fun setFrame() {

        binding.contentFrame.viewTreeObserver
            .addOnGlobalLayoutListener { // TODO Auto-generated method stub
                val w = binding.contentFrame.width
                val h = binding.contentFrame.height
                v("getWidthHeight", "$w   -   $h")
            }

        binding.contentFrameTest.viewTreeObserver
            .addOnGlobalLayoutListener { // TODO Auto-generated method stub
                val w = binding.contentFrameTest.width
                val h = binding.contentFrameTest.height
                v("getWidthHeight test", "$w   -   $h")
                max_bottle_height = h - 30
            }

    }

    @SuppressLint("SetTextI18n")
    private fun body() {
        val arr_data = dh!!.getdata(
            "tbl_drink_details",
            ("DrinkDate ='" + dth!!.getCurrentDate(AppUtils.DATE_FORMAT)) + "'"
        )
        old_drink_water = 0f
        for (k in arr_data.indices) {

            if (AppUtils.WATER_UNIT_VALUE.equals("ml",true)){
                val c = arr_data[k]["ContainerValue"]!!.toFloat()
                old_drink_water += c
            }
            else{
                val c = arr_data[k]["ContainerValueOZ"]!!.toFloat()
                old_drink_water += c
            }
        }

        count_today_drink(false)

        binding.selectedContainerBlock.setOnClickListener { openChangeContainerPicker() }

        binding.openHistory.setOnClickListener {
            intent = Intent(act, MenuActivity::class.java)
            intent!!.putExtra("Class",1)
            startActivity(intent)
        }

        binding.openReachedGoal.setOnClickListener {
            intent = Intent(act, MenuActivity::class.java)
            intent!!.putExtra("Class",7)
            startActivity(intent)
        }

        binding.addWater.setOnClickListener {
            if (containerArrayList.size > 0) {
                if (!dth!!.getDate(filter_cal!!.timeInMillis, AppUtils.DATE_FORMAT)
                        .equals(
                            dth!!.getDate(
                                today_cal!!.timeInMillis,
                                AppUtils.DATE_FORMAT
                            ),true
                        )
                ) {
                    return@setOnClickListener
                }

                if (!btnclick) return@setOnClickListener

                btnclick = false

                val random = Random()

                if (random.nextBoolean()) {
                    AppUtils.RELOAD_DASHBOARD = false
                    execute_add_water()
                    AppUtils.RELOAD_DASHBOARD = true
                } else {
                    execute_add_water()
                }
            }
        }

        load_all_container()

        val unit: String = SharedPreferencesManager.waterUnit.toString()

        if (unit.equals("ml", ignoreCase = true)) {
            binding.containerName.text =
                ("" + containerArrayList[selected_pos].containerValue + " " + unit)
            if (containerArrayList[selected_pos].isCustom) Glide.with(mContext!!)
                .load(R.drawable.ic_custom_ml).into(binding.imgSelectedContainer)
            else Glide.with(mContext!!)
                .load(getImage(containerArrayList[selected_pos].containerValue.toString())).into(
                    binding.imgSelectedContainer
                )
        } else {
            binding.containerName.text =
                ("" + containerArrayList[selected_pos].containerValueOZ) + " " + unit
            if (containerArrayList[selected_pos].isCustom) Glide.with(mContext!!)
                .load(R.drawable.ic_custom_ml).into(binding.imgSelectedContainer)
            else Glide.with(mContext!!)
                .load(containerArrayList[selected_pos].containerValueOZ?.let { getImage(it) }).into(
                    binding.imgSelectedContainer
                )
        }

        adapter = ContainerAdapterNew(act!!, containerArrayList, object : ContainerAdapterNew.CallBack {

            @SuppressLint("NotifyDataSetChanged")
            override fun onClickSelect(container: Container?, position: Int) {
                bottomSheetDialog!!.dismiss()

                selected_pos = position

                SharedPreferencesManager.selectedContainer = container!!.containerId!!.toInt()

                for (k in containerArrayList.indices) {
                    containerArrayList[k].isSelected(false)
                }

                containerArrayList[position].isSelected(true)

                adapter!!.notifyDataSetChanged()

                val unit: String = SharedPreferencesManager.waterUnit.toString()

                if (unit.equals("ml", ignoreCase = true)) {
                    binding.containerName.text = ("" + container.containerValue + " " + unit)
                    if (container.isCustom) Glide.with(mContext!!).load(R.drawable.ic_custom_ml)
                        .into(binding.imgSelectedContainer)
                    else Glide.with(mContext!!).load(container.containerValue?.let { getImage(it) }).into(
                        binding.imgSelectedContainer
                    )
                } else {
                    binding.containerName.text =
                        ("" + container.containerValueOZ) + " " + unit
                    if (container.isCustom) Glide.with(mContext!!).load(R.drawable.ic_custom_ml)
                        .into(binding.imgSelectedContainer)
                    else Glide.with(mContext!!).load(container.containerValueOZ?.let { getImage(it) }).into(
                        binding.imgSelectedContainer
                    )
                }
            }
        })
    }


    private fun execute_add_water() {

        if (AppUtils.WATER_UNIT_VALUE.equals("ml",true)
            && drink_water > 8000
        ) {
            showDailyMoreThanTargetDialog()
            btnclick = true
            return
        } else if (!(AppUtils.WATER_UNIT_VALUE.equals("ml",true))
            && drink_water > 270
        ) {
            showDailyMoreThanTargetDialog()
            btnclick = true
            return
        }

        var count_drink_after_add_current_water = drink_water

        if (AppUtils.WATER_UNIT_VALUE.equals("ml",true)) 
            count_drink_after_add_current_water += containerArrayList[selected_pos].containerValue!!.toFloat()
        else if (!(AppUtils.WATER_UNIT_VALUE.equals("ml",true))) 
            count_drink_after_add_current_water +=
                containerArrayList[selected_pos].containerValueOZ!!.toFloat()

        d(
            "above8000", (("" + AppUtils.WATER_UNIT_VALUE).toString() + " @@@  " + drink_water
                    + " @@@ " + count_drink_after_add_current_water)
        )

        if (AppUtils.WATER_UNIT_VALUE.equals("ml",true)
            && count_drink_after_add_current_water > 8000
        ) {
            if (drink_water >= 8000) showDailyMoreThanTargetDialog()
            else if (AppUtils.DAILY_WATER_VALUE < 
                (8000 - containerArrayList[selected_pos].containerValue!!.toFloat()))
                showDailyMoreThanTargetDialog()
        } else if (!(AppUtils.WATER_UNIT_VALUE.equals("ml",true))
            && count_drink_after_add_current_water > 270
        ) {
            if (drink_water >= 270) showDailyMoreThanTargetDialog()
            else if (AppUtils.DAILY_WATER_VALUE < (270 - containerArrayList[selected_pos]
                .containerValueOZ!!.toFloat())) showDailyMoreThanTargetDialog()
        }

        if (drink_water == 8000f && AppUtils.WATER_UNIT_VALUE.equals("ml",true)) {
            btnclick = true
            return
        } else if (drink_water == 270f && !AppUtils.WATER_UNIT_VALUE.equals("ml",true)) {
            btnclick = true
            return
        }

        if (!SharedPreferencesManager.disableSoundWhenAddWater) {
            ringtone!!.stop()
            ringtone!!.play()
        }

        val initialValues = ContentValues()

        initialValues.put(
            "ContainerValue",
            "" + containerArrayList[selected_pos].containerValue
        )
        initialValues.put(
            "ContainerValueOZ",
            "" + containerArrayList[selected_pos].containerValueOZ
        )
        initialValues.put(
            "DrinkDate",
            "" + dth!!.getDate(filter_cal!!.timeInMillis, AppUtils.DATE_FORMAT)
        )
        initialValues.put("DrinkTime", "" + dth!!.getCurrentTime(true))
        initialValues.put(
            "DrinkDateTime", (("" + dth!!.getDate(filter_cal!!.timeInMillis, AppUtils.DATE_FORMAT)
                    ).toString() + " " + dth!!.getCurrentDate("HH:mm:ss"))
        )

        if (AppUtils.WATER_UNIT_VALUE.equals("ml",true)) {
            initialValues.put("TodayGoal", "" + AppUtils.DAILY_WATER_VALUE)
            initialValues.put(
                "TodayGoalOZ",
                "" + HeightWeightHelper.mlToOzConverter(AppUtils.DAILY_WATER_VALUE)
            )
        } else {
            initialValues.put(
                "TodayGoal",
                "" + HeightWeightHelper.ozToMlConverter(AppUtils.DAILY_WATER_VALUE)
            )
            initialValues.put("TodayGoalOZ", "" + AppUtils.DAILY_WATER_VALUE)
        }

        dh!!.insert("tbl_drink_details", initialValues)

        count_today_drink(true)
        
        val intent = Intent(act, NewAppWidget::class.java)
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE)

        val ids = AppWidgetManager.getInstance(act).getAppWidgetIds(
            ComponentName(
                act!!,
                NewAppWidget::class.java
            )
        )
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        act!!.sendBroadcast(intent)
    }


    private fun load_all_container() {
        containerArrayList.clear()

        val arr_container = dh!!.getdata("tbl_container_details", "IsCustom", 1)

        var selected_container_id = "1"

        selected_container_id = if (SharedPreferencesManager.selectedContainer == 0) "1"
        else "" + SharedPreferencesManager.selectedContainer
        
        for (k in arr_container.indices) {
            val container = Container()
            container.containerId = arr_container[k]["ContainerID"]
            container.containerValue = arr_container[k]["ContainerValue"]
            container.containerValueOZ = arr_container[k]["ContainerValueOZ"]
            container.isOpen(
                arr_container[k]["IsOpen"].equals(
                        "1",
                        ignoreCase = true
                    )
            )
            container.isSelected(
                selected_container_id.equals(
                        arr_container[k]["ContainerID"],
                        ignoreCase = true
                    )
            )
            container.isCustom(
                arr_container[k]["IsCustom"].equals(
                        "1",
                        ignoreCase = true
                    )
            )
            if (container.isSelected) selected_pos = k //+1

            containerArrayList.add(container)
        }
    }

    private fun count_today_drink(isRegularAnimation: Boolean) {
       
        val arr_data = dh!!.getdata(
            "tbl_drink_details",
            ("DrinkDate ='" + dth!!.getDate(
                filter_cal!!.timeInMillis,
                AppUtils.DATE_FORMAT
            )) + "'"
        )

        drink_water = 0f
        for (k in arr_data.indices) {
            drink_water += if (AppUtils.WATER_UNIT_VALUE.equals("ml",true))
                arr_data[k]["ContainerValue"]!!.toFloat()
            else
                arr_data[k]["ContainerValueOZ"]!!.toFloat()
        }

        binding.lblTotalDrunk.text =
            getData("" + (drink_water).toInt() + " " + AppUtils.WATER_UNIT_VALUE)
        binding.lblTotalGoal.text =
            getData("" + (AppUtils.DAILY_WATER_VALUE) + " " + AppUtils.WATER_UNIT_VALUE)

        refresh_bottle(true, isRegularAnimation)
    }

    private fun getData(str: String): String {
        return str.replace(",", ".")
    }

    private fun callDialog() {
        if (old_drink_water < AppUtils.DAILY_WATER_VALUE) {
            if (drink_water >= AppUtils.DAILY_WATER_VALUE) {
                addDataToReachedGoal()
                showDailyGoalReachedDialog()
            }
        }

        old_drink_water = drink_water
    }

    private fun addDataToReachedGoal() {
        val initialValues = ContentValues()

        val arr_data = dh!!.getdata(
            "tbl_drink_details",
            ("DrinkDate ='" + dth!!.getDate(
                filter_cal!!.timeInMillis,
                AppUtils.DATE_FORMAT
            )) + "'"
        )

        var drink_water = 0f
        var drink_waterOZ = 0f
        for (k in arr_data.indices) {
            drink_water += arr_data[k]["ContainerValue"]!!.toFloat()
            drink_waterOZ += arr_data[k]["ContainerValueOZ"]!!.toFloat()
        }

        initialValues.put(
            "ContainerValue",
            "" + drink_water
        )
        initialValues.put(
            "ContainerValueOZ",
            "" + drink_waterOZ
        )
        initialValues.put(
            "DrinkDate",
            dth!!.formateDateFromString(
                "dd-MM-yy", "dd-MM-yyyy",
                dth!!.currentDate
            )
        )
        dh!!.insert("tbl_reached_goal_details", initialValues)
    }

    private fun refresh_bottle(isFromCurrentProgress: Boolean, isRegularAnimation: Boolean) {
        val animationDuration = (if (isRegularAnimation) 50 else 5).toLong()

        if (handler != null && runnable != null) handler!!.removeCallbacks(runnable!!)

        btnclick = false

        cp = progress_bottle_height
        np = Math.round((drink_water * max_bottle_height) / AppUtils.DAILY_WATER_VALUE).toInt()

        if (cp <= np && isFromCurrentProgress) {
            binding.animationView.visibility = View.VISIBLE
            runnable = Runnable {
                if (cp > max_bottle_height) {
                    btnclick = true
                    callDialog()
                } else if (cp < np) {
                    cp += 6
                    binding.contentFrame.layoutParams.height = cp
                    binding.contentFrame.requestLayout()
                    handler!!.postDelayed(runnable!!, animationDuration)
                } else {
                    btnclick = true
                    callDialog()
                }
            }
            handler = Handler()
            handler!!.postDelayed(runnable!!, animationDuration)
        } else if (np == 0) {
            binding.animationView.visibility = View.GONE
            binding.contentFrame.layoutParams.height = np
            binding.contentFrame.requestLayout()
            btnclick = true
            callDialog()
        } else {
            binding.contentFrame.layoutParams.height = 0
            cp = 0
            binding.animationView.visibility = View.VISIBLE
            runnable = Runnable {
                if (cp > max_bottle_height) {
                    btnclick = true
                    callDialog()
                } else if (cp < np) {
                    cp += 6
                    binding.contentFrame.layoutParams.height = cp
                    binding.contentFrame.requestLayout()
                    handler!!.postDelayed(runnable!!, animationDuration)
                } else {
                    btnclick = true
                    callDialog()
                }
            }
            handler = Handler()
            handler!!.postDelayed(runnable!!, animationDuration)
        }

        progress_bottle_height = np

        if (np > 0) binding.animationView.visibility = View.VISIBLE
        else binding.animationView.visibility = View.GONE
    }

    private fun count_specific_day_drink(custom_date: String) {
        val arr_dataO = dh!!.getdata(
            "tbl_drink_details",
            "DrinkDate ='$custom_date'"
        )
        old_drink_water = 0f
        for (k in arr_dataO.indices) {
            if (AppUtils.WATER_UNIT_VALUE.equals("ml",true))
                    old_drink_water += arr_dataO[k]["ContainerValue"]!!.toFloat()
            else
                    old_drink_water += arr_dataO[k]["ContainerValueOZ"]!!.toFloat()
        }

        val arr_data22 = dh!!.getdata(
            "tbl_drink_details",
            "DrinkDate ='$custom_date'", "id", 1
        )

        var total_drink = 0.0

        if (arr_data22.size > 0) {
            total_drink =
                if (AppUtils.WATER_UNIT_VALUE.equals("ml",true)) arr_data22[0]["TodayGoal"]!!
                    .toDouble()
                else arr_data22[0]["TodayGoalOZ"]!!.toDouble()
        }

        val arr_data = dh!!.getdata(
            "tbl_drink_details",
            "DrinkDate ='$custom_date'"
        )

        drink_water = 0f
        for (k in arr_data.indices) {
            drink_water += if (AppUtils.WATER_UNIT_VALUE.equals("ml",true)) arr_data[k]["ContainerValue"]!!
                .toInt()
            else arr_data[k]["ContainerValueOZ"]!!.toInt()

        }

        if (custom_date.equals(
                dth!!.getCurrentDate(AppUtils.DATE_FORMAT),
                ignoreCase = true
            )
        ) AppUtils.DAILY_WATER_VALUE = SharedPreferencesManager.dailyWater
        else if (total_drink > 0) AppUtils.DAILY_WATER_VALUE = ("" + total_drink).toFloat()
        else AppUtils.DAILY_WATER_VALUE = SharedPreferencesManager.dailyWater

        binding.lblTotalDrunk.text =
            getData("" + (drink_water).toInt() + " " + AppUtils.WATER_UNIT_VALUE)
        binding.lblTotalGoal.text =
            getData("" + (AppUtils.DAILY_WATER_VALUE) + " " + AppUtils.WATER_UNIT_VALUE)

        refresh_bottle(false, false)
    }

    val activity: Activity
        get() = act!!

    private fun setCustomDate(date: String) {
        count_specific_day_drink(date)
    }

    @SuppressLint("InflateParams")
    private fun openChangeContainerPicker() {
        bottomSheetDialog = BottomSheetDialog(act!!)

        bottomSheetDialog!!.setOnShowListener(OnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
                    ?: return@OnShowListener
            val bottomSheetBehavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
            bottomSheet.background = null
        })

        val layoutInflater = LayoutInflater.from(act)
        val view: View = layoutInflater.inflate(R.layout.bottom_sheet_change_container, null, false)

        val containerRecyclerViewN = view.findViewById<RecyclerView>(R.id.containerRecyclerView)
        val add_custom_container = view.findViewById<RelativeLayout>(R.id.add_custom_container)

        val manager = GridLayoutManager(act, 3, GridLayoutManager.VERTICAL, false)
        containerRecyclerViewN.layoutManager = manager
        containerRecyclerViewN.adapter = adapter

        add_custom_container.setOnClickListener {
            bottomSheetDialog!!.dismiss()
            openCustomContainerPicker()
        }

        bottomSheetDialog!!.setContentView(view)

        bottomSheetDialog!!.show()
    }


    @SuppressLint("Recycle", "SetTextI18n", "NotifyDataSetChanged")
    private fun openCustomContainerPicker() {
        val dialog = Dialog(act!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.drawable_background_tra)
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)


        val view: View = LayoutInflater.from(act)
            .inflate(R.layout.bottom_sheet_add_custom_container, null, false)

        val btn_cancel = view.findViewById<RelativeLayout>(R.id.btn_cancel)
        val btn_add = view.findViewById<RelativeLayout>(R.id.btn_add)
        val img_cancel = view.findViewById<ImageView>(R.id.img_cancel)

        val txt_value = view.findViewById<AppCompatEditText>(R.id.txt_value)
        val lbl_unit = view.findViewById<AppCompatTextView>(R.id.lbl_unit)

        lbl_unit.text =
            sh!!.get_string(R.string.str_capacity).replace("$1", AppUtils.WATER_UNIT_VALUE)

        if (AppUtils.WATER_UNIT_VALUE.equals("ml",true)) txt_value.filters =
            arrayOf<InputFilter>(InputFilterWeightRange(1.0, 8000.0))
        else txt_value.filters = arrayOf<InputFilter>(InputFilterWeightRange(1.0, 270.0))

        txt_value.requestFocus()

        btn_cancel.setOnClickListener { dialog.cancel() }

        img_cancel.setOnClickListener { dialog.cancel() }

        btn_add.setOnClickListener {
            if (sh!!.check_blank_data(txt_value.text.toString().trim { it <= ' ' })) {
                ah!!.customAlert(sh!!.get_string(R.string.str_enter_value_validation))
            } else if (txt_value.text.toString().trim { it <= ' ' }.toInt() == 0) {
                ah!!.customAlert(sh!!.get_string(R.string.str_enter_value_validation))
            } else {
                var tml = 0f
                var tfloz = 0f

                if (AppUtils.WATER_UNIT_VALUE.equals("ml",true)) {
                    tml = txt_value.text.toString().trim { it <= ' ' }.toFloat()
                    tfloz = HeightWeightHelper.mlToOzConverter(tml).toFloat()
                } else {
                    tfloz = txt_value.text.toString().trim { it <= ' ' }.toFloat()
                    tml = HeightWeightHelper.ozToMlConverter(tfloz).toFloat()
                }

                d("HeightWeightHelper", "$tml @@@ $tfloz")


                val c: Cursor = AppUtils.SDB!!.rawQuery(
                    "SELECT MAX(ContainerID) FROM tbl_container_details",
                    null
                )
                var nextContainerID = 0

                try {
                    if (c.count > 0) {
                        c.moveToNext()
                        nextContainerID = c.getString(0).toInt() + 1
                    }
                } catch (e: Exception) {
                    e.message?.let { it1 -> e(Throwable(e), it1) }
                }

                val initialValues = ContentValues()

                initialValues.put("ContainerID", "" + nextContainerID)
                initialValues.put("ContainerValue", "" + Math.round(tml))
                initialValues.put("ContainerValueOZ", "" + Math.round(tfloz))
                initialValues.put("IsOpen", "1")
                initialValues.put("IsCustom", "1")

                dh!!.insert("tbl_container_details", initialValues)

                load_all_container()

                SharedPreferencesManager.selectedContainer = nextContainerID

                var tmp_pos = -1

                for (k in containerArrayList.indices) {
                    try {
                        if (nextContainerID == containerArrayList[k].containerId!!.toInt()) {
                            containerArrayList[k].isSelected(true)
                            tmp_pos = k
                        } else containerArrayList[k].isSelected(false)
                    } catch (e: Exception) {
                        containerArrayList[k].isSelected(false)
                    }
                }


                val unit: String = SharedPreferencesManager.waterUnit.toString()

                if (tmp_pos >= 0) {
                    selected_pos = tmp_pos

                    val menu = containerArrayList[tmp_pos]

                    if (unit.equals("ml", ignoreCase = true)) {
                        binding.containerName.text =
                            ("" + menu.containerValue) + " " + unit
                        if (menu.isCustom) Glide.with(mContext!!).load(R.drawable.ic_custom_ml)
                            .into(binding.imgSelectedContainer)
                        else Glide.with(mContext!!).load(menu.containerValue?.let { it1 ->
                            getImage(
                                it1
                            )
                        }).into(
                            binding.imgSelectedContainer
                        )
                    } else {
                        binding.containerName.text =
                            ("" + menu.containerValueOZ) + " " + unit
                        if (menu.isCustom) Glide.with(mContext!!).load(R.drawable.ic_custom_ml)
                            .into(binding.imgSelectedContainer)
                        else Glide.with(mContext!!).load(menu.containerValueOZ?.let { it1 ->
                            getImage(
                                it1
                            )
                        }).into(
                            binding.imgSelectedContainer
                        )
                    }
                }

                adapter!!.notifyDataSetChanged()

                dialog.dismiss()
            }
        }

        dialog.setContentView(view)

        dialog.show()
    }

    //=============================
    fun getImage(`val`: String): Int {
        var drawable: Int = R.drawable.ic_custom_ml

        if (AppUtils.WATER_UNIT_VALUE.equals("ml",true)) {
            if (`val`.toFloat() == 50f) drawable = R.drawable.ic_50_ml
            else if (`val`.toFloat() == 100f) drawable = R.drawable.ic_100_ml
            else if (`val`.toFloat() == 150f) drawable = R.drawable.ic_150_ml
            else if (`val`.toFloat() == 200f) drawable = R.drawable.ic_200_ml
            else if (`val`.toFloat() == 250f) drawable = R.drawable.ic_250_ml
            else if (`val`.toFloat() == 300f) drawable = R.drawable.ic_300_ml
            else if (`val`.toFloat() == 500f) drawable = R.drawable.ic_500_ml
            else if (`val`.toFloat() == 600f) drawable = R.drawable.ic_600_ml
            else if (`val`.toFloat() == 700f) drawable = R.drawable.ic_700_ml
            else if (`val`.toFloat() == 800f) drawable = R.drawable.ic_800_ml
            else if (`val`.toFloat() == 900f) drawable = R.drawable.ic_900_ml
            else if (`val`.toFloat() == 1000f) drawable = R.drawable.ic_1000_ml
        } else {
            if (`val`.toFloat() == 2f) drawable = R.drawable.ic_50_ml
            else if (`val`.toFloat() == 3f) drawable = R.drawable.ic_100_ml
            else if (`val`.toFloat() == 5f) drawable = R.drawable.ic_150_ml
            else if (`val`.toFloat() == 7f) drawable = R.drawable.ic_200_ml
            else if (`val`.toFloat() == 8f) drawable = R.drawable.ic_250_ml
            else if (`val`.toFloat() == 10f) drawable = R.drawable.ic_300_ml
            else if (`val`.toFloat() == 17f) drawable = R.drawable.ic_500_ml
            else if (`val`.toFloat() == 20f) drawable = R.drawable.ic_600_ml
            else if (`val`.toFloat() == 24f) drawable = R.drawable.ic_700_ml
            else if (`val`.toFloat() == 27f) drawable = R.drawable.ic_800_ml
            else if (`val`.toFloat() == 30f) drawable = R.drawable.ic_900_ml
            else if (`val`.toFloat() == 34f) drawable = R.drawable.ic_1000_ml
        }

        return drawable
    }


    //=============================
    @SuppressLint("InflateParams")
    private fun showDailyGoalReachedDialog() {
        val dialog = Dialog(act!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.drawable_background_tra)
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)


        val view: View = LayoutInflater.from(act).inflate(R.layout.dialog_goal_reached, null, false)


        val img_cancel = view.findViewById<ImageView>(R.id.img_cancel)
        val btn_share = view.findViewById<RelativeLayout>(R.id.btn_share)

        img_cancel.setOnClickListener { dialog.dismiss() }

        btn_share.setOnClickListener {
            dialog.dismiss()
            val appPackageName = mContext!!.packageName

            var share_text = sh!!.get_string(R.string.str_share_text)
                .replace("$1", "" + (drink_water).toInt() + " " + AppUtils.WATER_UNIT_VALUE)

            //share_text=share_text.replace("$2","@ https://play.google.com/store/apps/details?id=" + appPackageName);
            share_text = share_text.replace("$2", "@ " + AppUtils.APP_SHARE_URL)
            ih!!.ShareText(getApplicationName(mContext!!), share_text)
        }

        dialog.setOnDismissListener { }

        dialog.setContentView(view)

        dialog.show()
    }

    @SuppressLint("InflateParams")
    private fun showDailyMoreThanTargetDialog() {
        val dialog = Dialog(act!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.drawable_background_tra)
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)


        val view: View =
            LayoutInflater.from(act).inflate(R.layout.dialog_goal_target_reached, null, false)


        val lbl_desc = view.findViewById<AppCompatTextView>(R.id.lbl_desc)
        val img_cancel = view.findViewById<ImageView>(R.id.img_cancel)
        val btn_share = view.findViewById<RelativeLayout>(R.id.btn_share)
        val img_bottle = view.findViewById<ImageView>(R.id.img_bottle)

        if (AppUtils.WATER_UNIT_VALUE.equals("ml",true)) img_bottle.setImageResource(R.drawable.ic_limit_ml)
        else img_bottle.setImageResource(R.drawable.ic_limit_oz)

        val desc =
            if (AppUtils.WATER_UNIT_VALUE.equals("ml",true)) "8000 ml" else "270 fl oz"

        lbl_desc.text =
            sh!!.get_string(R.string.str_you_should_not_drink_more_then_target).replace("$1", desc)

        img_cancel.setOnClickListener { dialog.dismiss() }

        dialog.setContentView(view)

        dialog.show()
    }


    //=======================================
    //======================================
    @SuppressLint("InflateParams", "UseCompatLoadingForDrawables")
    private fun showReminderDialog() {
        val dialog = Dialog(act!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.drawable_background_tra)
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)


        val view: View = LayoutInflater.from(act).inflate(R.layout.dialog_reminder, null, false)


        val img_cancel = view.findViewById<ImageView>(R.id.img_cancel)
        val btn_save = view.findViewById<RelativeLayout>(R.id.btn_save)

        val off_block = view.findViewById<RelativeLayout>(R.id.off_block)
        val silent_block = view.findViewById<RelativeLayout>(R.id.silent_block)
        val auto_block = view.findViewById<RelativeLayout>(R.id.auto_block)

        val img_off = view.findViewById<ImageView>(R.id.img_off)
        val img_silent = view.findViewById<ImageView>(R.id.img_silent)
        val img_auto = view.findViewById<ImageView>(R.id.img_auto)

        val advance_settings = view.findViewById<AppCompatTextView>(R.id.advance_settings)

        advance_settings.setOnClickListener {
            dialog.dismiss()
            val i = Intent(act,MenuActivity::class.java)
            i.putExtra("Class",6)
            startActivity(i)
        }

        val custom_sound_block = view.findViewById<LinearLayout>(R.id.custom_sound_block)

        custom_sound_block.setOnClickListener { openSoundMenuPicker() }


        val switch_vibrate = view.findViewById<SwitchCompat>(R.id.switch_vibrate)

        switch_vibrate.isChecked = !SharedPreferencesManager.reminderVibrate

        switch_vibrate.setOnCheckedChangeListener { buttonView, isChecked ->
            SharedPreferencesManager.reminderVibrate = !isChecked
        }

        if (SharedPreferencesManager.reminderOpt == 1) {
            off_block.background =
                mContext!!.resources.getDrawable(R.drawable.drawable_circle_selected)
            img_off.setImageResource(R.drawable.ic_off_selected)

            silent_block.background =
                mContext!!.resources.getDrawable(R.drawable.drawable_circle_unselected)
            img_silent.setImageResource(R.drawable.ic_silent_normal)

            auto_block.background =
                mContext!!.resources.getDrawable(R.drawable.drawable_circle_unselected)
            img_auto.setImageResource(R.drawable.ic_auto_normal)
        } else if (SharedPreferencesManager.reminderOpt == 2) {
            off_block.background =
                mContext!!.resources.getDrawable(R.drawable.drawable_circle_unselected)
            img_off.setImageResource(R.drawable.ic_off_normal)

            silent_block.background =
                mContext!!.resources.getDrawable(R.drawable.drawable_circle_selected)
            img_silent.setImageResource(R.drawable.ic_silent_selected)

            auto_block.background =
                mContext!!.resources.getDrawable(R.drawable.drawable_circle_unselected)
            img_auto.setImageResource(R.drawable.ic_auto_normal)
        } else {
            off_block.background =
                mContext!!.resources.getDrawable(R.drawable.drawable_circle_unselected)
            img_off.setImageResource(R.drawable.ic_off_normal)

            silent_block.background =
                mContext!!.resources.getDrawable(R.drawable.drawable_circle_unselected)
            img_silent.setImageResource(R.drawable.ic_silent_normal)

            auto_block.background =
                mContext!!.resources.getDrawable(R.drawable.drawable_circle_selected)
            img_auto.setImageResource(R.drawable.ic_auto_selected)
        }

        off_block.setOnClickListener {
            off_block.background =
                mContext!!.resources.getDrawable(R.drawable.drawable_circle_selected)
            img_off.setImageResource(R.drawable.ic_off_selected)

            silent_block.background =
                mContext!!.resources.getDrawable(R.drawable.drawable_circle_unselected)
            img_silent.setImageResource(R.drawable.ic_silent_normal)

            auto_block.background =
                mContext!!.resources.getDrawable(R.drawable.drawable_circle_unselected)
            img_auto.setImageResource(R.drawable.ic_auto_normal)
            SharedPreferencesManager.reminderOpt = 1
        }

        silent_block.setOnClickListener {
            off_block.background =
                mContext!!.resources.getDrawable(R.drawable.drawable_circle_unselected)
            img_off.setImageResource(R.drawable.ic_off_normal)

            silent_block.background =
                mContext!!.resources.getDrawable(R.drawable.drawable_circle_selected)
            img_silent.setImageResource(R.drawable.ic_silent_selected)

            auto_block.background =
                mContext!!.resources.getDrawable(R.drawable.drawable_circle_unselected)
            img_auto.setImageResource(R.drawable.ic_auto_normal)
            SharedPreferencesManager.reminderOpt = 2
        }

        auto_block.setOnClickListener {
            off_block.background =
                mContext!!.resources.getDrawable(R.drawable.drawable_circle_unselected)
            img_off.setImageResource(R.drawable.ic_off_normal)

            silent_block.background =
                mContext!!.resources.getDrawable(R.drawable.drawable_circle_unselected)
            img_silent.setImageResource(R.drawable.ic_silent_normal)

            auto_block.background =
                mContext!!.resources.getDrawable(R.drawable.drawable_circle_selected)
            img_auto.setImageResource(R.drawable.ic_auto_selected)
            SharedPreferencesManager.reminderOpt = 0
        }

        img_cancel.setOnClickListener { dialog.dismiss() }

        btn_save.setOnClickListener { dialog.dismiss() }

        dialog.setContentView(view)

        dialog.show()
    }

    @SuppressLint("InflateParams")
    private fun openSoundMenuPicker() {
        loadSounds()

        val dialog = Dialog(act!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.drawable_background_tra)
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)


        val view: View = LayoutInflater.from(act).inflate(R.layout.dialog_sound_pick, null, false)


        val btn_cancel = view.findViewById<RelativeLayout>(R.id.btn_cancel)
        val btn_save = view.findViewById<RelativeLayout>(R.id.btn_save)


        btn_cancel.setOnClickListener { dialog.dismiss() }

        btn_save.setOnClickListener {
            for (k in lst_sounds.indices) {
                if (lst_sounds[k].isSelected) {
                    SharedPreferencesManager.reminderSound = k
                    break
                }
            }
            dialog.dismiss()
        }


        val soundRecyclerView = view.findViewById<RecyclerView>(R.id.soundRecyclerView)

        soundAdapter = SoundAdapter(act!!, lst_sounds, object : SoundAdapter.CallBack {
            @SuppressLint("NotifyDataSetChanged")
            override fun onClickSelect(time: SoundModel?, position: Int) {
                for (k in lst_sounds.indices) {
                    lst_sounds[k].isSelected(false)
                }

                lst_sounds[position].isSelected(true)
                soundAdapter!!.notifyDataSetChanged()

                playSound(position)
            }
        })

        soundRecyclerView.layoutManager =
            LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false)

        soundRecyclerView.adapter = soundAdapter

        dialog.setContentView(view)

        dialog.show()
    }

    private fun loadSounds() {
        lst_sounds.clear()

        lst_sounds.add(getSoundModel(0, "Default"))
        lst_sounds.add(getSoundModel(1, "Bell"))
        lst_sounds.add(getSoundModel(2, "Blop"))
        lst_sounds.add(getSoundModel(3, "Bong"))
        lst_sounds.add(getSoundModel(4, "Click"))
        lst_sounds.add(getSoundModel(5, "Echo droplet"))
        lst_sounds.add(getSoundModel(6, "Mario droplet"))
        lst_sounds.add(getSoundModel(7, "Ship bell"))
        lst_sounds.add(getSoundModel(8, "Simple droplet"))
        lst_sounds.add(getSoundModel(9, "Tiny droplet"))
    }

    private fun getSoundModel(index: Int, name: String?): SoundModel {
        val soundModel = SoundModel()
        soundModel.id = index
        soundModel.name = name
        soundModel.isSelected(SharedPreferencesManager.reminderSound == index)

        return soundModel
    }

    fun playSound(idx: Int) {
        var mp: MediaPlayer? = null

        when (idx) {
            0 -> mp = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI)
            1 -> mp = MediaPlayer.create(this, R.raw.bell)
            2 -> mp = MediaPlayer.create(this, R.raw.blop)
            3 -> mp = MediaPlayer.create(this, R.raw.bong)
            4 -> mp = MediaPlayer.create(this, R.raw.click)
            5 -> mp = MediaPlayer.create(this, R.raw.echo_droplet)
            6 -> mp = MediaPlayer.create(this, R.raw.mario_droplet)
            7 -> mp = MediaPlayer.create(this, R.raw.ship_bell)
            8 -> mp = MediaPlayer.create(this, R.raw.simple_droplet)
            9 -> mp = MediaPlayer.create(this, R.raw.tiny_droplet)
        }

        mp!!.start()
    }

    @SuppressLint("RtlHardcoded")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

        super.onBackPressed()
        try {
            if (binding.drawerLayout.isDrawerOpen(Gravity.LEFT)) binding.drawerLayout.closeDrawer(Gravity.LEFT)
            else finish()
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e), it) }
        }
    }

    companion object {
        var filter_cal: Calendar? = null
        var today_cal: Calendar? = null
        var yesterday_cal: Calendar? = null

        fun getApplicationName(context: Context): String {
            val applicationInfo = context.applicationInfo
            val stringId = applicationInfo.labelRes
            return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(
                stringId
            )
        }
    }
}