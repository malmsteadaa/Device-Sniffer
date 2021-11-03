package com.example.sec2courseprojectgroup3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

val bluetooth=BluetoothPage()
val wifi=WifiPage()
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener { item ->
            when (item.itemId) {
            R.id.IBlueTouth->{
            changeFragment(bluetooth)
                true
            }
             R.id.IWifi->{
                 changeFragment(wifi)
                 true
             }
                else -> false
            }
        }

    }
    private fun changeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentlayout,fragment)
            commit()
        }
    }
}