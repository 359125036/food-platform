# 通用 Mapper 接口所封装的常用方法

用了 Mybatis 逆向工具生成了 **pojo**、**mapper.java** 以及 **mapper.xml**，当然我们也提到了 **MyMapper** 这个接口，这个接口中封装了很多数据库操作方法，这一节我们一起来看一下吧。

> 建议大家在 idea 中打开相关源码，以此参照下面内容一起阅读噢～

<span></span>1. 首先先来看一下 **MyMapper** 所继承的父类，如：

```java
interface MyMapper<T> extends Mapper<T>,MySqlMapper<T>
```

这里有两个父类，**Mapper<T>** 与 **MySqlMapper<T>**，我们可以打开 **MySqlMapper<T>** 看一下：

```java
interface MySqlMapper<T> extends InsertListMapper<T>,InsertUseGeneratedKeysMapper<T>{}
```

这里面又继承了了两个 mapper，从类名上可以看得出来，是用于操作数据库的，这两个类里又分别包含了如下方法，简单归类一下：

|方法名|操作|备注|
|-|-|-|
|insertList(list)|数据批量插入|主键须自增|
|insertUseGeneratedKeys(record)|插入表数据|主键须自增|

很明显，在传统 JavaWeb 开发，这两个方法使用是没有问题的，但是我们的数据库表主键设计肯定是全局唯一的，所以不可能使用自增长 id（如何设计全局唯一分布式主键，在后续课程里有具体的讲解），所以这两个方法在我们开发过程中是不会使用的，这一点需要注意噢～！

<hr>

<span></span>2. 随后再来看一下 Mapper<T> 中所继承的父类，如下：

```java
interface Mapper<T> extends BaseMapper<T>,ExampleMapper<T>,RowBoundsMapper<T>,
```

分别来看一下各个父类中的方法有些啥？

* **BaseMapper<T>**

|类|方法|操作|
|-|-|-|
|BaseSelectMapper|T selectOne(T record)|根据实体类中的属性查询表数据，返回单个实体|
||List select(T record)|根据实体类中的属性查询表数据，返回符合条件的 list|
||List selectAll()|返回该表所有记录|
||int selectCount(T record)|根据条件查询记录数|
||T selectByPrimaryKey(Object key)|根据主键查询单挑记录|
||boolean existsWithPrimaryKey(Object key)|查询主键是否存在，返回 true 或 false|
|BaseInsertMapper|int insert(T record)|插入一条记录，属性为空也会保存|
||int insertSelective(T record)|插入一条记录，属性为空不保存，会使用默认值|
|BaseUpdateMapper|int updateByPrimaryKey(T record)|根据实体类更新数据库，属性有 null 会覆盖原记录|
||int updateByPrimaryKeySelective(T record)|根据实体类更新数据库，属性有 null 改属性会忽略|
|BaseDeleteMapper|int delete(T record)|根据实体类中属性多条件删除记录|
||int deleteByPrimaryKey(Object key)|根据主键删除记录|

* **ExampleMapper<T>**，Example 类是用于提供给用户实现自定义条件的，也就是 **where** 条件，主要方法见如下表格：

|类|方法|操作|
|-|-|-|
|SelectByExampleMapper|List selectByExample(Object example)|根据条件查询记录 list|
|SelectOneByExampleMapper|T selectOneByExample(Object example)|根据条件查询单条记录|
|SelectCountByExampleMapper|int selectCountByExample(Object example)|根据条件查询记录数|
|DeleteByExampleMapper|int deleteByExample(Object example)|根据条件删除记录|
|UpdateByExampleMapper|int updateByExample(T record, @Param(“example”) Object example);|根据条件更新数据，null 会覆盖原数据|
|UpdateByExampleSelectiveMapper|int updateByExampleSelective(T record, Object example);|根据条件更新数据，null 会忽略|

* **RowBoundsMapper<T>**，这个是用于做分页的，我们在后续阶段中会使用 page-helper 这个组件来替代这个分页实现。

## 总结

通用 mapper 所提供的 CRUD 方法对单表操作，大大提高开发效率，当然复杂的多表操作还是需要在 mapper.xml 中自己去编写 sql 代码实现。

本小节列举了通用 mapper 中常用的一些方法，在后续阶段课程里我们也都会去使用的。
