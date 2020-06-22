package fr.milleron.happy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    lateinit var bottomNavigation:BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        this.bottomNavigation.setOnNavigationItemSelectedListener(menuChange)
        this.bottomNavigation.setOnNavigationItemReselectedListener(menuReselect)
        refreshSentence()
        RandomAlarm.createNotificationChannel(this)
        RandomAlarm.cancelNotification(this)
        RandomAlarm.setRandomAlarm(this)
    }




    private fun refreshSentence() {
        val textView:TextView = findViewById(R.id.centralText)
        val sentences:Array<String> = resources.getStringArray(R.array.sentences)
        textView.text = sentences[Random.nextInt(0, sentences.size)]
    }

    private val menuChange = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.button_refresh -> {
                refreshSentence()
                return@OnNavigationItemSelectedListener true
            }
            R.id.button_close -> {
                finishAffinity()
                return@OnNavigationItemSelectedListener true
            }
            R.id.button_config -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        return@OnNavigationItemSelectedListener false
    }

    private val menuReselect = BottomNavigationView.OnNavigationItemReselectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.button_refresh -> {
                refreshSentence()
            }
            R.id.button_close -> {
                finishAffinity()
            }
            R.id.button_config -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
