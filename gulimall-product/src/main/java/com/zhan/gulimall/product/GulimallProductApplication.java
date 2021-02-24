package com.zhan.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/*

1、整合MyBatis-Plus
    1）、导入依赖
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.4.1</version>
        </dependency>
    2）、配置
        1、配置数据源；
            1）、导入数据库驱动
            2）、
        2、配置MyBatis-Plus
2逻辑删除
    1)配制全局的配置全局的逻辑删除规则(省略)
    2)配置逻辑删除的组件bean（省略）
    3）给bean加逻辑注释@TableLogic
 3、JSR303
    1)、给bean添加校验注解：javax.validation.constraints
    2)、开启校验功能@Valid
        效果：校验错误以后会有默认的响应
    3）、给校验的bean后紧跟一个BindingResult，就可以获取到校验的结果
    4）分组校验 多场景的复杂校验。
        1）、@NotBlank(message = "新增必须提交品牌名" ,groups = {AddGroup.class,})
	        给检验朱姐标注什么情况需要进行校验
	    2）、@Validated({AddGroup.class})
	    3）、默认没有指定分组的检验注解@NotBlank,在分组校验情况@Validated({AddGroup.class}) 下不生效
4、统一异常处理
    @controllerAdvice
    1)、编写异常处理类，使用@controllerAdvice
    2）、使用@exceptionhandler标注方法可以处理的异常
 */
@EnableFeignClients(basePackages = "com.zhan.gulimall.product.feign")
@MapperScan("com.zhan.gulimall.product.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class GulimallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallProductApplication.class, args);
    }

}
