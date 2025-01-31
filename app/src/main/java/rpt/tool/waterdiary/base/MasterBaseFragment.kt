package rpt.tool.waterdiary.base

import android.content.Context
import rpt.tool.waterdiary.utils.helpers.DBHelper

class MasterBaseFragment : BaseFragment() {
    var dbh: DBHelper? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dbh = activity?.let { DBHelper(it,it) }
    }
}