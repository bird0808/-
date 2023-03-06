<!--
 * @Descripttion: 
 * @Author: 只会Ctrl CV的菜鸟
 * @version: 
 * @Date: 2023-01-02 12:57:29
 * @LastEditTime: 2023-01-05 15:09:27
-->
## 1.asyncio
Asyncio,即Asynchronous I/O是python一个用来处理并发(concurrent)事件的包，通过asnyc/await来表达， 是目前很多python异步架构的基础，多用作高性能网络处理方面，很容易解决了IO-bound的问题。
## 2.协程
协程是一种用户态的轻量级线程，不同于线程。 每一个协程可以暂时挂起以便其他协程开始工作。 当一个协程在等待网络请求时，我们可以把他挂起然后去做其他的事情。

协程系统需要具备管理何时挂起不同的协程以及运行需要的协程。 这个架构多半是通过event loop实现的。

协程函数(coroutine function)是通过async/await标识的特殊函数。
协程对象(coroutine object)是协程函数返回的对象object。
协程函数区别于普通函数，不能直接运行，而需要通过asyncio封装的函数运行。协程的异步是通过event loop实现的。 协程函数可以嵌套。
```python {.line-numbers}
import asyncio
async def inner_coro(): # 定义
    return "Hello World"

async def main():
    message = await inner_coro()
    print(message)

result = asyncio.run(main()) # main()本身是一个协程
print(result)
```
async定义的函数inner_coro()直接运行会返回一个协程对象，await后面只能跟(协程对象，task，future)
通过for循环可以创建多个协程对象，然后通过run运行，run会自动创建事件循环event_loop
```python {.line-numbers}
async def func(url):
    await asyncio.sleep(2)
    print(url)

task=[func(i) for i in range(0,3)]
if __name__=='__main__':
    start=time.time()
    asyncio.run(asyncio.wait(task))
    print(time.time()-start)
```
`asyncio.run(asyncio.wait(task))`也可以使用
```python {.line-numbers}
loop=asyncio.get_event_loop()
loop.run_until_complete(asyncio.wait(task))
```
在一些需要io操作比较耗时的时候最好加上await
## 3.aiohttp
在爬虫中requests模块是一个同步模块，如果使用异步编程，不能实现异步的效果，因此需要使用aiohttp模块来实现异步网络请求
aiohttp是一个为Python提供异步HTTP 客户端/服务端编程，基于asyncio(Python用于支持异步编程的标准库)的异步库。asyncio可以实现单线程并发IO操作，其实现了TCP、UDP、SSL等协议，aiohttp就是基于asyncio实现的http框架。
1. 简单使用
    ```python {.line-numbers}
    async def fetch_async(url):
        print(url)
        async with aiohttp.request("GET",url) as r:
            reponse = await r.text(encoding="utf-8")　　#或者直接await r.read()不编码，直接读取，适合于图像等无法编码文件
            print(reponse)
    ```
2. 使用session
    ```python {.line-numbers}
    async def fetch_async(url):
        print(url)
        async with aiohttp.ClientSession() as session:　　#协程嵌套，只需要处理最外层协程即可fetch_async
            async with session.get(url) as resp:
                print(resp.status)
                print(await resp.text())　　#因为这里使用到了await关键字，实现异步，所有他上面的函数体需要声明为异步async
    ```
3. 上下文管理器
    ```python {.line-numbers}
    async with session.get(url,params=params) as r:　　#异步上下文管理器

    with open(filename,"wb") as fp:　　#普通上下文管理器
    ```
4. 自定义请求头（和requests一样）
    ```python {.line-numbers}
    async def func1(url,params,filename):
        async with aiohttp.ClientSession() as session:
            headers = {'Content-Type':'text/html; charset=utf-8'}
            async with session.get(url,params=params,headers=headers) as r:
                with open(filename,"wb") as fp:
                    while True:
                        chunk = await r.content.read(10)
                        if not chunk:
                            break
                        fp.write(chunk)
    ```
5. 自定义cookie
   对于自定义cookie，我们需要设置在ClientSession(cookies=自定义cookie字典),而不是session.get()中
   ```python {.line-numbers}
   cookies = {'cookies_are': 'working'}
   async with ClientSession(cookies=cookies) as session:
   ```
6. 获取cookie
	通过session.cookie_jar.filter_cookies(url)获取url网站下的cookie
   	```python {.line-numbers}
	for k,v in session.cookie_jar.filter_cookies(url).items():
       print(k,'=',v.value)
  	 ```
7. 异步登录的案例
   ```python {.line-numbers}
   async def login():
    y = YdmVerify()
    global loginUrl, loginApi
    async with aiohttp.ClientSession() as session:
        async with session.get(loginUrl) as result:
            response = await result.text()
            # 进行html的解析，获取图片的src
            tree = etree.HTML(response)
            path = "https://so.gushiwen.cn" + tree.xpath('//*[@id="imgCode"]/@src')[0]
        # 获取图片
        async with session.get(path) as result:
            s = await result.read()
            with open('ydm.jpg', 'wb') as f:
                f.write(s)
			# 获取验证码
            code = y.common_verify(image=s)

            data = {
                '__VIEWSTATE': 'owNL69crWb6uVywQTZ3df+9/05WyNTgbfZO08p9ENeU/LwlKRNW1ZdLwfLn4ve1ccI6730Y70XTGn832ITvLcw0joeIQZcxrszaSTOIztIeFG3KZI3fdc/2b6dhUh2J9zEeQj3LtwHIuluUXjbfeA74umtM=',
                '__VIEWSTATEGENERATOR': 'C93BE1AE',
                'from': 'http://so.gushiwen.cn/user/collect.aspx',
                'email': '160849360@qq.com',
                'pwd': 'f16881026',
                'code': code,
                'denglu': '登录'}
            headers = {
                'user-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36',
            }
        async with session.post(url=loginApi, data=data, headers=headers) as result:
            if result.status >= 200 and result.status <= 500:
                print('登录成功')
                print(result.status)
                print(session.cookie_jar.filter_cookies(loginApi))
                text = await result.text()
                with open('res.html', 'w', encoding='utf-8') as f:
                    f.write(text)
   ```
8. 使用cookie登录
   ```python {.line-numbers}
   async def useCookieLogin():
    url = 'https://so.gushiwen.cn/user/collect.aspx'
    cookie = {'gsw2017user': '3712909%7c4F47AF73FF66621E4571A0604222B892', 'gswEmail': '160849360%40qq.com',
              'gswZhanghao': '160849360%40qq.com', 'login': 'flase',
              'ticketStr': '208102432%7cgQGB8TwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyTGE2YVFGbGVkN2kxUDJCcU56MXYAAgTCmLJjAwQAjScA',
              'wsEmail': '160849360%40qq.com', 'wxopenid': 'defoaltid', 'ASP.NET_SessionId': 'lo3aeuz5iypunj12jm0qwnhb',
              'codeyzgswso': '100512f5e2539fe2'}
    async with aiohttp.ClientSession(cookies=cookie) as session:
        async with await session.get(url) as result:
            text = await result.text()
			# 打印cookie
            for k, v in session.cookie_jar.filter_cookies(url).items():
                print(k, '=', v.value)

            with open('res.html', 'w', encoding='utf-8') as f:
                f.write(text)
   ```
9. 使用示例
```python {.line-numbers}
import asyncio

import aiohttp
import requests
from lxml import etree

from ydm import YdmVerify

loginUrl = 'https://so.gushiwen.cn/user/login.aspx?from=http://so.gushiwen.cn/user/collect.aspx'
loginApi = 'https://so.gushiwen.cn/user/login.aspx?from=http%3a%2f%2fso.gushiwen.cn%2fuser%2fcollect.aspx'


async def login():
    y = YdmVerify()
    global loginUrl, loginApi
    async with aiohttp.ClientSession() as session:
        async with session.get(loginUrl) as result:
            response = await result.text()
            # 进行html的解析，获取图片的src
            tree = etree.HTML(response)
            path = "https://so.gushiwen.cn" + tree.xpath('//*[@id="imgCode"]/@src')[0]
        # 获取图片
        async with session.get(path) as result:
            s = await result.read()
            with open('ydm.jpg', 'wb') as f:
                f.write(s)
            code = y.common_verify(image=s)

            data = {
                '__VIEWSTATE': 'owNL69crWb6uVywQTZ3df+9/05WyNTgbfZO08p9ENeU/LwlKRNW1ZdLwfLn4ve1ccI6730Y70XTGn832ITvLcw0joeIQZcxrszaSTOIztIeFG3KZI3fdc/2b6dhUh2J9zEeQj3LtwHIuluUXjbfeA74umtM=',
                '__VIEWSTATEGENERATOR': 'C93BE1AE',
                'from': 'http://so.gushiwen.cn/user/collect.aspx',
                'email': '160849360@qq.com',
                'pwd': 'f16881026',
                'code': code,
                'denglu': '登录'}
            headers = {
                'user-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36',
            }
        async with session.post(url=loginApi, data=data, headers=headers) as result:
            if result.status >= 200 and result.status <= 500:
                print('登录成功')
                print(result.status)
                print(session.cookie_jar.filter_cookies(loginApi))
                text = await result.text()
                with open('res.html', 'w', encoding='utf-8') as f:
                    f.write(text)


async def useCookieLogin():
    url = 'https://so.gushiwen.cn/user/collect.aspx'
    cookie = {'gsw2017user': '3712909%7c4F47AF73FF66621E4571A0604222B892', 'gswEmail': '160849360%40qq.com',
              'gswZhanghao': '160849360%40qq.com', 'login': 'flase',
              'ticketStr': '208102432%7cgQGB8TwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyTGE2YVFGbGVkN2kxUDJCcU56MXYAAgTCmLJjAwQAjScA',
              'wsEmail': '160849360%40qq.com', 'wxopenid': 'defoaltid', 'ASP.NET_SessionId': 'lo3aeuz5iypunj12jm0qwnhb',
              'codeyzgswso': '100512f5e2539fe2'}
    async with aiohttp.ClientSession(cookies=cookie) as session:
        async with await session.get(url) as result:
            text = await result.text()
            for k, v in session.cookie_jar.filter_cookies(url).items():
                print(k, '=', v.value)

            with open('res.html', 'w', encoding='utf-8') as f:
                f.write(text)


def login1():
    global loginUrl, loginApi
    y = YdmVerify()
    # 创建session，requests.Session()和requests.session()都可以
    session = requests.Session()
    headers = {
        'user-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36',
    }
    session.headers.update(headers)
    result = session.get(loginUrl)
    response = result.text
    # 进行html的解析，获取图片的src
    tree = etree.HTML(response)
    path = "https://so.gushiwen.cn" + tree.xpath('//*[@id="imgCode"]/@src')[0]
    result = session.get(path)
    s = result.content
    with open('ydm.jpg', 'wb') as f:
        f.write(s)
    code = y.common_verify(image=s)
    data = {
        '__VIEWSTATE': 'owNL69crWb6uVywQTZ3df+9/05WyNTgbfZO08p9ENeU/LwlKRNW1ZdLwfLn4ve1ccI6730Y70XTGn832ITvLcw0joeIQZcxrszaSTOIztIeFG3KZI3fdc/2b6dhUh2J9zEeQj3LtwHIuluUXjbfeA74umtM=',
        '__VIEWSTATEGENERATOR': 'C93BE1AE',
        'from': 'http://so.gushiwen.cn/user/collect.aspx',
        'email': '160849360@qq.com',
        'pwd': 'f16881026',
        'code': code,
        'denglu': '登录'}
    # 在进行登录后，session会自动保存cookie，但是遇到301 302等重定向的时候并不一定能够保存cookie，推荐使用selenium模拟登录操作或则手动登录抓取cookie，allow_redirects=False禁止重定向
    res = session.post(url=loginApi, data=data, headers=headers)
    print(res.status_code)
    print(session.cookies.get_dict())
    print(session.headers.items())
    with open('res.html', 'w', encoding='utf-8') as f:
        f.write(res.text)


if __name__ == '__main__':
    # asyncio.run(useCookieLogin())
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    loop.run_until_complete(useCookieLogin())

```