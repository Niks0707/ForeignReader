package niks.foreignreader.presentation;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import niks.foreignreader.R;
import niks.foreignreader.presentation.library.LibraryActivity;


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

public class MainActivity extends AppCompatActivity {

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
        //showFileDialog();
    }

    @Override
    protected void onPause() {
        super.onPause();

        /*SharedPrefs.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_BOOK_PATH, mBookPath);
        editor.putString(APP_PREFERENCES_BOOK_PROGRESS, mBookProgress);
        editor.putString(APP_PREFERENCES_TEXT_FONT, mTextFont);
        editor.putString(APP_PREFERENCES_TEXT_SIZE, mTextSize);
        editor.apply();*/
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
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
//    }

    private static final int REQUEST_APP_SETTINGS = 168;

    private static final String[] requiredPermissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 && !hasPermissions(requiredPermissions)) {
            checkPermissions();
        } else {
            loadApp();
        }
    }

    private void checkPermissions() {
        if (!hasPermissions(requiredPermissions)) {
            // Should we show an explanation?
            for (String permission : requiredPermissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
//            showExplanation("Please, enable calendar permission!");
                }
            }
            ActivityCompat.requestPermissions(this,
                    requiredPermissions,
                    REQUEST_APP_SETTINGS);


        } else {
            loadApp();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isGranted = true;
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
                break;
            }
        }
        if (isGranted) {
            loadApp();
        } else {
//            checkPermissions();
        }
    }

    public boolean hasPermissions(@NonNull String... permissions) {
        for (String permission : permissions)
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, permission))
                return false;
        return true;
    }

    private void loadApp() {

        startActivity(new Intent(MainActivity.this, LibraryActivity.class));
        finish();

    }

}
