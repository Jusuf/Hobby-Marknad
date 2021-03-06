package se.netmine.hobby_marknad;


import android.app.ActionBar;
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
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.SugarDb;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Stack;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IMainActivity {

    private final Context context = this;
    private Stack<Fragment> fragmentStack = new Stack<Fragment>();
    Toolbar toolbar;
    private TextView txtUserName = null;
    private LinearLayout linearLayoutNavHeader = null;
    private NavigationView navigationView = null;
    public static final String PREFS_NAME = "MyHobbyMarketPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        MyHobbyMarket.getInstance().init(settings);

//        getSharedPreferences(PREFS_NAME, 0).edit().clear().commit();

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
        navigationView.setItemIconTintList(ColorStateList.valueOf(Color.WHITE));
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);

        linearLayoutNavHeader = (LinearLayout) headerLayout.findViewById(R.id.linearLayoutNavHeader);
        linearLayoutNavHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyHobbyMarket.getInstance().isUserLoggedIn())
                {
                    UserSettingsFragment fragment = new UserSettingsFragment();
                    onNavigateToFragment(fragment);
                    drawer.closeDrawers();
                }
                else
                {
                    LoginFragment fragment = new LoginFragment();
                    onNavigateToFragment(fragment);
                    drawer.closeDrawers();
                }
            }
        });

        txtUserName = (TextView) headerLayout.findViewById(R.id.txtUserName);
        this.setUserName();


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

        MyHobbyMarket.getInstance().mainActivity = this;

        MyHobbyMarket.getInstance().setDeviceToken("");

//        MyHobbyMarket.getInstance().loadCampingsFromFile();

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

    @Override
    public void onMessagesLoaded(UserMessage[] messages)
    {
        // Notify the active fragment that a faqs has bean loaded
        ((IFragment)fragmentStack.peek()).onMessagesUpdated(messages);
    }

    @Override
    public void onFaqsLoaded(Faq[] faqs)
    {
        // Notify the active fragment that a faqs has bean loaded
        ((IFragment)fragmentStack.peek()).onFaqsUpdated(faqs);
    }

    @Override
    public void onDealersLoaded(Dealer[] dealers)
    {
        // Notify the active fragment that a dealers has bean loaded
        ((IFragment)fragmentStack.peek()).onDealersUpdated(dealers);
    }

    @Override
    public void onDealerLoaded(Dealer dealer)
    {
        DealerFragment fragment = new DealerFragment();
        fragment.dealer = dealer;
        onNavigateToFragment(fragment);
    }

    @Override
    public void onCampingsLoaded(ArrayList<Camping> campings, ArrayList<FacilityOption> campingFacilityOptions)
    {
        // Notify the active fragment that a dealers has bean loaded
        ((IFragment)fragmentStack.peek()).onCampingsUpdated(campings, campingFacilityOptions);
    }

    @Override
    public void onRegistered()
    {

    }

    @Override
    public void onLoggedIn()
    {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.animator.enter_from_left, R.animator.exit_to_right);

        while(fragmentStack.size() > 1)
        {
            fragmentStack.peek().onPause();
            ft.hide(fragmentStack.peek());
            fragmentStack.pop();
        }
        Fragment fragment = new StartFragment();
        fragmentStack.push(fragment);

        ft.add(R.id.mainContent, fragment);
        ft.show(fragmentStack.peek());
        ft.commit();

        navigateToStartFragment();
        toolbar.setNavigationIcon(R.drawable.ic_menu_white);
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.activity_main_drawer);
        setUserName();
    }

    @Override
    public void onLoggedOut()
    {
        navigateToStartFragment();
        toolbar.setNavigationIcon(R.drawable.ic_menu_white);
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.activity_main_drawer_not_logged_in);
        setUserName();
    }

    private void setUserName()
    {
        if(MyHobbyMarket.getInstance().isUserLoggedIn())
        {
            if(MyHobbyMarket.getInstance().getFirstName() != null && MyHobbyMarket.getInstance().getLastName() != null)
            {
                txtUserName.setText(MyHobbyMarket.getInstance().getFirstName() + " " + MyHobbyMarket.getInstance().getLastName());
            }
            else
            {
                txtUserName.setText(MyHobbyMarket.getInstance().getEmail());
            }
        }
        else {
            txtUserName.setText(getResources().getString(R.string.menu_header_logged_out));
        }
        txtUserName.setGravity(Gravity.CENTER);
    }

    @Override
    public void onResetPassword()
    {
        String url = MyHobbyMarket.url + "#resetPasswordRequest";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            LoginFragment fragment = new LoginFragment();
            onNavigateToFragment(fragment);
        }
        else if (id == R.id.nav_my_hobby) {

            Intent intent = getPackageManager().getLaunchIntentForPackage("se.netmine.myhobby");
            if (intent != null) {
                // We found the activity now start the activity
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                // Bring user to the market or let them choose an app?
                intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("market://details?id=" + "se.netmine.myhobby"));
                startActivity(intent);
            }

        } else if (id == R.id.nav_service_book) {
            if(MyHobbyMarket.getInstance().caravan == null)
            {
                ServiceBookFragment fragment = new ServiceBookFragment();
                onNavigateToFragment(fragment);
            }
            else
            {
                ServiceBookConnectedFragment fragment = new ServiceBookConnectedFragment();
                fragment.caravan = MyHobbyMarket.getInstance().caravan;
                onNavigateToFragment(fragment);
            }

        } else if (id == R.id.nav_catalog_and_magazines) {
            CatalogueAndMagazinesFragment fragment = new CatalogueAndMagazinesFragment();
            onNavigateToFragment(fragment);
        } else if (id == R.id.nav_resellers) {
            DealersFragment fragment = new DealersFragment();
            onNavigateToFragment(fragment);

        } else if (id == R.id.nav_faq) {
            FaqListFragment fragment = new FaqListFragment();
            onNavigateToFragment(fragment);
        }
        else if (id == R.id.nav_campings) {
            CampingsFragment fragment = new CampingsFragment();
            onNavigateToFragment(fragment);
        }
        else if (id == R.id.nav_logout) {
            MyHobbyMarket.getInstance().logout();
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
        Fragment startFragment;
        startFragment =  new StartFragment();

        if(MyHobbyMarket.getInstance().isUserLoggedIn() == false)
        {
            // Not logged
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer_not_logged_in);
        }
        else
        {
            // Logged in
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer);

        }

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

    @Override
    public void showToast(String message)
    {
//        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

        LinearLayout  layout = new LinearLayout(this);
        layout.setBackgroundResource(R.drawable.warning_bg);
        layout.setPadding(30,30,30,30);


        TextView  tv = new TextView(this);
        // set the TextView properties like color, size etc
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(15);

        tv.setGravity(Gravity.CENTER_VERTICAL);

        // set the text you want to show in  Toast
        tv.setText(message);
        layout.addView(tv);

        Toast toast=new Toast(this); //context is object of Context write "this" if you are an Activity
        // Set The layout as Toast View
        toast.setView(layout);

        // Position you toast here toast position is 50 dp from bottom you can give any integral value
        toast.setGravity(Gravity.BOTTOM, 0, 50);

        toast.show();
    }

    @Override
    public String formatDate(String dateString) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(dateString);

        return formatter.format(date);
    }
}
