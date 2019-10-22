package test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import chen.e3.utils.jedis.JedisClient;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTesst {

	@Test
	public void test1(){
		//创建Jedis
		Jedis jedis = new Jedis("192.168.25.129", 6379);
		/*Set<String> keys = jedis.keys("*");
		for (String string : keys) {
			System.out.println(string);
		}*/
		jedis.set("ccc", "123");
		jedis.close();
	}
	
	@Test
	public void test2(){
		JedisPool pool=new JedisPool("192.168.25.129", 6379);
		Jedis jedis = pool.getResource();
		String string = jedis.get("ccc");
		System.out.println(string);
		jedis.close();
		pool.close();
	}
	
	//连接redis集群
//	@Test
//	public void test3(){
//		Set<HostAndPort> set=new HashSet<>();
//		HostAndPort hostAndPort1=new HostAndPort("192.168.25.129", 7001);
//		HostAndPort hostAndPort2=new HostAndPort("192.168.25.129", 7002);
//		HostAndPort hostAndPort3=new HostAndPort("192.168.25.129", 7003);
//		set.add(hostAndPort1);
//		set.add(hostAndPort2);
//		set.add(hostAndPort3);
//		JedisCluster jedisCluster=new JedisCluster(set);
//		String string = jedisCluster.get("ccc");
//		System.out.println(string);
//	}
	
	@Test
	public void test4(){
//		ApplicationContext ac=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
//		JedisClient jedisClient = ac.getBean(JedisClient.class);
//		String string = jedisClient.get("ccc");
//		System.out.println(string);
	}
}
