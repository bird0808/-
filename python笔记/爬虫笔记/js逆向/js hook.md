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
var AAA=Function.prototype.constructor
Function.prototype.constructor=function(x){
if(X! ="debugger"){
   return AAA(x) 
}
return function(){};
}
```
# Object.defineProperty()

