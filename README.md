# About_SpringCloud
SpringCloud相关工程
- Eureka 服务注册中心
- Spring Cloud Config 配置中心
- Feign实现远程RPC调用
    - Feign + Ribbon 实现客户端负载均衡
    - Feign + Hystrix 实现RPC调用保护
- 微服务网关Zuul
    - JWT+Spring Security进行网关安全认证


---
项目目录结构  
About_SpringCloud             --根目录  
├── cloud-center              -- 微服务的基础设施中心  
│   ├── cloud-config          -- 配置中心  
│   ├── cloud-eureka          -- 注册中心  
│   ├── cloud-zipkin          -- 监控中心  
│   ├── cloud-zuul            -- 网关服务  
├── springcloud-base          -- 公共基础依赖模块  
│   ├── base-auth             -- 基于JWT+SpringSecurity的用户凭证与认证模块  
│   ├── base-common           -- 普通的公共依赖，相关工具类  
│   ├── base-redis            -- 公共的Redis操作  
│   ├── base-runtime          -- 各Provider的运行时公共依赖，装配了一些通用的Spring IOC Bean实例  
│   ├── base-session          -- 分布式Session  
│   ├── base-zookeeper        -- 公共的ZooKeeper操作  
├── springcloud-demo          -- 业务模块：练习演示  
│   ├── demo-api              -- 演示模块API  
│   ├── demo-client           -- 演示模块客户端组件  
│   ├── demo-provider         -- 演示模块服务提供者  
├── springcloud-seckill       -- 业务模块：秒杀练习
│   ├── seckill-api           -- 秒杀模块API  
│   ├── seckill-client        -- 秒杀模块客户端组件  
│   ├── seckill-provider      -- 秒杀模块服务提供者  
└── springcloud-uaa           -- 业务模块：用户认证与授权  
    ├── uaa-api               -- 用户认证与授权API  
    ├── uaa-client            -- 用户认证与授权客户端组件  
    └── uaa-provider          -- 用户认证与授权服务提供者  

---