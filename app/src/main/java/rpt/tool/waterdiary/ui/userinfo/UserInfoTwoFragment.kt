package rpt.tool.waterdiary.ui.userinfo

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import rpt.tool.waterdiary.R
import rpt.tool.waterdiary.base.NavBaseFragment
import rpt.tool.waterdiary.databinding.FragmentInitUserInfoTwoBinding
import rpt.tool.waterdiary.utils.managers.SharedPreferencesManager


class UserInfoTwoFragment :
    NavBaseFragment<FragmentInitUserInfoTwoBinding>(FragmentInitUserInfoTwoBinding::inflate) {

    private var isMaleGender: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        body()
    }

    private fun body() {
        binding.maleBlock.setOnClickListener { setGender(true) }
        binding.femaleBlock.setOnClickListener { setGender(false) }
        binding.txtUserName.setText(SharedPreferencesManager.userName)
        setGender(!SharedPreferencesManager.userGender)

        binding.txtUserName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                SharedPreferencesManager.userName = binding.txtUserName.text.toString().trim()
            }
        })
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setGender(isMale: Boolean) {
        SharedPreferencesManager.setManuallyGoal = false

        if (isMale) {
            isMaleGender = true

            SharedPreferencesManager.userGender = false
            SharedPreferencesManager.isPregnant = false
            SharedPreferencesManager.isBreastfeeding = false

            binding.maleBlock.background = mContext!!.resources.getDrawable(R.drawable.rdo_gender_select)
            binding.imgMale.setImageResource(R.drawable.ic_male_selected)
            binding.femaleBlock.background = mContext!!.resources.getDrawable(R.drawable.rdo_gender_regular)
            binding.imgFemale.setImageResource(R.drawable.ic_female_normal)
        } else {
            isMaleGender = false
            SharedPreferencesManager.userGender = true

            binding.maleBlock.background = mContext!!.resources.getDrawable(R.drawable.rdo_gender_regular)
            binding.imgMale.setImageResource(R.drawable.ic_male_normal)
            binding.femaleBlock.background = mContext!!.resources.getDrawable(R.drawable.rdo_gender_select)
            binding.imgFemale.setImageResource(R.drawable.ic_female_selected)
        }
    }

}