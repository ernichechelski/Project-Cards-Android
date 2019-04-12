package com.example.ernestchechelski.projectcards;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ernest.chechelski on 9/12/2017.
 */

public class ImageGridViewAdapter<T extends ImageGridViewAdapter.ImageUrlProvider> extends ArrayAdapter {

    public interface ImageUrlProvider {
        String getImageUrl();
    }

    private Context context;
    private int layoutResourceId;
    private List<T> data = new ArrayList<>();

    public ImageGridViewAdapter(Context context, int layoutResourceId, List<T> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.image = row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        T item = data.get(position);
        Picasso.with(context).load(item.getImageUrl()).into(holder.image);
        return row;
    }

    static class ViewHolder {
        ImageView image;
    }
}