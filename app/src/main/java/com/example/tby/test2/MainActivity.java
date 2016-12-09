package com.example.tby.test2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String[] title={"国内","国际","军事","互联网","体育","娱乐","社会"};
    private SQLiteDatabase db;
    private ViewPager viewPager;
    private List<fragmentNews>fragList;
    private TabLayout tab1;
    //private PagerTabStrip tab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager= (ViewPager) findViewById(R.id.viewPager);

        final Button button = (Button) findViewById(R.id.titleBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(button);
            }
        });

        db=openOrCreateDatabase("bitmap.db", Context.MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS list2(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " title text not null, content text not null,url text not null,src text not null,imgurl text null, kind text not null,version INTEGER not null,img BLOB) ");
        if(db!=null){
            db.execSQL("delete from list2 where version=0");
            Log.d("DELETE","version");
        }
        //db.execSQL("Drop TABLE list2");
        tab1= (TabLayout) findViewById(R.id.tab1);
        tab1.setTabMode(TabLayout.MODE_SCROLLABLE);
        fragList=new ArrayList<fragmentNews>();
        List<String>tit=new ArrayList<String>();
        int i=0;
        for(String s:title){
            tit.add(s);
            Bundle bundle=new Bundle();
            bundle.putString("kind",s);
            fragmentNews f=new fragmentNews(i,db);
            f.setArguments(bundle);
            i++;
            fragList.add(f);
        }
        myPagerAdapter pagerAdapter=new myPagerAdapter(getSupportFragmentManager(),fragList,tit);
        viewPager.setAdapter(pagerAdapter);
        tab1.setupWithViewPager(viewPager);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(db!=null){
            db.execSQL("delete from list2 where version=0");
            Log.d("DELETE","version");
        }
    }

    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.main, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                Toast.makeText(getApplicationContext(), "关闭PopupMenu", Toast.LENGTH_SHORT).show();
            }
        });

        popupMenu.show();
    }

}
