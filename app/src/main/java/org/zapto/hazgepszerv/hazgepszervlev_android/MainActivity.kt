package org.zapto.hazgepszerv.hazgepszervlev_android

import android.content.Intent
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
import org.zapto.hazgepszerv.hazgepszervlev_android.fragments.TabbedJobreportsFragment
import com.google.firebase.messaging.FirebaseMessaging
import org.zapto.hazgepszerv.hazgepszervlev_android.fragments.NewJobReportFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var fab : FloatingActionsMenu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        fab = findViewById<FloatingActionsMenu>(R.id.multiple_actions)

        val fabi = findViewById<View>(R.id.action_a) as FloatingActionButton
        fabi.setSize(FloatingActionButton.SIZE_MINI);
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
            fragmentManager.beginTransaction().replace(R.id.flContent, asd).commit()
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

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            username.text = user.displayName
            email.text = user.email
        }

        FirebaseMessaging.getInstance().subscribeToTopic("notifications")
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        var fragmentClass: Class<*>? = null

        val id = item.itemId

        when (id) {
            R.id.menu_home -> {
            }
            R.id.menu_jobreports -> {
                fragmentClass = TabbedJobreportsFragment::class.java
            }
            R.id.menu_calendar -> {

            }
            R.id.settings -> {

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

            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit()
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}
