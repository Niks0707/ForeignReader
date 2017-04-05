package niks.foreignreader.ReaderActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import static niks.foreignreader.ReaderActivity.ReaderPageFragment.ARGUMENT_PAGE_TEXT;

/**
 * Created by Niks on 01.04.2017.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {


    ReaderClickableSpan mReaderClickableSpan;
    View.OnLongClickListener mTextViewOnLongClickListener;
    private String mText;

    public MyFragmentPagerAdapter(FragmentManager fm, String text) {
        super(fm);
        mText = text;
    }

    @Override
    public Fragment getItem(int position) {
        ReaderPageFragment readerPageFragment = new ReaderPageFragment();

        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_PAGE_TEXT, mText);
        readerPageFragment.setArguments(arguments);

        readerPageFragment.setReaderClickableSpan(mReaderClickableSpan);
        readerPageFragment.setTextViewOnLongClickListener(mTextViewOnLongClickListener);
        return readerPageFragment;
    }

    @Override
    public int getCount() {
        return 10;
    }


    public void setTextViewOnLongClickListener(View.OnLongClickListener textViewOnLongClickListener) {
        mTextViewOnLongClickListener = textViewOnLongClickListener;
    }

    public void setReaderClickableSpan(ReaderClickableSpan readerClickableSpan) {
        mReaderClickableSpan = readerClickableSpan;
    }
}
