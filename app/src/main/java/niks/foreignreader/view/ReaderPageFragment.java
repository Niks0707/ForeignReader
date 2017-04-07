package niks.foreignreader.view;

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
import niks.foreignreader.activities.ReaderActivity;

/**
 * Created by Niks on 28.03.2017.
 */

public class ReaderPageFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    static final String ARGUMENT_PAGE_TEXT = "arg_page_text";
    private int mPageNumber;
    private String mPageText;
    private ReaderActivity mActivity;


    public void setActivity(ReaderActivity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
        mPageText = getArguments().getString(ARGUMENT_PAGE_TEXT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reader, null);

        TextView textViewFragmentReader = (TextView) view.findViewById(R.id.textViewFragmentReader);

        textViewFragmentReader.setMovementMethod(LongClickLinkMovementMethod.getInstance());
        if (mActivity != null) {
            textViewFragmentReader.setOnLongClickListener(mActivity);
        }

        textViewFragmentReader.setText(getSpannableText(mPageText), TextView.BufferType.SPANNABLE);
        return view;
    }

    private Spannable getSpannableText(final String text) {
        final String trimedText = text.trim();
        Spannable spans = new SpannableString(trimedText);
        BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
        iterator.setText(trimedText);
        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator
                .next()) {
            String possibleWord = trimedText.substring(start, end);
            if (Character.isLetterOrDigit(possibleWord.charAt(0))) {
                ReaderClickableSpan clickSpan = new ReaderClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        mActivity.sendYandexTranslateQuery(mWord);
                    }
                };
                clickSpan.setWord(possibleWord);
                spans.setSpan(clickSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spans;
    }
}
