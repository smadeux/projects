package com.chinesequiz;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ToneAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<String> toneArr;

    @Override
    public int getCount() {
        return toneArr.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View vi;


        Object object = toneArr.get(position);
        if(object instanceof HashMap) {

            HashMap<String, Object> toneObj = (HashMap<String, Object>) object;
            if(toneObj.get("type").equals("0")) {
                vi = inflater.inflate(R.layout.item_tone_title, parent, false);
                TextView nameTv = vi.findViewById(R.id.textView4);
                nameTv.setText((String)toneObj.get("name"));
            }
            else {
                vi = inflater.inflate(R.layout.item_tone, parent, false);
                TextView nameTv = vi.findViewById(R.id.textView2);
                nameTv.setText((String)toneObj.get("name"));
            }
        }
        else {
            vi = inflater.inflate(R.layout.item_tone, parent, false);
            TextView nameTv = vi.findViewById(R.id.textView2);
            nameTv.setText((String)object);
        }

        return vi;
    }

    public ToneAdapter(Activity activity, Map<String, Object> subLessonObj) {
        this.activity = activity;
        toneArr = (ArrayList<String>) subLessonObj.get("tones");
    }
}
