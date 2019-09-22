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
 ```$xslt
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
 
 ```xml
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
 
 
### 服务消费者

 





  
  
    
    


