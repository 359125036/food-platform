##1.引入分页插件依赖
````
    <!--pagehelper mybatis分页助手-->
    <dependency>
        <groupId>com.github.pagehelper</groupId>
        <artifactId>pagehelper-spring-boot-starter</artifactId>
        <version>1.2.12</version>
    </dependency>

````
##2.配置yml
````
    #分页插件配置
    pagehelper:
      helper-dialect: mysql
      support-methods-arguments: true
````
##3.使用分页插件，在查询前使用分页插件，原理：统一拦截sql，为其提供分页功能
````
    /**
    * page:第几页
    * pageSize：每页显示条数
    */
    PageHelper.startPage(page,pageSize);
````
##4.分页数据封装到PagedGridResult.java传给前端
````
    PageInfo<?> pageList=new PageInfo<>(list);
    PagedGridResult grid=new PagedGridResult();
    grid.setPage(page);
    grid.setRows(list);
    grid.setTotal(pageList.getPages());
    grid.setRecords(pageList.getTotal());
````











