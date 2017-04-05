package niks.foreignreader.ReaderActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import niks.foreignreader.LibraryActivity.LibraryActivity;
import niks.foreignreader.PersistantStorage;
import niks.foreignreader.R;
import niks.foreignreader.YandexTranslate.Language;
import niks.foreignreader.YandexTranslate.YandexTranslateAsyncQuery;

public class ReaderActivity extends AppCompatActivity {

    String textToAddFavourite;
    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        Intent intent = getIntent();
        fileName = intent.getStringExtra(LibraryActivity.FILENAME);

        //setViewPager("text");
        setTranslateScreenDefault();
        setButtonTranslate();
        setButtonToFavourite();

        findViewById(R.id.buttonClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTranslateScreenDefault();
            }
        });


        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.post(new Runnable() {
            public void run() {

                BookFileReader bookFileReader = new BookFileReader(fileName, 5000);
                String text;
                text = "Clickable words in textView ";
                text = bookFileReader.getText();

                int charactersCount = new CharactersCounter(viewPager, 24).getCount();
                text = text.substring(0, (int) (charactersCount * 0.9));
                setViewPager(text);
            }
        });
    }


    private void setViewPager(String text) {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), text);

        pagerAdapter.setReaderClickableSpan(new MyReaderClickableSpan());
        pagerAdapter.setTextViewOnLongClickListener(new MyOnLongClickListener());
        viewPager.setAdapter(pagerAdapter);

        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                Log.d("TAG", "onPageSelected, position = " + position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setButtonToFavourite() {
        findViewById(R.id.floatingActionButtonToFavourite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        Toast.makeText(v.getContext(), "Text added to Favourite", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }
        });
    }

    private void setButtonTranslate() {
        findViewById(R.id.floatingActionButtonTranslate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) findViewById(R.id.textViewFragmentReader);
                int start = textView.getSelectionStart();
                int end = textView.getSelectionEnd();
                String text = textView.getText().toString().substring(start, end);
                sendYandexTranslateQuery(text);
            }
        });
    }

    private void setTranslateScreenDefault() {
        findViewById(R.id.floatingActionButtonTranslate).setVisibility(View.INVISIBLE);
        findViewById(R.id.floatingActionButtonToFavourite).setVisibility(View.INVISIBLE);
        findViewById(R.id.frameLayoutTranslateScreen).setVisibility(View.INVISIBLE);
        ((TextView) findViewById(R.id.textViewOrigText)).setText("");
        ((TextView) findViewById(R.id.textViewTranslText)).setText("");
    }

    private void sendYandexTranslateQuery(String text) {

        YandexTranslateAsyncQuery yandexTranslateQuery =
                new YandexTranslateAsyncQuery(text, Language.ENGLISH, Language.RUSSIAN) {
                    @Override
                    protected void onPostExecute(String arg) {
                        showTranslatedText(getTranslatedText());
                    }
                };
        yandexTranslateQuery.execute();
        showTranslateScreen(text);

    }

    private void showTranslateScreen(String origText) {
        ((TextView) findViewById(R.id.textViewOrigText)).setText(origText);
        ((TextView) findViewById(R.id.textViewTranslText)).setText("");
        findViewById(R.id.marker_progress).setVisibility(View.VISIBLE);
        findViewById(R.id.frameLayoutTranslateScreen).setVisibility(View.VISIBLE);
    }

    private void showTranslatedText(String translatedText) {
        findViewById(R.id.marker_progress).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.textViewTranslText)).setText(translatedText);
        findViewById(R.id.floatingActionButtonToFavourite).setVisibility(View.VISIBLE);
        findViewById(R.id.floatingActionButtonToFavourite).setClickable(true);
        Drawable myFabSrc = getResources().getDrawable(android.R.drawable.star_big_off);
        Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();
        willBeWhite.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        ((FloatingActionButton) findViewById(R.id.floatingActionButtonToFavourite)).setImageDrawable(willBeWhite);
    }

    public class MyReaderClickableSpan extends ReaderClickableSpan {
        @Override
        public void onClick(View widget) {
            findViewById(R.id.floatingActionButtonTranslate).setVisibility(View.INVISIBLE);
            sendYandexTranslateQuery(mWord);
        }
    }

    public class MyOnLongClickListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            findViewById(R.id.floatingActionButtonTranslate).setVisibility(View.VISIBLE);
            return false;
        }
    }
}