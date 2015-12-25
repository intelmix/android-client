//package com.intelmix.newzrobot.android;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import com.intelmix.newzrobot.android.data.NewsItem;
//import newzrobot.com.newzrobot.R;
//
///**
// * Created by mahdi on 12/9/15.
// */
//public class MySimpleArrayAdapter extends ArrayAdapter<NewsItem> {
//    private final Context context;
//    private final NewsItem[] values;
//
//    public MySimpleArrayAdapter(Context context, int resource, NewsItem[] items) {
//        super(context, resource, items);
//        this.context = context;
//        this.values = items;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        NewsItem p = this.getItem(position);
//
//        View v = convertView;
//
//        if (v == null) {
//            LayoutInflater vi;
//            vi = LayoutInflater.from(getContext());
//            v = vi.inflate(R.layout.itemlistrow, null);
//        }
//
//        if (p != null) {
//            TextView txt_source = (TextView) v.findViewById(R.id.news_source);
//            TextView txt_ts = (TextView) v.findViewById(R.id.news_timestamp);
//            TextView txt_title = (TextView) v.findViewById(R.id.news_title);
//
//            txt_source.setText(p.getSource());
//            txt_ts.setText(Long.toString(p.getTimestamp()));
//            txt_ts.setText(Long.toString(p.getTimestamp()));
//            txt_title.setText(p.getTitle());
//
//            v.setTag(p.getLink());
//        }
//
//        return v;
//    }
//}
