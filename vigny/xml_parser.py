#!/usr/bin/env python2
# -*- coding: UTF-8 -*-


import xml.etree.ElementTree as ET
import img_url_get
import feedparser
import re



class XmlParser(object):

    def __init__(self):
        img = img_url_get.get_img_url()
        self.img = img



    
    def parse(self, xml_cont):
        
        items=[]
        root = ET.fromstring(xml_cont)
        channel = root.find('channel')
        for item in channel.findall('item'):
            try:
                title_raw = item.find('title').text
                title = title_raw.replace("\n","").replace("\t","")
                compare = cmp(title,"")
                if compare==0 :
                    title = '-'

                    
            except:
                title = '-'
            
#             print title
            try:
                link = item.find('link').text
            except:
                link = '-'
            # print link
            try:
                author = item.find('author').text
            except:
                author = '-'
            try:
                category = item.find('category').text
            except:
                category = '-'
            try:
                pubDate = item.find('pubDate').text
            except:
                pubDate = '-'
            try:
                description_raw = item.find('description').text
                description = description_raw.replace("\n","").replace("\t","")
            except:
                description = '-'
#             print description
            try:
                img_urls_raw = self.img.get_url_from_desc(description)
                img_urls = ','.join(img_urls_raw)
            except:
                try:
                    img_urls_raw = self.img.get_url_from_web(link, author)
                    img_urls = ','.join(img_urls_raw)
                except:
                    img_urls = '-'
            # print img_urls
            dic = {"title" : title, "link" : link, "author" : author, "category" : category, "pubDate" : pubDate, "description" : description, "img_url" : img_urls}
            items.append(dic)    
        return items

    # use feedparser to parse RSS
    def feed_parse(self, url):
        items = []
        d = feedparser.parse(url)
        for e in d.entries:
            try:
                title_raw = e.title
                title = title_raw.replace("\n", "").replace("\t", "")
                compare = cmp(title, "")
                if compare == 0:
                    title = '-'


            except:
                title = '-'

                #             print title
            try:
                link = e.link
            except:
                link = '-'
            # print link
            try:
                author = e.author
            except:
                author = '-'
            try:
                category = e.category
            except:
                category = '-'
            try:
                pubDate = e.published
            except:
                pubDate = '-'
            try:
                # description_raw = item.find('description').text
                description_raw = e.description
                description_raw_re = description_raw.replace("\n", "").replace("\t", "")
                dr = re.compile(r'<[^>]+>', re.S)
                description = dr.sub('', description_raw_re)
            except:
                description = '-'
                #             print description
            try:
                img_urls_raw = self.img.get_url_from_desc(description_raw_re)
                if img_urls_raw:
                    img_urls = ','.join(img_urls_raw)
                else:
                    try:
                        img_urls_raw = self.img.get_url_from_web(link, author)
                        img_urls = ','.join(img_urls_raw)
                    except:
                        img_urls = '-'
            except:
                img_urls = '-'
            # print img_urls
            dic = {"title": title, "link": link, "author": author, "category": category, "pubDate": pubDate,
                   "description": description, "img_url": img_urls}
            items.append(dic)
        return items
            