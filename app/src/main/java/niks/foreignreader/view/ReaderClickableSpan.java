package niks.foreignreader.view;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;

public abstract class ReaderClickableSpan extends ClickableSpan {

    protected String mWord;

    public ReaderClickableSpan setWord(String word) {
        mWord = word;
        return this;
    }

    public void updateDrawState(TextPaint textPaint) {
        super.updateDrawState(textPaint);
        textPaint.setUnderlineText(false);
        textPaint.setColor(Color.BLACK);

    }
}
