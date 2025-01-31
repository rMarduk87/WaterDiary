package rpt.tool.waterdiary.ui.menu

import android.os.Bundle
import android.view.View
import rpt.tool.waterdiary.base.NavBaseFragment
import rpt.tool.waterdiary.databinding.FragmentMenuBinding
import rpt.tool.waterdiary.utils.navigation.safeNavController
import rpt.tool.waterdiary.utils.navigation.safeNavigate

class MenuFragment : NavBaseFragment<FragmentMenuBinding>(FragmentMenuBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val i = requireActivity().intent.getIntExtra("Class",1)
        when(i){
            1-> safeNavController?.safeNavigate(MenuFragmentDirections.actionMenuFragmentToHistoryFragment())
            2-> safeNavController?.safeNavigate(MenuFragmentDirections.actionMenuFragmentToReportFragment())
            3-> safeNavController?.safeNavigate(MenuFragmentDirections.actionMenuFragmentToSettingsFragment())
            4-> safeNavController?.safeNavigate(MenuFragmentDirections.actionMenuFragmentToFaqFragment())
            5-> safeNavController?.safeNavigate(MenuFragmentDirections.actionMenuFragmentToProfileFragment())
            6-> safeNavController?.safeNavigate(MenuFragmentDirections.actionMenuFragmentToReminderFragment())
        }
    }
}