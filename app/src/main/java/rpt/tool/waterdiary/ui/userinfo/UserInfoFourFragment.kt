package rpt.tool.waterdiary.ui.userinfo

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import rpt.tool.waterdiary.AppUtils
import rpt.tool.waterdiary.R
import rpt.tool.waterdiary.base.NavBaseFragment
import rpt.tool.waterdiary.databinding.FragmentInitUserInfoFourBinding
import rpt.tool.waterdiary.utils.helpers.HeightWeightHelper.lbToKgConverter
import rpt.tool.waterdiary.utils.helpers.HeightWeightHelper.mlToOzConverter
import rpt.tool.waterdiary.utils.log.e
import rpt.tool.waterdiary.utils.managers.SharedPreferencesManager
import rpt.tool.waterdiary.utils.view.custom.InputFilterWeightRange


class UserInfoFourFragment : NavBaseFragment<FragmentInitUserInfoFourBinding>(FragmentInitUserInfoFourBinding::inflate) {

    var isExecute: Boolean = true
    var isExecuteSeekbar: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        body()
    }

    private fun body() {
        if (SharedPreferencesManager.setManuallyGoal) {
            AppUtils.DAILY_WATER_VALUE = SharedPreferencesManager.setManuallyGoalValue
            SharedPreferencesManager.dailyWater = AppUtils.DAILY_WATER_VALUE

            binding.lblGoal.text = getData("" + AppUtils.DAILY_WATER_VALUE)

            if (SharedPreferencesManager.personWeightUnit) {
                binding.lblUnit.text = "ml"
            } else {
                binding.lblUnit.text = "fl oz"
            }
        } else {
            calculate_goal()
        }

        binding.lblSetGoalManually.setOnClickListener { showSetManuallyGoalDialog() }
    }

    private fun getData(str: String): String {
        return str.replace(",", ".")
    }

    @SuppressLint("SetTextI18n")
    @Deprecated("Deprecated in Java")
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (SharedPreferencesManager.setManuallyGoal) {
                AppUtils.DAILY_WATER_VALUE = SharedPreferencesManager.setManuallyGoalValue
                SharedPreferencesManager.dailyWater = AppUtils.DAILY_WATER_VALUE

                binding.lblGoal.text = getData("" + AppUtils.DAILY_WATER_VALUE)

                if (SharedPreferencesManager.personWeightUnit) {
                    binding.lblUnit.text = "ml"
                } else {
                    binding.lblUnit.text = "fl oz"
                }
            } else {
                calculate_goal()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun calculate_goal() {
        val tmp_weight = "" + SharedPreferencesManager.personWeight

        val isFemale: Boolean = SharedPreferencesManager.userGender
        val isActive: Boolean = SharedPreferencesManager.isActive
        val isPregnant: Boolean = SharedPreferencesManager.isPregnant
        val isBreastfeeding: Boolean = SharedPreferencesManager.isBreastfeeding
        val weatherIdx: Int = SharedPreferencesManager.weatherConditions

        if (!sh!!.check_blank_data(tmp_weight)) {
            var tot_drink = 0.0
            var tmp_kg = 0.0
            tmp_kg = if (SharedPreferencesManager.personWeightUnit) {
                ("" + tmp_weight).toDouble()
            } else {
                lbToKgConverter(tmp_weight.toDouble())
            }

            tot_drink =
                if (isFemale) if (isActive) tmp_kg * AppUtils.ACTIVE_FEMALE_WATER else tmp_kg * AppUtils.FEMALE_WATER
                else if (isActive) tmp_kg * AppUtils.ACTIVE_MALE_WATER else tmp_kg * AppUtils.MALE_WATER

            tot_drink *= when (weatherIdx) {
                1 -> AppUtils.WEATHER_CLOUDY
                2 -> AppUtils.WEATHER_RAINY
                3 -> AppUtils.WEATHER_SNOW
                else -> AppUtils.WEATHER_SUNNY
            }

            if (isPregnant && isFemale) {
                tot_drink += AppUtils.PREGNANT_WATER
            }

            if (isBreastfeeding && isFemale) {
                tot_drink += AppUtils.BREASTFEEDING_WATER
            }

            if (tot_drink < 900) tot_drink = 900.0

            if (tot_drink > 8000) tot_drink = 8000.0

            val tot_drink_fl_oz = mlToOzConverter(tot_drink.toFloat())

            if (SharedPreferencesManager.personWeightUnit) {
                binding.lblUnit.text = "ml"
                AppUtils.DAILY_WATER_VALUE = tot_drink.toFloat()
            } else {
                binding.lblUnit.text = "fl oz"
                AppUtils.DAILY_WATER_VALUE = tot_drink_fl_oz.toFloat()
            }

            AppUtils.DAILY_WATER_VALUE = Math.round(AppUtils.DAILY_WATER_VALUE).toFloat()
            binding.lblGoal.text = getData("" + AppUtils.DAILY_WATER_VALUE)
            SharedPreferencesManager.dailyWater = AppUtils.DAILY_WATER_VALUE
        }
    }

    @SuppressLint("InflateParams")
    private fun showSetManuallyGoalDialog() {
        val dialog = Dialog(act!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.drawable_background_tra)
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)


        val view: View =
            LayoutInflater.from(act).inflate(R.layout.dialog_set_manually_goal, null, false)


        val lbl_goal2: AppCompatEditText = view.findViewById(R.id.lbl_goal)
        val lbl_unit2: AppCompatTextView = view.findViewById(R.id.lbl_unit)
        val btn_cancel = view.findViewById<RelativeLayout>(R.id.btn_cancel)
        val btn_save = view.findViewById<RelativeLayout>(R.id.btn_save)
        val seekbarGoal = view.findViewById<SeekBar>(R.id.seekbarGoal)

        if (SharedPreferencesManager.setManuallyGoal) lbl_goal2.setText(
            getData(
                "" + SharedPreferencesManager.setManuallyGoalValue
            )
        )
        else lbl_goal2.setText(getData("" + SharedPreferencesManager.dailyWater))

        lbl_unit2.text = if (SharedPreferencesManager.personWeightUnit) "ml" else "fl oz"

        if (SharedPreferencesManager.personWeightUnit) {
            seekbarGoal.min = 900
            seekbarGoal.max = 8000
            lbl_goal2.filters = arrayOf(InputFilterWeightRange(0.0, 8000.0), LengthFilter(4))
        } else {
            seekbarGoal.min = 30
            seekbarGoal.max = 270
            lbl_goal2.filters = arrayOf(InputFilterWeightRange(0.0, 270.0), LengthFilter(3))
        }

        val f =
            if (SharedPreferencesManager.setManuallyGoal) SharedPreferencesManager.setManuallyGoalValue
            else SharedPreferencesManager.dailyWater
        seekbarGoal.progress = f.toInt()

        lbl_goal2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                isExecuteSeekbar = false
            }

            override fun afterTextChanged(editable: Editable) {
                try {
                    if (!sh!!.check_blank_data(
                            lbl_goal2.getText().toString().trim()
                        ) && isExecute
                    ) {
                        val data: Int = lbl_goal2.getText().toString().trim().toInt()
                        seekbarGoal.progress = data
                    }
                } catch (e: Exception) {
                    e.message?.let { e(Throwable(e), it) }
                }

                isExecuteSeekbar = true
            }
        })

        seekbarGoal.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBars: SeekBar, progress: Int, fromUser: Boolean) {
                if (isExecuteSeekbar) {

                    lbl_goal2.setText("" + progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                isExecute = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                isExecute = true
            }
        })




        btn_cancel.setOnClickListener { dialog.dismiss() }

        btn_save.setOnClickListener {
            if (SharedPreferencesManager.personWeightUnit && lbl_goal2.getText().toString()
                    .trim().toFloat() >= 900
            ) {
                AppUtils.DAILY_WATER_VALUE = lbl_goal2.getText().toString().trim().toFloat()
                SharedPreferencesManager.dailyWater = AppUtils.DAILY_WATER_VALUE
                binding.lblGoal.text = getData("" + AppUtils.DAILY_WATER_VALUE)
                SharedPreferencesManager.setManuallyGoal = true
                SharedPreferencesManager.setManuallyGoalValue = AppUtils.DAILY_WATER_VALUE
                dialog.dismiss()
            } else {
                if (!SharedPreferencesManager.personWeightUnit && lbl_goal2.getText().toString()
                        .trim().toFloat() >= 30
                ) {
                    AppUtils.DAILY_WATER_VALUE = lbl_goal2.getText().toString().trim().toFloat()
                    SharedPreferencesManager.dailyWater = AppUtils.DAILY_WATER_VALUE
                    binding.lblGoal.text = getData("" + AppUtils.DAILY_WATER_VALUE)
                    SharedPreferencesManager.setManuallyGoal = true
                    SharedPreferencesManager.setManuallyGoalValue = AppUtils.DAILY_WATER_VALUE
                    dialog.dismiss()
                } else {
                    ah!!.customAlert(sh!!.get_string(R.string.str_set_daily_goal_validation))
                }
            }
        }

        dialog.setContentView(view)

        dialog.show()
    }


}