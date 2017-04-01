package niks.foreignreader.ReaderActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

/**
 * Created by Niks on 01.04.2017.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {


    private String mText;
    private ReaderClickableSpan mReaderClickableSpan;
    private View.OnLongClickListener mOnLongClickListener;
    public MyFragmentPagerAdapter(FragmentManager fm, String text,
                                  ReaderClickableSpan readerClickableSpan,
                                  View.OnLongClickListener onLongClickListener) {
        super(fm);
        mText = text;
        mReaderClickableSpan = readerClickableSpan;
        mOnLongClickListener = onLongClickListener;
    }

    @Override
    public Fragment getItem(int position) {
        ReaderPageFragment readerPageFragment = ReaderPageFragment.newInstance(position, mText);
        readerPageFragment.setReaderClickableSpan(mReaderClickableSpan);
        readerPageFragment.setTextViewOnLongClickListener(mOnLongClickListener);
        return readerPageFragment;
    }

    @Override
    public int getCount() {
        return 10;
    }

}
