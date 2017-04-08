package niks.foreignreader.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import niks.foreignreader.BookFileReader;
import niks.foreignreader.LongClickLinkMovementMethod;
import niks.foreignreader.PersistantStorage;
import niks.foreignreader.R;
import niks.foreignreader.view.PagerTask;
import niks.foreignreader.view.ReaderClickableSpan;
import niks.foreignreader.view.ReaderPagerAdapter;
import niks.foreignreader.yandextranslate.Language;
import niks.foreignreader.yandextranslate.YandexTranslateAsyncQuery;

public class ReaderActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "ReaderActivity";
    String textToAddFavourite;
    String fileName;
    private LinearLayout mPageIndicator;
    private ProgressBar mProgressBar;
    private String mContentString = "";
    private Display mDisplay;
    private ReaderPagerAdapter mPagerAdapter;
    private Map<String, String> mPages = new HashMap<>();
    TextView textViewFragmentReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        Intent intent = getIntent();
        fileName = intent.getStringExtra(LibraryActivity.FILENAME);

        setTranslateScreenDefault();

        findViewById(R.id.buttonClose).setOnClickListener(this);
        findViewById(R.id.floatingActionButtonToFavourite).setOnClickListener(this);
        findViewById(R.id.floatingActionButtonTranslate).setOnClickListener(this);

        BookFileReader bookFileReader = new BookFileReader(fileName, 5000);
        String text;
        mContentString = bookFileReader.getText();
        ViewGroup textViewPage = (ViewGroup) getLayoutInflater().
                inflate(R.layout.fragment_reader,
                        (ViewGroup) getWindow().getDecorView().
                                findViewById(android.R.id.content), false);

        textViewFragmentReader = (TextView) textViewPage.findViewById(R.id.textViewFragmentReader);
        textViewFragmentReader.setMovementMethod(LongClickLinkMovementMethod.getInstance());
        textViewFragmentReader.setOnLongClickListener(this);

        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mDisplay = getWindowManager().getDefaultDisplay();
        ViewAndPaint vp = new ViewAndPaint(textViewFragmentReader.getPaint(),
                textViewPage,
                getScreenWidth(),
                getMaxLineCount(textViewFragmentReader),
                mContentString);


        PagerTask pagerTask = new PagerTask(this);
        pagerTask.execute(vp);
        //initViewPager(text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonClose:
                setTranslateScreenDefault();
                break;
            case R.id.floatingActionButtonToFavourite:
                onClickButtonToFavourite();
                break;
            case R.id.floatingActionButtonTranslate:
                onClickButtonTranslate();
                break;
            default:
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.textViewFragmentReader:
                findViewById(R.id.floatingActionButtonTranslate).setVisibility(View.VISIBLE);
                break;
            default:
        }
        return false;
    }


    private void initViewPager() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        mPagerAdapter =
                new ReaderPagerAdapter(this, getSupportFragmentManager(), 0);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

                showPageIndicator(position);
            }
        });
    }


    private void onClickButtonToFavourite() {
        String origText = ((TextView) findViewById(R.id.textViewOrigText)).getText().toString();
        String translatedText = ((TextView) findViewById(R.id.textViewTranslText)).getText().toString();
        if (origText != "" && translatedText != "") {
            if (origText != textToAddFavourite) {
                PersistantStorage.init(ReaderActivity.this);
                PersistantStorage.addProperty(origText, translatedText);
                textToAddFavourite = origText;
                Drawable myFabSrc = getResources().getDrawable(android.R.drawable.star_big_on);
                Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();
                willBeWhite.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                ((FloatingActionButton) findViewById(R.id.floatingActionButtonToFavourite)).setImageDrawable(willBeWhite);

                findViewById(R.id.floatingActionButtonToFavourite).setClickable(false);
                Toast.makeText(this, "Text added to Favourite", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public Spannable getSpannable(final String text) {
        final String trimText = text.trim();
        Spannable spans = new SpannableString(trimText);
        BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
        iterator.setText(trimText);
        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator
                .next()) {
            String possibleWord = trimText.substring(start, end);
            if (Character.isLetterOrDigit(possibleWord.charAt(0))) {
                ReaderClickableSpan clickSpan = new ReaderClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        ReaderActivity.this.sendYandexTranslateQuery(mWord);
                    }
                };
                clickSpan.setWord(possibleWord);
                spans.setSpan(clickSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spans;
    }


    private int getScreenWidth() {
        float horizontalMargin = getResources().getDimension(R.dimen.activity_horizontal_margin) * 2;
        int screenWidth = (int) (mDisplay.getWidth() - horizontalMargin);
        return screenWidth;
    }

    private int getMaxLineCount(TextView view) {
        float verticalMargin = getResources().getDimension(R.dimen.activity_vertical_margin) * 2;
        int screenHeight = mDisplay.getHeight();
        TextPaint paint = view.getPaint();

        //Working Out How Many Lines Can Be Entered In The Screen
        Paint.FontMetrics fm = paint.getFontMetrics();
        float textHeight = fm.top - fm.bottom;
        textHeight = Math.abs(textHeight);

        int maxLineCount = (int) ((screenHeight - verticalMargin) / textHeight);

        // add extra spaces at the bottom, remove 2 lines
        maxLineCount -= 2;

        return maxLineCount;
    }

    private void onClickButtonTranslate() {
        TextView textView = (TextView) findViewById(R.id.textViewFragmentReader);
        int start = textView.getSelectionStart();
        int end = textView.getSelectionEnd();
        String text = textView.getText().toString().substring(start, end);
        sendYandexTranslateQuery(text);
    }

    private void setTranslateScreenDefault() {
        findViewById(R.id.floatingActionButtonTranslate).setVisibility(View.INVISIBLE);
        findViewById(R.id.floatingActionButtonToFavourite).setVisibility(View.INVISIBLE);
        findViewById(R.id.frameLayoutTranslateScreen).setVisibility(View.INVISIBLE);
        ((TextView) findViewById(R.id.textViewOrigText)).setText("");
        ((TextView) findViewById(R.id.textViewTranslText)).setText("");
    }

    public void sendYandexTranslateQuery(String origText) {
        new YandexTranslateAsyncQuery(this,
                origText,
                Language.ENGLISH,
                Language.RUSSIAN)
                .execute();
        showTranslateScreen(origText);
    }

    private void showTranslateScreen(String origText) {
        ((TextView) findViewById(R.id.textViewOrigText)).setText(origText);
        ((TextView) findViewById(R.id.textViewTranslText)).setText("");
        findViewById(R.id.marker_progress).setVisibility(View.VISIBLE);
        findViewById(R.id.frameLayoutTranslateScreen).setVisibility(View.VISIBLE);
    }

    public void showTranslatedText(String translatedText) {
        findViewById(R.id.marker_progress).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.textViewTranslText)).setText(translatedText);
        findViewById(R.id.floatingActionButtonToFavourite).setVisibility(View.VISIBLE);
        findViewById(R.id.floatingActionButtonToFavourite).setClickable(true);
        Drawable myFabSrc = getResources().getDrawable(android.R.drawable.star_big_off);
        Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();
        willBeWhite.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        ((FloatingActionButton) findViewById(R.id.floatingActionButtonToFavourite))
                .setImageDrawable(willBeWhite);
    }


    public void onPageProcessedUpdate(ProgressTracker progress) {
        mPages = progress.pages;
        // init the pager if necessary
        if (mPagerAdapter == null) {
            initViewPager();
            hideProgress();
        } else {
            mPagerAdapter.incrementPageCount();
        }
        addPageIndicator(progress.totalPages);
    }

    private void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    private void addPageIndicator(int pageNumber) {
        mPageIndicator = (LinearLayout) findViewById(R.id.pageIndicator);
        View view = new View(this);
        ViewGroup.LayoutParams params = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
        view.setLayoutParams(params);
        view.setBackgroundDrawable(getResources().getDrawable(pageNumber == 0 ? R.drawable.current_page_indicator : R.drawable.indicator_background));
        view.setTag(pageNumber);
        mPageIndicator.addView(view);
    }

    protected void showPageIndicator(int position) {
        try {
            mPageIndicator = (LinearLayout) findViewById(R.id.pageIndicator);
            View selectedIndexIndicator = mPageIndicator.getChildAt(position);
            selectedIndexIndicator.setBackgroundDrawable(getResources().getDrawable(R.drawable.current_page_indicator));
            // dicolorize the neighbours
            if (position > 0) {
                View leftView = mPageIndicator.getChildAt(position - 1);
                leftView.setBackgroundDrawable(getResources().getDrawable(R.drawable.indicator_background));
            }
            if (position < mPages.size()) {
                View rightView = mPageIndicator.getChildAt(position + 1);
                rightView.setBackgroundDrawable(getResources().getDrawable(R.drawable.indicator_background));
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public String getContents(int pageNumber) {
        String page = String.valueOf(pageNumber);
        String textBoundaries = mPages.get(page);
        if (textBoundaries != null) {
            String[] bounds = textBoundaries.split(",");
            int startIndex = Integer.valueOf(bounds[0]);
            int endIndex = Integer.valueOf(bounds[1]);
            return mContentString.substring(startIndex, endIndex).trim();
        }
        return "";
    }

    public static class ViewAndPaint {

        public ViewGroup textViewPage;
        public TextPaint paint;
        public int screenWidth;
        public int maxLineCount;
        public String contentString;

        public ViewAndPaint(TextPaint paint, ViewGroup textViewPage, int screenWidth, int maxLineCount, String contentString) {
            this.paint = paint;
            this.textViewPage = textViewPage;
            this.maxLineCount = maxLineCount;
            this.contentString = contentString;
            this.screenWidth = screenWidth;
        }
    }

    public static class ProgressTracker {

        public int totalPages;
        public Map<String, String> pages = new HashMap<String, String>();

        public void addPage(int page, int startIndex, int endIndex) {
            String thePage = String.valueOf(page);
            String indexMarker = String.valueOf(startIndex) + "," + String.valueOf(endIndex);
            pages.put(thePage, indexMarker);
        }
    }
}