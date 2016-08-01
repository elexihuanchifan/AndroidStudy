package com.example.administrator.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

/**
 * Created by Administrator on 2016/7/27.
 */
public class ExpandableAdapter extends BaseExpandableListAdapter {

    private GroupHolder groupHolder;
    private ChildeHolder childeHolder;

    private Context context;

    public ExpandableAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return 10;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 10;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_group_exp, parent, false);

            groupHolder = new GroupHolder();
            groupHolder.tvId = (TextView) convertView.findViewById(R.id.tv_group_id);

            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }

        groupHolder.tvId.setText(String.valueOf(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_child_exp, parent, false);

            childeHolder = new ChildeHolder();
            childeHolder.tvId = (TextView) convertView.findViewById(R.id.tv_child_id);

            convertView.setTag(childeHolder);
        } else {
            childeHolder = (ChildeHolder) convertView.getTag();
        }

        childeHolder.tvId.setText(groupPosition + " " + childPosition);

        Animation anim = AnimationUtils.loadAnimation(context, R.anim.explv_expand_anim);
        anim.setDuration(100);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private class GroupHolder {
        private TextView tvId;
    }

    private class ChildeHolder {
        private TextView tvId;
    }
}
