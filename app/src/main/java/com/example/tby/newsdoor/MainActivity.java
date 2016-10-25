package com.example.tby.newsdoor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String[] url={"http://www.baidu.com","http://www.icourse163.org"};
    private List<myBean>bean;
    private ListView li;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //Uri u=Uri.parse("http://www.baidu.com");
        //Intent intent=new Intent(Intent.ACTION_VIEW,u);
       // startActivity(intent);

        bean=new ArrayList<myBean>();
        for(int i=0;i<20;i++){
            myBean mb=new myBean(R.mipmap.ic_launcher,
                    "我是第"+i+"行 标题",
                    "我是第"+i+"行 内容");
            bean.add(mb);
        }
        myAdapter ma=new myAdapter(bean,this);
        li= (ListView) findViewById(R.id.list1);
        li.setAdapter(ma);
        li.setOnItemClickListener(new listener());


    }

    class listener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent in=new Intent();
            in.setClass(MainActivity.this,second.class);
            Bundle bundle=new Bundle();
            bundle.putString("url",url[position%2]);
            in.putExtras(bundle);
            startActivity(in);
        }
    }


}
