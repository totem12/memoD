package com.example.memod;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ListItem> data;
    private int resource;

    MyListAdapter(Context context, ArrayList<ListItem> data, int resource){
        this.context = context;
        this.data = data;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position){
        return  data.get(position);
    }

    @Override
    public long getItemId(int position){
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Activity activity = (Activity) context;
        ListItem item = (ListItem)getItem(position);
        if(convertView == null){
            convertView = activity.getLayoutInflater().inflate(resource, null);
        }
        ((TextView) convertView.findViewById(R.id.title)).setText(item.getTitle());
        ((TextView) convertView.findViewById(R.id.body)).setText(item.getBody());
        ((TextView) convertView.findViewById(R.id.uuid)).setText(item.getUuid());

        return convertView;
    }

    public String getId(int position){return data.get(position).getUuid();}

    public void remove(int position) {
        data.remove(position);
        this.notifyDataSetChanged();
    }
}
