package niks.foreignreader.presentation.reader

import android.text.Spannable
import android.text.SpannableString
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.item_reader_page.view.*
import niks.foreignreader.data.model.BookPage
import niks.foreignreader.presentation.base.BaseViewHolder
import java.text.BreakIterator
import java.util.*

class ReaderPageViewHolder(itemView: View) : BaseViewHolder<BookPage>(itemView) {

    private var listener: ReaderListener? = null
    private var textSize: Float? = null

    override fun onBind(item: BookPage) {

        itemView.textViewFragmentReader.movementMethod = LongClickLinkMovementMethod.getInstance()
        itemView.textViewFragmentReader.setOnLongClickListener(listener)

        textSize?.let {
            itemView.textViewFragmentReader.setTextSize(TypedValue.COMPLEX_UNIT_PX, it)
        }

        item.content?.let {
            itemView.textViewFragmentReader.setText(getSpannable(it), TextView.BufferType.SPANNABLE)
        }
    }

    fun setListener(listener: ReaderListener): ReaderPageViewHolder {
        this.listener = listener
        return this
    }

    fun setTextSize(textSize: Float): ReaderPageViewHolder {
        this.textSize = textSize
        return this
    }

    private fun getSpannable(text: String): Spannable {
        val trimText = text.trim { it <= ' ' }
        val spans = SpannableString(trimText)
        val iterator = BreakIterator.getWordInstance(Locale.US)
        iterator.setText(trimText)
        var start = iterator.first()
        var end = iterator.next()
        while (end != BreakIterator.DONE) {
            val possibleWord = trimText.substring(start, end)
            if (Character.isLetterOrDigit(possibleWord[0])) {
                val clickSpan = object : ReaderClickableSpan() {
                    override fun onClick(widget: View) {
                        listener?.onClickSpan(this.word)
                    }
                }
                clickSpan.setWord(possibleWord)
                spans.setSpan(clickSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            start = end
            end = iterator.next()
        }
        return spans
    }
}