package com.example.tby.test1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tby on 2016/10/19.
 */

public class myAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<myBean> bean;

    public myAdapter(List<myBean> b, Context i){
        inflater=LayoutInflater.from(i);
        bean=b;
    }

    @Override
    public int getCount() {
        return bean.size();
    }

    @Override
    public Object getItem(int position) {
        return bean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        view v;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item,null);
            v=new view();
            v.textView1= (TextView) convertView.findViewById(R.id.tv1);
            v.textView2= (TextView) convertView.findViewById(R.id.tv2);
            v.iv= (ImageView) convertView.findViewById(R.id.iv);
            convertView.setTag(v);
        }
        else{
            v= (view) convertView.getTag();
        }
        v.iv.setImageResource(bean.get(position).imgId);
        v.textView1.setText(bean.get(position).title);
        v.textView2.setText(bean.get(position).content);
        return convertView;
    }

    class view{
        public TextView textView1,textView2;
        public ImageView iv;
    }

}
