# Data Porter from Kafka to PG
车联网项目中从kfk读取数据，并存入pg数据库的代码。做备份用。

## 程序功能：
1. 从KAFKA里取数据。
2.对无效数据过滤。
3. 对缺失字段填充
4. 数据存DB
5. 数据更新Memcache。

## 各个包的作用：
- config：存放程序的配置，包括kafka，zookeeper，pgsql等
- dataHandler：数据处理包，包括处理类（总管），过滤类，帮助类（负责启动线程并传输数据到各个线程）和种植类（使用pgclient将数据写入pg）
- kafkaPorter：kafka消费报，负责从kafka里面消费，把数据传给处理类处理
- lib：

