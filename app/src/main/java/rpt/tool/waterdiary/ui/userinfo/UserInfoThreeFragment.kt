package rpt.tool.waterdiary.ui.userinfo

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import rpt.tool.waterdiary.AppUtils
import rpt.tool.waterdiary.base.NavBaseFragment
import rpt.tool.waterdiary.databinding.FragmentInitUserInfoThreeBinding
import rpt.tool.waterdiary.utils.helpers.HeightWeightHelper.kgToLbConverter
import rpt.tool.waterdiary.utils.helpers.HeightWeightHelper.lbToKgConverter
import rpt.tool.waterdiary.utils.log.d
import rpt.tool.waterdiary.utils.log.e
import rpt.tool.waterdiary.utils.managers.SharedPreferencesManager
import rpt.tool.waterdiary.utils.view.custom.DigitsInputFilter
import rpt.tool.waterdiary.utils.view.custom.InputFilterRange
import rpt.tool.waterdiary.utils.view.custom.InputFilterWeightRange


class UserInfoThreeFragment :
    NavBaseFragment<FragmentInitUserInfoThreeBinding>(FragmentInitUserInfoThreeBinding::inflate) {

    var isExecute: Boolean = true
    var isExecuteSeekbar: Boolean = true
    var height_feet_elements: MutableList<Double> = ArrayList()
    var isExecuteWeight: Boolean = true
    var isExecuteSeekbarWeight: Boolean = true
    var weight_lb_lst: MutableList<String> = ArrayList()
    var weight_kg_lst: MutableList<String> = ArrayList()
    var height_cm_lst: MutableList<String> = ArrayList()
    var height_feet_lst: MutableList<String> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(SharedPreferencesManager.personWeightUnit) {
            init_WeightLB()
            init_WeightKG()
            binding.pickerLB.visibility = View.GONE
        }
        else {
            init_WeightKG()
            init_WeightLB()
            binding.pickerKG.visibility = View.GONE
        }

        if(SharedPreferencesManager.personHeightUnit) {
            init_HeightFeet()
            init_HeightCM()
            binding.pickerFeet.visibility = View.GONE
        }
        else
        {
            init_HeightCM()
            init_HeightFeet()
            binding.pickerCM.visibility = View.GONE
        }

        body()

        binding.rdoKg.setOnCheckedChangeListener{ compoundButton, b ->
            binding.pickerKG.visibility = if (b) View.VISIBLE else View.GONE
            binding.pickerLB.visibility = if (b) View.GONE else View.VISIBLE
        }

        binding.rdoFeet.setOnCheckedChangeListener{ compoundButton, b ->
            binding.pickerFeet.visibility = if (b) View.VISIBLE else View.GONE
            binding.pickerCM.visibility = if (b) View.GONE else View.VISIBLE
        }
    }

    private fun init_WeightKG() {

        weight_kg_lst.clear()
        var f = 30.0f
        weight_kg_lst.add("" + f)
        for (k in 0..199) {
            f += 0.5.toFloat()
            weight_kg_lst.add("" + f)
        }

        val st = arrayOfNulls<CharSequence>(weight_kg_lst.size)
        for (k in weight_kg_lst.indices) {
            st[k] = "" + weight_kg_lst[k]
        }

        binding.pickerKG.values = st
        binding.pickerKG.sideItems = 1
        binding.pickerKG.setOnItemSelectedListener{ index ->
            binding.txtWeight.setText(st[index])

            d("MYHSCROLL : ", "onItemSelected KG")
        }

    }

    private fun init_WeightLB() {

        weight_lb_lst.clear()
        for (k in 66..287) {
            weight_lb_lst.add("" + k)
        }

        val st = arrayOfNulls<CharSequence>(weight_lb_lst.size)
        for (k in weight_lb_lst.indices) {
            st[k] = "" + weight_lb_lst[k]
        }

        binding.pickerLB.values = st
        binding.pickerLB.sideItems = 1
        binding.pickerLB.setOnItemSelectedListener { index ->
            binding.txtWeight.setText(st[index])

            d("MYHSCROLL : ", "onItemSelected LB")
            //}
        }

    }


    //===============
    private fun init_HeightCM() {

        height_cm_lst.clear()
        for (k in 60..240) {
            height_cm_lst.add("" + k)
        }

        val st = arrayOfNulls<CharSequence>(height_cm_lst.size)
        for (k in height_cm_lst.indices) {
            st[k] = "" + height_cm_lst[k]
        }

        binding.pickerCM.values = st
        binding.pickerCM.sideItems = 1
        binding.pickerCM.setOnItemSelectedListener { index ->
            binding.txtHeight.setText(st[index])

            d("MYHSCROLL : ", "onItemSelected 2")
            //}
        }
    }

    private fun init_HeightFeet() {

        height_feet_lst.clear()
        height_feet_lst.add("2.0")
        height_feet_lst.add("2.1")
        height_feet_lst.add("2.2")
        height_feet_lst.add("2.3")
        height_feet_lst.add("2.4")
        height_feet_lst.add("2.5")
        height_feet_lst.add("2.6")
        height_feet_lst.add("2.7")
        height_feet_lst.add("2.8")
        height_feet_lst.add("2.9")
        height_feet_lst.add("2.10")
        height_feet_lst.add("2.11")
        height_feet_lst.add("3.0")
        height_feet_lst.add("3.1")
        height_feet_lst.add("3.2")
        height_feet_lst.add("3.3")
        height_feet_lst.add("3.4")
        height_feet_lst.add("3.5")
        height_feet_lst.add("3.6")
        height_feet_lst.add("3.7")
        height_feet_lst.add("3.8")
        height_feet_lst.add("3.9")
        height_feet_lst.add("3.10")
        height_feet_lst.add("3.11")
        height_feet_lst.add("4.0")
        height_feet_lst.add("4.1")
        height_feet_lst.add("4.2")
        height_feet_lst.add("4.3")
        height_feet_lst.add("4.4")
        height_feet_lst.add("4.5")
        height_feet_lst.add("4.6")
        height_feet_lst.add("4.7")
        height_feet_lst.add("4.8")
        height_feet_lst.add("4.9")
        height_feet_lst.add("4.10")
        height_feet_lst.add("4.11")
        height_feet_lst.add("5.0")
        height_feet_lst.add("5.1")
        height_feet_lst.add("5.2")
        height_feet_lst.add("5.3")
        height_feet_lst.add("5.4")
        height_feet_lst.add("5.5")
        height_feet_lst.add("5.6")
        height_feet_lst.add("5.7")
        height_feet_lst.add("5.8")
        height_feet_lst.add("5.9")
        height_feet_lst.add("5.10")
        height_feet_lst.add("5.11")
        height_feet_lst.add("6.0")
        height_feet_lst.add("6.1")
        height_feet_lst.add("6.2")
        height_feet_lst.add("6.3")
        height_feet_lst.add("6.4")
        height_feet_lst.add("6.5")
        height_feet_lst.add("6.6")
        height_feet_lst.add("6.7")
        height_feet_lst.add("6.8")
        height_feet_lst.add("6.9")
        height_feet_lst.add("6.10")
        height_feet_lst.add("6.11")
        height_feet_lst.add("7.0")
        height_feet_lst.add("7.1")
        height_feet_lst.add("7.2")
        height_feet_lst.add("7.3")
        height_feet_lst.add("7.4")
        height_feet_lst.add("7.5")
        height_feet_lst.add("7.6")
        height_feet_lst.add("7.7")
        height_feet_lst.add("7.8")
        height_feet_lst.add("7.9")
        height_feet_lst.add("7.10")
        height_feet_lst.add("7.11")
        height_feet_lst.add("8.0")

        val st = arrayOfNulls<CharSequence>(height_feet_lst.size)
        for (k in height_feet_lst.indices) {
            st[k] = "" + height_feet_lst[k]
        }

        binding.pickerFeet.values = st
        binding.pickerFeet.sideItems = 1
        binding.pickerFeet.setOnItemSelectedListener { index ->
            binding.txtHeight.setText(st[index])

            d("MYHSCROLL : ", "onItemSelected")
            //}
        }
    }

    @SuppressLint("SetTextI18n")
    private fun body() {
        height_feet_elements.add(2.0)
        height_feet_elements.add(2.1)
        height_feet_elements.add(2.2)
        height_feet_elements.add(2.3)
        height_feet_elements.add(2.4)
        height_feet_elements.add(2.5)
        height_feet_elements.add(2.6)
        height_feet_elements.add(2.7)
        height_feet_elements.add(2.8)
        height_feet_elements.add(2.9)
        height_feet_elements.add(2.10)
        height_feet_elements.add(2.11)
        height_feet_elements.add(3.0)
        height_feet_elements.add(3.1)
        height_feet_elements.add(3.2)
        height_feet_elements.add(3.3)
        height_feet_elements.add(3.4)
        height_feet_elements.add(3.5)
        height_feet_elements.add(3.6)
        height_feet_elements.add(3.7)
        height_feet_elements.add(3.8)
        height_feet_elements.add(3.9)
        height_feet_elements.add(3.10)
        height_feet_elements.add(3.11)
        height_feet_elements.add(4.0)
        height_feet_elements.add(4.1)
        height_feet_elements.add(4.2)
        height_feet_elements.add(4.3)
        height_feet_elements.add(4.4)
        height_feet_elements.add(4.5)
        height_feet_elements.add(4.6)
        height_feet_elements.add(4.7)
        height_feet_elements.add(4.8)
        height_feet_elements.add(4.9)
        height_feet_elements.add(4.10)
        height_feet_elements.add(4.11)
        height_feet_elements.add(5.0)
        height_feet_elements.add(5.1)
        height_feet_elements.add(5.2)
        height_feet_elements.add(5.3)
        height_feet_elements.add(5.4)
        height_feet_elements.add(5.5)
        height_feet_elements.add(5.6)
        height_feet_elements.add(5.7)
        height_feet_elements.add(5.8)
        height_feet_elements.add(5.9)
        height_feet_elements.add(5.10)
        height_feet_elements.add(5.11)
        height_feet_elements.add(6.0)
        height_feet_elements.add(6.1)
        height_feet_elements.add(6.2)
        height_feet_elements.add(6.3)
        height_feet_elements.add(6.4)
        height_feet_elements.add(6.5)
        height_feet_elements.add(6.6)
        height_feet_elements.add(6.7)
        height_feet_elements.add(6.8)
        height_feet_elements.add(6.9)
        height_feet_elements.add(6.10)
        height_feet_elements.add(6.11)
        height_feet_elements.add(7.0)
        height_feet_elements.add(7.1)
        height_feet_elements.add(7.2)
        height_feet_elements.add(7.3)
        height_feet_elements.add(7.4)
        height_feet_elements.add(7.5)
        height_feet_elements.add(7.6)
        height_feet_elements.add(7.7)
        height_feet_elements.add(7.8)
        height_feet_elements.add(7.9)
        height_feet_elements.add(7.10)
        height_feet_elements.add(7.11)
        height_feet_elements.add(8.0)


        isExecute = false
        isExecuteSeekbar = false
        isExecuteWeight = false
        isExecuteSeekbarWeight = false

        binding.txtHeight.filters = arrayOf<InputFilter>(InputFilterRange(0.00, height_feet_elements))
        binding.txtWeight.filters = arrayOf<InputFilter>(InputFilterWeightRange(0.0, 130.0))


        isExecute = true
        isExecuteSeekbar = true
        isExecuteWeight = true
        isExecuteSeekbarWeight = true
        
        binding.txtHeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                isExecuteSeekbar = false
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {


                var height: String = binding.txtHeight.getText().toString().trim()

                if (height.equals(".", ignoreCase = true)) binding.txtHeight.setText("2.0")

                height = binding.txtHeight.getText().toString().trim()

                if (!sh!!.check_blank_data(height) && isExecute) {
                    d("MYHSCROLL : ", "afterTextChanged")

                    val h = height.toFloat()

                    if (binding.rdoFeet.isChecked) {
                        for (k in height_feet_lst.indices) {
                            d(
                                "height_feet_lst",
                                k.toString() + "  " + h + " " + height_feet_lst[k].toFloat()
                            )

                            if (height.equals(height_feet_lst[k], ignoreCase = true)) {
                                binding.pickerFeet.setSelectedItem(k)
                                break
                            }
                        }
                    } else {
                        for (k in height_cm_lst.indices) {
                            d(
                                "height_cm_lst",
                                k.toString() + "  " + h + " " + height_cm_lst[k].toFloat()
                            )

                            if (h == height_cm_lst[k].toFloat()) {
                                binding.pickerCM.setSelectedItem(k)
                                break
                            }
                        }
                    }

                    saveData()
                }

                isExecuteSeekbar = true
            }
        })
        
        binding.rdoCm.setOnClickListener {
            if (!sh!!.check_blank_data(binding.txtHeight.getText().toString())) {
                var final_height_cm = 61

                try {
                    val tmp_height = getData(binding.txtHeight.getText().toString().trim())

                    val d = (binding.txtHeight.getText().toString().trim().toFloat()) as Int

                    d("after_decimal", "" + tmp_height.indexOf("."))

                    if (tmp_height.indexOf(".") > 0) {
                        val after_decimal = tmp_height.substring(tmp_height.indexOf(".") + 1)

                        if (!sh!!.check_blank_data(after_decimal)) {
                            val after_decimal_int = after_decimal.toInt()

                            val final_height = ((d * 12) + after_decimal_int).toDouble()

                            final_height_cm = Math.round(final_height * 2.54).toInt()
                            
                        } else {
                            final_height_cm = Math.round(d * 12 * 2.54).toInt()
                        }
                    } else {
                        final_height_cm = Math.round(d * 12 * 2.54).toInt()
                    }
                } catch (e: Exception) {
                    e.message?.let { it1 -> e(Throwable(e), it1) }
                }
                
                for (k in height_cm_lst.indices) {
                    if (height_cm_lst[k].toInt() == final_height_cm) {
                        binding.pickerCM.setSelectedItem(k)
                        break
                    }
                }

                binding.rdoFeet.isClickable = true
                binding.rdoCm.isClickable = false
                binding.txtHeight.filters = arrayOf<InputFilter>(DigitsInputFilter(3, 0, 240.0))
                binding.txtHeight.setText(getData("" + final_height_cm))
                binding.txtHeight.setSelection(binding.txtHeight.getText().toString().trim().length)


                saveData()
            } else {
                binding.rdoFeet.isChecked = true
                binding.rdoCm.isChecked = false
            }
        }

        binding.rdoFeet.setOnClickListener(View.OnClickListener {
            if (!sh!!.check_blank_data(binding.txtHeight.getText().toString())) {
                var final_height_feet = "5.0"

                try {
                    val d = (binding.txtHeight.getText().toString().trim().toFloat()) as Int

                    val tmp_height_inch = Math.round(d / 2.54).toInt()

                    val first = tmp_height_inch / 12
                    val second = tmp_height_inch % 12

                    final_height_feet = "$first.$second"
                } catch (e: Exception) {
                    e.message?.let { it1 -> e(Throwable(e), it1) }
                }

                for (k in height_feet_lst.indices) {
                    if (getData(final_height_feet).equals(height_feet_lst[k], ignoreCase = true)) {
                        binding.pickerFeet.setSelectedItem(k)
                        break
                    }
                }

                binding.rdoFeet.isClickable = false
                binding.rdoCm.isClickable = true
                binding.txtHeight.filters = arrayOf<InputFilter>(
                    InputFilterRange(
                        0.00,
                        height_feet_elements
                    )
                )
                binding.txtHeight.setText(getData(final_height_feet))
                binding.txtHeight.setSelection(binding.txtHeight.getText().toString().trim().length)

                saveData()
            } else {
                binding.rdoFeet.isChecked = false
                binding.rdoCm.isChecked = true
            }
        })


        //====================================================================================
        binding.txtWeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                isExecuteSeekbarWeight = false
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(editable: Editable) {
                try {
                    var weight: String = binding.txtWeight.getText().toString().trim()

                    if (weight.equals(".", ignoreCase = true)) binding.txtWeight.setText("30.0")

                    weight = binding.txtWeight.getText().toString().trim()

                    if (!sh!!.check_blank_data(weight) && isExecuteWeight) {
                        d("MYHSCROLL : ", "afterTextChanged W")

                        val h = weight.toFloat()

                        val tot = h.toInt()

                        if (binding.rdoKg.isChecked) {
                            var pos = 0
                            for (k in weight_kg_lst.indices) {
                                val h2 = weight_kg_lst[k].toFloat()
                                if (h == h2) {
                                    pos = k
                                    break
                                }
                            }

                            binding.pickerKG.setSelectedItem(pos)
                        } else {
                            for (k in weight_lb_lst.indices) {
                                if (weight_lb_lst[k].toInt() == tot) binding.pickerLB.setSelectedItem(k)
                            }
                        }
                    }

                    saveWeightData()
                } catch (e: Exception) {
                    e.message?.let { e(Throwable(e), it) }
                }

                isExecuteSeekbarWeight = true
            }
        })

        binding.rdoKg.setOnClickListener {
            if (!sh!!.check_blank_data(binding.txtWeight.getText().toString())) {
                val weight_in_lb: Double = binding.txtWeight.getText().toString().toDouble()

                var weight_in_kg = 0.0

                if (weight_in_lb > 0) weight_in_kg =
                    Math.round(lbToKgConverter(weight_in_lb)).toDouble()

                val tmp = weight_in_kg.toInt()

                var sel_pos = 0
                for (k in weight_kg_lst.indices) {
                    if (weight_kg_lst[k].toFloat() == ("" + tmp).toFloat()) sel_pos = k
                }

                binding.pickerKG.setSelectedItem(sel_pos)

                binding.txtWeight.filters = arrayOf<InputFilter>(InputFilterWeightRange(0.0, 130.0))
                binding.txtWeight.setText(getData("" + AppUtils.decimalFormat2.format(tmp)))
                binding.rdoKg.isClickable = false
                binding.rdoLb.isClickable = true
            }
            saveWeightData()
        }

        binding.rdoLb.setOnClickListener {
            if (!sh!!.check_blank_data(binding.txtWeight.getText().toString())) {
                val weight_in_kg: Double = binding.txtWeight.getText().toString().toDouble()

                var weight_in_lb = 0.0

                if (weight_in_kg > 0) weight_in_lb =
                    Math.round(kgToLbConverter(weight_in_kg)).toDouble()

                val tmp = weight_in_lb.toInt()

                var sel_pos = 0
                for (k in weight_lb_lst.indices) {
                    if (weight_lb_lst[k].toFloat() == ("" + tmp).toFloat()) sel_pos = k
                }

                binding.pickerLB.setSelectedItem(sel_pos)

                binding.txtWeight.filters = arrayOf<InputFilter>(DigitsInputFilter(3, 0, 287.0))
                binding.txtWeight.setText(getData("" + tmp))
                binding.rdoKg.isClickable = true
                binding.rdoLb.isClickable = false
            }
            saveWeightData()
        }

        if (SharedPreferencesManager.personHeightUnit) {
            binding.rdoCm.isChecked = true
            binding.rdoCm.isClickable = false
            binding.rdoFeet.isClickable = true
        } else {
            binding.rdoFeet.isChecked = true
            binding.rdoCm.isClickable = true
            binding.rdoFeet.isClickable = false
        }

        if (SharedPreferencesManager.personWeightUnit) {
            binding.rdoKg.isChecked = true
            binding.rdoKg.isClickable = false
            binding.rdoLb.isClickable = true
        } else {
            binding.rdoLb.isChecked = true
            binding.rdoKg.isClickable = true
            binding.rdoLb.isClickable = false
        }

        if (!sh!!.check_blank_data(SharedPreferencesManager.personHeight.toString())) {
            if (binding.rdoCm.isChecked) {
                binding.txtHeight.filters = arrayOf<InputFilter>(DigitsInputFilter(3, 0, 240.0))
                binding.txtHeight.setText(getData(SharedPreferencesManager.personHeight.toString()))
            } else {
                binding.txtHeight.filters = arrayOf<InputFilter>(
                    InputFilterRange(
                        0.00,
                        height_feet_elements
                    )
                )
                binding.txtHeight.setText(getData(SharedPreferencesManager.personHeight.toString()))
            }
        } else {
            if (binding.rdoCm.isChecked) {
                binding.txtHeight.filters = arrayOf<InputFilter>(DigitsInputFilter(3, 0, 240.0))
                binding.txtHeight.setText("150")
            } else {
                binding.txtHeight.filters = arrayOf<InputFilter>(
                    InputFilterRange(
                        0.00,
                        height_feet_elements
                    )
                )
                binding.txtHeight.setText("5.0")
            }
        }

        if (!sh!!.check_blank_data(SharedPreferencesManager.personWeight)) {
            if (binding.rdoKg.isChecked) {
                binding.txtWeight.filters = arrayOf<InputFilter>(InputFilterWeightRange(0.0, 130.0))
                binding.txtWeight.setText(getData(SharedPreferencesManager.personWeight))
            } else {
                binding.txtWeight.filters = arrayOf<InputFilter>(DigitsInputFilter(3, 0, 287.0))
                binding.txtWeight.setText(getData(SharedPreferencesManager.personWeight))
            }
        } else {
            if (binding.rdoKg.isChecked) {
                binding.txtWeight.filters = arrayOf<InputFilter>(InputFilterWeightRange(0.0, 130.0))
                binding.txtWeight.setText("80.0")
            } else {
                binding.txtWeight.filters = arrayOf<InputFilter>(DigitsInputFilter(3, 0, 287.0))
                binding.txtWeight.setText("176")
            }
        }
    }

    fun saveData() {
        d(
            "saveData",
            ("" + binding.txtHeight.getText().toString()
                .trim()).toString() + " @@@ " + binding.txtWeight.getText().toString().trim()
        )

        SharedPreferencesManager.personHeight = binding.txtHeight.getText().toString().trim()
        SharedPreferencesManager.personHeightUnit = binding.rdoCm.isChecked
        SharedPreferencesManager.setManuallyGoal = false
    }

    fun saveWeightData() {
        d(
            "saveWeightData",
            ("" + binding.rdoKg.isChecked).toString() + " @@@ " + binding.txtWeight.getText().toString().trim()
        )

        SharedPreferencesManager.personWeight = binding.txtWeight.getText().toString().trim()
        SharedPreferencesManager.personWeightUnit = binding.rdoKg.isChecked
        SharedPreferencesManager.setManuallyGoal = false
        SharedPreferencesManager.waterUnit = if (binding.rdoKg.isChecked) "ml" else "fl oz"
    }

    fun getData(str: String): String {
        return str.replace(",", ".")
    }
}