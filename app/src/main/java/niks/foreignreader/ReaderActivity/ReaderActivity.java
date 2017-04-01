package niks.foreignreader.ReaderActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
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
import niks.foreignreader.YandexTranslate.YandexTranslateAsyncQuery;

public class ReaderActivity extends AppCompatActivity {

    String filename;
    String text;
    String textToAddFavourite;
    BookFileReader bookFileReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        Intent intent = getIntent();
        text = "Clickable words in textView ";
        filename = intent.getStringExtra(LibraryActivity.FILENAME);
        bookFileReader = new BookFileReader(filename, 5000);
        text = bookFileReader.getNextText();
        onCreatePager();
        init();
    }


    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    private void onCreatePager() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
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

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ReaderPageFragment readerPageFragment = ReaderPageFragment.newInstance(position, text);
            readerPageFragment.setReaderClickableSpan(new ReaderClickableSpan() {
                @Override
                public void onClick(View widget) {
                    findViewById(R.id.floatingActionButtonTranslate).setVisibility(View.INVISIBLE);
                    sendYandexTranslateAsyncQuery(mWord, widget.getContext());
                }
            });
            readerPageFragment.setTextViewOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    findViewById(R.id.floatingActionButtonTranslate).setVisibility(View.VISIBLE);
                    return false;
                }
            });
            return readerPageFragment;
        }

        @Override
        public int getCount() {
            return 10;
        }

    }


    private void init() {
        setTranslateScreenDefault();

        findViewById(R.id.buttonClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTranslateScreenDefault();
            }
        });

        findViewById(R.id.floatingActionButtonToFavourite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String origText = ((TextView) findViewById(R.id.textViewOrigText)).getText().toString();
                String translatedText = ((TextView) findViewById(R.id.textViewTranslText)).getText().toString();
                if (origText != "" && translatedText != "") {
                    if (origText != textToAddFavourite) {
//                    favouriteWords.put(origText, translatedText);
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


        findViewById(R.id.floatingActionButtonTranslate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView definitionView = (TextView) findViewById(R.id.textViewFragmentReader);
                int start = definitionView.getSelectionStart();
                int end = definitionView.getSelectionEnd();
                String mText = text.substring(start, end);
                sendYandexTranslateAsyncQuery(mText, v.getContext());
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


    private void sendYandexTranslateAsyncQuery(String text, Context context) {

        ((TextView) findViewById(R.id.textViewOrigText)).setText(text);
        try {
            YandexTranslateAsyncQuery yandexTranslateQuery = new YandexTranslateAsyncQuery(text) {
                @Override
                protected void onPostExecute(String arg) {
                    try {
                        findViewById(R.id.marker_progress).setVisibility(View.GONE);
                        ((TextView) findViewById(R.id.textViewTranslText)).setText(getTranslatedText());
                        findViewById(R.id.floatingActionButtonToFavourite).setVisibility(View.VISIBLE);
                        findViewById(R.id.floatingActionButtonToFavourite).setClickable(true);
                        Drawable myFabSrc = getResources().getDrawable(android.R.drawable.star_big_off);
                        Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();
                        willBeWhite.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                        ((FloatingActionButton) findViewById(R.id.floatingActionButtonToFavourite)).setImageDrawable(willBeWhite);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            };
            yandexTranslateQuery.execute();
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT)
                    .show();
        }
        ((TextView) findViewById(R.id.textViewTranslText)).setText("");
        findViewById(R.id.marker_progress).setVisibility(View.VISIBLE);
        findViewById(R.id.frameLayoutTranslateScreen).setVisibility(View.VISIBLE);

    }


}