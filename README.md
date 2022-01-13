# About_SpringCloud
SpringCloud相关工程

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
│   ├── demo-api              --
│   ├── demo-client           --
│   ├── demo-provider         --
├── springcloud-seckill       -- 业务模块：秒杀练习
│   ├── seckill-api           --
│   ├── seckill-client        --
│   ├── seckill-provider      --
└── springcloud-uaa           -- 业务模块：用户认证与授权
    ├── uaa-api               --
    ├── uaa-client            --
    └── uaa-provider          --

