package niks.foreignreader.presentation.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import niks.foreignreader.R;


public class LibraryItemsArrayAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final LibraryItemsArray libraryItemsArray;
    private final int resource;

    public LibraryItemsArrayAdapter(Activity _context, int _resource, LibraryItemsArray
            _libraryItemsArray) {
        super(_context, _resource, _libraryItemsArray.getItemArray());
        this.context = _context;
        this.libraryItemsArray = _libraryItemsArray;
        this.resource = _resource;
    }

    @Override
    public View getView(int _position, View _convertView, ViewGroup parent) {
        ViewHolder holder;
        View rowView = _convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(resource, null, true);
            holder = new ViewHolder();
            holder.textViewItem = (TextView) rowView
                    .findViewById(R.id.library_listview_textView_item);
            holder.textViewItemDescription = (TextView) rowView
                    .findViewById(R.id.library_listview_textView_itemDescription);
            holder.imageView = (ImageView) rowView.findViewById(R.id.library_listview_imageView_item);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.textViewItem.setText(libraryItemsArray.getItemArray().get(
                _position));
        holder.textViewItemDescription.setText(libraryItemsArray.getItemDescriptionArray().get(
                _position));
        holder.imageView.setImageResource(libraryItemsArray.getImageArray()
                .get(_position));

        return rowView;
    }

    class ViewHolder {
        public ImageView imageView;
        public TextView textViewItem;
        public TextView textViewItemDescription;
    }
}
