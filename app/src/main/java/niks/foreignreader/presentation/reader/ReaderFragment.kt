package niks.foreignreader.presentation.reader

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.text.TextPaint
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_reader.*
import kotlinx.android.synthetic.main.item_reader_page.*
import kotlinx.android.synthetic.main.view_translate_text.*
import niks.foreignreader.R
import niks.foreignreader.data.datasource.BookDataSource
import niks.foreignreader.data.model.PageSize
import niks.foreignreader.data.yandextranslate.Language
import niks.foreignreader.data.yandextranslate.YandexTranslateAsyncQuery
import niks.foreignreader.utils.SharedPrefs

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ReaderFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 */
class ReaderFragment : Fragment(),
        View.OnClickListener,
        View.OnLongClickListener,
        ReaderListener,
        YandexTranslateAsyncQuery.YandexTranslateListener {

    private var mListener: OnFragmentInteractionListener? = null

    private var textToAddFavourite: String? = null

    private var fileName: String? = null

    private var bookDataSource: BookDataSource? = null
    private var pageLength = 0
    private var textSize: Float = 0f
    private var pageSize: PageSize? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_reader, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textSize = context.resources.getDimension(R.dimen.text_size_1)
        pageSize = calculatePageSize()

        val fileName = this.fileName
        val pageSize = this.pageSize
        if (fileName != null && pageSize != null) {
            bookDataSource = BookDataSource(fileName, pageSize)
        }
        bookDataSource?.startLoad()

        initViews()
        initViewPager()
        hideProgress()
        setPageIndicator(0, bookDataSource?.getCountOfLoadedPages() ?: 0)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context?.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    fun setListener(listener: OnFragmentInteractionListener) {
        this.mListener = listener
    }

    fun setFileName(fileName: String) {
        this.fileName = fileName
    }

    private fun initViews() {
        initTranslateScreen()
    }

    private fun initTranslateScreen() {
        orig_text_textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        translated_text_text_view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        val drawable = VectorDrawableCompat.create(context.resources, R.drawable.ic_star_off, context.theme)
        to_favourite_floatingActionButton.setImageDrawable(drawable)

        close_button.setOnClickListener(this)
        to_favourite_floatingActionButton.setOnClickListener(this)
        translate_floatingActionButton.setOnClickListener(this)

        setTranslateScreenDefault()
    }

    private fun calculatePageSize(): PageSize {
        textViewForMeasure.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        val paint: TextPaint = textViewForMeasure.paint
        val wordWidth = paint.measureText("a", 0, 1)
        val screenWidth = resources.displayMetrics.widthPixels
        val charPerLine: Int = (screenWidth / wordWidth).toInt()

        val lineHeight = paint.fontMetrics.descent - paint.fontMetrics.ascent
        val lines: Int = (resources.displayMetrics.widthPixels / lineHeight).toInt()

        return PageSize(charPerLine, lines)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.close_button -> setTranslateScreenDefault()
            R.id.to_favourite_floatingActionButton -> onClickButtonToFavourite()
            R.id.translate_floatingActionButton -> onClickButtonTranslate()
        }
    }

    override fun onLongClick(v: View): Boolean {
        when (v.id) {
            R.id.textViewFragmentReader -> translate_floatingActionButton.visibility = View.VISIBLE
        }
        return false
    }

    private fun initViewPager() {

        bookDataSource?.let {
            val pagerAdapter = ReaderPagerAdapter(this, it, textSize)
            viewPager.adapter = pagerAdapter

            viewPager.setOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {

                    setPageIndicator(position, bookDataSource?.getCountOfLoadedPages() ?: 0)
                }
            })
        }
    }

    private fun onClickButtonToFavourite() {
        val origText = orig_text_textView.text.toString()
        val translatedText = translated_text_text_view.text.toString()
        if (origText !== "" && translatedText !== "") {
            if (origText !== textToAddFavourite) {
                SharedPrefs.INSTANCE.setProperty(this@ReaderFragment.context,
                        SharedPrefs.Preference.APP_PREFERENCES_BOOK_PATH,
                        translatedText)
                textToAddFavourite = origText
                val myFabSrc = ContextCompat.getDrawable(context, android.R.drawable.star_big_on)
                val willBeWhite = myFabSrc.constantState?.newDrawable()
                willBeWhite?.mutate()?.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)
                to_favourite_floatingActionButton.setImageDrawable(willBeWhite)

                to_favourite_floatingActionButton.isClickable = false
                Toast.makeText(this.context, "Text added to Favourite", Toast.LENGTH_SHORT)
                        .show()
            }
        }
    }

    override fun onClickSpan(word: String) {
        this.sendYandexTranslateQuery(word)
    }

    private fun onClickButtonTranslate() {
        val start = textViewFragmentReader.selectionStart
        val end = textViewFragmentReader.selectionEnd
        val text = textViewFragmentReader.text.toString().substring(start, end)
        sendYandexTranslateQuery(text)
    }

    private fun setTranslateScreenDefault() {
        translate_floatingActionButton.visibility = View.INVISIBLE
        to_favourite_floatingActionButton.visibility = View.INVISIBLE
        translate_screen_layout.visibility = View.INVISIBLE
        orig_text_textView.text = ""
        translated_text_text_view.text = ""
    }

    private fun sendYandexTranslateQuery(origText: String) {
        YandexTranslateAsyncQuery(this,
                origText,
                Language.ENGLISH,
                Language.RUSSIAN)
                .execute()
        showTranslateScreen(origText)
    }

    private fun showTranslateScreen(origText: String) {
        orig_text_textView.text = origText
        translated_text_text_view.text = ""
        marker_progress.visibility = View.VISIBLE
        translate_screen_layout.visibility = View.VISIBLE
    }

    override fun showTranslatedText(translatedText: String) {
        marker_progress.visibility = View.GONE
        translated_text_text_view.text = translatedText
        to_favourite_floatingActionButton.visibility = View.VISIBLE
        to_favourite_floatingActionButton.isClickable = true
        val myFabSrc = ContextCompat.getDrawable(context, android.R.drawable.star_big_off)
        val willBeWhite = myFabSrc.constantState?.newDrawable()
        willBeWhite?.mutate()?.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)
        to_favourite_floatingActionButton.setImageDrawable(willBeWhite)
    }

    private fun hideProgress() {
        progress.visibility = View.GONE
    }

    private fun setPageIndicator(position: Int, maxValue: Int) {
        pageIndicator.progress = position + 1
        pageIndicator.max = maxValue
    }

    companion object {
        const val TAG = "ReaderFragment"
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction()
    }
}