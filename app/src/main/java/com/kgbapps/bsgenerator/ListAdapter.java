package com.kgbapps.bsgenerator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;

public class ListAdapter extends ArrayAdapter<Phrase> {
    public ListAdapter(Context context, List<Phrase> phrases) {
        super(context, 0, phrases);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Phrase phrase = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.itemTextView);
        textView.setText(phrase.toString());
        return convertView;
    }
}
