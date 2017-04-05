package niks.foreignreader.ReaderActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.BreakIterator;
import java.util.Locale;

import niks.foreignreader.LongClickLinkMovementMethod;
import niks.foreignreader.R;

/**
 * Created by Niks on 28.03.2017.
 */

public class ReaderPageFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    static final String ARGUMENT_PAGE_TEXT = "arg_page_text";
    private ReaderClickableSpan mReaderClickableSpan;
    private View.OnLongClickListener mTextViewOnLongClickListener;
    int pageNumber;
    String pageText;

    {
        mReaderClickableSpan = new ReaderClickableSpan() {

            @Override
            public void onClick(View v) {
            }

        };
    }

    public void setTextViewOnLongClickListener(View.OnLongClickListener textViewOnLongClickListener) {
        mTextViewOnLongClickListener = textViewOnLongClickListener;
    }

    public void setReaderClickableSpan(ReaderClickableSpan readerClickableSpan) {
        mReaderClickableSpan = readerClickableSpan;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
        pageText = getArguments().getString(ARGUMENT_PAGE_TEXT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reader, null);

        TextView textViewFragmentReader = (TextView) view.findViewById(R.id.textViewFragmentReader);

        textViewFragmentReader.setMovementMethod(LongClickLinkMovementMethod.getInstance());
        if (mTextViewOnLongClickListener != null) {
            textViewFragmentReader.setOnLongClickListener(mTextViewOnLongClickListener);
        }

        textViewFragmentReader.setText(getSpannableText(pageText), TextView.BufferType.SPANNABLE);
        return view;
    }

    private Spannable getSpannableText(String text) {
        final String trimedText = text.trim();
        Spannable spans = new SpannableString(trimedText);
        BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
        iterator.setText(trimedText);
        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator
                .next()) {
            String possibleWord = trimedText.substring(start, end);
            if (Character.isLetterOrDigit(possibleWord.charAt(0))) {
                ReaderClickableSpan clickSpan = mReaderClickableSpan.setWord(possibleWord).clone();
                //mReaderClickableSpan.setWord(possibleWord);
                spans.setSpan(clickSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spans;
    }
}
