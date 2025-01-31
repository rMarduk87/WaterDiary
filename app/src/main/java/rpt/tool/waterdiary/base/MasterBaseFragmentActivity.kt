package rpt.tool.waterdiary.base

import android.os.Bundle
import rpt.tool.waterdiary.utils.helpers.DBHelper

class MasterBaseFragmentActivity : BaseFragmentActivity() {
    var dbh: DBHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbh = DBHelper(this@MasterBaseFragmentActivity,this)
    }
}