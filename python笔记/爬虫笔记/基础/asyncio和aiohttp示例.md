<!--
 * @Descripttion: 
 * @Author: 只会Ctrl CV的菜鸟
 * @version: 
 * @Date: 2023-01-15 20:11:46
 * @LastEditTime: 2023-02-07 16:08:46
-->


## 示例为通过异步进行小说的爬取，首先是同步获取小说每一章的链接，然后才通过有序的链接进行异步爬取每一个章节
```python {.line-numbers}
import asyncio
import time

import aiohttp
from lxml import etree
import requests
from threading import Thread

headers1 = {
    'user-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36',
}

count = 0
# 用来控制并发量
semaphore = asyncio.Semaphore(32)
# 目录的url
list_url='https://www.bbiquge.net/book/24881/'
# 目录页的最大数量
catalogue_max_pages=25
# 小说名字
novel_name='name'
def getChapterUrl():
    """
    利用requests同步方式顺序获取小说章节的url
    :return:章节链接的列表
    """
    # 小说章节目录的url
    urls = [f'{list_url}index_{index}.html' for index in range(1, catalogue_max_pages+1)]
    chapter_urls = []

    session = requests.session()
    for url in urls:
        # print(url)
        res = session.get(url, headers=headers1)
        tree = etree.HTML(res.text)
        global novel_name
        novel_name=str(tree.xpath('//*[@id="info"]/h1/text()')[0])
        for link in tree.xpath('//dd//a/@href'):
            chapter_urls.append(list_url + link)
        print('download:',url)
    session.close()
    return chapter_urls


async def getNovel(url: str):
    """
    从章节链接中提取小说
    :param url:
    :param session:
    :return:
    """
    # 通过async with来控制并发量为semaphore规定的数量
    async with semaphore:
        # 如果复用同一个session，实测发现后面的章节的下载会卡住
        async with aiohttp.ClientSession(headers=headers1) as session:
            async with session.get(url) as result:
                # 获取response
                response = await result.text(encoding='GB2312', errors='ignore')
                # 解析出标题和小说正文
                tree = etree.HTML(response)

                title = tree.xpath('//*[@id="main"]/h1/text()')
                text = tree.xpath('//*[@id="content"]/text()')

                global count
                count += 1
                
                data = []
                data.append(str(title[0]))
                for i in text:
                    text=str(i)
                    text=text.replace('笔趣阁 www.bbiquge.net，最快更新','')
                    text=text.replace('最新章节！','')
                    text=text.replace('    ','')
                    data.append(text)

                print(f'下载成功：{title[0]}', count)
                return '\n'.join(data)


async def getAllNovel(urls):
    task = []
    for i in urls:
        task.append(asyncio.create_task(getNovel(i)))
    # gather方法会根据task的顺序，返回有序的结果，asyncio.wait()返回的则是无序的结果
    results = await asyncio.gather(*task)
    return results


async def main():
    url = 'https://www.bbiquge.net/book/133312/56524592.html'
    urls = getChapterUrl()

    data = await getAllNovel(urls)
    
    with open(f'../file/{novel_name}.txt', 'w',encoding="Utf-8") as f:
        for chapter in data:
            f.write(chapter)
            f.write('\n')
       
if __name__ == '__main__':
    start = time.time()
    
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    loop.run_until_complete(main())

    end = time.time()
    print(f'total time: {end-start:.2f}s')

```
## 下面是获取章节链接也通过异步的案例
```python {.line-numbers}
import asyncio
import time
import aiohttp
from lxml import etree
import requests

headers1 = {
    'user-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36',
}

count = 0
# 用来控制并发量
semaphore = asyncio.Semaphore(32)
# 目录的url
catalogue_url= 'https://www.bbiquge.net/book/30/'

# 目录页的最大数量
catalogue_max_pages=51
# 小说名字
novel_name='name'

async def asyncGetChapterUrl(url, session):
    async with semaphore:
        async with session.get(url) as result:
            response=await result.text(errors='ignore')
            tree = etree.HTML(response)
            urls=[]
            for i in tree.xpath("//dd//a/@href"):
                urls.append(catalogue_url + i)
            print('获取章节链接:',url)
            return urls

async def asyncGetAllCatalogueUrl():
    urls = [f'{catalogue_url}index_{index}.html' for index in range(1, catalogue_max_pages + 1)]
    task=[]
    # 复用session避免会话多次建立消耗性能
    async with aiohttp.ClientSession(headers=headers1) as session:
        for url in urls:
            task.append(asyncio.create_task(asyncGetChapterUrl(url, session)))
        results=await asyncio.gather(*task)
        data=results
        data1 = []
        for i in data:
            for j in i:
                data1.append(j)
    return data1

async def getNovelByOneSession(url: str,session):
    """
    从章节链接中提取小说
    :param url:
    :return:
    """
    # 通过async with来控制并发量为semaphore规定的数量
    async with semaphore:
        # 复用session避免会话多次建立消耗性能
        async with session.get(url) as result:
            # 获取response
            response = await result.text(encoding='GB2312', errors='ignore')
            # 解析出标题和小说正文
            tree = etree.HTML(response)

            title = tree.xpath('//*[@id="main"]/h1/text()')
            text = tree.xpath('//*[@id="content"]/text()')

            global count
            count += 1

            data = []
            if len(title)>0:
                data.append(str(title[0]))
            else:
                print('title is null',url)
            for i in text:
                text=str(i)
                text=text.replace('笔趣阁 www.bbiquge.net，最快更新','')
                text=text.replace('最新章节！','')
                text=text.replace('    ','')
                data.append(text)

            if len(title) > 0:
                print(f'下载成功：{title[0]}', count)
                return '\n'.join(data)



async def asyncGetAllNovelByOneSession(urls):
    task = []
    async with aiohttp.ClientSession(headers=headers1) as session:
        for i in urls:
            task.append(asyncio.create_task(getNovelByOneSession(i,session)))
        # gather方法会根据task的顺序，返回有序的结果，asyncio.wait()返回的则是无序的结果
        results = await asyncio.gather(*task)
    return results

async def main():
    url = 'https://www.bbiquge.net/book/133312/56524592.html'

    urls = await asyncGetAllCatalogueUrl()

    data = await asyncGetAllNovelByOneSession(urls)

    with open(f'../file/{novel_name}.txt', 'w',encoding="Utf-8") as f:
        for chapter in data:
            f.write(chapter)
            f.write('\n')

def getNovelMessage(url):
    res=requests.get(url,headers=headers1)
    tree=etree.HTML(res.text)
    global catalogue_max_pages
    catalogue_max_pages=len(tree.xpath('/html/body/div[4]/div/select//option'))
    global novel_name
    novel_name=str(tree.xpath('//*[@id="info"]/h1/text()')[0])

if __name__ == '__main__':
    start = time.time()
    print('你将要爬取的网站:https://www.bbiquge.net/')
    catalogue_url=input('从中选择一本小说,输入小说目录第一页链接:')
    getNovelMessage(catalogue_url)
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    loop.run_until_complete(main())
    end = time.time()
    print(f'total time: {end-start:.2f}s')
```

