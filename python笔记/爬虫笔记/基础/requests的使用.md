# 一、requests的基本使用
1. 实现get请求
   ```python
   url = "https://search.jd.com/Search?"
    data={
        'keyword':'手机'
    }
    headers={
        'user-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36',
        'cookie': ' __jdu=61421975; areaId=20; shshshfpa=ab7d0ab7-99f4-2c80-c19b-93f2127c0a92-1668090517; shshshfpb=fT2sX_3xKiNxm6l3rURZkcg; qrsc=3; ipLoc-djd=20-1726-22884-51455; unpl=JF8EAJVnNSttCEtcBR9VTBVCGFkAW1VfTx8BPWdXAAgNTFMGEgIYEkR7XlVdXhRLFx9vYBRUWlNKVQ4ZBysSEXteU11bD00VB2xXXAQDGhUQR09SWEBJJVhXW1QKSxUDa2EMZG1bS2QFGjIcEhVIXVxYWThKJwRfVzVQW1BJUTUaMhoiUh8BU1daDksUTmhnAFddUE1QNRoyGA; CCC_SE=ADC_my9s5e2JW1kkpWL5xMcpEZWEHu9KKFZg48%2bXHIIphVUAzsHAVYsopjNm3EKLBws%2fuMrfsHG4oN3AuKmhWlum8HaEwxZ85zejAo%2bud5D95VINV3RLRw6%2fSfsGAhptp3cYlGuL6fDEltxdASQ8KkIehZK4ad6AlxSQDR65I7inX0TLPBWDyf%2b6kRCvZzOnn85w%2b9LbSfC1b9%2baLbedM%2bZ4Ro36VAumfwmHrwMjcMyvfaiLWriMVGc7%2bQ%2bQizV26%2bMLkfkvZLwXEPbg6kgy1u35KlLkM7%2fIT9WuEeytiHbE%2buWkVYDgzUsE4KXcR70Rsa3xABfFnL8a6K0i9Kya3HG6joYJhNhUerQLjTjI%2fdhhCKykhIRX2zRAiPoXJ08WvC%2fEb6WNNOVfNrOBuGuwxuNofNMTtOHqfQS97hkmA44htFvTR6ZQNEKtxtMt0CX4ykFn2NngkHKM8SupK1v5mrQqgEYM9OmvX%2fD76HIqg5OuC2TCK0Vmu8UCIwAxChL0GNDP; __jdv=76161171|haosou-search|t_262767352_haosousearch|cpc|42483131578_0_a1915ff6cb5e49f593c1cedd6628121e|1668585634483; PCSYCityID=CN_450000_450300_0; __jda=122270672.61421975.1662811853.1668262238.1668585634.4; __jdc=122270672; jsavif=0; jsavif=0; shshshfp=41378840cb33a1cd74bca5ff309a4f04; rkv=1.0; shshshsID=b9b8fcac59a9ca8b5ece7ea124a33b73_4_1668585722270; __jdb=122270672.7.61421975|4.1668585634; wlfstk_smdl=j2cmfymiohfinq54r86k4ra5quafkda3; 3AB9D23F7A4B3C9B=GR7R6LVTRRL6WAVMHP3TZI24KM5A5XM6SXZBP6XIPORSQDMFKWAW63AV7UIDEWBESJHWIQGCLIXBA6JMEY7GEKBAZU'
    }
    # 发起get请求
    response = requests.get(url, params=data,headers=headers)
    # 获取返回体的文本
    text = response.text
    print(response.json())
    ```
    请求头headers和参数data是不一样的，params=headers是不对的
2. requests 方法如下表:
    - `delete(url, args)`	发送 DELETE 请求到指定 url
    - `get(url, params, args)`	发送 GET 请求到指定 url
    - `head(url, args)`	发送 HEAD 请求到指定 url
    - `patch(url, data, args)`	发送 PATCH 请求到指定 url
    - `post(url, data, json, args)`	发送 POST 请求到指定 url
    - `put(url, data, args)`	发送 PUT 请求到指定 url
    - `request(method, url, args)`	向指定的 url 发送指定的请求方法
3. 属性或方法	说明
    - `apparent_encoding`	编码方式
    - `close()`	关闭与服务器的连接
    - `content`	返回响应的内容，以字节为单位
    - `cookies`	返回一个 CookieJar 对象，包含了从服务器发回的 cookie
    - `elapsed`	返回一个 timedelta 对象，包含了从发送请求到响应到达之间经过的时间量，
    可以用于测试响应速度。比如 r.el`apsed.
    - `microseconds` 表示响应到达需要多少微秒。
    - `encoding`	解码 r.text 的编码方式
    - `headers`	返回响应头，字典格式
    - `history`	返回包含请求历史的响应对象列表（url）
    - `is_permanent_redirect`	如果响应是永久重定向的 url，则返回 True，否则返回 False
    - `is_redirect`	如果响应被重定向，则返回 True，否则返回 False
    - `iter_content()`	迭代响应
    - `iter_lines()`	迭代响应的行
    - `json()`	返回结果的 JSON 对象 (结果需要以 JSON 格式编写的，否则会引发错误)
    - `links`	返回响应的解析头链接
    - `next`	返回重定向链中下一个请求的 PreparedRequest 对象
    - `ok`	检查 "status_code" 的值，如果小于400，则返回 True，如果不小于 400，则返回 False
    - `raise_for_status()`	如果发生错误，方法返回一个 HTTPError 对象
    - `reason`	响应状态的描述，比如 "Not Found" 或 "OK"
    - `request`	返回请求此响应的请求对象
    - `status_code`	返回 http 的状态码，比如 404 和 200（200 是 OK，404 是 Not Found）
    - `text`	返回响应的内容，unicode 类型数据
    - `url`	返回响应的 URL

# 二、打码平台的使用
1. 利用打码平台实现验证码识别
   ```python
    from lxml import etree
    import requests
    from ydm import YdmVerify

    if __name__ == '__main__':
        y = YdmVerify()
        #先下载html
        url = r'https://so.gushiwen.cn/user/login.aspx?from=http://so.gushiwen.cn/user/collect.aspx'
        response = requests.get(url).text
        #进行html的解析，获取图片的src
        tree=etree.HTML(response)
        path="https://so.gushiwen.cn"+tree.xpath('//*[@id="imgCode"]/@src')[0]
        #用requests获取图片
        s=requests.get(path).content
        #进行图片的保存，也可以不保存
        with open('./c.jpg','wb') as f:
            f.write(s)
        #这一步利用云打码平台进行识别，每个平台用法不同，参考各个平台的开发文档
        y.common_verify(image=s)
    ```
2. 模拟验证码登录
    ```python
    #登录的url
    loginUrl='https://so.gushiwen.cn/user/login.aspx?from=http%3a%2f%2fso.gushiwen.cn%2fuser%2fcollect.aspx'
    #通过抓包获取登录请求的参数，把账号和密码等一些参数修改一下
    data={
        '__VIEWSTATE': 'kMkSEnRKraSLJybt9IUqygaZ7ADsvFLRx19eJUVSciZ4Zo0C0GhXHQt8sQkz9D%2FIQbIYm1XYspt3F9cSrK45VHFR2B0ioUDCiSHQdvlPsMkRIcsG9v99s9cxqBzZjJr0UOaivRVw9q%2FM%2FnMFSZ7nj17qvZA%3D',
        '__VIEWSTATEGENERATOR': 'C93BE1AE',
        'from':'http%3A%2F%2Fso.gushiwen.cn%2Fuser%2Fcollect.aspx',
        'email':'',
        'pwd': '',
        'code':code,#code为获取到的验证码
        'denglu': '%E7%99%BB%E5%BD%95'
    }
    #登录返回的response的text不一定就是登录成功的html，有可能网站重定向了
    response=requests.post(loginUrl,data=data,headers=headers)
    #通过状态码判断登录是否成功，200为成功
    print(response.status_code)
    ```
3. 利用session提高效率以及保存cookie
   ```python
    #创建session，requests.Session()和requests.session()都可以
    session = requests.Session()
    #登录的api
    loginAPI='https://so.gushiwen.cn/user/login.aspx?from=http%3a%2f%2fso.gushiwen.cn%2fuser%2fcollect.aspx'
    #在进行登录后，session会自动保存cookie，但是遇到301 302等重定向的时候并不一定能够保存cookie，推荐使用selenium模拟登录操作或则手动登录抓取cookie，allow_redirects=False禁止重定向
    res=session.post(url=loginAPI,data=loginData,allow_redirects=False)
   ``` 
   通过res.history可以查看重定向的信息