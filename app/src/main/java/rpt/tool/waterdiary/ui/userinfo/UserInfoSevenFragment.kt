package rpt.tool.waterdiary.ui.userinfo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import rpt.tool.waterdiary.R
import rpt.tool.waterdiary.base.NavBaseFragment
import rpt.tool.waterdiary.databinding.FragmentInitUserInfoSevenBinding
import rpt.tool.waterdiary.utils.managers.SharedPreferencesManager


class UserInfoSevenFragment : NavBaseFragment<FragmentInitUserInfoSevenBinding>(FragmentInitUserInfoSevenBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        body()
    }

    private fun body() {
        setActive()
        setBreastfeeding()
        setPregnant()

        binding.activeBlock.setOnClickListener{
            SharedPreferencesManager.isActive = !SharedPreferencesManager.isActive
            setActive()
        }

        binding.pregnantBlock.setOnClickListener {
            if (SharedPreferencesManager.isPregnant) SharedPreferencesManager.isPregnant = false
            else SharedPreferencesManager.isPregnant = true
            setPregnant()
        }

        binding.breastfeedingBlock.setOnClickListener {
            SharedPreferencesManager.isBreastfeeding = !SharedPreferencesManager.isBreastfeeding
            setBreastfeeding()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setActive() {
        SharedPreferencesManager.setManuallyGoal = false

        if (SharedPreferencesManager.isActive) {
            binding.activeBlock.background = mContext!!.resources.getDrawable(R.drawable.rdo_gender_select)
            binding.imgActive.setImageResource(R.drawable.active_selected)
        } else {
            binding.activeBlock.background = mContext!!.resources.getDrawable(R.drawable.rdo_gender_regular)
            binding.imgActive.setImageResource(R.drawable.active)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setPregnant() {
        SharedPreferencesManager.setManuallyGoal = false

        if (SharedPreferencesManager.isPregnant) {
            binding.pregnantBlock.background = mContext!!.resources.getDrawable(R.drawable.rdo_gender_select)
            binding.imgPregnant.setImageResource(R.drawable.pregnant_selected)
        } else {
            binding.pregnantBlock.background = mContext!!.resources.getDrawable(R.drawable.rdo_gender_regular)
            binding.imgPregnant.setImageResource(R.drawable.pregnant)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setBreastfeeding() {
        SharedPreferencesManager.setManuallyGoal = false

        if (SharedPreferencesManager.isBreastfeeding) {
            binding.breastfeedingBlock.background = mContext!!.resources.getDrawable(R.drawable.rdo_gender_select)
            binding.imgBreastfeeding.setImageResource(R.drawable.breastfeeding_selected)
        } else {
            binding.breastfeedingBlock.background = mContext!!.resources.getDrawable(R.drawable.rdo_gender_regular)
            binding.imgBreastfeeding.setImageResource(R.drawable.breastfeeding)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Deprecated("Deprecated in Java")
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {

            if (SharedPreferencesManager.isPregnant) {
                binding.pregnantBlock.background = mContext!!.resources.getDrawable(R.drawable.rdo_gender_select)
                binding.imgPregnant.setImageResource(R.drawable.pregnant_selected)
            } else {
                binding.pregnantBlock.background = mContext!!.resources.getDrawable(R.drawable.rdo_gender_regular)
                binding.imgPregnant.setImageResource(R.drawable.pregnant)
            }

            if (SharedPreferencesManager.isBreastfeeding) {
                binding.breastfeedingBlock.background = mContext!!.resources.getDrawable(R.drawable.rdo_gender_select)
                binding.imgBreastfeeding.setImageResource(R.drawable.breastfeeding_selected)
            } else {
                binding.breastfeedingBlock.background = mContext!!.resources.getDrawable(R.drawable.rdo_gender_regular)
                binding.imgBreastfeeding.setImageResource(R.drawable.breastfeeding)
            }

            if (SharedPreferencesManager.userGender)  // female
            {

                binding.pregnantBlock.isFocusableInTouchMode = true
                binding.pregnantBlock.isClickable = true
                binding.pregnantBlock.isFocusable = true
                binding.pregnantBlock.alpha = 1F

                for (i in 0 until binding.pregnantBlock.childCount) {
                    val child: View = binding.pregnantBlock.getChildAt(i)
                    child.isEnabled = true
                }

                binding.breastfeedingBlock.isFocusableInTouchMode = true
                binding.breastfeedingBlock.isClickable = true
                binding.breastfeedingBlock.isFocusable = true
                binding.breastfeedingBlock.alpha = 1F

                for (i in 0 until binding.breastfeedingBlock.childCount) {
                    val child: View = binding.breastfeedingBlock.getChildAt(i)
                    child.isEnabled = true
                }
            } else {

                binding.pregnantBlock.isFocusableInTouchMode = false
                binding.pregnantBlock.isClickable = false
                binding.pregnantBlock.isFocusable = false
                binding.pregnantBlock.alpha = 0.50f

                for (i in 0 until binding.pregnantBlock.childCount) {
                    val child: View = binding.pregnantBlock.getChildAt(i)
                    child.isEnabled = false
                }

                binding.breastfeedingBlock.isFocusableInTouchMode = false
                binding.breastfeedingBlock.isClickable = false
                binding.breastfeedingBlock.isFocusable = false
                binding.breastfeedingBlock.alpha = 0.50f

                for (i in 0 until binding.breastfeedingBlock.childCount) {
                    val child: View = binding.breastfeedingBlock.getChildAt(i)
                    child.isEnabled = false
                }
            }
        } else {
        }
    }
}