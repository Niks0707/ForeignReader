package niks.foreignreader.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import niks.foreignreader.activities.ReaderActivity;

import static niks.foreignreader.view.ReaderPageFragment.ARGUMENT_PAGE_TEXT;

/**
 * Created by Niks on 01.04.2017.
 */

public class ReaderPagerAdapter extends FragmentPagerAdapter {

    private String mText;
    private ReaderActivity mActivity;

    public ReaderPagerAdapter(ReaderActivity activity, FragmentManager fm, String text) {
        super(fm);
        mText = text;
        mActivity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        ReaderPageFragment readerPageFragment = new ReaderPageFragment();
        readerPageFragment.setActivity(mActivity);
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_PAGE_TEXT, mText);
        readerPageFragment.setArguments(arguments);

        return readerPageFragment;
    }

    @Override
    public int getCount() {
        return 10;
    }


}
