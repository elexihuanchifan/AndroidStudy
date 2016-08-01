package com.example.administrator.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.adapter.ExpandableAdapter;

/**
 * Created by Administrator on 2016/7/27.
 */
public class ExpandleActivity extends Activity {

    private ExpandableListView explv;

    private ExpandableAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandl);

        findViewByIds();

        adapter = new ExpandableAdapter(this);
        explv.setAdapter(adapter);

        explv.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        explv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });

        explv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        explv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });
    }

    private void findViewByIds() {
        explv = (ExpandableListView) findViewById(R.id.explv_test);
    }
}
