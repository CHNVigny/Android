package com.example.tby.test2;

import android.annotation.SuppressLint;
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

/**
 * Created by tby on 2016/10/30.
 */
@SuppressLint("ValidFragment")
public class fragmentNews extends Fragment {
    private String kind;
    private AlexListView listView;
    private myAdapter adapter;
    private SQLiteDatabase db;
    private boolean isFirst;
    private List<Bean>beanList;
    private Handler handler;
    private String[] title={"国内","国际","军事","互联网","体育","娱乐","社会"};
    private String[] channelId={"5572a108b3cdc86cf39001cd","5572a108b3cdc86cf39001ce","5572a108b3cdc86cf39001cf",
            "5572a108b3cdc86cf39001d1","5572a108b3cdc86cf39001d4","5572a108b3cdc86cf39001d5","5572a109b3cdc86cf39001da"};
    private ImageLoader imageLoader;

    public fragmentNews(){
        super();
        isFirst=true;
        imageLoader=new ImageLoader();
    }
/*    public fragmentNews(int i){
        super();
        imageLoader=new ImageLoader();
        kind=channelId[i];
    }*/

    public fragmentNews(int i,SQLiteDatabase db){
        super();
        isFirst=true;
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



    private void refresh(){
        new AsyncTask<Void, Void,List<Bean> >() { //刷新过程中需要做的操作在这里
            boolean flash=false;
            protected List<Bean> doInBackground(Void... params) {
                List<Bean>beanList1=getBeanListFromNet(kind);

                return beanList1;

            }
            //刷新完成后要通知listview进行界面调整
            @Override
            protected void onPostExecute(List<Bean> beanList1) {
                boolean result;
                if(beanList1!=null&&beanList!=null&&beanList.size()>0 && beanList1.get(0).url.compareTo(beanList.get(0).url)!=0){
                    if(beanList.get(0).url==null)beanList.remove(0);
                    beanList.addAll(0,beanList1);
                    flash=true;
                }
                if(beanList1==null)
                    result= false;
                else result=true;

                if(!result){
                    Toast t=Toast.makeText(
                            fragmentNews.this.getContext(),
                            "网络异常",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
                if(flash){

                    adapter.notifyDataSetChanged();

                    flash=false;
                }
                listView.onRefreshComplete();
            }
        }.execute();
    }

    private void init(){
        beanList=new ArrayList<Bean>();
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
            if(s==null){
                return null;
            }
            Log.d("getJSON", s);
            List<Bean> myB = new ArrayList<Bean>();
            try {
                JSONObject jo = new JSONObject(s);
                jo = jo.getJSONObject("showapi_res_body");
                jo = jo.getJSONObject("pagebean");
                JSONArray data = jo.getJSONArray("contentlist");
                db.execSQL("update list2 set version = 0 where version = 1 and kind like '"+kind+"'");
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
                        myB.add(mb);
                        //contentValues.put("_id",i);
                    }
                    else{

                        mb.setImgnum(0);
                        myB.add(mb);
                    }
                }
                for(int i=myB.size()-1;i>=0;i--){
                    ContentValues contentValues=new ContentValues();
                    Bean mb=myB.get(i);
                    contentValues.put("title",mb.title);
                    contentValues.put("content",mb.content);
                    contentValues.put("imgurl",mb.imgurl);
                    contentValues.put("url",mb.url);
                    contentValues.put("kind",kind);
                    contentValues.put("version",1);
                    contentValues.put("src",mb.src);
                    db.insert("list2",null,contentValues);
                    contentValues.clear();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        if(myB.size()==0)
            return null;
        return myB;

        }
        List<Bean> getBean (String url){

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
            } catch(MalformedURLException e){
                    e.printStackTrace();
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
            List<Bean>bean1=getBeanListFromSQL(params[0]);
            return  bean1;
        }


        @Override
        protected void onPostExecute(List<Bean> myBeen) {
            super.onPostExecute(myBeen);
            if(beanList==null){
                beanList=new ArrayList<Bean>();
                if(myBeen!=null)
                beanList.addAll(myBeen);
            }

            if(listView!=null){
                adapter=new myAdapter(beanList,context,listView,imageLoader,db);
                listView.setAdapter(adapter);
                Log.d("img","jiazai");
                listView.setOnItemClickListener(new listener());

                if(isFirst){
                    refresh();
                    isFirst=false;
                }
                onResume();
            }
        }
    }

}
