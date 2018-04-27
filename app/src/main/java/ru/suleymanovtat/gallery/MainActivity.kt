package ru.suleymanovtat.gallery

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ru.suleymanovtat.gallery.fragment.MainFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.container, MainFragment(), BuildConfig.TAG_MAIN_FRAGMENT).addToBackStack(null).commit()
        }
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount === 1) {
            finish()
        } else {
            fragmentManager.popBackStack()
        }
    }
}
