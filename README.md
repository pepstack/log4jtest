## log4jtest 漏洞修复测试

2022-05-26

务必首先阅读下面的文章：

[Apache log4j漏洞常规修复方法](https://blog.csdn.net/zhangmingcai/article/details/122407246)

不同版本的 Java 使用不同版本的 log4j2, 升级到不同版本的 log4j2:


- Java 8 and later

    https://www.apache.org/dyn/closer.lua/logging/log4j/2.17.2/apache-log4j-2.17.2-bin.tar.gz

- Java 7

    https://www.apache.org/dyn/closer.lua/logging/log4j/2.12.4/apache-log4j-2.12.4-bin.tar.gz

- Java 6

    https://www.apache.org/dyn/closer.lua/logging/log4j/2.3.2/apache-log4j-2.3.2-bin.tar.gz

log4j1 升级到 log4j2：

    https://logging.apache.org/log4j/2.x/manual/migration.html#Log4j1.2Bridge

#### 工程测试

创建 log4jtest.jar 包 （修改 pom.xml 的 <groupId>org.apache.logging.log4j</groupId> 部分以包含或不包含依赖 log4j-xxx.jar）

    $ mvn clean package

log4jtest.jar 内部包含依赖jar运行：

    $ java -jar -Xms1024m -Xmx1024m target/log4jtest.jar

log4jtest.jar 不包含依赖jar运行（<scope>provided</scope>）：

    $ java -jar -Xms1024m -Xmx1024m -Djava.ext.dirs=./lib target/log4jtest.jar

### 如何修复线上 log4j-core (< 2.15.0) 漏洞 (以2.11.1为例)

线上某个 jar 包 (如: log4jtest.jar) 用到了 log4j-core-2.11.1, 分2种情况:

- log4jtest.jar 没有包含 log4j-core-2.11.1, log4j 仅仅作为外部依赖使用。此时直接替换

        log4j-api-2.11.1.jar => log4j-api-2.17.2.jar
        log4j-core-2.11.1.jar => log4j-core-2.17.2.jar

 - log4jtest.jar 内部包含了 log4j-core-2.11.1, 此时需要替换 log4jtest.jar 内部的 log4j
 
**务必做好jar文件备份**

    1) 解包 log4jtest.jar
    2) 替换里面的 log4j
    3) 重新打包 log4jtest.jar

具体操作如下($projectdir 为本文件所在目录）:

    $ cd $projectdir
    $ ll target/log4jtest.jar
    $ mkdir -p build/{log4jtest,log4jcore}
    
解压无漏洞版本的 log4j 备用

    $ cd build/log4jcore
    $ jar xvf ../../lib/log4j-core-2.17.2.jar
    $ jar xvf ../../lib/log4j-api-2.17.2.jar

解压包含了 log4j-core-2.11.1 的 jar 包
    
    $ cd ../../
    $ cd build/log4jtest
    $ jar xvf ../../target/log4jtest.jar

替换 log4j/core 部分并重新打包

    $ rm -rf org/apache/logging/log4j
    $ cp -r ../log4jcore/org/apache/logging/log4j org/apache/logging/
    $ jar -cvfM ../log4jtest-new.jar ./
    
build/log4jtest-new.jar 为漏洞修复后的 log4jtest.jar

    $ cd ../../
    $ java -jar build/log4jtest-new.jar

