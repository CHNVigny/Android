package com.example.tby.test2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by tby on 2016/10/30.
 */
public class fragmentNews extends Fragment {
    private String kind;
    private AlexListView listView;
    private myAdapter adapter;
    private SQLiteDatabase db;
    private boolean isFirst;
    private List<Bean> beanList;
    private Handler handler;
    String status="1";
    int inum;
    private String[] title = {"国内", "国际", "社会", "体育", "科技", "军事", "财经"};

    public fragmentNews() {
        super();
        isFirst = true;
    }

    public fragmentNews(int i, SQLiteDatabase db) {
        super();
        kind=i+"";
        isFirst = true;
        this.inum = i;
        this.db = db;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.news, container, false);
        //kind=getArguments().getString("kind")+"";
        listView = (AlexListView) view.findViewById(R.id.listview);
        listView.setOnItemLongClickListener(new listenerLong());

            listView = (AlexListView) view.findViewById(R.id.listview);
            ans a = new ans(listView, this.getContext());
            a.execute(kind);
            listView.setonRefreshListener(new AlexListView.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refresh();
                }
            });
        return view;
    }


    private void refresh() {
        new AsyncTask<Void, Void, List<Bean>>() { //刷新过程中需要做的操作在这里
            boolean flash = false;

            protected List<Bean> doInBackground(Void... params) {
                List<Bean> beanList1 = getBeanListFromNet(kind);

                return beanList1;

            }

            //刷新完成后要通知listview进行界面调整
            @Override
            protected void onPostExecute(List<Bean> beanList1) {
                boolean result;
                if (beanList1 != null && beanList != null && beanList.size() > 0 &&(beanList.get(0).url==null|| beanList1.get(0).url.compareTo(beanList.get(0).url) != 0)){
                    if (beanList.get(0).url == null) beanList.remove(0);
                    beanList.addAll(0, beanList1);
                    flash = true;
                }
                if (beanList1 == null)
                    result = false;
                else result = true;

                if (!result) {
                    Toast t = Toast.makeText(
                            fragmentNews.this.getContext(),
                            "网络异常",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
                if (flash) {

                    adapter.notifyDataSetChanged();

                    flash = false;
                }
                listView.onRefreshComplete();
            }
        }.execute();
    }


    private void init() {
        beanList = new ArrayList<Bean>();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

            super.setUserVisibleHint(isVisibleToUser);

    }

    List<Bean> getBeanListFromSQL(String kind) {
        List<Bean> myB = new ArrayList<Bean>();

        ContentValues contentValues = new ContentValues();
        Cursor c = db.rawQuery("select * from list2 where kind like '" + kind + "' order by _id DESC", null);
        Bean newsBean = null;
        if (c != null) {
            while (c.moveToNext()) {
                status=c.getString(c.getColumnIndex("status"));
                newsBean = new Bean(c.getString(c.getColumnIndex("imgurl")), c.getString(c.getColumnIndex("title")), c.getString(c.getColumnIndex("content"))
                        , c.getString(c.getColumnIndex("url")), c.getString(c.getColumnIndex("src")));
                newsBean.setImgnum(c.getInt(c.getColumnIndex("imgnum")));
                myB.add(newsBean);
                // Log.d("title",newsBean.newsTitle);
            }
            if (myB.size() > 0)
                return myB;
        }
        return null;
    }

    List<Bean> getBeanListFromNet(String kind) {
        boolean f=true;
        GetNewsApi getNewsApi = new GetNewsApi();
        String s = getNewsApi.request(status,inum+"");
        if (s == null) {
            return null;
        }
        Log.d("getJSON", s);
        List<Bean> myB = new ArrayList<Bean>();
        try {
            JSONObject jo = new JSONObject(s.replace(" ",""));
            Long statu = jo.getLong("status");
            status=""+statu;
            JSONArray data = jo.getJSONArray("data");
            db.execSQL("update list2 set version = 0 where version = 1 and kind like '" + kind + "'");
            for (int i = 0; i < data.length(); i++) {
                JSONObject j1 = data.getJSONObject(i);
                boolean haveimg = j1.getBoolean("havepic");

                String imgUrl = null;
                int width;
                String title = j1.getString("title");
                String source = j1.getString("source");

                String link = j1.getString("link");
                String desc = j1.getString("desc");
                Bean mb = new Bean(imgUrl, title,
                        desc, link, source);
                if (haveimg) {
                    JSONArray img = j1.getJSONArray("imgurl");
                    imgUrl = img.getJSONObject(0).getString("url");
                    mb.setImgurl(imgUrl);
                    mb.setImgnum(1);
                    if(f){
                        myB.add(0,mb);
                        f=false;
                    }
                    else
                        myB.add(mb);
                    //contentValues.put("_id",i);
                } else {

                    mb.setImgnum(0);
                    myB.add(mb);
                }
            }
            for (int i = myB.size() - 1; i >= 0; i--) {
                ContentValues contentValues = new ContentValues();
                Bean mb = myB.get(i);
                contentValues.put("imgnum", mb.imgnum);
                contentValues.put("title", mb.title);
                contentValues.put("content", mb.content);
                contentValues.put("imgurl", mb.imgurl);
                contentValues.put("url", mb.url);
                contentValues.put("kind", kind);
                contentValues.put("version", 1);
                contentValues.put("src", mb.src);
                contentValues.put("status",status);
                db.insert("list2", null, contentValues);
                contentValues.clear();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (myB.size() == 0)
            return null;
        return myB;

    }

    List<Bean> getBean(String url) {

        List<Bean> myB = new ArrayList<Bean>();
        String r = "";


        try {
            URL u = new URL(url);
            BufferedReader
                    br = new BufferedReader(new InputStreamReader(
                    u.openStream(), "utf-8"));
            String line;
            while ((line = br.readLine()) != null) {
                r += line;
            }
            Log.d("abc", r);
            JSONObject jo = new JSONObject(r);
            JSONArray data = jo.getJSONArray("data");
            String[] s = {"http://www.baidu.com", "http://www.sina.com"};
            for (int i = 0; i < data.length(); i++) {
                jo = data.getJSONObject(i);
                Bean mb = new Bean(jo.getString("picSmall"), jo.getString("name"),
                        jo.getString("description"), s[i % 2], "imooc");
                myB.add(mb);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return myB;

    }


    class listener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent in = new Intent(fragmentNews.this.getContext(), webView.class);
            //Log.d(position+"",beanList.get(position).url);
            Bundle b = new Bundle();
            b.putString("url", beanList.get(position - 1).url);
            b.putString("title", beanList.get(position - 1).title);
            b.putString("src", beanList.get(position - 1).src);
            b.putString("content", beanList.get(position - 1).content);
            in.putExtras(b);
            fragmentNews.this.getContext().startActivity(in);
        }
    }

    class ans extends AsyncTask<String, Void, List<Bean>> {
        ListView lv;
        Context context;

        ans(ListView lv, Context context) {
            this.lv = lv;
            this.context = context;
        }

        @Override
        protected List<Bean> doInBackground(String... params) {
            List<Bean> bean1 = getBeanListFromSQL(params[0]);
            return bean1;
        }


        @Override
        protected void onPostExecute(List<Bean> myBeen) {
            super.onPostExecute(myBeen);
            if (beanList == null) {
                beanList = new ArrayList<Bean>();
                if (myBeen != null)
                    beanList.addAll(myBeen);
                else{
                    beanList.add(new Bean());
                }
            }
            if (listView != null) {
                adapter = new myAdapter(beanList, context, listView, db);
                listView.setAdapter(adapter);
                Log.d("img", "jiazai");
                listView.setOnItemClickListener(new listener());
                if (isFirst) {
                    if(inum==5)
                        inum=5;
                    refresh();
                    isFirst = false;
                }
                adapter.notifyDataSetChanged();
//调用方法通知UI线程界面数据发生化
                listView.refreshDrawableState();
            }


        }


    }

    class listenerLong implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            showPopupMenu(view, position);

            return false;
        }
    }

        public void insert(int position){

        }
    private void showPopupMenu(View view, final int position) {


        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(fragmentNews.this.getContext(), view);

        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.main1, popupMenu.getMenu());
        popupMenu.show();
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //showShare();
                Intent intent;
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_collect:
                        Toast.makeText(fragmentNews.this.getContext(), "已收藏", Toast.LENGTH_SHORT).show();

                        Cursor c = db.rawQuery("select * from collection where url like '" + beanList.get(position - 1).url + "' order by _id DESC", null);
                        if (c.moveToNext()) {
                            c.close();
                            break;
                        } else {
                            new myThread(position).start();
                            break;
                        }
                    case R.id.action_share:
                        showShare(position);
                        break;
                }


                //Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        // PopupMenu关闭事
    }


    private void showShare(int position) {
        OnekeyShare oks = new OnekeyShare();

        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(beanList.get(position).url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我在看这个新闻\n"+beanList.get(position).url);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        //oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("我在看这个新闻");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("此处是评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(beanList.get(position).src);
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(beanList.get(position).url);

// 启动分享GUI
        oks.show(this.getContext());
    }


    class myThread extends Thread{
        int position;
        myThread(int position){
            this.position=position;
        }

        public void run(){
            ContentValues contentValues = new ContentValues();
            contentValues.put("url", beanList.get(position - 1).url);
            contentValues.put("src", beanList.get(position - 1).src);
            contentValues.put("title", beanList.get(position - 1).title);
            contentValues.put("content", beanList.get(position - 1).content);
            db.insert("collection", null, contentValues);
        }


    }


  /*  @Override
    public void onDestroyView() {
        super.onDestroyView();

        if(mPropsFragment != null){
            FragmentManager f = getFragmentManager();
            if(f != null && !f.isDestroyed()){
                final FragmentTransaction ft = f.beginTransaction();
                if(ft != null){
                    ft.remove(mPropsFragment).commit();
                }
            }
        }
    }*/
}
