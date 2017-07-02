package com.example.wojtekkurylo.newsappguardianapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wojtekkurylo on 01.07.2017.
 */

public final class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> listToDisplay) {
        super(context, 0, listToDisplay);
    }

    static class ViewHolder {
        @BindView(R.id.title_name)
        TextView titleView;
        @BindView(R.id.section_name)
        TextView sectionView;
        @BindView(R.id.date)
        TextView timeView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_news, parent, false);

            holder = new ViewHolder(convertView); // to reference the child views for later actions

            // associate/ SAVE the holder with the view for later lookup
            convertView.setTag(holder);

        } else {
            // view already exists, get the holder instance from the view
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the {@link currentNews} object located at this position in the list
        News currentNews = getItem(position);

        holder.sectionView.setText(currentNews.getSection());
        holder.titleView.setText(currentNews.getTitle());
        holder.timeView.setText(currentNews.getDate());

        return convertView;
    }
}
