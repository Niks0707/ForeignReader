package niks.foreignreader.presentation.reader

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan

abstract class ReaderClickableSpan : ClickableSpan() {

    protected var word: String = ""

    fun setWord(text: String): ReaderClickableSpan {
        this.word = text
        return this
    }

    override fun updateDrawState(textPaint: TextPaint) {
        super.updateDrawState(textPaint)
        textPaint.isUnderlineText = false
        textPaint.color = Color.BLACK
    }
}
