package niks.foreignreader.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import niks.foreignreader.LongClickLinkMovementMethod;
import niks.foreignreader.R;
import niks.foreignreader.activities.ReaderActivity;

/**
 * Created by Niks on 28.03.2017.
 */

public class ReaderPageFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    private int mPageNumber;
    private ReaderActivity mActivity;

    public static ReaderPageFragment create(ReaderActivity activity, int pageNumber) {
        ReaderPageFragment readerPageFragment = new ReaderPageFragment();
        readerPageFragment.setActivity(activity);
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, pageNumber);
        readerPageFragment.setArguments(arguments);
        return readerPageFragment;
    }


    public void setActivity(ReaderActivity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
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

        String text = mActivity.getContents(mPageNumber);

        textViewFragmentReader.setText(mActivity.getSpannable(text),
                TextView.BufferType.SPANNABLE);
        return view;
    }


}
