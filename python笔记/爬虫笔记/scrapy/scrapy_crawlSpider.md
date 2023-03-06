<!--
 * @Descripttion: 
 * @Author: 只会Ctrl CV的菜鸟
 * @version: 
 * @Date: 2023-01-10 21:25:17
 * @LastEditTime: 2023-01-12 16:49:07
-->
# crawlSpider
该类是继承自scrapy.Spider的子类，主要用来规则化爬取
## 1.创建一个规则化爬虫
通过命令`scrapy genspider -t crawl name domains` 可以创建继承crawlSpider的爬虫类,name是爬虫的名字，domains是域名
```python {.line-numbers}
class CrawlTestSpider(CrawlSpider):
    name = 'crawlTest'
    allowed_domains = ['www.xxx.com']
    start_urls = ['http://www.xxx.com/']

    rules = (
        Rule(LinkExtractor(allow=r'Items/'), callback='parse_item', follow=True),
    )

    def parse_item(self, response):
        item = {}
        #item['domain_id'] = response.xpath('//input[@id="sid"]/@value').get()
        #item['name'] = response.xpath('//div[@id="name"]').get()
        #item['description'] = response.xpath('//div[@id="description"]').get()
        return item
```
- 在上面，有一个rules元组，里面存放多个**Rule（链接解析规则）**，其中有个**LinkExtractor**，它是一个链接提取器，可以指定链接提取的规则，可以通过正则、css、xpath等进行解析。**注意正则匹配的是链接的正则，不是对整个response的文本的正则匹配，也就是说，这个正则表达式只对链接进行匹配。**<font color=red size=3>所以这里推荐用xpath进行链接提取规则的匹配，同时链接提取器异步进行提取，返回的链接全是乱序的</font>
  
- Rule中指定了回调方法为pasrse_item，<font color=red size=3>同时注意不能重写parse方法，否则会解析出错</font>。

- follow参数为True则表示对提取到的连接进行深度解析，**即对提取出来的链接的response再次用链接提取器定义的规则进行再次提取。**

- 在parse_item方法中，response是提取出来的链接的response，在本方法中可以进行数据解析并保存。

- 如果想要对提取到的链接返回的response再次进行另一规则的链接提取，可以在rules加入额外的Rule