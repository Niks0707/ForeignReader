package niks.foreignreader.presentation.reader

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Spannable
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_reader_page.*
import niks.foreignreader.R
import java.text.BreakIterator
import java.util.*

class ReaderPageFragment : Fragment() {

    private var pageNumber: Int? = null
    private var content: String? = null
    private var listener: ReaderListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.item_reader_page, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textViewFragmentReader.movementMethod = LongClickLinkMovementMethod.getInstance()
        if (listener != null) {
            textViewFragmentReader.setOnLongClickListener(listener)
        }

        content?.let {
            textViewFragmentReader.setText(getSpannable(it), TextView.BufferType.SPANNABLE)
        }
    }

    fun setListener(listener: ReaderListener) {
        this.listener = listener
    }

    fun setContent(pageNumber: Int, content: String) {
        this.content = content
        this.pageNumber = pageNumber

        textViewFragmentReader?.setText(getSpannable(content), TextView.BufferType.SPANNABLE)
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
                        listener?.onClickSpan(text)
//                        this@ReaderFragment.sendYandexTranslateQuery(text)
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
