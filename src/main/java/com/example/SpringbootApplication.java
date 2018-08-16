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
 *《Spring Boot 最流行的 16 条实践解读！》 -
 * 	1、使用自动配置 - Spring Boot的一个主要特性是使用自动配置。
 * 	   - 使用它的最简单方法是依赖Spring Boot Starters。
 * 	   	 借助于这些starters，这些繁琐的配置就可以很好地集成起来并协同工作，而且它们都是经过测试和验证的。这非常有助于避免可怕的Jar地狱.
 * 	   - 通过使用以下注解属性，可以从自动配置中排除某些配置类：@EnableAutoConfiguration（exclude = {ClassNotToAutoconfigure.class}）- 但只有在绝对必要时才应该这样做。
 * 	2、使用Spring Initializr来开始一个新的Spring Boot项目
 * 	   - Spring Initializr（https://start.spring.io/）提供了一个超级简单的方法来创建一个新的Spring Boot项目，并根据你的需要来加载可能使用到的依赖。
 * 	   - 使用Initializr创建应用程序可确保你获得经过测试和验证的依赖项，这些依赖项适用于Spring自动配置。你甚至可能会发现一些新的集成，但你可能并没有意识到这些。
 * 	3、正确设计代码目录结构
 * 	   - 将Application.java（应用的入口类）保留在顶级源代码目录中；
 * 	4、保持@Controller的简洁和专注 - Controller应该非常简单。
 * 	   - 控制器应该是无状态的！默认情况下，控制器是单例，并且任何状态都可能导致大量问题；
 * 	   - 控制器不应该执行业务逻辑，而是依赖委托；
 * 	   - 控制器应该处理应用程序的HTTP层，这不应该传递给服务；
 * 	5、使数据库独立于核心业务逻辑之外
 * 	   - 罗伯特C.马丁强烈地说明，你的数据库是一个“细节”，这意味着不将你的应用程序与特定数据库耦合。过去很少有人会切换数据库，我注意到，使用Spring Boot和现代微服务开发会让事情变得更快。
 * 	6、推荐使用构造函数注入
 * 	   - 保持业务逻辑免受Spring Boot代码侵入的一种方法是使用构造函数注入。 不仅是因为@Autowired注解在构造函数上是可选的，而且还可以在没有Spring的情况下轻松实例化bean。
 * 	7、熟悉并发模型 -
 * 	   - 在Spring Boot中，Controller和Service是默认是单例。
 * 	8、加强配置管理的外部化
 * 	   - 我推荐两种主要方法：1、使用配置服务器，例如Spring Cloud Config；
 * 	   					  2、将所有配置存储在环境变量中（可以基于git仓库进行配置）。
 * 	9、提供全局异常处理 - 你真的需要一种处理异常的一致方法。Spring Boot提供了两种主要方法：
 * 	                   1、你应该使用HandlerExceptionResolver定义全局异常处理策略；
 * 	                   2、你也可以在控制器上添加@ExceptionHandler注解，这在某些特定场景下使用可能会很有用。
 *  10、使用日志框架 - private static final Logger logger = LoggerFactory.getLogger(HttpController.class);
 *
 *
 */
