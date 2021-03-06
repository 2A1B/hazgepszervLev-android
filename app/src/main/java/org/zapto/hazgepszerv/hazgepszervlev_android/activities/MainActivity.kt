package org.zapto.hazgepszerv.hazgepszervlev_android.activities

import android.content.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.getbase.floatingactionbutton.FloatingActionButton
import com.getbase.floatingactionbutton.FloatingActionsMenu
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.support.v4.drawerListener
import com.google.firebase.messaging.FirebaseMessaging
import com.tapadoo.alerter.Alerter
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import org.zapto.hazgepszerv.hazgepszervlev_android.R
import com.google.firebase.auth.UserProfileChangeRequest
import org.zapto.hazgepszerv.hazgepszervlev_android.fragments.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var fab : FloatingActionsMenu

    var alert : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val user = FirebaseAuth.getInstance().currentUser

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val editor = sharedPreferences.edit()
        editor.putString("user_email_pref",user?.email)
        editor.commit()

        val userNamePref = sharedPreferences.getString("user_name_pref","")

        val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(userNamePref).build()

        user?.updateProfile(profileUpdates)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        fab = findViewById<FloatingActionsMenu>(R.id.multiple_actions)

        val fabi = findViewById<View>(R.id.action_a) as FloatingActionButton
        fabi.size = FloatingActionButton.SIZE_MINI;
        fabi.setOnClickListener(View.OnClickListener {
            var asd: Fragment? = null
            fab.collapseImmediately()
            try {
                asd = NewJobReportFragment::class.java.newInstance()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction().replace(R.id.flContent, asd).addToBackStack(asd.toString()).commit()
        })

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, 0, 0)
        drawerLayout.drawerListener { toggle }
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val navHeader = navigationView.getHeaderView(0)
        val username = navHeader.findViewById<TextView>(R.id.navbaruser)
        val email = navHeader.findViewById<TextView>(R.id.navbaremail)

        if (user != null) {
            username.text = user.displayName
            email.text = user.email
        }

        FirebaseMessaging.getInstance().subscribeToTopic("notifications")

        navigationView.setCheckedItem(R.id.menu_home)
        val item = navigationView.menu.getItem(0)
        onNavigationItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        var fragmentClass: Class<*>? = null

        val id = item.itemId

        when (id) {
            R.id.menu_home -> {
                fragmentClass = HomeScreenFragment::class.java
            }
            R.id.menu_jobreports -> {
                fragmentClass = TabbedJobreportsFragment::class.java
            }
            R.id.menu_calendar -> {
                fragmentClass = AsynchronousCalendar::class.java
            }
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }


        if (fragmentClass != null) {
            try {
                fragment = fragmentClass.newInstance() as Fragment
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val manager = supportFragmentManager
            manager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(fragment.toString()).commit()

        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun showAlert(){
        if(alert) {
            Alerter.create(this)
                    .setTitle("Figyelem")
                    .setText("Új hibajegy!")
                    .enableSwipeToDismiss()
                    .show()
            alert = false
        }
    }

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            alert = true
            showAlert()
        }
    }

    public override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                IntentFilter("myFunction"))
    }

    public override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onBackPressed() {
        val fm = supportFragmentManager
        println("Entry Count: " + fm.backStackEntryCount)
        if (fm.backStackEntryCount > 0) {
            Log.i("MainActivity", "popping backstack")
            fm.popBackStack()
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super")
            super.onBackPressed()
        }
    }
}
