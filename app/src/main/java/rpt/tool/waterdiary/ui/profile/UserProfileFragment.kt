package rpt.tool.waterdiary.ui.profile

import android.app.Dialog
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.theartofdev.edmodo.cropper.CropImage
import rpt.tool.waterdiary.AppUtils
import rpt.tool.waterdiary.DashboardActivity
import rpt.tool.waterdiary.R
import rpt.tool.waterdiary.base.NavBaseFragment
import rpt.tool.waterdiary.databinding.FragmentProfileBinding
import rpt.tool.waterdiary.utils.helpers.HeightWeightHelper.kgToLbConverter
import rpt.tool.waterdiary.utils.helpers.HeightWeightHelper.lbToKgConverter
import rpt.tool.waterdiary.utils.helpers.HeightWeightHelper.mlToOzConverter
import rpt.tool.waterdiary.utils.helpers.HeightWeightHelper.ozToMlConverter
import rpt.tool.waterdiary.utils.d
import rpt.tool.waterdiary.utils.log.d
import rpt.tool.waterdiary.utils.log.e
import rpt.tool.waterdiary.utils.managers.SharedPreferencesManager
import rpt.tool.waterdiary.utils.view.custom.DigitsInputFilter
import rpt.tool.waterdiary.utils.view.custom.InputFilterRange
import rpt.tool.waterdiary.utils.view.custom.InputFilterWeightRange
import rpt.tool.waterdiary.utils.view.widget.NewAppWidget
import java.io.File
import java.util.Locale


class UserProfileFragment : NavBaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    var isExecute: Boolean = true
    var isExecuteSeekbar: Boolean = true
    var weight_kg_lst: MutableList<String> = ArrayList()
    var weight_lb_lst: MutableList<String> = ArrayList()
    var height_cm_lst: MutableList<String> = ArrayList()
    var height_feet_lst: MutableList<String> = ArrayList()
    var height_feet_elements: MutableList<Double> = ArrayList()
    val STORAGE_PERMISSION: Int = 3
    val PICK_Camera_IMAGE: Int = 2
    val SELECT_FILE1: Int = 1
    var imageUri: Uri? = null
    var selectedImage: Uri? = null
    private var selectedImagePath: String? = null
    var bottomSheetDialog: BottomSheetDialog? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.navigationBarColor = requireContext().resources.getColor(
            R.color.water_color)

        AppUtils.DAILY_WATER_VALUE = SharedPreferencesManager.dailyWater

        header()
        body()

        init_WeightKG()
        init_WeightLB()
        init_HeightCM()
        init_HeightFeet()

        loadHeightData()
        conversion()
    }

    private fun loadHeightData() {
        height_feet_elements.clear()

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
    }

    private fun conversion() {

        convertUpperCase(binding.lblGender)
        convertUpperCase(binding.lblWeight)
        convertUpperCase(binding.lblHeight)
        convertUpperCase(binding.lblGoal)
        convertUpperCase(binding.lblActive)
        convertUpperCase(binding.lblPregnant)
        convertUpperCase(binding.lblBreastfeeding)
        convertUpperCase(binding.lblWeather)
        convertUpperCase(binding.lblOtherFactor)

        binding.txtUserName.text = SharedPreferencesManager.userName
        binding.txtGender.text = if (SharedPreferencesManager.userGender)
            sh!!.get_string(R.string.str_female) else sh!!.get_string(
            R.string.str_male
        )
        loadPhoto()
        val str: String = SharedPreferencesManager.personHeight + " " +
                (if (SharedPreferencesManager.personHeightUnit) "cm" else "feet")
        binding.txtHeight.text = str

        val str2: String =
            if (SharedPreferencesManager.personWeightUnit) AppUtils.decimalFormat2.format(
                SharedPreferencesManager.personWeight).toDouble()
            .toString() + " kg" else SharedPreferencesManager.personWeight + " lb"
        binding.txtWeight.text = str2

        val str3 = (getData("" + AppUtils.DAILY_WATER_VALUE) + " "
                + (if (SharedPreferencesManager.personWeightUnit) "ml" else "fl oz"))
        binding.txtGoal.text = str3
    }

    fun loadPhoto() {
        if (SharedPreferencesManager.userPhoto?.let { sh!!.check_blank_data(it) } == true) {
            Glide.with(act!!).load(
                if (SharedPreferencesManager.userGender)
                    R.drawable.female_white
                else
                    R.drawable.male_white
            ).apply(RequestOptions.circleCropTransform()).into(binding.imgUser)
        } else {
            var ex = false

            try {
                val f: File? = SharedPreferencesManager.userPhoto?.let { File(it) }
                if (f!!.exists()) ex = true
            } catch (e: Exception) {
                e.message?.let { e(Throwable(e), it) }
            }

            if (ex) {
                Glide.with(act!!).load(SharedPreferencesManager.userPhoto)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.imgUser)
            } else {
                Glide.with(act!!).load(
                    if (SharedPreferencesManager.userGender)
                        R.drawable.female_white
                    else
                        R.drawable.male_white
                ).apply(RequestOptions.circleCropTransform())
                    .into(binding.imgUser)
            }
        }
    }

    private fun getData(str: String): String {
        return str.replace(",", ".")
    }

    private fun convertUpperCase(appCompatTextView: AppCompatTextView) {
        appCompatTextView.text = appCompatTextView.text.toString().uppercase(Locale.getDefault())
    }

    private fun header() {
        binding.headerBlock.lblToolbarTitle.text = sh!!.get_string(R.string.str_my_profile)
        binding.headerBlock.leftIconBlock.setOnClickListener { finish() }
        binding.headerBlock.rightIconBlock.visibility = View.GONE
    }

    private fun finish() {
        startActivity(Intent(requireActivity(), DashboardActivity::class.java))
    }

    fun body() {
        binding.changeProfile.setOnClickListener {
            checkStoragePermissions()
        }
        
        binding.genderBlock.setOnClickListener { v ->
            initiatePopupWindow(v)
        }
        
        binding.editUserNameBlock.setOnClickListener { openNameDialog() }
        
        binding.goalBlock.setOnClickListener { showSetManuallyGoalDialog() }
        
        binding.heightBlock.setOnClickListener { openHeightDialog() }
        
        binding.weightBlock.setOnClickListener { openWeightDialog() }
        
        binding.switchActive.setChecked(SharedPreferencesManager.isActive)
        
        binding.switchActive.setOnCheckedChangeListener { buttonView, isChecked ->
            SharedPreferencesManager.isActive = isChecked
            val tmp_weight = "" + SharedPreferencesManager.personWeight
            val isFemale: Boolean = SharedPreferencesManager.userGender
            val min = (if (SharedPreferencesManager.personWeightUnit) 900f else 30f).toFloat()
            val max = (if (SharedPreferencesManager.personWeightUnit) 8000 else 270).toFloat()
            val weatherIdx: Int = SharedPreferencesManager.weatherConditions

            d("maxmaxmaxmax : ", "$max @@@ $min  @@@  $tmp_weight")

            var tmp_kg = 0.0
            tmp_kg = if (SharedPreferencesManager.personWeightUnit) {
                ("" + tmp_weight).toDouble()
            } else {
                lbToKgConverter(tmp_weight.toDouble())
            }

            d("maxmaxmaxmax : ", "" + tmp_kg)

            var diff = 0.0

            if (isFemale) diff = tmp_kg * AppUtils.DEACTIVE_FEMALE_WATER
            else diff = tmp_kg * AppUtils.DEACTIVE_MALE_WATER

            d("maxmaxmaxmax DIFF : ", "" + diff)

            diff *= if (weatherIdx == 1) AppUtils.WEATHER_CLOUDY
            else if (weatherIdx == 2) AppUtils.WEATHER_RAINY
            else if (weatherIdx == 3) AppUtils.WEATHER_SNOW
            else AppUtils.WEATHER_SUNNY


            d("maxmaxmaxmax : ", "" + diff + " @@@ " + AppUtils.DAILY_WATER_VALUE)

            if (isChecked) {
                if (SharedPreferencesManager.personWeightUnit) {
                    AppUtils.DAILY_WATER_VALUE += diff.toFloat()
                } else {
                    AppUtils.DAILY_WATER_VALUE += mlToOzConverter(diff.toFloat()).toFloat()
                }

                if (AppUtils.DAILY_WATER_VALUE > max) AppUtils.DAILY_WATER_VALUE = max
            } else {
                if (SharedPreferencesManager.personWeightUnit) {
                    AppUtils.DAILY_WATER_VALUE -= diff.toFloat()
                } else {
                    AppUtils.DAILY_WATER_VALUE -= mlToOzConverter(diff.toFloat()).toFloat()
                }

                if (AppUtils.DAILY_WATER_VALUE > max) AppUtils.DAILY_WATER_VALUE = max
            }

            AppUtils.DAILY_WATER_VALUE = Math.round(AppUtils.DAILY_WATER_VALUE).toFloat()

            val str = getData("" + AppUtils.DAILY_WATER_VALUE) + " " +
                    (if (SharedPreferencesManager.personWeightUnit) "ml" else "fl oz")

            binding.txtGoal.text = str
            SharedPreferencesManager.dailyWater = AppUtils.DAILY_WATER_VALUE
        }

        binding.switchBreastfeeding.setChecked(SharedPreferencesManager.isBreastfeeding)

        binding.switchBreastfeeding.setOnCheckedChangeListener { buttonView, isChecked ->
            SharedPreferencesManager.isBreastfeeding = isChecked
        }

        binding.switchPregnant.setChecked(SharedPreferencesManager.isPregnant)

        binding.switchPregnant.setOnCheckedChangeListener { buttonView, isChecked ->
            SharedPreferencesManager.isPregnant = isChecked
        }


        other_factors.setVisibility(if (ph.getBoolean(AppUtils.USER_GENDER)) View.VISIBLE else View.GONE)

        var str = ""
        str = if (ph.getInt(AppUtils.WEATHER_CONSITIONS) === 1) sh!!.get_string(R.string.cloudy)
        else if (ph.getInt(AppUtils.WEATHER_CONSITIONS) === 2) sh!!.get_string(R.string.rainy)
        else if (ph.getInt(AppUtils.WEATHER_CONSITIONS) === 3) sh!!.get_string(R.string.snow)
        else sh!!.get_string(R.string.sunny)
        txt_weather.setText(str)

        weather_block.setOnClickListener(View.OnClickListener {
            initiateWeatherPopupWindow(
                switch_active
            )
        })



        calculateActiveValue()
    }

    fun openPicker() {
        bottomSheetDialog = BottomSheetDialog(act!!)

        val layoutInflater = LayoutInflater.from(act)
        val view: View = layoutInflater.inflate(R.layout.bottom_sheet_pick_image, null, false)

        val btnGallery = view.findViewById<AppCompatTextView>(R.id.btnGallery)
        val btnCamera = view.findViewById<AppCompatTextView>(R.id.btnCamera)
        val btnCancel = view.findViewById<AppCompatTextView>(R.id.btnCancel)
        val btnRemove = view.findViewById<AppCompatTextView>(R.id.btnRemove)

        val btnRemoveLine = view.findViewById<View>(R.id.btnRemoveLine)

        if (sh!!.check_blank_data(ph.getString(AppUtils.USER_PHOTO))) {
            btnRemove.visibility = View.GONE
            btnRemoveLine.visibility = View.GONE
        } else {
            btnRemove.visibility = View.VISIBLE
            btnRemoveLine.visibility = View.VISIBLE
        }

        btnGallery.setOnClickListener {
            //if(isclicked) {
            selectImage()
            //isclicked=false;
            //}
        }

        btnCamera.setOnClickListener {
            //if(isclicked) {
            captureImage()
            //isclicked=false;
            //}
        }

        btnRemove.setOnClickListener {
            bottomSheetDialog!!.dismiss()
            val dialog: AlertDialog.Builder = Builder(act)
                .setMessage(sh!!.get_string(R.string.str_remove_photo_confirmation_message))
                .setPositiveButton(
                    sh!!.get_string(R.string.str_yes),
                    DialogInterface.OnClickListener { dialog, whichButton ->
                        dialog.dismiss()
                        ph.savePreferences(AppUtils.USER_PHOTO, "")
                        loadPhoto()
                    }
                )
                .setNegativeButton(
                    sh!!.get_string(R.string.str_no),
                    DialogInterface.OnClickListener { dialog, whichButton -> dialog.dismiss() }
                )
            dialog.show()
        }

        btnCancel.setOnClickListener { bottomSheetDialog!!.dismiss() }




        bottomSheetDialog!!.setContentView(view)

        bottomSheetDialog!!.show()
    }

    fun setSwitchData(isChecked: Boolean, water: Double) {
        var diff = 0.0
        val min = (if (ph.getBoolean(AppUtils.PERSON_WEIGHT_UNIT)) 900 else 30).toFloat()
        val max = (if (ph.getBoolean(AppUtils.PERSON_WEIGHT_UNIT)) 8000 else 270).toFloat()

        diff = if (ph.getBoolean(AppUtils.PERSON_WEIGHT_UNIT)) water
        else mlToOzConverter(water.toFloat())


        if (isChecked) {
            AppUtils.DAILY_WATER_VALUE += diff

            if (AppUtils.DAILY_WATER_VALUE > max) AppUtils.DAILY_WATER_VALUE = max
        } else {
            AppUtils.DAILY_WATER_VALUE -= diff

            if (AppUtils.DAILY_WATER_VALUE < min) AppUtils.DAILY_WATER_VALUE = min
        }


        AppUtils.DAILY_WATER_VALUE = Math.round(AppUtils.DAILY_WATER_VALUE)

        val str = getData("" + AppUtils.DAILY_WATER_VALUE as Int) + " " +
                (if (ph.getBoolean(AppUtils.PERSON_WEIGHT_UNIT)) "ml" else "fl oz")

        txt_goal.setText(str)

        ph.savePreferences(AppUtils.DAILY_WATER, AppUtils.DAILY_WATER_VALUE)

        calculateActiveValue()
    }


    fun showGenderMenu(v: View?) {
        val popup: PopupMenu = PopupMenu(this, v)
        popup.setOnMenuItemClickListener(object : OnMenuItemClickListener() {
            override fun onMenuItemClick(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.male_item -> {
                        // do your code
                        ph.savePreferences(AppUtils.USER_GENDER, false)

                        //img_user.setImageResource(R.drawable.ic_male_normal);
                        loadPhoto()


                        return true
                    }

                    R.id.female_item -> {
                        // do your code
                        ph.savePreferences(AppUtils.USER_GENDER, true)

                        //img_user.setImageResource(R.drawable.ic_female_normal);
                        loadPhoto()

                        return true
                    }

                    else -> return false
                }
            }
        })
        popup.inflate(R.menu.gender_menu)
        popup.show()
    }

    private fun initiatePopupWindow(v: View): PopupWindow {
        try {
            val mInflater = getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layout: View = mInflater.inflate(R.layout.row_item_gender, null)

            //If you want to add any listeners to your textviews, these are two //textviews.
            val lbl_male = layout.findViewById<TextView>(R.id.lbl_male)

            lbl_male.text = sh!!.get_string(R.string.str_male)


            val lbl_female = layout.findViewById<TextView>(R.id.lbl_female)

            lbl_female.text = sh!!.get_string(R.string.str_female)

            lbl_male.setOnClickListener {
                ph.savePreferences(AppUtils.USER_GENDER, false)
                //img_user.setImageResource(R.drawable.ic_male_normal);
                loadPhoto()
                mDropdown.dismiss()
                txt_gender.setText(sh!!.get_string(R.string.str_male))
                other_factors.setVisibility(View.GONE)
                switch_breastfeeding.setChecked(false)
                switch_pregnant.setChecked(false)
                calculate_goal()
            }

            lbl_female.setOnClickListener {
                ph.savePreferences(AppUtils.USER_GENDER, true)
                //img_user.setImageResource(R.drawable.ic_female_normal);
                loadPhoto()
                mDropdown.dismiss()
                txt_gender.setText(sh!!.get_string(R.string.str_female))
                other_factors.setVisibility(View.VISIBLE)
                calculate_goal()
            }


            layout.measure(
                View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED
            )
            mDropdown = PopupWindow(
                layout, FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT, true
            )
            //Drawable background = getResources().getDrawable(android.R.drawable.editbox_dropdown_dark_frame);
            //mDropdown.setBackgroundDrawable(background);
            mDropdown.showAsDropDown(v, 5, 5)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mDropdown
    }

    private fun initiateWeatherPopupWindow(v: View): PopupWindow {
        try {
            val mInflater = getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layout: View = mInflater.inflate(R.layout.row_item_weather, null)

            //If you want to add any listeners to your textviews, these are two //textviews.
            val lbl_sunny = layout.findViewById<TextView>(R.id.lbl_sunny)
            lbl_sunny.text = sh!!.get_string(R.string.sunny)

            val lbl_cloudy = layout.findViewById<TextView>(R.id.lbl_cloudy)
            lbl_cloudy.text = sh!!.get_string(R.string.cloudy)

            val lbl_rainy = layout.findViewById<TextView>(R.id.lbl_rainy)
            lbl_rainy.text = sh!!.get_string(R.string.rainy)

            val lbl_snow = layout.findViewById<TextView>(R.id.lbl_snow)
            lbl_snow.text = sh!!.get_string(R.string.snow)

            lbl_sunny.setOnClickListener {
                ph.savePreferences(AppUtils.WEATHER_CONSITIONS, 0)
                mDropdownWeather.dismiss()
                txt_weather.setText(sh!!.get_string(R.string.sunny))
                calculate_goal()
            }

            lbl_cloudy.setOnClickListener {
                ph.savePreferences(AppUtils.WEATHER_CONSITIONS, 1)
                mDropdownWeather.dismiss()
                txt_weather.setText(sh!!.get_string(R.string.cloudy))
                calculate_goal()
            }

            lbl_rainy.setOnClickListener {
                ph.savePreferences(AppUtils.WEATHER_CONSITIONS, 2)
                mDropdownWeather.dismiss()
                txt_weather.setText(sh!!.get_string(R.string.rainy))
                calculate_goal()
            }

            lbl_snow.setOnClickListener {
                ph.savePreferences(AppUtils.WEATHER_CONSITIONS, 3)
                mDropdownWeather.dismiss()
                txt_weather.setText(sh!!.get_string(R.string.snow))
                calculate_goal()
            }


            layout.measure(
                View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED
            )
            mDropdownWeather = PopupWindow(
                layout, FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT, true
            )

            //Drawable background = getResources().getDrawable(android.R.drawable.editbox_dropdown_dark_frame);
            //mDropdown.setBackgroundDrawable(background);
            mDropdownWeather.showAsDropDown(v, 5, 5)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mDropdownWeather
    }

    fun openNameDialog() {
        val dialog = Dialog(act!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.drawable_background_tra)
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)


        val view: View = LayoutInflater.from(act).inflate(R.layout.dialog_user_name, null, false)

        val btn_cancel = view.findViewById<RelativeLayout>(R.id.btn_cancel)
        val btn_add = view.findViewById<RelativeLayout>(R.id.btn_add)
        val img_cancel = view.findViewById<ImageView>(R.id.img_cancel)

        val txt_name = view.findViewById<AppCompatEditText>(R.id.txt_name)

        txt_name.requestFocus()

        btn_cancel.setOnClickListener { dialog.cancel() }

        img_cancel.setOnClickListener { dialog.cancel() }

        txt_name.setText(ph.getString(AppUtils.USER_NAME))
        txt_name.setSelection(txt_name.text.toString().trim { it <= ' ' }.length)

        btn_add.setOnClickListener {
            if (sh!!.check_blank_data(txt_name.text.toString().trim { it <= ' ' })) {
                ah!!.customAlert(sh!!.get_string(R.string.str_your_name_validation))
            } else if (txt_name.text.toString().trim { it <= ' ' }.length < 3) {
                ah!!.customAlert(sh!!.get_string(R.string.str_valid_name_validation))
            } else {
                ph.savePreferences(
                    AppUtils.USER_NAME,
                    txt_name.text.toString().trim { it <= ' ' })

                txt_user_name.setText(ph.getString(AppUtils.USER_NAME))

                dialog.dismiss()
            }
        }

        dialog.setContentView(view)

        dialog.show()
    }


    fun showSetManuallyGoalDialog() {
        val dialog = Dialog(act!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.drawable_background_tra)
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)


        val view: View =
            LayoutInflater.from(act).inflate(R.layout.dialog_set_manually_goal, null, false)


        val lbl_goal2 = view.findViewById<AppCompatEditText>(R.id.lbl_goal)
        val lbl_unit2 = view.findViewById<AppCompatTextView>(R.id.lbl_unit)
        val btn_cancel = view.findViewById<RelativeLayout>(R.id.btn_cancel)
        val btn_save = view.findViewById<RelativeLayout>(R.id.btn_save)
        val seekbarGoal = view.findViewById<SeekBar>(R.id.seekbarGoal)


        /*if(ph.getBoolean(AppUtils.SET_MANUALLY_GOAL))
			lbl_goal2.setText( getData(AppUtils.decimalFormat.format(ph.getFloat(AppUtils.SET_MANUALLY_GOAL_VALUE))));
		else
			lbl_goal2.setText( getData(AppUtils.decimalFormat.format(ph.getFloat(AppUtils.DAILY_WATER))));*/
        if (ph.getBoolean(AppUtils.SET_MANUALLY_GOAL)) lbl_goal2.setText(
            getData(
                "" + (ph.getFloat(
                    AppUtils.SET_MANUALLY_GOAL_VALUE
                )) as Int
            )
        )
        else lbl_goal2.setText(getData("" + (ph.getFloat(AppUtils.DAILY_WATER)) as Int))

        lbl_unit2.text = if (ph.getBoolean(AppUtils.PERSON_WEIGHT_UNIT)) "ml" else "fl oz"



        if (ph.getBoolean(AppUtils.PERSON_WEIGHT_UNIT)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                seekbarGoal.min = 900
            }
            seekbarGoal.max = 8000
            lbl_goal2.filters =
                arrayOf(InputFilterWeightRange(0.0, 8000.0), LengthFilter(4))
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                seekbarGoal.min = 30
            }
            seekbarGoal.max = 270
            lbl_goal2.filters =
                arrayOf(InputFilterWeightRange(0.0, 270.0), LengthFilter(3))
        }

        val f =
            if (ph.getBoolean(AppUtils.SET_MANUALLY_GOAL)) ph.getFloat(AppUtils.SET_MANUALLY_GOAL_VALUE) else ph.getFloat(
                AppUtils.DAILY_WATER
            )
        seekbarGoal.progress = f

        lbl_goal2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                isExecuteSeekbar = false
            }

            override fun afterTextChanged(editable: Editable) {
                try {
                    if (!sh!!.check_blank_data(
                            lbl_goal2.text.toString().trim { it <= ' ' }) && isExecute
                    ) {
                        val data = lbl_goal2.text.toString().trim { it <= ' ' }.toInt()
                        seekbarGoal.progress = data
                    }
                } catch (e: Exception) {
                }

                isExecuteSeekbar = true
            }
        })

        seekbarGoal.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBars: SeekBar, progress: Int, fromUser: Boolean) {
                var progress = progress
                if (isExecuteSeekbar) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                        progress = if (ph.getBoolean(AppUtils.PERSON_WEIGHT_UNIT)) {
                            if (progress < 900) 900 else progress
                        } else {
                            if (progress < 30) 30 else progress
                        }
                        seekbarGoal.progress = progress
                    }

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
            val unit = if (ph.getBoolean(AppUtils.PERSON_WEIGHT_UNIT)) "ml" else "fl oz"
            if (ph.getBoolean(AppUtils.PERSON_WEIGHT_UNIT) && lbl_goal2.text.toString()
                    .trim { it <= ' ' }.toFloat() >= 900
            ) {
                AppUtils.DAILY_WATER_VALUE =
                    lbl_goal2.text.toString().trim { it <= ' ' }.toFloat()
                ph.savePreferences(AppUtils.DAILY_WATER, AppUtils.DAILY_WATER_VALUE)
                txt_goal.setText(getData("" + AppUtils.DAILY_WATER_VALUE as Int) + " " + unit)
                ph.savePreferences(AppUtils.SET_MANUALLY_GOAL, true)
                ph.savePreferences(
                    AppUtils.SET_MANUALLY_GOAL_VALUE,
                    AppUtils.DAILY_WATER_VALUE
                )
                dialog.dismiss()

                refreshWidget()
            } else {
                if (!ph.getBoolean(AppUtils.PERSON_WEIGHT_UNIT) && lbl_goal2.text.toString()
                        .trim { it <= ' ' }.toFloat() >= 30
                ) {
                    AppUtils.DAILY_WATER_VALUE =
                        lbl_goal2.text.toString().trim { it <= ' ' }.toFloat()
                    ph.savePreferences(AppUtils.DAILY_WATER, AppUtils.DAILY_WATER_VALUE)
                    txt_goal.setText(getData("" + AppUtils.DAILY_WATER_VALUE as Int) + " " + unit)
                    ph.savePreferences(AppUtils.SET_MANUALLY_GOAL, true)
                    ph.savePreferences(
                        AppUtils.SET_MANUALLY_GOAL_VALUE,
                        AppUtils.DAILY_WATER_VALUE
                    )
                    dialog.dismiss()

                    refreshWidget()
                } else {
                    ah!!.customAlert(sh!!.get_string(R.string.str_set_daily_goal_validation))
                }
            }
        }

        dialog.setContentView(view)

        dialog.show()
    }


    fun openHeightDialog() {
        val dialog = Dialog(act!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.drawable_background_tra)
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)


        val view: View = LayoutInflater.from(act).inflate(R.layout.dialog_height, null, false)

        val btn_cancel = view.findViewById<RelativeLayout>(R.id.btn_cancel)
        val btn_add = view.findViewById<RelativeLayout>(R.id.btn_add)
        val img_cancel = view.findViewById<ImageView>(R.id.img_cancel)
        val txt_name = view.findViewById<AppCompatEditText>(R.id.txt_name)


        rdo_cm = view.findViewById<View>(R.id.rdo_cm)
        rdo_feet = view.findViewById<View>(R.id.rdo_feet)

        txt_name.requestFocus()




        rdo_cm.setOnClickListener(View.OnClickListener {
            if (!sh!!.check_blank_data(txt_name.text.toString())) {
                var final_height_cm = 61

                try {
                    val tmp_height = getData(txt_name.text.toString().trim { it <= ' ' })

                    val d = (txt_name.text.toString().trim { it <= ' ' }.toFloat()) as Int

                    d("after_decimal", "" + tmp_height.indexOf("."))

                    if (tmp_height.indexOf(".") > 0) {
                        val after_decimal = tmp_height.substring(tmp_height.indexOf(".") + 1)

                        if (!sh!!.check_blank_data(after_decimal)) {
                            val after_decimal_int = after_decimal.toInt()

                            val final_height = ((d * 12) + after_decimal_int).toDouble()

                            final_height_cm = Math.round(final_height * 2.54).toInt()

                            /*ah.Show_Alert_Dialog(d + " @@@  " + tmp_height + " @@@  "
										+ after_decimal + " @@@  " + final_height + "  @@@@ "
										+ final_height_cm);*/
                        } else {
                            final_height_cm = Math.round(d * 12 * 2.54).toInt()

                            //ah.Show_Alert_Dialog(""+final_height_cm);
                        }
                    } else {
                        final_height_cm = Math.round(d * 12 * 2.54).toInt()

                        //ah.Show_Alert_Dialog(""+final_height_cm);
                    }
                } catch (e: Exception) {
                }






                rdo_feet.setClickable(true)
                rdo_cm.setClickable(false)
                txt_name.filters = arrayOf<InputFilter>(DigitsInputFilter(3, 0, 240.0))
                txt_name.setText(getData("" + final_height_cm))
                txt_name.setSelection(txt_name.length())


                //saveData();
            } else {
                rdo_feet.setChecked(true)
                rdo_cm.setChecked(false)
            }
        })

        rdo_feet.setOnClickListener(View.OnClickListener {
            if (!sh!!.check_blank_data(txt_name.text.toString())) {
                var final_height_feet = "5.0"

                try {
                    val d = (txt_name.text.toString().trim { it <= ' ' }.toFloat()) as Int

                    val tmp_height_inch = Math.round(d / 2.54).toInt()

                    val first = tmp_height_inch / 12
                    val second = tmp_height_inch % 12

                    //ah.Show_Alert_Dialog(""+final_height_feet+" @@@  "+first+" @@@ "+second);
                    final_height_feet = "$first.$second"
                } catch (e: Exception) {
                }

                rdo_feet.setClickable(false)
                rdo_cm.setClickable(true)
                txt_name.filters =
                    arrayOf<InputFilter>(InputFilterRange(0.00, height_feet_elements))
                txt_name.setText(getData(final_height_feet))
                txt_name.setSelection(txt_name.length())

                //saveData();
            } else {
                rdo_feet.setChecked(false)
                rdo_cm.setChecked(true)
            }
        })

        if (ph.getBoolean(AppUtils.PERSON_HEIGHT_UNIT)) {
            rdo_cm.setChecked(true)
            rdo_cm.setClickable(false)
            rdo_feet.setClickable(true)
        } else {
            rdo_feet.setChecked(true)
            rdo_cm.setClickable(true)
            rdo_feet.setClickable(false)
        }

        if (!sh!!.check_blank_data(ph.getString(AppUtils.PERSON_HEIGHT))) {
            if (rdo_cm.isChecked()) {
                txt_name.filters = arrayOf<InputFilter>(DigitsInputFilter(3, 0, 240.0))
                txt_name.setText(getData(ph.getString(AppUtils.PERSON_HEIGHT)))
            } else {
                txt_name.filters =
                    arrayOf<InputFilter>(InputFilterRange(0.00, height_feet_elements))
                txt_name.setText(getData(ph.getString(AppUtils.PERSON_HEIGHT)))
            }
        } else {
            if (rdo_cm.isChecked()) {
                txt_name.filters = arrayOf<InputFilter>(DigitsInputFilter(3, 0, 240.0))
                txt_name.setText("150")
            } else {
                txt_name.filters =
                    arrayOf<InputFilter>(InputFilterRange(0.00, height_feet_elements))
                txt_name.setText("5.0")
            }
        }



        btn_cancel.setOnClickListener { dialog.cancel() }

        img_cancel.setOnClickListener { dialog.cancel() }

        txt_name.setSelection(txt_name.text.toString().length)

        btn_add.setOnClickListener {
            if (sh!!.check_blank_data(txt_name.text.toString().trim { it <= ' ' })) {
                ah!!.customAlert(sh!!.get_string(R.string.str_height_validation))
            } else {
                var str = txt_name.text.toString().trim { it <= ' ' }

                if (rdo_feet.isChecked()) {
                    if (!str.contains(".11") && !str.contains(".10")) str =
                        AppUtils.decimalFormat2.format(str.toDouble())
                }

                str += " " + (if (rdo_feet.isChecked()) "feet" else "cm")

                txt_height.setText(str)

                saveData(txt_name)

                dialog.dismiss()
            }
        }

        dialog.setContentView(view)

        dialog.show()
    }


    fun openWeightDialog() {
        val dialog = Dialog(act!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.drawable_background_tra)
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)


        val view: View = LayoutInflater.from(act).inflate(R.layout.dialog_weight, null, false)

        val btn_cancel = view.findViewById<RelativeLayout>(R.id.btn_cancel)
        val btn_add = view.findViewById<RelativeLayout>(R.id.btn_add)
        val img_cancel = view.findViewById<ImageView>(R.id.img_cancel)
        val txt_name = view.findViewById<AppCompatEditText>(R.id.txt_name)


        rdo_kg = view.findViewById<View>(R.id.rdo_kg)
        rdo_lb = view.findViewById<View>(R.id.rdo_lb)

        txt_name.requestFocus()



        rdo_kg.setOnClickListener(View.OnClickListener {
            if (!sh!!.check_blank_data(txt_name.text.toString())) {
                val weight_in_lb = txt_name.text.toString().toDouble()

                var weight_in_kg = 0.0

                if (weight_in_lb > 0) weight_in_kg =
                    Math.round(lbToKgConverter(weight_in_lb)).toDouble()

                val tmp = weight_in_kg.toInt()

                txt_name.filters = arrayOf<InputFilter>(InputFilterWeightRange(0.0, 130.0))
                txt_name.setText(getData("" + AppUtils.decimalFormat2.format(tmp)))
                rdo_kg.setClickable(false)
                rdo_lb.setClickable(true)
            }
            //saveWeightData(txt_name);
        })

        rdo_lb.setOnClickListener(View.OnClickListener {
            if (!sh!!.check_blank_data(txt_name.text.toString())) {
                val weight_in_kg = txt_name.text.toString().toDouble()

                var weight_in_lb = 0.0

                if (weight_in_kg > 0) weight_in_lb =
                    Math.round(kgToLbConverter(weight_in_kg)).toDouble()

                val tmp = weight_in_lb.toInt()

                txt_name.filters = arrayOf<InputFilter>(DigitsInputFilter(3, 0, 287.0))
                txt_name.setText(getData("" + tmp))
                rdo_kg.setClickable(true)
                rdo_lb.setClickable(false)
            }
            //saveWeightData(txt_name);
        })

        if (ph.getBoolean(AppUtils.PERSON_WEIGHT_UNIT)) {
            rdo_kg.setChecked(true)
            rdo_kg.setClickable(false)
            rdo_lb.setClickable(true)
        } else {
            rdo_lb.setChecked(true)
            rdo_kg.setClickable(true)
            rdo_lb.setClickable(false)
        }

        if (!sh!!.check_blank_data(ph.getString(AppUtils.PERSON_WEIGHT))) {
            if (rdo_kg.isChecked()) {
                //ah.customAlert("if if");

                /*int sel_pos=0;
                for(int k=0;k<weight_kg_lst.size();k++)
                {
                    if(Float.parseFloat(weight_kg_lst.get(k))==Float.parseFloat(""+getData(ph.getString(AppUtils.PERSON_WEIGHT))))
                        sel_pos=k;
                }

                pickerKG.setSelectedItem(sel_pos);*/

                txt_name.filters = arrayOf<InputFilter>(InputFilterWeightRange(0.0, 130.0))
                txt_name.setText(getData(ph.getString(AppUtils.PERSON_WEIGHT)))
            } else {
                //ah.customAlert("if else");
                txt_name.filters = arrayOf<InputFilter>(DigitsInputFilter(3, 0, 287.0))
                txt_name.setText(getData(ph.getString(AppUtils.PERSON_WEIGHT)))
            }
        } else {
            if (rdo_kg.isChecked()) {
                //ah.customAlert("else if");
                txt_name.filters = arrayOf<InputFilter>(InputFilterWeightRange(0.0, 130.0))
                txt_name.setText("80.0")
            } else {
                //ah.customAlert("else else");
                txt_name.filters = arrayOf<InputFilter>(DigitsInputFilter(3, 0, 287.0))
                txt_name.setText("176")
            }
        }

        btn_cancel.setOnClickListener { dialog.cancel() }

        img_cancel.setOnClickListener { dialog.cancel() }

        txt_name.setSelection(txt_name.text.toString().length)

        btn_add.setOnClickListener {
            if (sh!!.check_blank_data(txt_name.text.toString().trim { it <= ' ' })) {
                ah!!.customAlert(sh!!.get_string(R.string.str_weight_validation))
            } else {
                var str = txt_name.text.toString().trim { it <= ' ' }

                if (rdo_kg.isChecked()) {
                    str = AppUtils.decimalFormat2.format(str.toDouble())
                }


                str += " " + (if (rdo_kg.isChecked()) "kg" else "lb")

                txt_weight.setText(str)

                saveWeightData(txt_name)

                calculate_goal()

                dialog.dismiss()
            }
        }

        dialog.setContentView(view)

        dialog.show()
    }

    fun calculate_goal_old() {
        val tmp_weight = "" + ph.getString(AppUtils.PERSON_WEIGHT)
        if (!sh!!.check_blank_data(tmp_weight)) {
            var tmp_lbs = 0.0
            tmp_lbs = if (ph.getBoolean(AppUtils.PERSON_WEIGHT_UNIT)) {
                kgToLbConverter(tmp_weight.toDouble())
            } else {
                ("" + tmp_weight).toDouble()
            }

            val tmp_oz = tmp_lbs * 0.5

            val tmp_ml = ozToMlConverter(tmp_oz.toFloat())

            //double rounded_up = 100 * Math.ceil(tmp_ml / 100);
            if (ph.getBoolean(AppUtils.PERSON_WEIGHT_UNIT)) {
                AppUtils.DAILY_WATER_VALUE = tmp_ml.toFloat()
            } else {
                AppUtils.DAILY_WATER_VALUE = tmp_oz.toFloat()
            }

            AppUtils.DAILY_WATER_VALUE = Math.round(AppUtils.DAILY_WATER_VALUE)

            val str = getData("" + AppUtils.DAILY_WATER_VALUE as Int) + " " +
                    (if (ph.getBoolean(AppUtils.PERSON_WEIGHT_UNIT)) "ml" else "fl oz")

            txt_goal.setText(str)

            ph.savePreferences(AppUtils.DAILY_WATER, AppUtils.DAILY_WATER_VALUE)
        }
    }


    fun calculate_goal() {
        val tmp_weight = "" + ph.getString(AppUtils.PERSON_WEIGHT)

        val isFemale: Boolean = ph.getBoolean(AppUtils.USER_GENDER)
        val isActive: Boolean = ph.getBoolean(AppUtils.IS_ACTIVE)
        val isPregnant: Boolean = ph.getBoolean(AppUtils.IS_PREGNANT)
        val isBreastfeeding: Boolean = ph.getBoolean(AppUtils.IS_BREATFEEDING)
        val weatherIdx: Int = ph.getInt(AppUtils.WEATHER_CONSITIONS)

        if (!sh!!.check_blank_data(tmp_weight)) {
            var tot_drink = 0.0
            var tmp_kg = 0.0
            tmp_kg = if (ph.getBoolean(AppUtils.PERSON_WEIGHT_UNIT)) {
                ("" + tmp_weight).toDouble()
            } else {
                lbToKgConverter(tmp_weight.toDouble())
            }

            tot_drink =
                if (isFemale) if (isActive) tmp_kg * AppUtils.ACTIVE_FEMALE_WATER else tmp_kg * AppUtils.FEMALE_WATER
                else if (isActive) tmp_kg * AppUtils.ACTIVE_MALE_WATER else tmp_kg * AppUtils.MALE_WATER

            tot_drink *= if (weatherIdx == 1) AppUtils.WEATHER_CLOUDY
            else if (weatherIdx == 2) AppUtils.WEATHER_RAINY
            else if (weatherIdx == 3) AppUtils.WEATHER_SNOW
            else AppUtils.WEATHER_SUNNY

            if (isPregnant && isFemale) {
                tot_drink += AppUtils.PREGNANT_WATER
            }

            if (isBreastfeeding && isFemale) {
                tot_drink += AppUtils.BREASTFEEDING_WATER
            }

            if (tot_drink < 900) tot_drink = 900.0

            if (tot_drink > 8000) tot_drink = 8000.0

            val tot_drink_fl_oz = mlToOzConverter(tot_drink.toFloat())

            if (ph.getBoolean(AppUtils.PERSON_WEIGHT_UNIT)) {
                //lbl_unit.setText("ml");
                AppUtils.DAILY_WATER_VALUE = tot_drink.toFloat()
            } else {
                //lbl_unit.setText("fl oz");
                AppUtils.DAILY_WATER_VALUE = tot_drink_fl_oz.toFloat()
            }

            AppUtils.DAILY_WATER_VALUE = Math.round(AppUtils.DAILY_WATER_VALUE)

            //txt_goal.setText(getData("" + (int)AppUtils.DAILY_WATER_VALUE));
            val str = getData("" + AppUtils.DAILY_WATER_VALUE as Int) + " " +
                    (if (ph.getBoolean(AppUtils.PERSON_WEIGHT_UNIT)) "ml" else "fl oz")

            txt_goal.setText(str)

            ph.savePreferences(AppUtils.DAILY_WATER, AppUtils.DAILY_WATER_VALUE)

            refreshWidget()

            calculateActiveValue()
        }
    }


    fun calculateActiveValue() {
        var pstr = ""

        pstr = if (ph.getBoolean(AppUtils.PERSON_WEIGHT_UNIT)) {
            (AppUtils.PREGNANT_WATER as Int).toString() + " ml"
        } else {
            Math.round(mlToOzConverter(AppUtils.PREGNANT_WATER)).toInt().toString() + " fl oz"
        }

        lbl_pregnant.setText(sh!!.get_string(R.string.pregnant))
        convertUpperCase(lbl_pregnant)
        lbl_pregnant.setText(lbl_pregnant.getText().toString() + " (+" + pstr + ")")

        //====================================
        var bstr = ""

        bstr = if (ph.getBoolean(AppUtils.PERSON_WEIGHT_UNIT)) {
            (AppUtils.BREASTFEEDING_WATER as Int).toString() + " ml"
        } else {
            Math.round(mlToOzConverter(AppUtils.BREASTFEEDING_WATER)).toInt()
                .toString() + " fl oz"
        }

        lbl_breastfeeding.setText(sh!!.get_string(R.string.breastfeeding))
        convertUpperCase(lbl_breastfeeding)
        lbl_breastfeeding.setText(lbl_breastfeeding.getText().toString() + " (+" + bstr + ")")

        //====================================
        val tmp_weight = "" + ph.getString(AppUtils.PERSON_WEIGHT)
        val isFemale: Boolean = ph.getBoolean(AppUtils.USER_GENDER)
        val weatherIdx: Int = ph.getInt(AppUtils.WEATHER_CONSITIONS)

        var tmp_kg = 0.0
        tmp_kg = if (ph.getBoolean(AppUtils.PERSON_WEIGHT_UNIT)) {
            ("" + tmp_weight).toDouble()
        } else {
            lbToKgConverter(tmp_weight.toDouble())
        }

        //====================
        var diff = 0.0

        if (isFemale) diff = tmp_kg * AppUtils.DEACTIVE_FEMALE_WATER
        else diff = tmp_kg * AppUtils.DEACTIVE_MALE_WATER


        //====================
        diff *= if (weatherIdx == 1) AppUtils.WEATHER_CLOUDY
        else if (weatherIdx == 2) AppUtils.WEATHER_RAINY
        else if (weatherIdx == 3) AppUtils.WEATHER_SNOW
        else AppUtils.WEATHER_SUNNY


        //====================
        bstr = ""

        bstr = if (ph.getBoolean(AppUtils.PERSON_WEIGHT_UNIT)) {
            Math.round(diff).toInt().toString() + " ml"
        } else {
            Math.round(mlToOzConverter(diff.toFloat())).toInt().toString() + " fl oz"
        }

        lbl_active.setText(sh!!.get_string(R.string.active))
        convertUpperCase(lbl_active)
        lbl_active.setText(lbl_active.getText().toString() + " (+" + bstr + ")")
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }


    fun saveData(txt_name: AppCompatEditText) {
        d("saveData", "" + txt_name.text.toString().trim { it <= ' ' })

        ph.savePreferences(
            AppUtils.PERSON_HEIGHT,
            "" + txt_name.text.toString().trim { it <= ' ' })
        ph.savePreferences(AppUtils.PERSON_HEIGHT_UNIT, rdo_cm.isChecked())

        ph.savePreferences(AppUtils.SET_MANUALLY_GOAL, false)
    }

    fun saveWeightData(txt_name: AppCompatEditText) {
        d(
            "saveWeightData",
            ("" + rdo_kg.isChecked()).toString() + " @@@ " + txt_name.text.toString()
                .trim { it <= ' ' })

        ph.savePreferences(
            AppUtils.PERSON_WEIGHT,
            "" + txt_name.text.toString().trim { it <= ' ' })
        ph.savePreferences(AppUtils.PERSON_WEIGHT_UNIT, rdo_kg.isChecked())

        ph.savePreferences(AppUtils.WATER_UNIT, if (rdo_kg.isChecked()) "ml" else "fl oz")

        ph.savePreferences(AppUtils.SET_MANUALLY_GOAL, false)

        AppUtils.WATER_UNIT_VALUE = if (rdo_kg.isChecked()) "ml" else "fl oz"

        refreshWidget()
    }

    fun refreshWidget() {
        val intent = Intent(act, NewAppWidget::class.java)
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
        // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
        // since it seems the onUpdate() is only fired on that:
        val ids = AppWidgetManager.getInstance(act).getAppWidgetIds(
            ComponentName(
                act!!,
                NewAppWidget::class.java
            )
        )
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        act!!.sendBroadcast(intent)
    }


    //===============
    fun init_WeightKG() {
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
    }

    fun init_WeightLB() {
        weight_lb_lst.clear()
        for (k in 66..287) {
            weight_lb_lst.add("" + k)
        }

        val st = arrayOfNulls<CharSequence>(weight_lb_lst.size)
        for (k in weight_lb_lst.indices) {
            st[k] = "" + weight_lb_lst[k]
        }
    }


    //===============
    fun init_HeightCM() {
        height_cm_lst.clear()
        for (k in 60..240) {
            height_cm_lst.add("" + k)
        }

        val st = arrayOfNulls<CharSequence>(height_cm_lst.size)
        for (k in height_cm_lst.indices) {
            st[k] = "" + height_cm_lst[k]
        }
    }

    fun init_HeightFeet() {
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
    }


    //===============================================
    fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, SELECT_FILE1)
    }

    fun captureImage() {
        getSaveImageUri()
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(
            MediaStore.EXTRA_OUTPUT,
            imageUri
        )

        startActivityForResult(cameraIntent, PICK_Camera_IMAGE)
    }

    fun getSaveImageUri() {
        try {
            val root: File = File(
                (((Environment.getExternalStorageDirectory()
                    .toString() + "/" + AppUtils.APP_DIRECTORY_NAME).toString() + "/" + AppUtils.APP_PROFILE_DIRECTORY_NAME).toString() + "/")
            )
            if (!root.exists()) {
                root.mkdirs()
            }
            //String imageName = "profile_image_" + System.currentTimeMillis() + ".png";
            val imageName = "profile_image.png"

            val sdImageMainDirectory: File = File(root, imageName)


            val isNoget = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
            if (isNoget) {
                imageUri = FileProvider.getUriForFile(
                    act!!,
                    act!!.packageName + ".provider",
                    sdImageMainDirectory
                )
                selectedImagePath = sdImageMainDirectory.absolutePath
            } else {
                imageUri = Uri.fromFile(sdImageMainDirectory)
                selectedImagePath = FileUtils.getPath(act, imageUri)
            }
        } catch (e: Exception) {
            d("Incident Photo ", "Error occurred. Please try again later." + e.message)
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    fun checkStoragePermissions() {
        if (ContextCompat.checkSelfPermission(
                act!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED || (ContextCompat.checkSelfPermission(
                act!!, Manifest.permission.CAMERA
            )
                    != PackageManager.PERMISSION_GRANTED)
        ) {
            requestPermissions(
                arrayOf<String>(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ), STORAGE_PERMISSION
            )
        } else {
            //selectImage();
            openPicker()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>?,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        when (requestCode) {
            STORAGE_PERMISSION -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // selectImage();
                openPicker()
                return
            } else {
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            SELECT_FILE1 -> if (resultCode == RESULT_OK) {
                if (ContextCompat.checkSelfPermission(
                        act!!,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    bottomSheetDialog!!.dismiss()
                } else {
                    try {
                        selectedImage = data.data

                        if (selectedImage != null) {
                            /*Glide.with(act)
                                        .load(selectedImage)
                                        .apply(RequestOptions.circleCropTransform())
                                        .into(img_user);

                                String path = FileUtils.getPath(act, selectedImage);

                                ph.savePreferences(AppUtils.USER_PHOTO,path);*/

                            bottomSheetDialog!!.dismiss()

                            CropImage.activity(selectedImage).start(act!!)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            PICK_Camera_IMAGE -> if (resultCode == RESULT_OK) {
                val path = selectedImagePath!!

                /*Glide.with(act)
                            .load(path)
                            .apply(RequestOptions.circleCropTransform())
                            .into(img_user);

                    ph.savePreferences(AppUtils.USER_PHOTO,path);*/
                bottomSheetDialog!!.dismiss()

                CropImage.activity(imageUri).start(act!!)
            }

            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == RESULT_OK) {
                    val resultUri = result.uri

                    val path: String = FileUtils.getPath(act, resultUri)

                    ph.savePreferences(AppUtils.USER_PHOTO, path)

                    Glide.with(act!!)
                        .load(resultUri)
                        .apply(RequestOptions.circleCropTransform())
                        .into(img_user)
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    val error = result.error
                }
            }
        }
    }
}