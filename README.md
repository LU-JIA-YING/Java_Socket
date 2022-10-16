# Java_Socket

## Log4J
### 目標
- 了解 Log4j
- 可應用在開發
- 知道埋 log 的時機

### 參考
[log4j介紹1](https://ithelp.ithome.com.tw/articles/10261299?sc=rss.iron)
[log4j介紹2](https://dotblogs.azurewebsites.net/Leon-Yang/2021/01/06/155519)

[Log4J](https://hackmd.io/@IDdlPCCwQoeX-9DvmEbLyw/B1DUJcCZo)

- [ ] Log4j 介紹
- [ ] Log 級别 (Logger)
- [ ] Log 輸出目的地 (Appender)
- [ ] Log 輸出格式 (Layout)
- [ ] Log4j properties 參數說明
- [ ] 應用在開發中 (實作)

## Java Thread

- [ ] Single Thread
- [ ] Multi-thread
- [ ] Process vs Thread
- [ ] Synchronous vs Asynchronous
- [ ] Thread Pool

## Socket 連線

[HTTP、TCP和Socket的概念和原理及其區別](https://www.jianshu.com/p/947a2673102a)

- [ ] TCP  vs Socket vs HTTP
- [ ] Socket vs WebSocket
- [ ] 長連線、短連線
- [ ] Socket Pool

## Spring Boot Scheduling Tasks

- [ ] `@Scheduled `
- [ ] crontab

## Sprint 2 實作
### 目標

不使用 Spring Application
使用原生JAVA專案進行撰寫

- [x] Java Socket Server (CRUD)、Java Socket Client 
- [x] Java JDBC API
- [x] Log4j
- [x] Multi-thread

* 1:查詢Cashi全部資料/ 2:用Id查詢Mgni/ 3:用Id查詢Cashi/ 4:Mgni動態查詢/ 5:Mgni新增/ 6:Mgni更新/ 7:Mgni刪除
* 新增、修改、刪除時，Mgni跟Cashi會連動

```java=

< Ture >  
      
	{"requestType":"1","request":{}}

        {"requestType":"2","request":{"id":"MGI20220929171333135"}}

        {"requestType":"3","request":{"id":"MGI20220929171333135"}}

        {"requestType":"4","request":{"cmNo":"1111111","ccy":""}}

        {"requestType":"5","request":{"cmNo":"9","kacType":"1","bankNo":"999","ccy":"TWD","pvType":"1","bicaccNo":"0000000","iType":"1","pReason":"money","clearingAccountList":[{"accNo":"1","amt":10},{"accNo":"2","amt":20}],"ctName":"Joey","ctTel":"1234578"}}

        {"requestType":"6","request":{"id":"MGI20221004222746688","cmNo":"3","kacType":"1","bankNo":"999","ccy":"TWD","pvType":"1","bicaccNo":"0000000","iType":"1","pReason":"deposit some money in the bank","clearingAccountList":[{"accNo":"1","amt":10000},{"accNo":"2","amt":200}],"ctName":"Joey","ctTel":"12345678"}}

        {"requestType":"6","request":{"id":"MGI20221004222746688","cmNo":"3","kacType":"1","bankNo":"999","ccy":"TWD","pvType":"1","bicaccNo":"0000000","iType":"1","pReason":"deposit some money in the bank","clearingAccountList":[{"accNo":"1","amt":10000}],"ctName":"Joey","ctTel":"12345678"}}

        {"requestType":"7","request":{"id":"MGI20221005091849492"}}

 ==============================================================================================================================
< Error >

        {"requestType":"2","request":{"id":"MGI20220929171333131"}}

        {"requestType":"5","request":{"cmNo":"9","kacType":"1","bankNo":"999","ccy":"TWD","pvType":"3","bicaccNo":"0000000","iType":"1","pReason":"money","clearingAccountList":[{"accNo":"1","amt":10},{"accNo":"2","amt":20}],"ctName":"Joey","ctTel":"1234578"}}

        {"requestType":"6","request":{"id":"MGI20221004222746688","cmNo":"3","kacType":"1","bankNo":"999","ccy":"HKD","pvType":"3","bicaccNo":"0000000","iType":"1","pReason":"deposit some money in the bank","clearingAccountList":[{"accNo":"1","amt":10000},{"accNo":"2","amt":200}],"ctName":"Joey","ctTel":"12345678"}}
	
```
