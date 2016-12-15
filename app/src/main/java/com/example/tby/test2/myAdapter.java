package com.example.tby.test2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by tby on 2016/10/30.
 */

public class myAdapter extends BaseAdapter
{
    private List<Bean> beanList;
    DisplayImageOptions options;
    private LayoutInflater in;
    private boolean mIsListViewIdle = true;
    private ListView mlistview;
    private ImageLoader imageLoader;
    private  SQLiteDatabase db;
    public myAdapter(List<Bean> beanList, Context i, ListView listView,SQLiteDatabase db){
        this.beanList=beanList;
        in= LayoutInflater.from(i);
        this.mlistview=listView;
        imageLoader = ImageLoader.getInstance();
        this.db=db;
    }
   private void init(){
       options = new DisplayImageOptions.Builder()
               .showImageOnLoading(R.drawable.newstitle) //设置图片在下载期间显示的图片
               .showImageForEmptyUri(R.drawable.newstitle)//设置图片Uri为空或是错误的时候显示的图片
               .showImageOnFail(R.drawable.newstitle)  //设置图片加载/解码过程中错误时候显示的图片
               .cacheInMemory(true)//设置下载的图片是否缓存在内存中
               .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
               .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
               .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
               .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
//.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
//设置图片加入缓存前，对bitmap进行设置
//.preProcessor(BitmapProcessor preProcessor)
               .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
               .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
               .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
               .build();//构建完成
   }

    @Override
    public int getCount() {
        return beanList.size();
    }

    @Override
    public Object getItem(int position) {
        return beanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        int p = beanList.get(position).getImgnum();
        switch (p){
            case 0:return 2;
            case 1:return 1;
            default:return 1;
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type=getItemViewType(position);
        view v=null;
        view1 v1=null;
        view2 v2=null;
        if(convertView==null){

            switch (type){
            case 1:
                convertView=in.inflate(R.layout.listviewitem,parent,false);
                v=new view();
                v.textView1= (TextView) convertView.findViewById(R.id.tv1);
                v.textView2= (TextView) convertView.findViewById(R.id.tv2);
                v.textView3= (TextView) convertView.findViewById(R.id.tv3);
                v.iv= (ImageView) convertView.findViewById(R.id.iv);
                convertView.setTag(v);
                break;
            case 0:
                convertView=in.inflate(R.layout.item2,parent,false);
                v1=new view1();
                v1.textView1= (TextView) convertView.findViewById(R.id.text1);
                v1.iv= (ImageView) convertView.findViewById(R.id.img1);
                convertView.setTag(v1);
                break;

            case 2:
                convertView=in.inflate(R.layout.itemnull,parent,false);
                v2=new view2();
                v2.textView1= (TextView) convertView.findViewById(R.id.tv1);
                v2.textView2= (TextView) convertView.findViewById(R.id.tv2);
                v2.textView3= (TextView) convertView.findViewById(R.id.tv3);
                //v1.iv= (ImageView) convertView.findViewById(R.id.img1);
                convertView.setTag(v2);
                break;
            }
        }
        else{
            switch (type){
                case 1:
                    v= (view) convertView.getTag();break;
                case 0:
                    v1= (view1) convertView.getTag();break;
                case 2:
                    v2= (view2) convertView.getTag();break;
            }
        }

        Cursor c;byte[]b;
        switch (type){
            case 1:
                c=db.rawQuery("SELECT * from list2 where imgurl like '"+beanList.get(position).imgurl+"'",null);

                if(c.moveToNext()&&(b=c.getBlob(c.getColumnIndex("img")))!=null){
                    v.iv.setImageBitmap(BitmapFactory.decodeByteArray(b,0,b.length));
                    //v.pb.setVisibility(View.GONE);
                    c.close();
                }

                //if(beanList.get(position).imgurl!=null) {
                else {
                    c.close();
                    Log.d("urlString", beanList.get(position).imgurl);
                    if (imageLoader != null)
                        imageLoader.displayImage( beanList.get(position).imgurl, v.iv);
                }
                v.textView3.setText(beanList.get(position).src);
                v.textView1.setText(beanList.get(position).title);
                v.textView2.setText(beanList.get(position).content);
                return convertView;
            case 0:
                c=db.rawQuery("SELECT * from list2 where imgurl like '"+beanList.get(position).imgurl+"'",null);
                if(c.moveToNext()&&(b=c.getBlob(c.getColumnIndex("img")))!=null){
                    v1.iv.setImageBitmap(BitmapFactory.decodeByteArray(b,0,b.length));
                    //v1.pb.setVisibility(View.GONE);
                }
                //if(beanList.get(position).imgurl!=null) {
                else
                if(imageLoader!=null)
                    imageLoader.displayImage( beanList.get(position).imgurl, v1.iv);  //setImg(v.iv,v.pb,beanList.get(position).imgurl);
                v1.textView1.setText(beanList.get(position).title);
                return convertView;
            case 2:
                v2.textView3.setText(
                        beanList.get(position).src);
                v2.textView1.setText(beanList.get(position).title);
                v2.textView2.setText(beanList.get(position).content);
                return convertView;
        }
        return convertView;

        }

 /*   @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            mIsListViewIdle = true;
            this.notifyDataSetChanged();
        } else {
            mIsListViewIdle = false;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }*/

    class view{
        public TextView textView1,textView2,textView3;
        public ProgressBar pb;
        public ImageView iv;
    }
    class view1{
        public TextView textView1;
        public ProgressBar pb;
        public ImageView iv;
    }
    class view2{
        public TextView textView1,textView2,textView3;
    }

    public void setImg(final ImageView im, final ProgressBar pb, final String url){

        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                im.setImageBitmap((Bitmap) msg.obj);
                pb.setVisibility(View.GONE);
                im.setVisibility(View.VISIBLE);
            }
        };


        new Thread(){
            @Override
            public void run() {
                super.run();
                try {

                    Bitmap bt=decodeSampledBitmapFromFile("http://qq.5068.com/uploads/allimg/121205/14061510a-1.jpg",
                    80, 80);
                            //= BitmapFactory.decodeStream(new URL("http://qq.5068.com/uploads/allimg/121205/14061510a-1.jpg").openStream());

                    Message m=Message.obtain();
                    m.obj=bt;
                    handler.sendMessage(m);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     *
     * @param imgurl
     * @param reqWidth
     * @param reqHeight
     * @return 一个设定好属性的Bitmap对象
     */
    public static Bitmap decodeSampledBitmapFromFile(String imgurl,
                                                     int reqWidth, int reqHeight) throws IOException {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream is=new URL(imgurl).openStream();
        BitmapFactory.decodeStream(is,null, options);

        // Calculate inSampleSize 计算大小
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        is.close();
        is=new URL(imgurl).openStream();
        return BitmapFactory.decodeStream(is,null,options);
    }
    /**
     * 计算实际的采样率
     * @param options
     * @param reqWidth 设定的宽度
     * @param reqHeight 设定的高度
     * @return 一个压缩的比例
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;

    }


}
