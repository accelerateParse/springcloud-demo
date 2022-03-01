
## springcloud相关组件使用
相关目录涉及使用组件及说明
 - auth
   1.jwt鉴权:用户登录存个token redis里，并且神不知鬼不觉将token放入请求头，不用前端知道,前端连 userId也不需要知道,之后设计用户的请求直接请求头鉴权。
 - cloudComponent
   1.eureka:微服务注册管家，负载均衡请求到相同微服务不同节点。
   2.gateway:网关，有路由、限流、过滤请求、配置跨域功能。
   3.hystrix_turbine:微服务熔断，当底层微服务出错的时候服务降级，多级降级防止雪崩（还没写）。
   4.hystrix_dashboard:hystrix大盘,检测微服务出现降级的情况（还没写）。
 - common
   common:放了些实体类，枚举类被其他模块依赖。
   webComponent:面向切面日志，swagger配置。
 - server
   user:用户相关api,运用了Feign直接被其他微服务内部调用
   search:主搜模块，运用了elasticsearch,使用ik+pinyin分词器，能实现输入p g 简拼返回苹果的搜索结果。

