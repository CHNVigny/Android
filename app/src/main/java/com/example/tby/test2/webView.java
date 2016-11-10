package com.example.tby.test2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by tby on 2016/10/19.
 */

public class webView extends AppCompatActivity {


    private ProgressDialog dialog;
    private WebView webView1;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.web);
        url=this.getIntent().getExtras().getString("url");
        webView1 = (WebView) findViewById(R.id.web);
        Log.d("url",url);
        webView1.loadUrl(url);
       // webView1.loadUrl(url);
    // 覆盖WebView默认通过第三方或者是系统浏览器打开网页的行为，使得网页可以在WebVIew中打开
        webView1.setWebViewClient(new WebViewClient(){

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
    settings.setJavaScriptEnabled(true);
    //WebView加载页面优先使用缓存加载
    settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
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




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode== KeyEvent.KEYCODE_BACK)
        {
            //Toast.makeText(this, webView.getUrl(), Toast.LENGTH_SHORT).show();
            System.exit(0);
            if(webView1.canGoBack()&&webView1.getUrl().equals(url))
            {
                System.exit(0);
                this.finish();//返回上一页面
                return true;
            }
            else if(webView1.canGoBack()){
                webView1.goBack();this.finish();return true;
            }
            else
            {
                System.exit(0);this.finish();//退出程序
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
