package com.microservice.cloud.product.api;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHash {
    /**
     * 服务器列表,一共有3台服务器提供服务, 将根据性能分配虚拟节点
     */
    public static String[] servers = {
            "192.168.0.1#100", //服务器1: 性能指数100, 将获得1000个虚拟节点
            "192.168.0.2#100", //服务器2: 性能指数100, 将获得1000个虚拟节点
            "192.168.0.3#30"   //服务器3: 性能指数30,  将获得300个虚拟节点
    };
    /**
     * 真实服务器列表, 由于增加与删除的频率比遍历高, 用链表存储比较划算
     */
    private static List<String> realNodes = new LinkedList<>();
    /**
     * 虚拟节点列表
     */
    private static TreeMap<Integer, String> virtualNodes = new TreeMap<>();

    static{
        for(String s : servers){
            //把服务器加入真实服务器列表中
            realNodes.add(s);
            String[] strs = s.split("#");
            //服务器名称, 省略端口号
            String name = strs[0];
            //根据服务器性能给每台真实服务器分配虚拟节点, 并把虚拟节点放到虚拟节点列表中.
            int virtualNodeNum = Integer.parseInt(strs[1]) * 10;
            for(int i = 1; i <= virtualNodeNum; i++){
                virtualNodes.put(FVNHash(name + "@" + i), name + "@" + i);
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new RequestProcess()).start();
    }

    static class RequestProcess implements Runnable{
        @Override
        public void run() {
            String client = null;
            for(int i=0; i < 100; i++){
                //模拟产生一个请求
                client = getN() + "." + getN() + "." + getN() + "." + getN() + ":" + (1000 + (int)(Math.random() * 9000));
                //计算请求的哈希值
                int hash = FVNHash(client);
                //判断请求将由哪台服务器处理
                System.out.println(client + " 的请求将由 " + getServer(client) + " 处理");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private static String getServer(String client) {
        //计算客户端请求的哈希值
        int hash = FVNHash(client);
        //得到大于该哈希值的所有map集合
        SortedMap<Integer, String> subMap = virtualNodes.tailMap(hash);
        //找到比该值大的第一个虚拟节点, 如果没有比它大的虚拟节点, 根据哈希环, 则返回第一个节点.
        Integer targetKey = subMap.size() == 0 ? virtualNodes.firstKey() : subMap.firstKey();
        //通过该虚拟节点获得真实节点的名称
        String virtualNodeName = virtualNodes.get(targetKey);
        String realNodeName = virtualNodeName.split("@")[0];
        return realNodeName;
    }

    public static int getN(){
        return (int)(Math.random() * 128);
    }

    public static int FVNHash(String data){
        final int p = 16777619;
        int hash = (int)2166136261L;
        for(int i = 0; i < data.length(); i++)
            hash = (hash ^ data.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return hash < 0 ? Math.abs(hash) : hash;
    }
}
