##错误总结
1.
{
      "timestamp": 1515816602377,
      "status": 403,
      "error": "Forbidden",
      "message": "Invalid CSRF Token 'null' was found on the request parameter '_csrf' or header 'X-CSRF-TOKEN'.",
      "path": "/wechat_punch/wxjs_config"
  }
  
遇到这种情况，我们关闭CSRF,Spring Security4.0之后，引入了CSRF,默认是开启的。CSRF和RESTful技术有冲突,CSRF默认支持的方法:GET|HEAD|TRACE|OPTIONS,不支持POST

如果这个http请求是get方式发起的请求，意味着它只是访问服务器 的资源，仅仅只是查询，没有更新服务器的资源，所以对于这类请求，spring security的防御策略是允许的，

如果这个请求是通过post请求发起的， 那么spring security是默认拦截这类请求的，因为这类请求是带有更新服务器资源的危险操作，如果恶意第三方可以通过劫持session id来更新 服务器资源，那会造成服务器数据被非法的篡改，所以这类请求是会被Spring security拦截的，在默认的情况下，spring security是启用csrf 拦截功能的，这会造成，在跨域的情况下，post方式提交的请求都会被拦截无法被处理（包括合理的post请求），前端发起的post请求后端无法正常 处理，虽然保证了跨域的安全性，但影响了正常的使用，如果关闭csrf防护功能，虽然可以正常处理post请求，但是无法防范通过劫持session id的非法的post请求，所以spring security为了正确的区别合法的post请求，采用了token的机制。

Token机制:
在跨域的场景下，客户端访问服务端会首先发起get请求，这个get请求在到达服务端的时候，服务端的Spring security会有一个过滤 器 CsrfFilter去检查这个请求，如果这个request请求的http header里面的X-CSRF-COOKIE的token值为空的时候，服务端就好自动生成一个 token值放进这个X-CSRF-COOKIE值里面，客户端在get请求的header里面获取到这个值，如果客户端有表单提交的post请求，则要求客户端要 携带这个token值给服务端，在post请求的header里面设置_csrf属性的token值，提交的方式可以是ajax也可以是放在form里面设置hidden 属性的标签里面提交给服务端，服务端就会根据post请求里面携带的token值进行校验，如果跟服务端发送给合法客户端的token值是一样的，那么 这个post请求就可以受理和处理，如果不一样或者为空，就会被拦截。由于恶意第三方可以劫持session id，而很难获取token值，所以起到了 安全的防护作用。

