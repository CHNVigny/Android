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

    //String httpArg = "channelId=5572a109b3cdc86cf39001db&channelName=%E5%9B%BD%E5%86%85%E6%9C%80%E6%96%B0&title=%E4%B8%8A%E5%B8%82&page=1&needContent=0&needHtml=0";


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
        }
        return result;
    }



}
