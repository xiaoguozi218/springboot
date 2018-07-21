package com.example;

import com.example.config.OtherProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableRetry    //入口类上开启retry的拦截，使用@EnableRetry注解。
//@ServletComponentScan	//这里关键是要在启动类上加上注解@ServletComponentScan
@EnableAspectJAutoProxy	//添加对aspect的支持
@EnableScheduling //定时任务注解
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
/**
 *
 *《Spring Boot启动流程详解》：1、构建SpringApplication对象。 2、执行run()
 *
 * 	一、构建 SpringApplication对象
 *
 *
 *
 *	总结：整个SpringApplication初始化的过程，就是初始化了
 *		1、一个包含入参SpringbootApplication.class的sources的Set<Object>
 *		2、一个当前环境是否是web环境的 boolean webEnvironment
 *		3、一个包含4个 ApplicationContextInitializer 实例的List
 *		4、一个包含8个 ApplicationListener实例的List
 *		5、一个main方法所在的主类的Class对象。
 *
 */
