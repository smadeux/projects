package com.chinesequiz;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LessonAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<Map<String, Object>> lessonArr;
    @Override
    public int getCount() {
        return lessonArr.size();
    }

    @Override
    public Object getItem(int position) {
        return lessonArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View vi = inflater.inflate(R.layout.item_lesson, parent, false);
        configureItem(vi, position);
        return vi;
    }

    private void configureItem(View view, int position) {
        Map<String, Object> lessonObj = lessonArr.get(position);
        TextView nameTv = view.findViewById(R.id.textView2);
        nameTv.setText((String)lessonObj.get("name"));
    }

    public LessonAdapter(Activity activity) {
        this.activity = activity;
        getLessons();
    }

    private void getLessons() {
        lessonArr = new ArrayList<>();
        Util.showProgressDialog("Loading..", activity);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference lessonRef = (CollectionReference) db.collection("lesson");
        lessonRef.orderBy("index").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Util.hideProgressDialog();
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot document: task.getResult()) {
                        Log.d("FIREBASE LESSON DATA", document.getId() + "->" + document.getData());
                        Map<String, Object> lessonObj = document.getData();
                        lessonObj.put("id", document.getId());
                        lessonArr.add(lessonObj);
                    }
                    notifyDataSetChanged();
                }
            }
        });
    }
}
