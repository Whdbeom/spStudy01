package com.sparta.java_02;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

@SpringBootTest
public class RedisDataTest {

  private static final Logger log = LoggerFactory.getLogger(RedisDataTest.class);

  @Autowired
  private Jedis jedis;

  @Test
  void redisStringExample() {

    //api : /api/users/55 <- 해당 API 호출
    jedis.set("users:55:session", "{\"id\": 34, \"name\" : \"홍길동\"}");
    jedis.expire("users:55:session", 3600);

    String response = jedis.get("users:55:session");
    log.info("/api/users/34 요청에 따른 캐싱 된 응답 값 : {}", response);

    Long ttl = jedis.ttl("users:55:session");
    log.info("데이터 만료시간 : {}", ttl);

    jedis.set("article:101:views", "0");

    log.info("article:101:views : {}", jedis.get("article:101:views"));

    jedis.incr("article:101:views");

    log.info("article:101:views : {}", jedis.get("article:101:views"));

    jedis.decr("article:101:views");

    log.info("article:101:views : {}", jedis.get("article:101:views"));
  }

  @Test
  void redisListExample() {

    jedis.lpush("queue:tasks", "task1", "task2", "task3", "task4", "task5");

    Long queueSize = jedis.llen("queue:tasks");

    log.info("queueSize : {}", queueSize);

    String L_task = jedis.lpop("queue:tasks");
    log.info("L_task : {}", L_task);

    String R_task = jedis.rpop("queue:tasks");
    log.info("R_task : {}", R_task);
  }

  @Test
  void redisSetExample() {

    jedis.sadd("set1", "task1", "task2", "task3", "task4", "task5");
    jedis.sadd("set2", "task3", "task4", "task5", "task6", "task7");

    Set<String> sInterSet = jedis.sinter("set1", "set2");
    log.info("sInterSet : {}", sInterSet);

    Set<String> sUnionSet = jedis.sunion("set1", "set2");
    log.info("sUnionSet : {}", sUnionSet);

  }

  @Test
  void redisHashExample() {
    jedis.hset("user:123", "name", "John Doe");
    jedis.hset("user:123", "email", "John Doe@example.com");
    jedis.hset("user:123", "age", "30");
    jedis.hset("user:123", "city", "NewYork");

    String name = jedis.hget("user:123", "name");

    Map<String, String> hget = jedis.hgetAll("user:123");

    log.info("java map : {}, redis: {}", hget.get("name"), name);

  }

  @Test
  void redisSortedSetExample() {
    jedis.zadd("user:123:friendly", 100, "friend1");
    jedis.zadd("user:123:friendly", 200, "friend2");
    jedis.zadd("user:123:friendly", 300, "friend3");
    jedis.zadd("user:123:friendly", 400, "friend4");

    // 오름차순
    List<String> zrevrangeFriends = jedis.zrevrange("user:123:friendly", 1, 3);
    //내림차순
    List<String> zrangeFriends = jedis.zrange("user:123:friendly", 1, 3);

    log.info("zrevrangeFriends: {}", zrevrangeFriends);
    log.info("zrangeFriends: {}", zrangeFriends);

    Double score = jedis.zscore("user:123:friendly", "friend1");
    log.info("score: {}", score);

    jedis.zincrby("user:123:friendly", 120, "friend1"); // 데이터베이스의 기준 값 업데이트 시점
    // 오름차순
    List<String> zrevrangeFriends1 = jedis.zrevrange("user:123:friendly", 1, 3);
    //내림차순
    List<String> zrangeFriends1 = jedis.zrange("user:123:friendly", 1, 3);

    log.info("zrevrangeFriends1: {}", zrevrangeFriends1);
    log.info("zrangeFriends1: {}", zrangeFriends1);
  }


}
