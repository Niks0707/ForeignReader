package niks.foreignreader.LibraryActivity;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Niks on 25.10.2016.
 * Array for library items. It considered item name, description,
 * image id, tag.
 */


public class LibraryItemsArray {

    private ArrayList<String> itemArray;
    private ArrayList<String> itemDescriptionArray;
    private ArrayList<Integer> imageArray;
    private ArrayList<String> itemTag;

    public LibraryItemsArray() {
        itemArray = new ArrayList<>();
        itemDescriptionArray = new ArrayList<>();
        imageArray = new ArrayList<>();
        itemTag = new ArrayList<>();
    }

    static class ViewHolder {
        public ImageView imageView;
        public TextView textViewItem;
        public TextView textViewItemDescription;
    }

    public void Add(String _item, String _itemDescripton, Integer _image) {
        this.itemArray.add(_item);
        this.itemDescriptionArray.add(_itemDescripton);
        this.imageArray.add(_image);
    }

    public void AddAll(Collection<? extends String> _itemArray,
                       Collection<? extends String> _itemDescriptionArray,
                       Collection<? extends Integer> _imageArray) {
        this.itemArray.addAll(_itemArray);
        this.itemDescriptionArray.addAll(_itemDescriptionArray);
        this.imageArray.addAll(_imageArray);
    }

    public ArrayList<String> getItemArray() {
        return itemArray;
    }

    public ArrayList<String> getItemDescriptionArray() {
        return itemDescriptionArray;
    }

    public ArrayList<Integer> getImageArray() {
        return imageArray;
    }
}
