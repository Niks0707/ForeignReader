package niks.foreignreader.ReaderActivity;

import android.view.View;

/**
 * Created by Niks on 02.04.2017.
 */

public class CharactersCounter {

    private int mHeight;
    private int mWidth;
    private float mTextSize;

    public CharactersCounter(int height, int width, float textSize) {
        mHeight = height;
        mWidth = width;
        mTextSize = textSize;
    }

    public CharactersCounter(View view, float textSize) {
        mHeight = view.getHeight();
        mWidth = view.getWidth();
        mTextSize = textSize;
    }

    public int getCount() {
        return ((int) (mHeight / mTextSize) - 1) * ((int) (mWidth / mTextSize) - 1);
    }
}
