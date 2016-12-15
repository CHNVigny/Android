package com.example.tby.test2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.tby.test2.tool.DataBase;
import com.example.tby.test2.tool.massage;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String[] title;
    private String[] title1= {"国内", "国际", "社会", "体育", "科技", "军事", "财经"};

    private ViewPager viewPager;
    private List<fragmentNews>fragList;
    private TabLayout tab1;
    private String kind="";
    Bundle savedInstanceState;
    boolean aBoolean[]=new boolean[]{false,false,false,false,false,false,false,false,false,false,false,false};
    ImageButton button;
    //private PagerTabStrip tab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.savedInstanceState=savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        imageInit();
    }

    private void change(){
        Cursor c=DataBase.db.rawQuery("select * from kind where _id=0", null);

        if(c==null){
            ContentValues contentValues=new ContentValues();
            contentValues.put("_id",0);
            contentValues.put("kind",kind);
            DataBase.db.insert("kind",null,contentValues);
        }
        else{
            if(c.moveToNext())
                kind=c.getString(c.getColumnIndex("kind"));
            c.close();
        }
        setTitle(kind);
        fragList=new ArrayList<fragmentNews>();
        List<String>tit=new ArrayList<String>();
        int i=0;
        for(String s:title1){
            if(aBoolean[i]){
                tit.add(s);
                Bundle bundle=new Bundle();
                bundle.putString("kind",s);
                fragmentNews f=new fragmentNews(i,DataBase.db);
                f.setArguments(bundle);
                //i++;
                fragList.add(f);
            }
            i++;
        }
        myPagerAdapter pagerAdapter=new myPagerAdapter(getSupportFragmentManager(),fragList,tit);
        viewPager.setAdapter(pagerAdapter);
        tab1.setupWithViewPager(viewPager);
    }

    private void init(){
        viewPager= (ViewPager) findViewById(R.id.viewPager);

        button = (ImageButton) findViewById(R.id.titleBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(button);
            }
        });

        DataBase.db=openOrCreateDatabase("bitmap.db", Context.MODE_PRIVATE, null);
        DataBase.init();
        Cursor c=DataBase.db.rawQuery("select * from kind where _id=0", null);

        if(c==null){
            ContentValues contentValues=new ContentValues();
            contentValues.put("_id",0);
            contentValues.put("kind",kind);
            DataBase.db.insert("kind",null,contentValues);
        }
        else{
            if(c.moveToNext())
                kind=c.getString(c.getColumnIndex("kind"));
            c.close();
        }
        setTitle(kind);
        //db.execSQL("Drop TABLE list2");
        tab1= (TabLayout) findViewById(R.id.tab1);
        tab1.setTabMode(TabLayout.MODE_SCROLLABLE);
        fragList=new ArrayList<fragmentNews>();
        List<String>tit=new ArrayList<String>();
        int i=0;
        for(String s:title1){
            if(aBoolean[i]){
                tit.add(s);
                Bundle bundle=new Bundle();
                bundle.putString("kind",s);
                fragmentNews f=new fragmentNews(i,DataBase.db);
                f.setArguments(bundle);
                //i++;
                fragList.add(f);
            }
            i++;
        }
        myPagerAdapter pagerAdapter=new myPagerAdapter(getSupportFragmentManager(),fragList,tit);
        viewPager.setAdapter(pagerAdapter);
        tab1.setupWithViewPager(viewPager);
    }


    @Override
    protected void onResume() {
        super.onResume();
        change();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(DataBase.db!=null){
            DataBase.db.execSQL("delete from list2 where version=0");
            Log.d("DELETE","version");
        }
    }

    private void setTitle(String kind){

        boolean g;

        if(kind.equals("")){
            g=true;
        }
        else g=false;
        for(int i=0;i<aBoolean.length;i++){
            aBoolean[i]=g;
        }
        if(g)
        return ;
        String[] kind1=kind.split("&");
        for(String s:kind1){
            aBoolean[Integer.parseInt(s)]=true;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            Bundle bundle=data.getExtras();
            kind=bundle.getString("kind");
            ContentValues contentValues=new ContentValues();
            contentValues.put("_id",0);
            contentValues.put("kind",kind);
            DataBase.db.update("kind", contentValues, "_id=0", null);
            onResume();
        }
    }

    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);

        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.main, popupMenu.getMenu());
        popupMenu.show();
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //showShare();
                Intent intent;
                int id=item.getItemId();
                switch (id) {
                    case R.id.action_collection:
                        intent=new Intent(MainActivity.this,QueryActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_choose:
                        intent=new Intent(MainActivity.this,Choose.class);
                        Bundle bundle=new Bundle();
                        massage m=new massage();
                        m.setMessage(kind);
                        bundle.putSerializable("message",m);
                        intent.putExtras(bundle);
                        startActivityForResult(intent,0);
                        break;
                    case R.id.action_simple:
                        break;
                }



                //Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        // PopupMenu关闭事
    }


    private void imageInit(){
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .memoryCacheExtraOptions(80, 80) // max width, max height，即保存的每个缓存文件的最大长宽
                //.discCacheExtraOptions(80, 80, Bitmap.CompressFormat.JPEG, 75, null) // Can slow ImageLoader, use it carefully (Better don't use it)/设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache
                        (2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100) //缓存的文件数量
                .discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();//开始构建
        ImageLoader.getInstance().init(config);//全局初始化此配置
        // Initialize ImageLoader with configuration.
    }

}
