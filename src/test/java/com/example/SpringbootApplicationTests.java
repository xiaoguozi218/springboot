package com.example;

import com.example.service.PayService;
import com.example.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

	@Test
	public void payTest() throws Exception {
		redisService.set(UUID.randomUUID().toString(),"test",1l, TimeUnit.HOURS);
//		System.out.println("库存为：" + store);
	}

//	@Test
//	public void contextLoads() {
//	}

}
