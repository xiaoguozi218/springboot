package com.example;

import com.example.service.PayService;
import com.example.service.RedisService;
import com.example.utils.RedisLock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootApplicationTests {

	@Autowired
	private PayService payService;

	@Autowired
	RedisService redisService;

	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void payTest() throws Exception {
//		redisService.set(UUID.randomUUID().toString(),"test",1l, TimeUnit.HOURS);
//		System.out.println("库存为：" + store);
	}

	@Test
	public void contextLoads() {
		String key = "mk:name";
		RedisLock lock = new RedisLock(redisTemplate, key, 10000, 20000);
		try {
			if(lock.lock()) {
				//需要加锁的代码
				Thread.sleep(2000);	//模拟业务
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			//为了让分布式锁的算法更稳键些，持有锁的客户端在解锁之前应该再检查一次自己的锁是否已经超时，再去做DEL操作，因为可能客户端因为某个耗时的操作而挂起，
			//操作完的时候锁因为超时已经被别人获得，这时就不必解锁了。 ————这里没有做
			lock.unlock();
		}

	}

}
