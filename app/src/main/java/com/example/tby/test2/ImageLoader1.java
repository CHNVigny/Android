package com.example.tby.test2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by geyan on 2016/7/25.
 */
public class ImageLoader1 {


    private String l="";

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final long KEEP_ALIVE = 10L;
    private SQLiteDatabase db;



    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "ImageLoader#" + mCount.getAndIncrement());
        }
    };

    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), sThreadFactory);

    private static final int MESSAGE_POST_RESULT = 1;
    private final LruCache<String, Bitmap> mMemoryCache;
    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            //对象开始传入主线程中，开始处理
            LoaderResult result = (LoaderResult) msg.obj;
            ImageView imageView = result.imageView;
            String url = (String) imageView.getTag();
            if (url.equals(result.url)) {
                imageView.setImageBitmap(result.bitmap);
                result.pb.setVisibility(View.GONE);
                //Log.d("gy1", "set!"+l);
            } else {
                Log.d("gy", "when set image bitmap, but url has changed!");
            }
        }
    };

    /**
     * 在构造函数中创建LruCache，一般创建完该对象后，应该实现该对象的添加和查找逻辑。
     */
    public ImageLoader1() {
        //可使用的最大内存，转换成KB
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    public ImageLoader1(SQLiteDatabase db) {
        //可使用的最大内存，转换成KB
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
        this.db=db;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * 这边我考虑使用线程池，而不是使用线程的方法
     *
     * @param imageView
     * @param urlString
     */
    public void showImageByThreads(final ImageView imageView, final String urlString, final ProgressBar pb,final String i,final int width,final int height) {
        l=i;
        Runnable loadBitmapTask = new Runnable() {
            @Override
            public void run() {
                imageView.setTag(urlString);
                if(urlString==null)
                    return ;
                Bitmap bitmap = loadBitmap(urlString,width,height);
                if (bitmap != null) {
                    //将对应的imageView,url,bitmap封装成一个对象，然后将对象传入Handler
                    LoaderResult result = new LoaderResult(imageView, urlString, bitmap,pb);
                    mMainHandler.obtainMessage(MESSAGE_POST_RESULT, result).sendToTarget();

                }
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }

    /**
     * 这边我考虑使用线程池，而不是使用线程的方法
     *
     * @param imageView
     * @param urlString
     */
    public void showImageByThreads(final ImageView imageView, final String urlString, final ProgressBar pb,final String i) {
        l=i;
        Runnable loadBitmapTask = new Runnable() {
            @Override
            public void run() {
                imageView.setTag(urlString);
                Bitmap bitmap = loadBitmap(urlString,0,0);
                if (bitmap != null) {
                    //将对应的imageView,url,bitmap封装成一个对象，然后将对象传入Handler
                    LoaderResult result = new LoaderResult(imageView, urlString, bitmap,pb);
                    mMainHandler.obtainMessage(MESSAGE_POST_RESULT, result).sendToTarget();

                }
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }

    /**
     * 这里我使用AsyncTask来异步加载图片
     *
     * @param imageView
     * @param urlString
     */
    public void showImagebyAsyncTask(ImageView imageView, String urlString) {

        new NewsBitmapAsyncTask(imageView, urlString).execute(urlString);
    }

    class NewsBitmapAsyncTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView mImageView;
        private String mUrl;

        public NewsBitmapAsyncTask(ImageView imageView, String url) {
            this.mImageView = imageView;
            this.mUrl = url;
            mImageView.setTag(mUrl);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return loadBitmap(params[0],0,0);
        }

        //在主线程中操作，设置图片
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (mUrl.equals(mImageView.getTag())){
                mImageView.setImageBitmap(bitmap);
            }
        }
    }


    /**
     * @param urlString
     * @return
     */
    private Bitmap loadBitmap(String urlString,int width,int height) {
        Bitmap bitmap = loadBitmapFromMemoryCache(urlString);
        if (bitmap != null) {
            Log.d("gy", "loadBitmapFromMemoryCache,url:" + urlString);
            return bitmap;
        }
        else{
            bitmap = loadBitmapFromHttp(urlString,width,height);
            if(bitmap!=null)
            addBitmapToMemoryCache(hashKeyFromUrl(urlString),bitmap);
        }

        return bitmap;
    }

    //从网络中加载
    //主要的操作还是Java IO流，源：urlString，目的：Bitmap
    private Bitmap loadBitmapFromHttp(String urlString,int width,int height) {

        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("can not visit network from UI Thread.");
        }
        try {

            URL url = new URL(urlString);
            HttpURLConnection
                    urlConnection = (HttpURLConnection) url.openConnection();
            BufferedInputStream bufIn = new BufferedInputStream(urlConnection.getInputStream());
            Bitmap bitmap = //BitmapFactory.decodeStream(bufIn);
            decodeSampledBitmapFromFile(urlString,width,height,
            80, 80);

            ContentValues values = new ContentValues();
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            values.put("img",os.toByteArray());//key为字段名，value为值
            db.update("list2", values, "imgurl like ?", new String[]{urlString});

             Log.d("urlString",l+urlString);
           // Bitmap bitmap=decodeSampledBitmapFromFile(urlString,
           // 80, 80);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param imgurl
     * @param reqWidth
     * @param reqHeight
     * @return 一个设定好属性的Bitmap对象
     */
    public static Bitmap decodeSampledBitmapFromFile(String imgurl,int width,int height,
                                                     int reqWidth, int reqHeight) throws IOException {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        InputStream is;//=new URL(imgurl).openStream();
        /*options.inJustDecodeBounds = true;

        Log.d("img","jiazai");
        BitmapFactory.decodeStream(is,null, options);*/

        // Calculate inSampleSize 计算大小
        options.inSampleSize = calculateInSampleSize(height,width, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        is=new URL(imgurl).openStream();
        return BitmapFactory.decodeStream(is,null,options);
    }
    /**
     * 计算实际的采样率
     * @param reqWidth 设定的宽度
     * @param reqHeight 设定的高度
     * @return 一个压缩的比例
     */
    public static int calculateInSampleSize(int height,int width,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image

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



    //从内存中加载，使用LruCache，先将urlString做MD5编码，因为url中总是含有特殊字符
    private Bitmap loadBitmapFromMemoryCache(String urlString) {
        String key = hashKeyFromUrl(urlString);
        return getBitmapFromMemoryCache(key);
    }

    //url做MD5编码，转化成key
    private String hashKeyFromUrl(String urlString) {
        String cacheKey;
        try {
            MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(urlString.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(urlString.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    class LoaderResult {
        private ImageView imageView;
        private String url;
        private Bitmap bitmap;
        private ProgressBar pb;
        private int width,height;
        public LoaderResult(ImageView imageView, String url, Bitmap bitmap,ProgressBar pb) {
            this.imageView = imageView;
            this.url = url;
            this.bitmap = bitmap;
            this.pb=pb;
        }
    }


    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
