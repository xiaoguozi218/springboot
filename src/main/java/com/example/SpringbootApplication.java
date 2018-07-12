package com.example;

import com.example.config.OtherProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry    //入口类上开启retry的拦截，使用@EnableRetry注解。
//@ServletComponentScan	//这里关键是要在启动类上加上注解@ServletComponentScan
@EnableAspectJAutoProxy	//添加对aspect的支持
public class SpringbootApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApplication.class, args);
	}


	@Autowired
	private OtherProperties otherProperties;

	@Override
	public void run(String... strings) throws Exception {
//		System.out.println("\n"+otherProperties.toString());
		System.out.println();
	}


}
