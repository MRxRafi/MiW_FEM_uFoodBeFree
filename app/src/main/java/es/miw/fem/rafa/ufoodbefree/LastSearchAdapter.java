package es.miw.fem.rafa.ufoodbefree;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import es.miw.fem.rafa.ufoodbefree.models.LastSearch;

public class LastSearchAdapter extends ArrayAdapter<LastSearch> {
    public LastSearchAdapter(@NonNull Context context, int resource, List<LastSearch> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.search_item, parent, false);
        }

        TextView tvLastSearch = (TextView) convertView.findViewById(R.id.tvSearchItem);

        String message = getItem(position).getLastSearch();

        tvLastSearch.setText(message);

        return convertView;
    }
}
