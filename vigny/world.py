#!E:\Py_trade\pythonNews
# -*- coding: UTF-8 -*-






import xml_parser
import xml_outputer
import xml_downloader


class SpiderMain(object):
    def __init__(self):
        # self.urls = url_manager.UrlManager()
        self.downloader = xml_downloader.XmlDownloader()
        self.parser = xml_parser.XmlParser()
        self.outputer = xml_outputer.XmlOutputer()

    def craw(self, root_url):
        # count = 1
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
        try:

            # print 'craw %d : %s' % (count, new_url)
            # xml_cont = self.downloader.download(root_url)#xml_cont�����غõ�xml����
            #             print xml_cont
            #             items = self.parser.parse(xml_cont)
            items = self.parser.feed_parse(root_url)
            # self.urls.add_new_urls(new_urls)
            # self.outputer.collect_data(new_data)



        except:
            print 'craw failed'

        # self.outputer.output_xml(items)
        self.outputer.oprate_db(items, category)


if __name__ == "__main__":
    category = "world"
    urls = ["http://rss.huanqiu.com/world/roll.xml", "http://rss.sina.com.cn/news/world/focus15.xml", "http://news.baidu.com/n?cmd=1&class=internews&tn=rss", "http://news.baidu.com/n?cmd=4&class=internews&tn=rss", "http://news.qq.com/newsgj/rss_newswj.xml"]
    for root_url in urls:
        obj_spider = SpiderMain()
        obj_spider.craw(root_url)
    print "done"


