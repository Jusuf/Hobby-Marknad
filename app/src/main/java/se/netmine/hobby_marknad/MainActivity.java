package se.netmine.hobby_marknad;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Map;
import java.util.Stack;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IMainActivity {

    private final Context context = this;
    private Stack<Fragment> fragmentStack = new Stack<Fragment>();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toolbar.setNavigationIcon(R.drawable.ic_menu_white);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentStack.size() == 1) {
                    drawer.openDrawer(GravityCompat.START);
                } else {
                    onBackPressed();
                }
            }
        });

        navigateToStartFragment();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (fragmentStack.size() > 1) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left);

                fragmentStack.peek().onPause();
                ft.hide(fragmentStack.peek());
                ft.remove(fragmentStack.pop());

                if (fragmentStack.size() == 1) {
                    toolbar.setNavigationIcon(R.drawable.ic_menu_white);
                }

                fragmentStack.peek().onResume();


                ft.show(fragmentStack.peek());

                //((IFragment)fragmentStack.peek()).updateUi();

                ft.commit();
            } else {
                super.onBackPressed();
            }
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_hobby) {
            MyHobbyFragment fragment = new MyHobbyFragment();
            onNavigateToFragment(fragment);
        } else if (id == R.id.nav_service_book) {

        } else if (id == R.id.nav_catalog_and_magazines) {
            CatalogueAndMagazinesFragment fragment = new CatalogueAndMagazinesFragment();
            onNavigateToFragment(fragment);
        } else if (id == R.id.nav_resellers) {

        } else if (id == R.id.nav_faq) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void navigateToFragment(Fragment fragment) {
        if( fragmentStack.isEmpty() == false &&
                fragment.getClass().getSimpleName().compareTo(fragmentStack.peek().getClass().getSimpleName()) == 0 &&
                fragment.getClass().getSimpleName().compareTo(StartFragment.class.getSimpleName()) != 0)
        {
            return;
        }

        if (fragmentStack.isEmpty() == false &&
                fragment.getClass().getSimpleName().compareTo(fragmentStack.peek().getClass().getSimpleName()) == 0) {
            return;
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.animator.enter_from_left, R.animator.exit_to_right);

        if (fragmentStack.size() > 0) {
            fragmentStack.peek().onPause();
            ft.hide(fragmentStack.peek());
        }
        fragmentStack.push(fragment);

        ft.add(R.id.mainContent, fragment);
        ft.show(fragmentStack.peek());
        ft.commit();

        //fragmentStack.peek().onResume();

        if (fragmentStack.size() > 1) {
            toolbar.setNavigationIcon(R.drawable.ic_navigate_back);
        } else {
            toolbar.setNavigationIcon(R.drawable.ic_menu_white);
        }
    }

    private void navigateToStartFragment()
    {
        Fragment startFragment = null;


        startFragment =  new StartFragment();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        if(fragmentStack.size() > 0)
        {
            fragmentStack.peek().onPause();
            ft.hide(fragmentStack.peek());
            ft.commit();
        }

        // Clear all previously fragments.
        fragmentStack.clear();

        if(startFragment != null)
        {
            navigateToFragment(startFragment);
        }

    }

    @Override
    public void onNavigateToFragment(Fragment fragment) {
        navigateToFragment(fragment);
    }

    @Override
    public void onNavigateBack()
    {
        onBackPressed();
    }

    @Override
    public void setTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public Context getContext() {
        return this;
    }



}
