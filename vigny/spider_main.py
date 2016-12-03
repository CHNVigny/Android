#!E:\Py_trade\pythonNews
# -*- coding: UTF-8 -*-

# 爬虫调度端

## URL管理器

### 添加新的URL到待爬取集合中
### 判断待添加URL是否在容器中
### 获取待爬取URL
### 判断是否还有待爬取URL
### 将URL从待爬取移动到已爬取

## 网页下载器
### urllib2
### requests

## 网页解析器

### 正则表达式
### html.parser
### BeautifulSoup
### lxml


## 分析目标
### URL格式
### 数据格式
### 网页编码





import xml_parser
import xml_outputer
import xml_downloader


class SpiderMain(object):

    def __init__(self):
        #self.urls = url_manager.UrlManager()
        self.downloader = xml_downloader.XmlDownloader()
        self.parser = xml_parser.XmlParser()
        self.outputer = xml_outputer.XmlOutputer()



    def craw(self, root_url):
        #count = 1
        """
        self.urls.add_new_url(root_url)
        while self.urls.has_new_url():
            try :
                new_url = self.urls.get_new_url()
                print 'craw %d : %s' % (count, new_url)
                html_cont = self.downloader.download(new_url)
                new_urls, new_data = self.parser.parse(new_url, html_cont)
                self.urls.add_new_urls(new_urls)
                self.outputer.collect_data(new_data)


                if count == 100:
                    break
                count = count + 1
            except:
                print 'craw failed'
                
        """
        try :
            
            #print 'craw %d : %s' % (count, new_url)
            xml_cont = self.downloader.download(root_url)#xml_cont是下载好的xml内容
#             print xml_cont
            items = self.parser.parse(xml_cont)
            #self.urls.add_new_urls(new_urls)
            #self.outputer.collect_data(new_data)


            
        except:
            print 'craw failed'

        # self.outputer.oprate_db(items)
        self.outputer.output_xml(items)

    
        
if __name__ == "__main__":
    news_categoties = ["china","world","society"]
    for news_categotie in news_categoties:
        # root_url = "http://rss.sina.com.cn/news/%s/focus15.xml" % news_categotie
        root_url = "http://www.nbweekly.com/rss/smw/"
        obj_spider = SpiderMain()
        obj_spider.craw(root_url)
    print "done"
    
