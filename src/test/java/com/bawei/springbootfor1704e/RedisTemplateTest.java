package com.bawei.springbootfor1704e;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTemplateTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testKeys() {
        Set<String> keys = redisTemplate.keys("*");
        for(String key : keys) {
            System.out.println(key);
        }
    }

    //插入普通的keyvalue结构
    @Test
    public void testKeyValuePut() {
        redisTemplate.opsForValue().set("bar","foo");
    }

    //设置过期时间
    @Test
    public void testSetExpire() {
        redisTemplate.expire("bar",5, TimeUnit.MINUTES);
    }

    //获取过期时间
    @Test
    public void testGetExpire() {
        Long timeout = redisTemplate.getExpire("bar", TimeUnit.MILLISECONDS);
        System.out.println(timeout);
    }

    //设置普通keyvalue结构并且设置过期时间
    @Test
    public void testKeyValuePutWithExpire() {
        redisTemplate.opsForValue().set("foo","bar",30000,TimeUnit.MILLISECONDS);
    }

    //查看key是否存在
    @Test
    public void testKeyExist() {
        Boolean flag = redisTemplate.hasKey("foo");
        System.out.println(flag);
    }

    //过去普通key value结构
    @Test
    public void testGetKeyValue() {
        Object ordersum = redisTemplate.opsForValue().get("ordersum");
        System.out.println(Double.valueOf((Integer) ordersum));
    }

    //递增
    @Test
    public void testIncrValue() {
        redisTemplate.opsForValue().increment("testIncr",3L);
    }

    //di递减
    @Test
    public void testDecrValue() {
        redisTemplate.opsForValue().decrement("testIncr",2L);
    }

    //====================================================================================================
    //====================================================================================================
    //=========================================hash===========================================================
    //redis中hash的结构操作
    //redis中hash结构的key不能重复

    //向hash结构中put一批数据
    //hmset stu_score zhangfuda 67.2 guozhu 73.5
    @Test
    public void testHashBatchPut() {
        HashMap<String, Double> map = new HashMap<>();
        map.put("zhangfuda",67.2);
        map.put("guozhu",73.5);
        redisTemplate.opsForHash().putAll("stu_score",map);
    }

    //向hash中put单个数据
    //hset stu_score tanguoning 77.7
    @Test
    public void testHashSinglePut() {
        redisTemplate.opsForHash().put("stu_score","tanguoning",77.7);
    }

    //获取hash结构中全部的key值
    //hkeys stu_score
    @Test
    public void testGetHashAllKeys() {
        Set<String> keys = redisTemplate.opsForHash().keys("stu_score");
        for(String key : keys) {
            System.out.println(key);
        }
    }

    //获取hash中的数据值
    //hget stu_score zhangfuda
    @Test
    public void testGetHashValue() {
        Object score = redisTemplate.opsForHash().get("stu_score", "zhangfuda");
        System.out.println((Double) score);
    }

    //批量获取hash中的值
    //HMGET stu_score guozhu tanguoning
    @Test
    public void testHashBatchGet() {
        List<Double> list = redisTemplate.opsForHash().multiGet("stu_score", Arrays.asList("guozhu", "tanguoning"));
        for(Double score : list) {
            System.out.println(score);
        }
    }

    //判断hash结构中是否有某个field
    //hexists stu_score lijiawang
    @Test
    public void testJudgeFieldExist() {
        Boolean flag = redisTemplate.opsForHash().hasKey("stu_score", "lijiawang");
        System.out.println(flag);
    }


    //删除hash结构中的某些fields
    //hdel stu_score zhangfuda likefeng
    @Test
    public void testDelHashFiled() {
        Long deleteNum = redisTemplate.opsForHash().delete("stu_score", "zhangfuda","likefeng");
        System.out.println(deleteNum);
    }

    //递增hash中的值
    //HINCRBYFLOAT stu_score guozhu 10.0
    @Test
    public void testIncrHashValue() {
        redisTemplate.opsForHash().increment("stu_score","guozhu",10.0);
    }

    //递减hash中的值
    //HINCRBYFLOAT stu_score guozhu -5.0
    @Test
    public void testDescHashValue() {
        redisTemplate.opsForHash().increment("stu_score","guozhu",-5.0);
    }

    //===================================================================================================

    //==============================================set===========================================================
    //set结构值不能重复

    //向redis中put set数据
    //sadd 1704e liangjunwei wangmingyuan huangyoujie
    @Test
    public void testPutSet() {
        redisTemplate.opsForSet().add("1704e","liangjunwei","wangmingyuan","huangyoujie");
    }

    //获取set中全部的值
    //SMEMBERS 1704e
    @Test
    public void testGetSet() {
        Set<String> members = redisTemplate.opsForSet().members("1704e");
        for(String member : members) {
            System.out.println(member);
        }
    }

    //删除redis中set的值
    //SREM 1704e "\"liangjunwei\""
    @Test
    public void testDelSetValue() {
        redisTemplate.opsForSet().remove("1704e","wangmingyuan");
    }

    //判断元素是否存在
    //SISMEMBER 1704e "\"liangjunwei\""
    @Test
    public void testSetValueExist() {
        Boolean flag = redisTemplate.opsForSet().isMember("1704e", "liangjunwei");
        System.out.println(flag);
    }

    //获取set的size
    //SCARD 1704e
    @Test
    public void testGetSetLength() {
        Long size = redisTemplate.opsForSet().size("1704e");
        System.out.println(size);
    }

    //在set之间移动元素
    //SMOVE 1704e 1705e "\"zhangbotang\""
    @Test
    public void testSetMove(){
        Boolean flag = redisTemplate.opsForSet().move("1704e", "zhangbotang", "1705e");
        System.out.println(flag);
    }

    //随机从set中取出一定数量的元素
    //SRANDMEMBER 1704e 1
    @Test
    public void testGetByRandom() {
        List<String> list = redisTemplate.opsForSet().randomMembers("1704e", 1L);
        for(String name : list) {
            System.out.println(name);
        }
    }

    //弹出set中指定个数个元素
    //SPOP 1704e 1
    @Test
    public void testSpop() {
        List<String> pop = redisTemplate.opsForSet().pop("1704e", 1L);
        for(String name : pop) {
            System.out.println(name);
        }
    }

    //求两个set集合的差集
    //SDIFF 1704e 1705e
    @Test
    public void testSetDiff() {
        Set<String> difference = redisTemplate.opsForSet().difference("1704e", "1705e");
        for(String name : difference) {
            System.out.println(name);
        }
    }

    //求差集并存到新的set中
    //SDIFFSTORE 1704ediff1705e 1704e 1705e
    @Test
    public void testSetDiffAndStore() {
        redisTemplate.opsForSet().differenceAndStore("1704e","1705e","1704ediff1705e");
    }

    //求两个set的交集
    @Test
    public void testSetInsert() {
        Set<String> intersect = redisTemplate.opsForSet().intersect("1704e", "1705e");
        for(String name : intersect) {
            System.out.println(name);
        }
    }

    @Test
    public void testSetInsertAndStore() {
        redisTemplate.opsForSet().intersectAndStore("1704e","1705e","1704einsert1705e");
    }

    //求set的并集
    @Test
    public void testSetUnion() {
        Set<String> union = redisTemplate.opsForSet().union("1704e", "1705e");
        for(String name : union) {
            System.out.println(name);
        }
    }


}
