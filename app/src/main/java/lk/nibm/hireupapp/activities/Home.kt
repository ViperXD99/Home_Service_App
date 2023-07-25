package lk.nibm.hireupapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.style.TtsSpan.OrdinalBuilder
import androidx.fragment.app.Fragment
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.databinding.ActivityHomeBinding
import lk.nibm.hireupapp.fragments.BookingFragment
import lk.nibm.hireupapp.fragments.HomeFragment
import lk.nibm.hireupapp.fragments.OrdersFragment
import lk.nibm.hireupapp.fragments.ProfileFragment


class Home : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())
        initializeComponents()

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home_btn -> replaceFragment(HomeFragment())
                R.id.profile_btn -> replaceFragment(ProfileFragment())
                R.id.bookings_btn -> replaceFragment(BookingFragment())
                R.id.orders_btn -> replaceFragment(OrdersFragment())

            }
            true
        }
    }

    private fun initializeComponents() {

    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
    }
}