package niks.foreignreader.presentation.favourite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import niks.foreignreader.R;
import niks.foreignreader.presentation.adapters.FavouriteItemsArrayAdapter;

public class FavouriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        setListViewItem();
    }

    private void setListViewItem() {
        ListView listView = (ListView) findViewById(R.id.favourite_list_view);


        ArrayList<String> origText = new ArrayList<>();
        ArrayList<String> translatedText = new ArrayList<>();

//        SharedPrefs.init(FavouriteActivity.this);
        HashMap<String, String> textAddedFavourite = new LinkedHashMap<>();//SharedPrefs.getAllProperties();

        for (String key : textAddedFavourite.keySet()) {
            origText.add(key);
            translatedText.add(textAddedFavourite.get(key));
        }

        FavouriteItemsArrayAdapter adapter = new FavouriteItemsArrayAdapter(this, R.layout.favourite_list_view_item,
                origText, translatedText);

        listView.setAdapter(adapter);
    }

}
