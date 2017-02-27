package niks.foreignreader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.BreakIterator;
import java.util.Locale;

public class ReaderActivity extends AppCompatActivity {

    String filename;
    String text;
    String textToAddFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        Intent intent = getIntent();
        text = "Clickable words in text view ";
        filename = intent.getStringExtra(LibraryActivity.FILENAME);
        readFileSD();

        init();
    }

    private void readFile() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(filename)));
            text = "";
            String str;
            while ((str = br.readLine()) != null) {
                text += str;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void readFileSD() {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d("ERROR", "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath());
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(filename);
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new FileReader(sdFile));
            text = "";
            String str;
            while ((str = br.readLine()) != null) {
                text += str;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void init() {
        final String definition = text.trim();
        TextView definitionView = (TextView) findViewById(R.id.textView);
        definitionView.setMovementMethod(LongClickLinkMovementMethod.getInstance());
        definitionView.setText(definition, TextView.BufferType.SPANNABLE);
        Spannable spans = (Spannable) definitionView.getText();

        BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
        iterator.setText(definition);
        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator
                .next()) {
            String possibleWord = definition.substring(start, end);
            if (Character.isLetterOrDigit(possibleWord.charAt(0))) {
                ClickableSpan clickSpan = getClickableSpan(possibleWord);
                spans.setSpan(clickSpan, start, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        setTranslateScreenDefault();

        definitionView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                findViewById(R.id.floatingActionButtonTranslate).setVisibility(View.VISIBLE);
                return false;
            }
        });

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
                TextView definitionView = (TextView) findViewById(R.id.textView);
                int start = definitionView.getSelectionStart();
                int end = definitionView.getSelectionEnd();
                String mText = definition.substring(start, end);
                sendYandexTranslateAsyncQuery(mText, v.getContext());
            }
        });
    }

    private void setTranslateScreenDefault() {

        findViewById(R.id.floatingActionButtonTranslate).setVisibility(View.INVISIBLE);
        findViewById(R.id.floatingActionButtonToFavourite).setVisibility(View.INVISIBLE);

//        FrameLayout frameLayoutFAButtonTranslate = (FrameLayout) findViewById(R.id.frameLayoutFAButtonTranslate);
//        FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//        frameLayoutParams.gravity = Gravity.BOTTOM|Gravity.LEFT;
//        frameLayoutFAButtonTranslate.setLayoutParams(frameLayoutParams);

        findViewById(R.id.frameLayoutTranslateScreen).setVisibility(View.INVISIBLE);
        ((TextView) findViewById(R.id.textViewOrigText)).setText("");
        ((TextView) findViewById(R.id.textViewTranslText)).setText("");
    }

    private ClickableSpan getClickableSpan(final String word) {
        return new LongClickableSpan() {
            final String mWord;

            {
                mWord = word;
            }

            @Override
            public void onClick(View v) {
                findViewById(R.id.floatingActionButtonTranslate).setVisibility(View.INVISIBLE);
                sendYandexTranslateAsyncQuery(mWord, v.getContext());
            }

            public void onLongClick(View widget) {
            }

            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(Color.BLACK);

            }
        };
    }

    private void sendYandexTranslateAsyncQuery(String text, Context context) {
        TextView originTextView = (TextView) findViewById(R.id.textViewOrigText);
        originTextView.setText(text);
        TextView resultTextView = (TextView) findViewById(R.id.textViewTranslText);

        try {
            YandexTranslateAsyncQuery ytaQuery = new YandexTranslateAsyncQuery(text, context, resultTextView);
            ytaQuery.execute();
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT)
                    .show();
        }
        ((TextView) findViewById(R.id.textViewTranslText)).setText("");
        findViewById(R.id.marker_progress).setVisibility(View.VISIBLE);
        findViewById(R.id.frameLayoutTranslateScreen).setVisibility(View.VISIBLE);

    }

    ;


    private void showDialogResult(String word, View widget) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReaderActivity.this);
        builder.setTitle("Translate");
        TextView textView = new TextView(ReaderActivity.this);
        textView.setText(word);
        textView.setTextSize(24f);
        builder.setView(textView);
        //builder.setMessage(mWord);
        builder.setCancelable(true);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() { // Кнопка ОК
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Отпускает диалоговое окно
                    }
                });
        AlertDialog dialog = builder.create();
        String translatedText = "";

        try {
            YandexTranslateAsyncQuery ytaQuery = new YandexTranslateAsyncQuery(word, widget.getContext(), textView);
            ytaQuery.execute();
        } catch (Exception ex) {
            Toast.makeText(widget.getContext(), ex.getMessage(), Toast.LENGTH_SHORT)
                    .show();
        }

        dialog.show();
    }


    class YandexTranslateAsyncQuery extends AsyncTask<String, String, String> {


        String responseString;
        String translatedText;
        Context mContext;
        TextView mTextView;

        public YandexTranslateAsyncQuery(String text, Context context, TextView textView) {
            super();
            responseString = text;
            mContext = context;
            mTextView = textView;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressWarnings("deprecation")
        protected String doInBackground(String... args) {

            try {
                Translate.setKey(ApiKeys.YANDEX_API_KEY);

                translatedText = Translate.execute(responseString, "en", "ru");


            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }

            return null;
        }

        protected void onPostExecute(String arg) {


            try {
                findViewById(R.id.marker_progress).setVisibility(View.GONE);
                mTextView.setText(translatedText);
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
    }

}