package rpt.tool.waterdiary.ui.userinfo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import rpt.tool.waterdiary.R
import rpt.tool.waterdiary.base.NavBaseFragment
import rpt.tool.waterdiary.databinding.FragmentInitUserInfoEightBinding
import rpt.tool.waterdiary.utils.managers.SharedPreferencesManager


class UserInfoEightFragment : NavBaseFragment<FragmentInitUserInfoEightBinding>(FragmentInitUserInfoEightBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        body()
    }

    private fun body() {
        setWeather(SharedPreferencesManager.weatherConditions)

        binding.sunnyBlock.setOnClickListener{ setWeather(0) }
        binding.cloudyBlock.setOnClickListener{ setWeather(1) }
        binding.rainyBlock.setOnClickListener{ setWeather(2) }
        binding.snowBlock.setOnClickListener{ setWeather(3) }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setWeather(idx: Int) {
        SharedPreferencesManager.setManuallyGoal = false
        SharedPreferencesManager.weatherConditions = idx

        binding.sunnyBlock.background = if (idx == 0)
            mContext!!.resources.getDrawable(R.drawable.rdo_gender_select)
        else
            mContext!!.resources.getDrawable(R.drawable.rdo_gender_regular)
        binding.imgSunny.setImageResource(if (idx == 0) R.drawable.sunny_selected else R.drawable.sunny)

        binding.cloudyBlock.background = if (idx == 1)
            mContext!!.resources.getDrawable(R.drawable.rdo_gender_select)
        else
            mContext!!.resources.getDrawable(R.drawable.rdo_gender_regular)

        binding.imgCloudy.setImageResource(if (idx == 1) R.drawable.cloudy_selected else R.drawable.cloudy)

        binding.rainyBlock.background = if (idx == 2)
            mContext!!.resources.getDrawable(R.drawable.rdo_gender_select)
        else
            mContext!!.resources.getDrawable(R.drawable.rdo_gender_regular)
        binding.imgRainy.setImageResource(if (idx == 2) R.drawable.rainy_selected else R.drawable.rainy)

        binding.snowBlock.background = if (idx == 3)
            mContext!!.resources.getDrawable(R.drawable.rdo_gender_select)
        else
            mContext!!.resources.getDrawable(R.drawable.rdo_gender_regular)
        binding.imgSnow.setImageResource(if (idx == 3) R.drawable.snow_selected else R.drawable.snow)
    }
}