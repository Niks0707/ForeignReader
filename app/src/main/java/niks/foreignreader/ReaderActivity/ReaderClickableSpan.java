package niks.foreignreader.ReaderActivity;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public abstract class ReaderClickableSpan extends ClickableSpan implements Cloneable {

    protected String mWord;
    private View.OnLongClickListener mTextViewOnLongClickListener;

    public ReaderClickableSpan() {
        mTextViewOnLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        };
    }

    public void setWord(String word) {
        mWord = word;
    }

    public void updateDrawState(TextPaint textPaint) {
        super.updateDrawState(textPaint);
        textPaint.setUnderlineText(false);
        textPaint.setColor(Color.BLACK);

    }

    public ReaderClickableSpan clone() {
        try {
            ReaderClickableSpan readerClickableSpan = (ReaderClickableSpan) super.clone();
            return readerClickableSpan;
        } catch (CloneNotSupportedException e) {

        }
        return null;
    }

    public ReaderClickableSpan setTextViewOnLongClickListener(View.OnLongClickListener textViewOnLongClickListener) {
        this.mTextViewOnLongClickListener = textViewOnLongClickListener;
        return this;
    }

    public View.OnLongClickListener getTextViewOnLongClickListener() {
        return mTextViewOnLongClickListener;
    }
}
