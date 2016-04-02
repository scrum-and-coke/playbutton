package application.musicplayer.muse.tabs;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.app.Activity;
import android.view.View;

import application.musicplayer.muse.MainActivity;

/**
 * Created by Edwin on 15/02/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    private Activity activityHolder;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 0)// if the position is 0 we are returning the First tab
        {
            Tab1 tab1 = new Tab1();
            if(MainActivity.controller!=null) {
                MainActivity.controller.setVisibility(View.INVISIBLE);
                //MainActivity.controller.setVisibility(View.GONE);
            }
            return tab1;
        }
        else if(position == 1)
        {
            Tab2 tab2 = new Tab2();
            if(MainActivity.controller!=null) {
                MainActivity.controller.setVisibility(View.INVISIBLE);
                //MainActivity.controller.setVisibility(View.GONE);
            }
            return tab2;
        }
        else if(position == 2)             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            Tab3 tab3 = new Tab3();
            if(MainActivity.controller!=null) {
                MainActivity.controller.setVisibility(View.INVISIBLE);
            }
            return tab3;
        }
        if(MainActivity.controller!=null) {
            MainActivity.controller.setVisibility(View.VISIBLE);
        }
        return null;
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}