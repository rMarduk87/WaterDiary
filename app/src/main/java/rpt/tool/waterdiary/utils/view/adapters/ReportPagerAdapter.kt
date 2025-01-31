package rpt.tool.waterdiary.utils.view.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import rpt.tool.waterdiary.R
import rpt.tool.waterdiary.ui.report.MonthReportFragment
import rpt.tool.waterdiary.ui.report.WeekReportFragment
import rpt.tool.waterdiary.ui.report.YearReportFragment

class ReportPagerAdapter(fm: FragmentManager, context: Context) :
    FragmentStatePagerAdapter(fm) {
    var tab1Fragment: WeekReportFragment = WeekReportFragment()
    var tab2Fragment: MonthReportFragment = MonthReportFragment()
    var tab3Fragment: YearReportFragment = YearReportFragment()

    var mContext: Context = context

    override fun getItem(position: Int): Fragment {
        return if (position == 0) {
            tab1Fragment
        } else if (position == 1) {
            tab2Fragment
        } else {
            tab3Fragment
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        if (position == 0) {
            title = mContext.resources.getString(R.string.str_week)
        } else if (position == 1) {
            title = mContext.resources.getString(R.string.str_month)
        } else if (position == 2) {
            title = mContext.resources.getString(R.string.str_year)
        }
        return title
    }
}
