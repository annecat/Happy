package fr.milleron.happy

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        //supportFragmentManager
        //    .beginTransaction()
        //    .replace(R.id.settings, SettingsFragment())
        //    .commit()
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // load preferences
        val minimumTime = findViewById<TextView>(R.id.minimum_time)
        val maximumTime = findViewById<TextView>(R.id.maximum_time)
        val timeSlotText = findViewById<TextView>(R.id.timeSlot)
        val rangeSeekBar = findViewById<CrystalRangeSeekbar>(R.id.range_seek_bar)
        val seekBarLayout = findViewById<LinearLayout>(R.id.linearLayoutSeekBar)
        val sharedPref = getSharedPreferences(getString(R.string.pref_happy), Context.MODE_PRIVATE)
        sharedPref.getString(getString(R.string.pref_receive_random), "YES")

        // Update seekbar
        rangeSeekBar.setMinStartValue(sharedPref.getInt(getString(R.string.pref_min_time), 10).toFloat())
        rangeSeekBar.setMaxStartValue(sharedPref.getInt(getString(R.string.pref_max_time), 18).toFloat())
        rangeSeekBar.apply()
        //update radiobutton
        val radioButtonYes = findViewById<RadioButton>(R.id.radioButtonYes)
        val radioButtonNo = findViewById<RadioButton>(R.id.radioButtonNo)

        if (sharedPref.getString(getString(R.string.pref_receive_random), "YES") == "NO")
        {
            radioButtonYes.isChecked = false
            radioButtonNo.isChecked = true
            timeSlotText.visibility = View.INVISIBLE
            seekBarLayout.visibility = View.INVISIBLE
        }


        radioButtonNo.setOnClickListener{
            seekBarLayout.visibility = View.INVISIBLE
            timeSlotText.visibility = View.INVISIBLE
        }

        radioButtonYes.setOnClickListener{
            seekBarLayout.visibility = View.VISIBLE
            timeSlotText.visibility = View.VISIBLE
        }

        rangeSeekBar.setOnRangeSeekbarChangeListener { n, n2 ->
            minimumTime.text = getString(R.string.hours_short, n.toInt())
            maximumTime.text = getString(R.string.hours_short, n2.toInt())
        }
    }

    fun settingsSave(v:View) {
        // save preferences
        val rangeSeekBar = findViewById<CrystalRangeSeekbar>(R.id.range_seek_bar)
        val sharedPref = getSharedPreferences(getString(R.string.pref_happy), Context.MODE_PRIVATE)
        val edit = sharedPref.edit()

        val radioButtonYes = findViewById<RadioButton>(R.id.radioButtonYes)

        if (radioButtonYes.isChecked)
            edit.putString(getString(R.string.pref_receive_random), "YES")
        else
            edit.putString(getString(R.string.pref_receive_random), "NO")
        edit.putInt(getString(R.string.pref_min_time), rangeSeekBar.selectedMinValue.toInt())
        edit.putInt(getString(R.string.pref_max_time), rangeSeekBar.selectedMaxValue.toInt())
        edit.apply()
        RandomAlarm.setRandomAlarm(this, sharedPref)
        finish()
    }

    fun settingsCancel(v:View) {
        finish()
    }
}