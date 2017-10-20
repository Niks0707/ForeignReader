package niks.foreignreader.presentation.library;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import niks.foreignreader.R;
import niks.foreignreader.presentation.adapters.LibraryItemsArray;
import niks.foreignreader.presentation.adapters.LibraryItemsArrayAdapter;
import niks.foreignreader.presentation.favourite.FavouriteActivity;
import niks.foreignreader.presentation.reader.ReaderActivity;
import niks.foreignreader.utils.OpenFileDialogAnother;

/**
 * Created by Niks on 25.10.2016.
 */

public class LibraryActivity extends AppCompatActivity {
    public static final String FILENAME = "niks.foreignreader.filename";
    private final int BOOKSHELF = 0;
    private final int FAVORITE = 1;
    private final int RECENT = 2;
    private final int OPEN_FILE = 3;
    private final int OPEN_TEST_FILE = 4;
    private final String[] FILTER_FILENAME_END_WITH = {".txt", ".fb2", ".zip.fb2", ".epub"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        setListViewItem();
    }

    private void setListViewItem() {
        ListView listView = (ListView) findViewById(R.id.activity_library_listview);
        ArrayList<Integer> list = new ArrayList<Integer>();
        //ArrayList<String> list2 = new ArrayList<String>();  //unused
        for (int i = 0; i < getResources().getStringArray(R.array.array_library_main_items).length; i++) {
            list.add(R.drawable.ic_folder);
            //list2.add(((Integer) (R.drawable.ic_cross)).toString());  //unused
        }
        LibraryItemsArray libraryItemsArray = new LibraryItemsArray();
        libraryItemsArray.AddAll(Arrays.asList(getResources().getStringArray(R.array.array_library_main_items)),
                Arrays.asList(getResources().getStringArray(R.array.array_library_main_items_description)),
                list);
        LibraryItemsArrayAdapter adapter = new LibraryItemsArrayAdapter(this, R.layout.library_listview_item,
                libraryItemsArray);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClickListLibraryItem(position);
            }
        });
        listView.setAdapter(adapter);
    }

    private void onClickListLibraryItem(int position) {
        switch (position) {
            case BOOKSHELF:
                break;
            case FAVORITE:
                Intent intent = new Intent(LibraryActivity.this, FavouriteActivity.class);
                startActivity(intent);
                break;
            case RECENT:
                break;
            case OPEN_FILE:
                showFileDialog();
                break;
            case OPEN_TEST_FILE:
                openTestFile();
                break;
            default:
                break;
        }
    }

    private void openTestFile() {
        Intent intent = new Intent(LibraryActivity.this, ReaderActivity.class);
        intent.putExtra(FILENAME, "/storage/emulated/0/read2.txt");
        startActivity(intent);
    }

    private void showFileDialog() {
        OpenFileDialogAnother openFileDialogAnother = new OpenFileDialogAnother(this);
        openFileDialogAnother.setFileIcon(this.getResources().getDrawable(R.drawable.ic_file));
        openFileDialogAnother.setFolderIcon(this.getResources().getDrawable(R.drawable.ic_folder));
        openFileDialogAnother.setFiltersEndWith(FILTER_FILENAME_END_WITH);

        openFileDialogAnother.setOpenDialogListener(new OpenFileDialogAnother.OpenDialogListener() {
            @Override
            public void OnSelectedFile(String fileName) {
                //Toast.makeText(LibraryActivity.this, fileName, Toast.LENGTH_LONG);
                Intent intent = new Intent(LibraryActivity.this, ReaderActivity.class);
                intent.putExtra(FILENAME, fileName);
                startActivity(intent);

            }
        }).show();
    }
}
