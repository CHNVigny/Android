package com.example.tby.test2;

/**
 * Created by tby on 2016/10/30.
 */

public class Bean {
    String imgurl=null;String title;String content;String url,src;int height;int width;
    int imgnum;
    public Bean(){
        imgurl=null;
        title = "标题";
        content="摘要";
        url=null;
        src="hit";
    }

    public void setImgnum(int n){
        imgnum=n;
    }

    public int getImgnum(){
        return imgnum;
    }

    public Bean(String imgurl,String title,String content,String url,String src){
        this.content=content;
        this.imgurl=imgurl;
        this.url=url;
        this.title=title;
        this.src=src;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }
}
