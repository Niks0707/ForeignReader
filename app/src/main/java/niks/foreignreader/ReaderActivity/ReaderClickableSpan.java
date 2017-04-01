package niks.foreignreader.ReaderActivity;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;

public abstract class ReaderClickableSpan extends ClickableSpan implements Cloneable {

    protected String mWord;

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
}
