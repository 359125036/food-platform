# 使用 MyBatis 数据库逆向生成工具

## 1. 在 pom 中引入通用 mapper 工具
```xml
<!-- 通用mapper逆向工具 -->
<dependency>
    <groupId>tk.mybatis</groupId>
    <artifactId>mapper-spring-boot-starter</artifactId>
    <version>2.1.5</version>
</dependency>
```

## 2. 在 yml 中引入通用 mapper 配置
```yml
############################################################
#
# mybatis mapper 配置
#
############################################################
# 通用 Mapper 配置
mapper:
  mappers: com.imooc.my.mapper.MyMapper
  not-empty: false
  identity: MYSQL
```
  
## 3. 引入 MyMapper 接口类
```java
package com.imooc.my.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 继承自己的MyMapper
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
```
