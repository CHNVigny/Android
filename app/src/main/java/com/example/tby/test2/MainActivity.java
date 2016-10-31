package com.example.tby.test2;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String[] title={"要闻","军事","娱乐","搞笑","天气"};
    private ViewPager viewPager;
    private List<fragmentNews>fragList;
    private TabLayout tab1;
    //private PagerTabStrip tab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager= (ViewPager) findViewById(R.id.viewPager);

        /*tab= (PagerTabStrip) findViewById(R.id.tab);
        tab.setTabIndicatorColor(Color.BLUE);
        tab.setDrawFullUnderline(true);
        tab.setTextColor(Color.RED);*/
        tab1= (TabLayout) findViewById(R.id.tab1);
        tab1.setTabMode(TabLayout.MODE_SCROLLABLE);
        fragList=new ArrayList<fragmentNews>();

        List<String>tit=new ArrayList<String>();
        for(String s:title){
            tit.add(s);
            Bundle bundle=new Bundle();
            bundle.putString("kind",s);

            fragmentNews f=new fragmentNews();
            f.setArguments(bundle);
            fragList.add(f);
        }
        myPagerAdapter pagerAdapter=new myPagerAdapter(getSupportFragmentManager(),fragList,tit);
        viewPager.setAdapter(pagerAdapter);
        tab1.setupWithViewPager(viewPager);

    }
}
