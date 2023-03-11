<!--
 * @Descripttion: 
 * @Author: 只会Ctrl CV的菜鸟
 * @version: 
 * @Date: 2023-02-04 23:12:11
 * @LastEditTime: 2023-02-08 21:48:23
-->
# hook替换debugger

在f12控制台输入代码，把Function的构造函数替换，来跳过debugger
```js
// 先保留原 constructor
Function.prototype.constructor_ = Function.prototype.constructor;
Function.prototype.constructor = function (a) {
    // 如果参数为 debugger，就返回空方法
    if(a == "debugger") {
        return function (){};
    }
    // 如果参数不为 debugger，还是返回原方法
    return Function.prototype.constructor_(a);
};
```
# Object.defineProperty()

```js
(function() {
    //严谨模式 检查所有错误
    'use strict';
    //window 为要 hook 的对象，这里是 hook 的 _signature
    var _signatureTemp = "";
    Object.defineProperty(window, '_signature', {
        //hook set 方法也就是赋值的方法 
        set: function(val) {
                console.log('Hook 捕获到 _signature 设置->', val);
                debugger;
                _signatureTemp = val;
                return val;
        },
        //hook get 方法也就是取值的方法 
        get: function()
        {
            return _signatureTemp;
        }
    });
})();
```

# 定时器

```js
// 先保留原定时器
var setInterval_ = setInterval
setInterval = function (func, time){
    // 如果时间参数为 0x7d0，就返回空方法
    // 当然也可以不判断，直接返回空，有很多种写法
    if(time == 0x7d0)
    {
        return function () {};
    }
    // 如果时间参数不为 0x7d0，还是返回原方法
    return setInterval_(func, time)
}
```

