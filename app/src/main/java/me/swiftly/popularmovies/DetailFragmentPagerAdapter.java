package me.swiftly.popularmovies;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by vishnu on 19/04/16.
 */
public class DetailFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String[] tabTitles = {"Overview", "Trailers", "Reviews"};
    private Context context;

    public DetailFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = Fragment.instantiate(context, OverviewFragment.class.getName());
                break;
            case 1:
                fragment = Fragment.instantiate(context, TrailersFragment.class.getName());
                break;
            case 2:
                fragment = Fragment.instantiate(context, ReviewsFragment.class.getName());
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
