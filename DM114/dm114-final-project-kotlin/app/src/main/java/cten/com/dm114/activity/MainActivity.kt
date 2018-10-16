package cten.com.dm114.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import cten.com.dm114.R
import cten.com.dm114.R.id.*
import cten.com.dm114.fragment.OrdersFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configNavDrawer()
    }

    private fun configNavDrawer() {
        val toolbar = toolbar
        setSupportActionBar(toolbar)

        val drawer = drawer_layout
        val toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            nav_orders -> openOrders()
            nav_products -> {}
            nav_logout -> {}

            else -> {}
        }

        drawer_layout.closeDrawer(GravityCompat.START)

        return true
    }

    private fun openOrders() {

        /*supportFragmentManager
            .beginTransaction()
            .replace(R.id.container,
                OrdersFragment.newInstance())
            .commit()*/
    }
}
