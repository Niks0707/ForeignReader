package niks.foreignreader.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import niks.foreignreader.activities.ReaderActivity;

/**
 * Created by Niks on 01.04.2017.
 */

public class ReaderPagerAdapter extends FragmentPagerAdapter {

    private ReaderActivity mActivity;
    private int mCountPages;

    public ReaderPagerAdapter(ReaderActivity activity, FragmentManager fm, int countPages) {
        super(fm);
        mActivity = activity;
        mCountPages = countPages;
    }

    @Override
    public Fragment getItem(int position) {
        return ReaderPageFragment.create(mActivity, position);
    }

    @Override
    public int getCount() {
        return mCountPages;
    }

    public void incrementPageCount() {
        mCountPages += 1;
        notifyDataSetChanged();
    }

}
