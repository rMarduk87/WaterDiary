package rpt.tool.waterdiary

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import rpt.tool.waterdiary.databinding.ActivityMenuBinding
import rpt.tool.waterdiary.utils.managers.SharedPreferencesManager

class MenuActivity : AppCompatActivity() {

    lateinit var binding: ActivityMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onNavigateUp(): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.menu_activity_nav_host_fragment)
                    as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp()
                || super.onSupportNavigateUp()
    }
}