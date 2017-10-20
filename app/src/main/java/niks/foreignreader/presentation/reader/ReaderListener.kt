package niks.foreignreader.presentation.reader

import android.view.View

interface ReaderListener : View.OnLongClickListener {
    fun onClickSpan(word: String)
}