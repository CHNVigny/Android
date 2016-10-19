package com.example.tby.test1;

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
    int [] arr=new int[]{
            R.drawable.address_book,R.drawable.calendar,R.drawable.camera,R.drawable.clock,
            R.drawable.games_control,R.drawable.ic_launcher,R.drawable.messenger,R.drawable.ringtone,
            R.drawable.settings,R.drawable.speech_balloon,R.drawable.weather,R.drawable.youtube
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //Uri u=Uri.parse("http://www.baidu.com");
        //Intent intent=new Intent(Intent.ACTION_VIEW,u);
       // startActivity(intent);

        bean=new ArrayList<myBean>();
        for(int i=0;i<arr.length;i++){
            myBean mb=new myBean(arr[i],
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
