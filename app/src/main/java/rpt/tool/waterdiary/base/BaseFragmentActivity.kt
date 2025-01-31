package rpt.tool.waterdiary.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import rpt.tool.waterdiary.utils.ExceptionHandler
import rpt.tool.waterdiary.utils.UtilityFunction
import rpt.tool.waterdiary.utils.helpers.AlertHelper
import rpt.tool.waterdiary.utils.helpers.BitmapHelper
import rpt.tool.waterdiary.utils.helpers.DatabaseHelper
import rpt.tool.waterdiary.utils.helpers.DateHelper
import rpt.tool.waterdiary.utils.helpers.IntentHelper
import rpt.tool.waterdiary.utils.helpers.JsonHelper
import rpt.tool.waterdiary.utils.helpers.MapHelper
import rpt.tool.waterdiary.utils.helpers.StringHelper
import rpt.tool.waterdiary.utils.helpers.ZipHelper
import rpt.tool.waterdiary.utils.managers.SharedPreferencesManager

open class BaseFragmentActivity : FragmentActivity() {
    var mContext: Context? = null
    var act: Activity? = null

    var uf: UtilityFunction? = null
    var ah: AlertHelper? = null
    var jh: JsonHelper? = null
    var bh: BitmapHelper? = null
    var dth: DateHelper? = null
    var dh: DatabaseHelper? = null
    var mah: MapHelper? = null
    var sh: StringHelper? = null
    var ph: SharedPreferencesManager? = null
    var zh: ZipHelper? = null
    var ih: IntentHelper? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = this@BaseFragmentActivity
        act = this@BaseFragmentActivity

        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(act!!))

        uf = UtilityFunction(mContext!!, act!!)
        ah = AlertHelper(mContext!!)
        jh = JsonHelper(mContext!!)
        bh = BitmapHelper(mContext!!)
        dth = DateHelper()
        dh = DatabaseHelper(mContext!!,act!!)
        ih = IntentHelper(mContext!!, act!!)
        mah = MapHelper()
        sh = StringHelper(mContext!!, act!!)
        ph = SharedPreferencesManager
        zh = ZipHelper(mContext!!)

        uf!!.permission_StrictMode()
    }
}
