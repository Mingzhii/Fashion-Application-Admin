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
import my.com.fashionapp.data.UserViewModel
import my.com.fashionappstaff.data.email1
import my.com.fashionappstaff.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val nav by lazy { supportFragmentManager.findFragmentById(R.id.host)!!.findNavController() }
    private val vm: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> nav.navigate(R.id.staffHomeFragment)
                R.id.nav_product -> nav.navigate(R.id.productFragment)
                R.id.nav_reward -> nav.navigate(R.id.rewardFragment)
                R.id.nav_profile -> nav.navigate(R.id.staffProfileFragment)
            }
            true
        }

//        binding.bottomNavigation.setupWithNavController(nav)

//        vm.getUserLiveData().observe(this) { user ->
//
//            // TODO Check Login Status
//            if (user == null) {
//                // no login yet
//
//            } else {
//                // login
//                if(user.userType == "Admin")
//                {
//                    binding.bottomNavigation.setOnItemSelectedListener {
//                        when (it.itemId) {
//                            R.id.nav_home -> nav.navigate(R.id.staffHomeFragment)
//                            R.id.nav_product -> nav.navigate(R.id.insertProductFragment)
//                            R.id.nav_reward -> nav.navigate(R.id.insertRewardFragment)
//                            R.id.nav_profile -> nav.navigate(R.id.staffProfileFragment)
//                        }
//                        true
//                    }
//                }
//                else
//                {
//                    binding.bottomNavigation.setOnItemSelectedListener {
//                        when (it.itemId) {
//                            R.id.nav_home -> nav.navigate(R.id.staffHomeFragment)
//                            R.id.nav_product -> nav.navigate(R.id.insertProductFragment)
//                            R.id.nav_reward -> nav.navigate(R.id.insertRewardFragment)
//                            R.id.nav_profile -> nav.navigate(R.id.blankFragment)
//                        }
//                        true
//                    }
//                }
//
//            }
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return nav.navigateUp() || super.onSupportNavigateUp()
    }
}