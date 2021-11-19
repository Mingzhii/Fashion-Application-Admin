package my.com.fashionappstaff

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.fashionappstaff.data.email1
import my.com.fashionappstaff.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val nav by lazy { supportFragmentManager.findFragmentById(R.id.host)!!.findNavController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> nav.navigate(R.id.action_global_staffHomeFragment)
                R.id.nav_product -> nav.navigate(R.id.action_global_productFragment)
                R.id.nav_reward -> nav.navigate(R.id.action_global_rewardFragment)
                R.id.nav_profile -> nav.navigate(R.id.action_global_staffProfileFragment)
                R.id.nav_delivery -> nav.navigate(R.id.listDeliveryFragment)
            }
            true
        }

    }

    override fun onBackPressed() {

        val num = nav.currentDestination?.label

        when(num){
            "fragment_staff_home"   -> super.finish()
            "fragment_product"      -> super.finish()
            "fragment_reward"       -> super.finish()
            "staffProfileFragment" -> super.finish()
            "fragment_sign_in" -> super.finish()
        }

        nav.popBackStack()

    }

    override fun onSupportNavigateUp(): Boolean {
        return nav.navigateUp() || super.onSupportNavigateUp()
    }
}