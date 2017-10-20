package niks.foreignreader.presentation.reader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import niks.foreignreader.R;
import niks.foreignreader.presentation.library.LibraryActivity;

public class ReaderActivity extends AppCompatActivity
        implements ReaderFragment.OnFragmentInteractionListener {

    private static final String TAG = "ReaderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        Intent intent = getIntent();
        String fileName = intent.getStringExtra(LibraryActivity.FILENAME);

        initReaderFragment(fileName);
    }

    private void initReaderFragment(String fileName) {
        ReaderFragment readerFragment = new ReaderFragment();
        readerFragment.setListener(this);
        readerFragment.setFileName(fileName);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.content_main, readerFragment, ReaderFragment.TAG)
                .commit();
    }

    @Override
    public void onFragmentInteraction() {

    }
}