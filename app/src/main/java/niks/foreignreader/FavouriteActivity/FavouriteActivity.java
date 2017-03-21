package niks.foreignreader.FavouriteActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import niks.foreignreader.PersistantStorage;
import niks.foreignreader.R;

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

        PersistantStorage.init(FavouriteActivity.this);
        HashMap<String, String> textAddedFavourite = PersistantStorage.getAllProperties();

        for (String key : textAddedFavourite.keySet()) {
            origText.add(key);
            translatedText.add(textAddedFavourite.get(key));
        }

        FavouriteItemsArrayAdapter adapter = new FavouriteItemsArrayAdapter(this, R.layout.favourite_list_view_item,
                origText, translatedText);

        listView.setAdapter(adapter);
    }

}
