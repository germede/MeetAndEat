package net.gerardomedina.meetandeat.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.view.fragment.ContactsFragment;
import net.gerardomedina.meetandeat.view.fragment.DashboardFragment;
import net.gerardomedina.meetandeat.view.fragment.SettingsFragment;

public class MainActivity extends BaseActivity {

    private Toolbar toolbar;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;

    private final int DEFAULT_SECTION = 1;
    private ContactsFragment contactsFragment;
    private DashboardFragment dashboardFragment;
    private SettingsFragment settingsFragment;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).setEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_notifications) ;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.fragment_container);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setCurrentItem(DEFAULT_SECTION, true);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.getMenu().getItem(DEFAULT_SECTION).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_contacts:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_dashboard:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_preferences:
                        viewPager.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        });
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    contactsFragment = new ContactsFragment();
                    return contactsFragment;
                case 1:
                    dashboardFragment = new DashboardFragment();
                    return dashboardFragment;
                case 2:
                    settingsFragment = new SettingsFragment();
                    return settingsFragment;
            }
            return null;
        }
        @Override
        public int getCount() {
            return 3;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return getString(R.string.app_name);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NewMeetingActivity.PLACE_PICKER_REQUEST) {
            showToast("AJAJA");
//            NewMeetingActivity.setLocation("Hola");
        }
    }
}

