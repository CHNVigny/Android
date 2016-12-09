package com.example.tby.test2;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tby on 2016/11/2.
 */

public class GetNewsApi {

    /**

     * @return 返回结果
     */
    public static String request(String kind) {
        String httpArg = "page=1&needContent=0&needHtml=0&needAllList=0";
        if(!kind.equals("")){
            httpArg ="channelId="+kind+"&"+ httpArg;
        }
        Log.d("httpArg",httpArg);
        String httpUrl = "http://apis.baidu.com/showapi_open_bus/channel_news/search_news";


        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            //URL url = new URL("http://192.168.0.2:8080/hello/refresh?status=122&kind=1");
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey",  "57a8924efc96882b140d7ffabb7c1dad");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

}
