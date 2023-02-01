package org.cloudxue.springcloud.distribute.idGenerator.impl;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.cloudxue.springcloud.distribute.zookeeper.ZKClient;

/**
 * @ClassName SnowflakeIdWorker
 * @Description 基于ZooKeeper集群节点的命名服务
 *  1、启动节点服务，连接ZooKeeper，检查命名服务根节点是否存在，如果不存在就创建系统根节点；
 *  2、在根节点下创建一个临时顺序ZNode节点，取回ZNode的编号，作为分布式系统中节点的NodeId；
 *  3、如果临时节点太多，可以根据需要删除临时顺序ZNode节点。
 * @Author xuexiao
 * @Date 2022/2/7 下午3:59
 * @Version 1.0
 **/
public class SnowflakeIdWorker {
    //Zk客户端
    private CuratorFramework client = null;

    //工作节点的路径
    private String pathPrefix = "/test/IDMaker/worker-";
    private String pathRegistered = null;
    private  boolean inited=false;


    public SnowflakeIdWorker() {
        client = ZKClient.getSingleton().getClient();
    }


    public SnowflakeIdWorker(String type) {
        pathPrefix = "/snowflakeId/" + type + "/worker-";
    }

    // 在zookeeper中创建临时节点并写入信息
    public void init() {
        client = ZKClient.getSingleton().getClient();

        // 创建一个 ZNode 节点，节点的 payload 为当前worker 实例
        try {
            pathRegistered = client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(pathPrefix);
        } catch (Exception e) {
            e.printStackTrace();
        }
        inited = true;
    }

    public long getId() {
        if(!inited) {
            init();
        }
        String sid = null;
        if (null == pathRegistered) {
            throw new RuntimeException("节点注册失败" );
        }
        int index = pathRegistered.lastIndexOf(pathPrefix);
        if (index >= 0) {
            index += pathPrefix.length();
            sid = index <= pathRegistered.length() ? pathRegistered.substring(index) : null;
        }

        if (null == sid) {
            throw new RuntimeException("节点ID生成失败" );
        }
        return Long.parseLong(sid);
    }
}
