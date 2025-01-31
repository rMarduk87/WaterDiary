package rpt.tool.waterdiary


import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import rpt.tool.waterdiary.base.MasterBaseAppCompatActivity
import rpt.tool.waterdiary.databinding.ActivitySplashScreenBinding
import rpt.tool.waterdiary.utils.managers.SharedPreferencesManager
import rpt.tool.waterdiary.utils.view.widget.NewAppWidget


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : MasterBaseAppCompatActivity() {
    var handler: Handler? = null
    var runnable: Runnable? = null
    var millisecond: Int = 1000
    lateinit var binding : ActivitySplashScreenBinding

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent: Intent = Intent(act, NewAppWidget::class.java)
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

    @SuppressLint("UnsafeIntentLaunch")
    protected override fun onResume() {
        super.onResume()

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

        runnable = Runnable {
            if (SharedPreferencesManager.hideWelcomeScreen) {
                intent = Intent(
                    this@SplashScreenActivity,
                    DashboardActivity::class.java
                )
            } else {
                SharedPreferencesManager.personWeightUnit = true
                SharedPreferencesManager.personWeight = "80"
                SharedPreferencesManager.userName = ""
                intent = Intent(
                    this@SplashScreenActivity,
                    InitUserInfoActivity::class.java
                )
            }
            startActivity(intent)
            finish()
        }
        handler = Handler()
        handler!!.postDelayed(runnable!!, millisecond.toLong())
    }
}