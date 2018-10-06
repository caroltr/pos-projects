package br.com.cten.dm114projetofinal.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import br.com.cten.dm114projetofinal.GCM.GCMRegister;
import br.com.cten.dm114projetofinal.R;
import br.com.cten.dm114projetofinal.fragments.OrderInfoFragment;
import br.com.cten.dm114projetofinal.fragments.PedidosFragment;
import br.com.cten.dm114projetofinal.fragments.ProductInfoFragment;
import br.com.cten.dm114projetofinal.fragments.UserProductsFragment;
import br.com.cten.dm114projetofinal.model.OrderInfo;
import br.com.cten.dm114projetofinal.model.Product;
import br.com.cten.dm114projetofinal.model.UserCredentials;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);
        TextView userLogin = hView.findViewById(R.id.tv_email);
        userLogin.setText(getUserCredentialsStored().getLogin());

        if(savedInstanceState == null) {
            // Select first item as default
            navigationView.getMenu().getItem(0).setChecked(true);
            onNavigationItemSelected(navigationView.getMenu().getItem(0));

            gcmRegister();
        }

        createNotificationChannel();
        checkForNotificationPayload(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkForNotificationPayload(intent);
    }

    private void checkForNotificationPayload(Intent intent) {
        Bundle extras = intent.getExtras();

        if (extras != null && extras.containsKey("orderInfo")) {

            Fragment fragment = OrderInfoFragment.newInstance((OrderInfo) extras.getSerializable("orderInfo"));
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        } else if (extras != null && extras.containsKey("productOfInterest")) {

            Fragment fragment = ProductInfoFragment.newInstance((Product) extras.getSerializable("productOfInterest"));
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }

    private void gcmRegister() {
        GCMRegister gcmRegister = new GCMRegister(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Class fragmentClass;
        Fragment fragment = null;

        int backStackEntryCount;
        backStackEntryCount = getFragmentManager().getBackStackEntryCount();
        for (int j = 0; j < backStackEntryCount; j++) {
            getFragmentManager().popBackStack();
        }

        int id = item.getItemId();

        try {
            switch (id) {
                case R.id.nav_orders:
                    fragmentClass = PedidosFragment.class;
                    fragment = (Fragment) fragmentClass.newInstance();

                    break;

                case R.id.nav_products:
                    fragmentClass = UserProductsFragment.class;
                    fragment = (Fragment) fragmentClass.newInstance();
                    break;

                case R.id.nav_logout:
                    clearPreferences();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                    break;

                default:
                    fragmentClass = PedidosFragment.class;
                    fragment = (Fragment) fragmentClass.newInstance();

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container,
                    fragment).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", "Vendas", importance);
            channel.setDescription("Orders");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private UserCredentials getUserCredentialsStored() {
        SharedPreferences preferences = getSharedPreferences("PREFS_CREDENTIAL", MODE_PRIVATE);
        String login = preferences.getString("login", null);
        String password = preferences.getString("password", null);

        UserCredentials user = null;
        if(login != null && password != null) {
            user = new UserCredentials();
            user.setLogin(login);
            user.setPassword(password);
        }

        return user;
    }

    private void clearPreferences() {
        SharedPreferences.Editor editor = getSharedPreferences("PREFS_CREDENTIAL", MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }
}
