package rpt.tool.waterdiary.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
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

open class BaseFragment : Fragment() {
    var mContext: Context? = null
    var act: Activity? = null

    var intent: Intent? = null

    var uf: UtilityFunction? = null
    var ah: AlertHelper? = null
    var jh: JsonHelper? = null
    var bh: BitmapHelper? = null
    var dth: DateHelper? = null
    var mah: MapHelper? = null
    var sh: StringHelper? = null
    var zh: ZipHelper? = null
    var ih: IntentHelper? = null
    var dh: DatabaseHelper? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = activity
        act = activity

        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(act!!))

        uf = UtilityFunction(mContext!!, act!!)
        ah = AlertHelper(mContext!!)
        jh = JsonHelper(mContext!!)
        bh = BitmapHelper(mContext!!)
        dth = DateHelper()
        ih = IntentHelper(mContext!!, act!!)
        mah = MapHelper()
        sh = StringHelper(mContext!!, act!!)
        zh = ZipHelper(mContext!!)
        dh = DatabaseHelper(mContext!!,act!!)

        uf!!.permission_StrictMode()
    }
}
