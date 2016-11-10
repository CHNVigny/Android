package com.example.tby.test2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by tby on 2016/10/30.
 */

public class myAdapter extends BaseAdapter implements AbsListView.OnScrollListener
{
    private List<Bean> beanList;
    private LayoutInflater in;
    private boolean mIsListViewIdle = true;
    private ListView mlistview;
    private ImageLoader imageLoader;
    public myAdapter(List<Bean> beanList,Context i,ListView listView,ImageLoader imageLoader){
        this.beanList=beanList;
        in= LayoutInflater.from(i);
        this.mlistview=listView;
        mlistview.setOnScrollListener(this);
        this.imageLoader=imageLoader;
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
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        int p = position;
        if (p == 0)
            return 0;
        else return 1;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type=getItemViewType(position);
        view v=null;
        view1 v1=null;
        if(convertView==null){

            switch (type){
            case 1:
                convertView=in.inflate(R.layout.listviewitem,parent,false);
                v=new view();
                v.pb= (ProgressBar) convertView.findViewById(R.id.pb1);
                v.textView1= (TextView) convertView.findViewById(R.id.tv1);
                v.textView2= (TextView) convertView.findViewById(R.id.tv2);
                v.textView3= (TextView) convertView.findViewById(R.id.tv3);
                v.iv= (ImageView) convertView.findViewById(R.id.iv);
                convertView.setTag(v);
                break;
            case 0:
                convertView=in.inflate(R.layout.item2,parent,false);
                v1=new view1();
                v1.pb= (ProgressBar) convertView.findViewById(R.id.pb21);
                v1.textView1= (TextView) convertView.findViewById(R.id.text1);
                v1.iv= (ImageView) convertView.findViewById(R.id.img1);
                convertView.setTag(v1);
                break;
            }
        }
        else{
            switch (type){
                case 1:
                    v= (view) convertView.getTag();break;
                case 0:
                    v1= (view1) convertView.getTag();break;
            }
        }
        switch (type){
            case 1:
                if(beanList.get(position).imgurl!=null) {
                    imageLoader.showImageByThreads(v.iv, beanList.get(position).imgurl, v.pb, beanList.get(position).title, beanList.get(position).width, beanList.get(position).getHeight());
                    //Log.d("imgurl",beanList.get(position).imgurl);
                }//Log.d("imgurl",beanList.get(position).imgurl);
                //setImg(v.iv,v.pb,beanList.get(position).imgurl);
                //v.iv.setImageResource(bean.get(position).imgId);
                v.textView3.setText(beanList.get(position).src);
                v.textView1.setText(beanList.get(position).title);
                v.textView2.setText(beanList.get(position).content);
                return convertView;
            case 0:
                if(beanList.get(position).imgurl!=null) {
                    imageLoader.showImageByThreads(v1.iv, beanList.get(position).imgurl, v1.pb, beanList.get(position).title, beanList.get(position).width, beanList.get(position).getHeight());
                    Log.d("imgurl",beanList.get(position).imgurl);
                }//Log.d("imgurl",beanList.get(position).imgurl);
                //setImg(v.iv,v.pb,beanList.get(position).imgurl);
                //v.iv.setImageResource(bean.get(position).imgId);
                v1.textView1.setText(beanList.get(position).title);
                return convertView;
        }
        return convertView;
        //if(mIsListViewIdle)
/*        if(beanList.get(position).imgurl!=null) {
            imageLoader.showImageByThreads(v.iv, beanList.get(position).imgurl, v.pb, beanList.get(position).title, beanList.get(position).width, beanList.get(position).getHeight());
            Log.d("imgurl",beanList.get(position).imgurl);
        }//Log.d("imgurl",beanList.get(position).imgurl);
        //setImg(v.iv,v.pb,beanList.get(position).imgurl);
        //v.iv.setImageResource(bean.get(position).imgId);
        v.textView3.setText(beanList.get(position).src);
        v.textView1.setText(beanList.get(position).title);
        v.textView2.setText(beanList.get(position).content);
        return convertView;
*/
        }

    @Override
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

    }

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
