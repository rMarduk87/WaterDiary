package rpt.tool.waterdiary.utils.view.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import rpt.tool.waterdiary.ui.userinfo.UserInfoEightFragment
import rpt.tool.waterdiary.ui.userinfo.UserInfoFiveFragment
import rpt.tool.waterdiary.ui.userinfo.UserInfoFourFragment
import rpt.tool.waterdiary.ui.userinfo.UserInfoSevenFragment
import rpt.tool.waterdiary.ui.userinfo.UserInfoSixFragment
import rpt.tool.waterdiary.ui.userinfo.UserInfoThreeFragment
import rpt.tool.waterdiary.ui.userinfo.UserInfoTwoFragment

class InitUserInfoPagerAdapter(fm: FragmentManager, context: Context) :
    FragmentStatePagerAdapter(fm) {
    var tab2Fragment: UserInfoTwoFragment = UserInfoTwoFragment()
    var tab3Fragment: UserInfoThreeFragment = UserInfoThreeFragment()
    var tab4Fragment: UserInfoFourFragment = UserInfoFourFragment()
    var tab5Fragment: UserInfoFiveFragment = UserInfoFiveFragment()
    var tab6Fragment: UserInfoSixFragment = UserInfoSixFragment()
    var tab7Fragment: UserInfoSevenFragment = UserInfoSevenFragment()
    var tab8Fragment: UserInfoEightFragment = UserInfoEightFragment()

    var mContext: Context = context

    override fun getItem(position: Int): Fragment {
        return when (position) {
            1 -> {
                tab3Fragment
            }
            2 -> {
                tab7Fragment
            }
            3 -> {
                tab8Fragment
            }
            4 -> {
                tab4Fragment
            }
            5 -> {
                tab6Fragment
            }
            6 -> {
                tab5Fragment
            }
            else -> {
                tab2Fragment
            }
        }
    }

    override fun getCount(): Int {
        return 7
    }
}