package rpt.tool.waterdiary.base

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import rpt.tool.waterdiary.R
import rpt.tool.waterdiary.utils.helpers.DBHelper

interface OnActivityListener {
    fun onBackPressed(): Boolean
}

open class MasterBaseAppCompatActivity : BaseAppCompatActivity() {
    var dbh: DBHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbh = DBHelper(this@MasterBaseAppCompatActivity,this@MasterBaseAppCompatActivity)
    }

    companion object {
        fun getThemeColor(ctx: Context): Int {
            return ctx.resources.getColor(R.color.colorPrimaryDark)
        }

        fun getThemeColorArray(ctx: Context?): IntArray {
            val colors = intArrayOf(Color.parseColor("#001455da"), Color.parseColor("#FF1455da"))

            return colors
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.init_user_info_activity_nav_host_fragment)
        if (fragment is OnActivityListener) {
            val handled = (fragment as OnActivityListener).onBackPressed()
            if (!handled) {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }
}
