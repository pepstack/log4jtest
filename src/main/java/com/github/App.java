package com.github;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * apache log4j 漏洞测试程序
 *
 *   https://blog.csdn.net/qq_54088719/article/details/121881093
 */
public class App 
{
    private static Logger logger = LogManager.getLogger(App.class);

    public static void main( String[] args )
    {
        System.out.println( ">>>>>>>> App start..." );

		// 记录debug级别的信息
        logger.debug("This is debug message.");

		// 记录info级别的信息  
        logger.info("This is info message.");

		// 记录error级别的信息
        logger.error("This is error message.");

        (new App()).register("zhang");

		System.out.println( "<<<<<<<< App exit !!");		
    }


	// 一个简单的登录方法，传入用户名字
    public void register(String username) {
		// 日志在控制台打印
        logger.info("{}, 在此时登录", username);
    }
}
