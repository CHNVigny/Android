package com.example.tby.test2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.tby.test2.tool.DataBase;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by tby on 2016/10/19.
 */

public class webView extends Activity {

    private ImageButton imgb1, imgb3;
    private ImageButton  imgb2;

    private ProgressDialog dialog;
    private WebView webView1;
    private String url, title, content, src;
    boolean flag=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);

        Intent in = getIntent();
        Bundle b = in.getExtras();
        url = b.getString("url");
        src = b.getString("src");
        title = b.getString("title");
        content = b.getString("content");

        in=null;
        b=null;

        webView1 = (WebView) findViewById(R.id.web);
        //Log.d("url",url);
        webView1.loadUrl(url);
        imgb1 = (ImageButton) findViewById(R.id.imgBtnBack);
        imgb2 = (ImageButton) findViewById(R.id.imgBtnColl);
        imgb3 = (ImageButton) findViewById(R.id.imgBtnShare);
        // 覆盖WebView默认通过第三方或者是系统浏览器打开网页的行为，使得网页可以在WebVIew中打开
        webView1.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制网页在WebView中去打开，如果为false调用系统浏览器或第三方浏览器去打开
                view.loadUrl(url);
                return true;
            }
            //WebViewClient帮助WebView去处理一些页面控制和请求通知

        });
        //启用支持JavaScript
        WebSettings settings = webView1.getSettings();
        webView1.setOnLongClickListener(clicks);
        settings.setJavaScriptEnabled(true);
        setListener();
        //WebView加载页面优先使用缓存加载
        //settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView1.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                //newProgress 1-100之间的整数
                if (newProgress == 100) {
                    //网页加载完毕，关闭ProgressDialog
                    closeDialog();
                } else {
                    //网页正在加载,打开ProgressDialog
                    openDialog(newProgress);
                }
            }

            private void closeDialog() {
                // TODO Auto-generated method stub
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
            }

            private void openDialog(int newProgress) {
                // TODO Auto-generated method stub
                if (dialog == null) {
                    dialog = new ProgressDialog(webView.this);
                    dialog.setTitle("正在加载");
                    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    dialog.setProgress(newProgress);
                    dialog.show();

                } else {
                    dialog.setProgress(newProgress);
                }
            }
        });
    }
    private void setListener(){


    imgb3.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showShare();
        }
    });


    imgb2.setOnClickListener(new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            if (!flag) {
                imgb2.setBackground(getResources().getDrawable(R.drawable.collect));
                Toast.makeText(webView.this,"取消收藏",Toast.LENGTH_SHORT).show();
            } else {
                imgb2.setBackground(getResources().getDrawable(R.drawable.mycollect));
                Toast.makeText(webView.this,"已收藏",Toast.LENGTH_SHORT).show();
            }
            flag=!flag;
            new myThread(!flag).start();
           /* new AsyncTask<Void,
                    Void,Boolean>(){

                @Override
                protected Boolean doInBackground(Void... params) {
                    Cursor c=DataBase.db.rawQuery("select * from collection where url like '"+url+"'",null);
                    if(c!=null&&c.moveToNext())
                        return true;
                    return false;
                }

                @Override
                protected void onPostExecute(Boolean aVoid) {
                    flag=aVoid;
                    if (flag) {
                        imgb2.setBackground(getResources().getDrawable(R.drawable.collect));
                        Toast.makeText(webView.this,"取消收藏",Toast.LENGTH_SHORT).show();
                    } else {
                        imgb2.setBackground(getResources().getDrawable(R.drawable.mycollect));
                        Toast.makeText(webView.this,"已收藏",Toast.LENGTH_SHORT).show();
                    }
                    new myThread(!flag).start();

                    super.onPostExecute(aVoid);
                }
            }.execute();*/
        }
    });


    imgb1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    });

    }

    class myThread extends Thread{
        boolean b;
        myThread(boolean b){
            this.b=b;
        }
        public void run(){
            if(b){
                ContentValues contentValues=new ContentValues();
                contentValues.put("url",url);
                contentValues.put("src",src);
                contentValues.put("title",title);
                contentValues.put("content",content);
                DataBase.db.insert("collection",null,contentValues);
                contentValues.clear();
            }else{
                DataBase.db.delete("collection","url like ?",new String[]{url});
            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //Toast.makeText(this, webView.getUrl(), Toast.LENGTH_SHORT).show();
            //System.exit(0);
            if (webView1.canGoBack()) {
               // System.exit(0);
                finish();
                return true;
            }  else {
               // System.exit(0);
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我在看这个新闻");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }

   private View.OnLongClickListener clicks = new View.OnLongClickListener() {
       @Override
       public boolean onLongClick(View v) {
           Log.i("long", "onLongClick: ");
           showShare();
           return true;
       }
   };


}
