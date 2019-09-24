###spring cloud 学习笔记
 1.创建父类项目 所有的服务都下这个下面
  pom.xml 配置
  ```xml
<?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
        <modelVersion>4.0.0</modelVersion>
        <groupId>com.ddfoever</groupId>
        <artifactId>springCloudLearning</artifactId>
        <version>1.0</version>
    
        <!-- 表示此项目集成与 spring-boot-starter-parent 的一些特征-->
        <parent>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>2.1.6.RELEASE</version>
        </parent>
    
    
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
                <version>1.4.0.RELEASE</version>
            </dependency>
        </dependencies>
    
        <dependencyManagement>
            <dependencies>
                <dependency>
                    <groupId>org.springframework.cloud</groupId>
                    <artifactId>spring-cloud-dependencies</artifactId>
                    <version>Finchley.SR4</version>
                    <type>pom</type>
                    <scope>import</scope>
                </dependency>
            </dependencies>
        </dependencyManagement>
    </project>
```

+ ###创建Eureka注册中心(注册中心EurekaServer)
`eureke server pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springCloudLearning</artifactId>
        <groupId>com.ddfoever</groupId>
        <version>1.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>eurekaServer</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
            <version>2.0.2.RELEASE</version>
        </dependency>
    </dependencies>


</project>
```
+ eueka server `application.yml` 配置中心
 ```yaml
server:
  port: 8761
eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
    service-url:
      defaultZone: http://localhost:8761/eureka/

```
说明：

 `server.port`:eureka server的服务端口 `8761`是默认的
 
 `eureka.client.register-with-eureka`: 表示是否要注册自己到服务中心一般为false，不需要将自己再注册进去
 
 `eureka.client.fetch-registry`:是否关联其他的注册中心，将其他的服务数据同步过来
 
 `eureka.client.service-url.defaultZone`:配置eurekaserver的访问路径

+ 创建eurekaServer启动类

```java
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServer.class,args);
    }
}
```
> 注解说明
`@SpringBootApplication`: 表示springboot服务的入口

`@EnableEurekaServer`: 表示该类为一个Eureka Server微服务入口，提供服务注册和服务发现功能，即注册中心


### Eureka Client
 `pom.xml`
 
 ```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springCloudLearning</artifactId>
        <groupId>com.ddfoever</groupId>
        <version>1.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>EurekaClient</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-could-starter-netflix-eureka-clinet</artifactId>
            <version>2.0.2.RELEASE</version>
        </dependency>
    </dependencies>

</project>
```
+ 配置 `application.yml` 
 
 ```yaml
server:
  port: 8010
spring:
  application:
    name: serverProvider
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    prefer-ip-address: true

```
> 配置说明
 
`spring.application.name`: 当前服务的应用名字

`eureka.client.serivce-url.defaultZone`：注册中心的访问地址

`eureka.instance.prefer-ip-address`: s是否把当前服务的IP注册到Eureka Server

+ 配置启动类 `EurekaClientApplication`

````java
@SpringBootApplication
public class EurekaClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaClientApplication.class,args);
    }
}
````

### 测试服务提供者

+ 实体类 `User.java`

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    private String name;
    private int age;
}

```
+ 接口 `UserService.java`

```java
public interface UserService {

    Collection<User> findAll();

    User findById(int id);

    void saveOrUpdate(User u);

    void deleteById(int id);
}
```
+ 实现类 `UserServiceImpl.java`

```java
@Service
public class UserServerImpl implements UserService {

    private static Map<Integer,User> users ;

    static {
        users = new HashMap<>();
        users.put(1,new User(1,"aaa",11));
        users.put(2,new User(2,"bb",33));
        users.put(3,new User(3,"cc",22));
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User findById(int id) {
        return users.get(id);
    }

    @Override
    public void saveOrUpdate(User u) {
        users.put(u.getId(),u);
    }

    @Override
    public void deleteById(int id) {
        users.remove(id);
    }
}
```
+ 提供外部接口Controller `UserController.java`
```java
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public Collection<User> findAll(){
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User find(@PathVariable("id") int id){
        return userService.findById(id);
    }

    @PostMapping
    public void save(@RequestBody User u){
        userService.saveOrUpdate(u);
    }
    @PutMapping
    public void update(@RequestBody User u){
        userService.saveOrUpdate(u);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id){
        userService.deleteById(id);
    }

}
```

**Test 测试**

### RestTemplate 的使用
+ 首先呢RestTemplate是一Rest风格进行服务直接进行调用，底层基于Http协议的封装，提供了很多Rest访问的方法`get` `post` `put` `patch` `delete`,同时简化的代码的开发。
+ **如何使用RestTemplate**
 
 > 是springboot提供的组件
 > 是服务之间调用的桥梁 http请求
 
 
### 服务消费者（Consumer）

`pom.xml`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springCloudLearning</artifactId>
        <groupId>com.ddfoever</groupId>
        <version>1.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>consumer</artifactId>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
            <version>2.0.2.RELEASE</version>
        </dependency>
    </dependencies>
</project>
```
``
 
### api网关 gateway （spring cloud zuul）
什么是zuul？
spring could集成了zuul。
 帮我们做一个统一的入口 帮我们管理各个微服务的地址 ，向外提供一个api接口，可以实现反向代理；
 也需要想注册中心里面注册
 + 安全验证
 + 权限校验
 + 数据监控
 + 日志处理
 + 流量控制
 
 pom.xml
 ```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springCloudLearning</artifactId>
        <groupId>com.ddfoever</groupId>
        <version>1.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>zuul</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
            <version>2.0.2.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
            <version>2.0.0.RELEASE</version>
        </dependency>
    </dependencies>


</project>
```

配置pom 让这个project 成为一个网关
```xml
 <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
            <version>2.0.0.RELEASE</version>
        </dependency>
```

`application.yml`
```yaml
server:
  port: 8030
spring:
  application:
    name: gateway
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    prefer-ip-address: true
zuul:
  routes:
   serverProvider: /p/**


```
通过zuul.routes.serverProvider 来映射各个服务的地址  serverProvider 为服务提供者 在yml里面定义的名字，后面若有其他请求访问这个地址 `/p/**`
代表的是访问的是serverProvider这个微服务


+ 并且zuul还自带了负载均衡的功能
 当我启动多个服务提供者的实例后，通过zuul再次访问服务的rest接口 ,zuul就会把请求以轮询的方式分别请求到同一个服务的不同ip上
 zuul默认轮询方式；
 zuul主要的注解使用
 `@EnableZuulProxy`
 
 ###用Ribbon实现负载均衡
 什么是Ribbon？
 spring-cloud 继承了Ribbon 是由netflix 发布的， 是一个用于对http请求进行控制的客户端
 也需要在注册中心注册
 
 在注册中心注册后，Ribbon就会以某种方式进行负载均衡，例如轮询，加权轮询，随机，加权随机等自动帮助消费者调用接口，并且
 开发者可以自定义负载均衡算法，默认是轮询， spring could Ribbon 需要和spring cloud eureka server 结合使用，因为spring cloud eureka server 
 提供了可以调用的服务提供者列表；然后spring cloud ribbon 可以根据具体的业务需要采用具体的算法进行调用消费
 pom.xml
 ```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springCloudLearning</artifactId>
        <groupId>com.ddfoever</groupId>
        <version>1.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>Ribbon</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
            <version>2.0.0.RELEASE</version>
        </dependency>
    </dependencies>


</project>
```

```yaml
server:
  port: 8040
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    prefer-ip-address: true
spring:
  application:
    name: ribbon
```

```java
package com.ddfoever;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RibbonApplication {
    public static void main(String[] args) {
        SpringApplication.run(RibbonApplication.class,args);
    }
}

```

怎么使用Ribbon去实现负载均衡呢？

`Ribbon+RestTemplate+@LoadBalanced`来实现

当我们启动多个服务提供者实例后，能够看见他和zuul一样 默认以轮询的方式开始访问不同的实例

###Feign

什么是fegin
同样和Ribbon一样 ，实现负载均衡
它是对Ribbon的一次封装，简化一些操作，更加方便的使用，简化web服务端的操作；
比较简单的操作 使用接口和@注解来调用 restful api
并且fegin 整合了Ribbon和hystrix（容错机制）具有可插拔，负载均衡，熔断和服务降级

相比较与Ribbon+RestTemplate,大大简化了代码的开发；Fegin支持多种注解，JAX-RX ,Spring MVC注解等，
spring cloud对fegin 进行了优化，整合了Ribbon和eureka，从而使用fegin 更加方便

Ribbon 基于HTTP 的客户端，Fegin是基于Ribbon实现的；
Fegin特点
 1. Fegin 是一个声明式web service客户端
 2. Fegin基于注解，spring mvc ，JAX-RX注解
 3.Fegin 基于Ribbon 具有负载均衡的功能
 4. Fegin 集成了Hystrix，具有服务熔断功能

 
 `pom.xml`
 ```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springCloudLearning</artifactId>
        <groupId>com.ddfoever</groupId>
        <version>1.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>fegin</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
            <version>2.0.2.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
            <version>2.0.2.RELEASE</version>
        </dependency>
    </dependencies>

</project>
```
```java
package com.ddfoever;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FeignApplication {
    public static void main(String[] args) {
        SpringApplication.run(FeignApplication.class,args);
    }
}

```
@EnableFeignClients 表示是启用Feign客户端

```java
package com.ddfoever.controller;

import com.ddfoever.entity.User;
import com.ddfoever.feign.FeignInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/feign")
public class UserController {

    @Autowired
    private FeignInterface feignInterface;

    @GetMapping("/users")
    public Collection<User> findAll(){
        return feignInterface.findAll();
    }
    @GetMapping("/index")
    public String getIndex(){
        return feignInterface.index();
    }

}

```
```java
package com.ddfoever.feign;

import com.ddfoever.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@FeignClient(value = "serverProvider")
public interface FeignInterface {
    @GetMapping("/users/all")
    public Collection<User> findAll();
    @GetMapping("/users/index")
    public String index();
}

```
@FeignClients(value="serviceProvider") value为服务提供者在注册中心的名字

+ 基于Feign 的hystix 来实现熔断机制   
首先开始熔断开关
```yaml
feign:
  hystrix:
    enabled: true
```
`feign.hystrix.enabled`: 是否开始熔断机制

+ 通过实现Feign 的声明式接口 FeignError
```java
package com.ddfoever.feign;

import com.ddfoever.entity.User;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class FeignInterfaceImpl implements FeignInterface {
    @Override
    public Collection<User> findAll() {
        return null;
    }

    @Override
    public String index() {
        return "服务器维护中。。。";
    }
}

```
+ 同时也要添加 `@FeignClient` 的fallback 属性进行降级处理映射到FeignError实现类中
```java
@Component
public class FeignInterfaceImpl implements FeignInterface {
    @Override
    public Collection<User> findAll() {
        return null;
    }

    @Override
    public String index() {
        return "服务器维护中。。。";
    }
}
```

### Hystrix 容错机制

什么是容错机制？
在不改变各个微服务调用的关系前提下， 提前对错误进行预处理

有以下几个机制：
+ 服务隔离
+ 服务降级
+ 熔断机制
+ 实时监控和报警（对请求的一个监控）需要结合spring cloud actor 组件
  actor 提供了对数据健康检测，数据统计，可以通过hystrix.stream节点获取请求监控数据
  并且也提供了可视化的界面。
+ 实时配置的修改

`pom.xml`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springCloudLearning</artifactId>
        <groupId>com.ddfoever</groupId>
        <version>1.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>hystrix</artifactId>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
            <version>2.0.2.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
            <version>2.0.0.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>

        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
            <version>2.0.0.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
            <version>2.0.0.RELEASE</version>
        </dependency>
    </dependencies>


</project>
```
`http://localhost:8060/actuator/hystrix.stream` 实时数据监控 通过`hystrix.stream`
`http://localhost:8060/hystrix` 获取可视化界面 通过输入`http://localhost:8060/actuator/hystrix.stream`



  
  
    
    


