package rpt.tool.waterdiary.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import rpt.tool.waterdiary.DashboardActivity
import rpt.tool.waterdiary.MenuActivity
import rpt.tool.waterdiary.R
import rpt.tool.waterdiary.base.NavBaseFragment
import rpt.tool.waterdiary.databinding.FragmentSettingsBinding
import rpt.tool.waterdiary.utils.managers.SharedPreferencesManager



class SettingsFragment : NavBaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.navigationBarColor = requireContext().resources.getColor(R.color.str_green_card)
        body()
    }

    private fun body() {
        binding.include1.lblToolbarTitle.text = sh!!.get_string(R.string.str_settings)
        binding.include1.leftIconBlock.setOnClickListener { finish() }
        binding.include1.rightIconBlock.visibility = View.GONE

        binding.backupRestoreBlock.setOnClickListener {
            intent = Intent(act, MenuActivity::class.java)
            intent!!.putExtra("Class",9)
            startActivity(intent!!)
        }

        binding.weightBlock.setOnClickListener {
            intent = Intent(act, MenuActivity::class.java)
            intent!!.putExtra("Class",5)
            startActivity(intent!!)
        }

        binding.switchNotification.setChecked(SharedPreferencesManager.disableNotification)

        binding.switchNotification.setOnCheckedChangeListener { buttonView, isChecked ->
            SharedPreferencesManager.disableNotification = isChecked
        }

        binding.switchSound.setChecked(SharedPreferencesManager.disableSoundWhenAddWater)

        binding.switchSound.setOnCheckedChangeListener { buttonView, isChecked ->
            SharedPreferencesManager.disableSoundWhenAddWater = isChecked
        }
    }

    private fun finish() {
        startActivity(Intent(requireActivity(), DashboardActivity::class.java))
    }
}