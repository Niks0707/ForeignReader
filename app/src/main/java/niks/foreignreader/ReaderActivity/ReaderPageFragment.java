package niks.foreignreader.ReaderActivity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.ClickableSpan;
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
    ReaderClickableSpan mReaderClickableSpan;
    View.OnLongClickListener mTextViewOnLongClickListener;
    int pageNumber;
    String pageText;

    {
        mTextViewOnLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        };
        mReaderClickableSpan = new ReaderClickableSpan() {

            @Override
            public void onClick(View v) {
            }

        };
    }

    static ReaderPageFragment newInstance(int page, String text) {
        ReaderPageFragment pageFragment = new ReaderPageFragment();

        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        arguments.putString(ARGUMENT_PAGE_TEXT, text);
        pageFragment.setArguments(arguments);
        return pageFragment;
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
        setTextSpannable(textViewFragmentReader, pageText);
        return view;
    }

    private void setTextSpannable(TextView textView, String text) {
        final String definition = text.trim();
        textView.setMovementMethod(LongClickLinkMovementMethod.getInstance());
        textView.setText(definition, TextView.BufferType.SPANNABLE);
        Spannable spans = (Spannable) textView.getText();

        BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
        iterator.setText(definition);
        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator
                .next()) {
            String possibleWord = definition.substring(start, end);
            if (Character.isLetterOrDigit(possibleWord.charAt(0))) {
                ClickableSpan clickSpan = mReaderClickableSpan.clone();
                mReaderClickableSpan.setWord(possibleWord);
                spans.setSpan(clickSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        textView.setOnLongClickListener(mTextViewOnLongClickListener);
    }


}
