#!/usr/bin/env python2
# -*- coding: UTF-8 -*-

import re
import requests
from bs4 import BeautifulSoup
import chardet


class switch(object):
    def __init__(self, value):
        self.value = value
        self.fall = False

    def __iter__(self):
        """Return the match method once, then stop"""
        yield self.match
        raise StopIteration

    def match(self, *args):
        """Indicate whether or not to enter a case suite"""
        if self.fall or not args:
            return True
        elif self.value in args:  # changed for v1.5, see below
            self.fall = True
            return True
        else:
            return False


class get_img_url(object):
    def __init__(self):
        pass
#         self.html_downloader=html_downloader.HtmlDownloader()
    
    def htmldownload(self, link, encoding):
        res=requests.get(link)
        res.encoding = encoding
        return res.text

    def get_url_from_web(self, link, author):
        urls = []
        if link is None:
            return
#         print link



        for case in switch(author):
            if case("www.qq.com"):
                try:

                    html_cont = self.htmldownload(link, "gb2312")
                    # print html_cont
                    soup = BeautifulSoup(html_cont, 'html.parser')
                    nodes = soup.find('div', class_="qq_article").find_all('p')
                    for node in nodes:
                        img_node = node.find('img')
                        if img_node is None:
                            continue
                        url = img_node['src']
                        if url.find("data:image/png;base64,") != -1:
                            continue
                        urls.append(url)
                except:
                    try:
                        nodes = soup.find('div', class_="bd").find_all('p')
                        for node in nodes:
                            img_node = node.find('img')
                            if img_node is None:
                                continue
                            url = img_node['src']
                            if url.find("data:image/png;base64,") != -1:
                                continue
                            urls.append(url)
                    except:
                        return '-'
                        break
                    return urls
                    break
                    #         for node in nodes:
                    #             url = node['src']
                    #             urls.append(url)
                return urls
                break
            if case("WWW.SINA.COM.CN"):
                try:
                    html_cont = self.htmldownload(link, "utf-8")
                    # print html_cont
                    soup = BeautifulSoup(html_cont, 'html.parser')
                    nodes = soup.find('div', class_="article article_16").find_all('div')
                    for node in nodes:
                        img_node = node.find('img')
                        url = img_node['src']
                        if url.find("data:image/png;base64"):
                            continue
                        urls.append(url)
                except:
                    return '-'
                    break
                    #         for node in nodes:
                    #             url = node['src']
                    #             urls.append(url)
                return urls
                break
            if case():  # default
                return '-'
                break
        
    def get_url_from_desc(self, description):
        reg = r'img\s+.*?src="?(.+?\.jpg)"?'
        imgre = re.compile(reg)
        imglist = imgre.findall(description)
        '''
        for imgurl in imglist:
             urllib.urlretrieve(imgurl, '%s.jpg' % x)
            if x==2:
                break;
            else:
                x = x + 1
        '''
        return imglist
    
        