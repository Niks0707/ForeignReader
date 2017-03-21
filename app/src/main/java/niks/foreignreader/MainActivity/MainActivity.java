package niks.foreignreader.MainActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import niks.foreignreader.LibraryActivity.LibraryActivity;
import niks.foreignreader.R;


//TODO: Open file
//TODO: Show library
//TODO: Save library to DB
//TODO: Open book fb2, epub, txt
//TODO: Open book with formatting and style
//TODO: Add font settings
//TODO: Onclick word in text
//TODO: Save progress on device and use google account to save on another devices
//TODO: Translate word in Google Translate
//TODO: Add vocabulary
//TODO: Realize Morphologic analys of words
//TODO: Realize Fuzzy String Search.
//TODO: Realize Neural Net for Machine Translate

/**
 * Created by Niks on 20.10.2016.
 */

public class MainActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_BOOK_PATH = "book_path";
    public static final String APP_PREFERENCES_BOOK_PROGRESS = "book_progress";
    public static final String APP_PREFERENCES_TEXT_FONT = "text_font";
    public static final String APP_PREFERENCES_TEXT_SIZE = "text_size";

    private SharedPreferences mSettings;
    private String mBookPath;
    private String mBookProgress;
    private String mTextFont;
    private String mTextSize;

    private String mBookPathDefault;
    private String mBookProgressDefault;
    private String mTextFontDefault;
    private String mTextSizeDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(MainActivity.this, LibraryActivity.class));
        //showFileDialog();
    }

    @Override
    protected void onPause() {
        super.onResume();

        /*SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_BOOK_PATH, mBookPath);
        editor.putString(APP_PREFERENCES_BOOK_PROGRESS, mBookProgress);
        editor.putString(APP_PREFERENCES_TEXT_FONT, mTextFont);
        editor.putString(APP_PREFERENCES_TEXT_SIZE, mTextSize);
        editor.apply();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        if (mSettings.contains(APP_PREFERENCES_BOOK_PATH)) {
            mBookPath = mSettings.getString(APP_PREFERENCES_BOOK_PATH, mBookPathDefault);
        }
        if (mSettings.contains(APP_PREFERENCES_BOOK_PROGRESS)) {
            mBookProgress = mSettings.getString(APP_PREFERENCES_BOOK_PROGRESS, mBookProgressDefault);
        }
        if (mSettings.contains(APP_PREFERENCES_TEXT_FONT)) {
            mTextFont = mSettings.getString(APP_PREFERENCES_TEXT_FONT, mTextFontDefault);
        }
        if (mSettings.contains(APP_PREFERENCES_TEXT_SIZE)) {
            mTextSize = mSettings.getString(APP_PREFERENCES_TEXT_SIZE, mTextSizeDefault);
        }
        */
    }



}
