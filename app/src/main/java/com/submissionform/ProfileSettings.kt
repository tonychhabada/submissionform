package com.submissionform

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceActivity
import android.view.MenuItem


import android.widget.Toolbar
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatDelegate

import com.submissionform.utilities.Common
import kotlinx.android.synthetic.main.activity_profile_settings.*

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import android.preference.PreferenceFragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat


class ProfileSettings : AppCompatActivity(),SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    //    private var mDelegate: AppCompatDelegate? = null
//
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_settings)
        setTitle("Settings")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);

    supportFragmentManager.beginTransaction()
        // .replace(android.R.id.content, SettingsFragment())
        .replace(android.R.id.content, MyPreferenceFragment())
        .commit()
//
//        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);

//        val root = findViewById<View>(android.R.id.list).parent.parent.parent as LinearLayout
//        val bar =
//            LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false) as Toolbar
//        root.addView(bar, 0) // insert at top
//        bar.setNavigationOnClickListener { finish() }


    }
//
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}

class MyPreferenceFragment : PreferenceFragmentCompat() {


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings);
        val changePassword = findPreference("changePassword") as Preference
        changePassword.onPreferenceClickListener = Preference.OnPreferenceClickListener {

            var intent = Intent(activity, ChangePasswordActivity::class.java)
            startActivity(intent)
            true
        }


        val myPref = findPreference("signout") as Preference
        myPref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            Common.sharedInsance.saveStringPreference("", activity, "userid")
            var intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            true
        }

    }
}
