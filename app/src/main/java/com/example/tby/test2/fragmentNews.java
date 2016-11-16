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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tby on 2016/10/30.
 */

public class fragmentNews extends Fragment {
    private String kind;
    private AlexListView listView;
    private myAdapter adapter;
    private SQLiteDatabase db;
    private List<Bean>beanList;
    private String[] title={"国内","国际","军事","互联网","体育","娱乐","社会"};
    private String[] channelId={"5572a108b3cdc86cf39001cd","5572a108b3cdc86cf39001ce","5572a108b3cdc86cf39001cf",
            "5572a108b3cdc86cf39001d1","5572a108b3cdc86cf39001d4","5572a108b3cdc86cf39001d5","5572a109b3cdc86cf39001da"};
    private ImageLoader imageLoader;

    public fragmentNews(){
        super();
        imageLoader=new ImageLoader();
    }
/*    public fragmentNews(int i){
        super();
        imageLoader=new ImageLoader();
        kind=channelId[i];
    }*/

    public fragmentNews(int i,SQLiteDatabase db){
        super();
        imageLoader=new ImageLoader(db);
        kind=channelId[i];
        this.db=db;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.news,container,false);
        //kind=getArguments().getString("kind")+"";
        listView= (AlexListView) view.findViewById(R.id.listview);

        listView.setonRefreshListener(new AlexListView.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new AsyncTask<Void, Void, Void>() { //刷新过程中需要做的操作在这里
                    boolean flash=false;
                    protected Void doInBackground(Void... params) {
                        List<Bean>beanList1=getBeanListFromNet(kind);
                        if(beanList!=null&&beanList.size()>0 && !beanList1.get(0).url.equals(beanList.get(0).url)){
                            beanList.addAll(0,beanList1);
                            flash=true;
                        }
                        return null;
                    }
                    //刷新完成后要通知listview进行界面调整
                    @Override
                    protected void onPostExecute(Void result) {

                        if(flash){

                        adapter.notifyDataSetChanged();

                            flash=false;
                        }
                        listView.onRefreshComplete();
                    }
                }.execute();

            }
        });



        if(beanList==null)init();
        return view;
    }

    private void init(){
        beanList=new ArrayList<Bean>();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (beanList == null)
                init();
            ans a = new ans(listView, this.getContext());
            a.execute(kind);
        }
    }

    List<Bean> getBeanListFromSQL(String kind) {
        List<Bean> myB = new ArrayList<Bean>();
        //db.execSQL("Drop TABLE list2");
        db.execSQL("CREATE TABLE IF NOT EXISTS list2(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " title text not null, content text not null,url text not null,src text not null,imgurl text null, kind text not null,version INTEGER not null,img BLOB) ");
        ContentValues contentValues = new ContentValues();
        Cursor c = db.rawQuery("select * from list2 where kind like '" + kind + "'", null);
        Bean newsBean = null;
        if (c != null) {
            while (c.moveToNext()) {
                newsBean = new Bean(c.getString(c.getColumnIndex("imgurl")), c.getString(c.getColumnIndex("title")), c.getString(c.getColumnIndex("content"))
                        , c.getString(c.getColumnIndex("url")), c.getString(c.getColumnIndex("src")));
                myB.add(newsBean);
                // Log.d("title",newsBean.newsTitle);
            }
            if (myB.size() > 0)
                return myB;
        }
        return null;
    }
    List<Bean> getBeanListFromNet (String kind){
            GetNewsApi getNewsApi = new GetNewsApi();
            String s = getNewsApi.request(kind);
            Log.d("getJSON", s);
            List<Bean> myB = new ArrayList<Bean>();
            try {
                JSONObject jo = new JSONObject(s);
                jo = jo.getJSONObject("showapi_res_body");
                jo = jo.getJSONObject("pagebean");
                JSONArray data = jo.getJSONArray("contentlist");
                db.execSQL("update list2 set version = 0 where version = 1");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject j1 = data.getJSONObject(i);
                    boolean haveimg = j1.getBoolean("havePic");
                    JSONArray img = j1.getJSONArray("imageurls");
                    String imgUrl = null;
                    int width;
                    String title = j1.getString("title");
                    String source = j1.getString("source");
                    String link = j1.getString("link");
                    String desc = j1.getString("desc");
                    Bean mb = new Bean(imgUrl, title,
                            desc, link, source);
                    if (haveimg) {
                        imgUrl = img.getJSONObject(0).getString("url");
                        mb.setImgurl(imgUrl);
                        mb.setWidth(img.getJSONObject(0).getInt("width"));
                        mb.setWidth(img.getJSONObject(0).getInt("height"));
                        ContentValues contentValues=new ContentValues();
                        //contentValues.put("_id",i);
                        contentValues.put("title",mb.title);
                        contentValues.put("content",mb.content);
                        contentValues.put("imgurl",mb.imgurl);
                        contentValues.put("url",mb.url);
                        contentValues.put("kind",kind);
                        contentValues.put("version",1);
                        contentValues.put("src",mb.src);
                        db.insert("list2",null,contentValues);
                        contentValues.clear();
                        myB.add(mb);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return myB;

        }
        List<Bean> getBean (String url){

            List<Bean> myB = new ArrayList<Bean>();
            String r = "";
            try {
                BufferedReader
                        br = new BufferedReader(new InputStreamReader(
                        new URL(url).openStream(), "utf-8"));
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


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return myB;

        }


    class listener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent in=new Intent(fragmentNews.this.getContext(),webView.class);
            //Log.d(position+"",beanList.get(position).url);
            Bundle b=new Bundle();
            b.putString("url",beanList.get(position-1).url);
            in.putExtras(b);
            fragmentNews.this.getContext().startActivity(in);
        }
    }

    class ans extends AsyncTask<String,Void,List<Bean>> {
        ListView lv;
        Context context;
        ans(ListView lv,  Context context){
            this.lv=lv;
            this.context=context;

        }
        @Override
        protected List<Bean> doInBackground(String... params) {
            List<Bean> bean1=getBeanListFromSQL(params[0]);
            if(bean1==null)
                bean1= getBeanListFromNet(params[0]);
            beanList=bean1;
            return bean1;
        }


        @Override
        protected void onPostExecute(List<Bean> myBeen) {
            super.onPostExecute(myBeen);
            if(listView!=null){
                adapter=new myAdapter(myBeen,context,listView,imageLoader,db);
                listView.setAdapter(adapter);
                Log.d("img","jiazai");
                listView.setOnItemClickListener(new listener());
                onResume();
            }
        }
    }




}
