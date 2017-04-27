package net.gerardomedina.meetandeat.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.view.activity.MainActivity;
import net.gerardomedina.meetandeat.view.fragment.ContactsFragment;
import net.gerardomedina.meetandeat.view.fragment.DashboardFragment;
import net.gerardomedina.meetandeat.view.fragment.SettingsFragment;

public class SectionsAdapter extends FragmentPagerAdapter {
    private MainActivity mainActivity;
    private ContactsFragment contactsFragment;
    private DashboardFragment dashboardFragment;
    private SettingsFragment settingsFragment;

    public SectionsAdapter(MainActivity mainActivity, FragmentManager fm) {
        super(fm);
        this.mainActivity = mainActivity;
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
        return mainActivity.getString(R.string.app_name);
    }
}