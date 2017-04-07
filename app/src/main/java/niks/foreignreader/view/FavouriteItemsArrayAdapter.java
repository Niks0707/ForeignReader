package niks.foreignreader.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import niks.foreignreader.R;


public class FavouriteItemsArrayAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> origText;
    private final ArrayList<String> translatedText;
    private final int resource;


    public FavouriteItemsArrayAdapter(Activity _context, int _resource, ArrayList<String> _origText,
                                      ArrayList<String> _translatedText) {

        super(_context, _resource, _origText);
        this.context = _context;
        this.origText = _origText;
        this.translatedText = _translatedText;
        this.resource = _resource;
    }

    @Override
    public View getView(int _position, View _convertView, ViewGroup _parent) {
        ViewHolder holder;
        View rowView = _convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(resource, null, true);
            holder = new ViewHolder();
            holder.textViewItem = (TextView) rowView
                    .findViewById(R.id.textViewFavouriteOrigText);
            holder.textViewItemDescription = (TextView) rowView
                    .findViewById(R.id.textViewFavouriteTranslText);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.textViewItem.setText(origText.get(
                _position));
        holder.textViewItemDescription.setText(translatedText.get(
                _position));
        return rowView;
    }
    static class ViewHolder {
        public TextView textViewItem;
        public TextView textViewItemDescription;
    }
}

