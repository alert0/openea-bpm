# Open BPM 
## 概述
基于开源工作流引擎的BPM套装，整合各种开源项目或组件，实现企业应用快速集成工作流能力。


## solution 解决方案

* bpm-boot

现代spring boot方式使用BPM工作流

* bpm-cloud

微服务模式工作流（同eap-cloud整合）

# BPM模块简介
## bpm-后端核心功能
bpm包含业务对象bus,表单form和流程wf
* bus 业务对象模块
* form 表单模块
* wf 流程模块

## support-后端支持功能
support包含base, auth,org以及sys
* org 内建组织权限功能
* sys 内建后台基础服务功能

## ui-前端UI相关功能
ui包含web前端explorer-ng和web打包工程以及mobile-vue 移动端APP
* explorer-ng BPM Web端UI
* mobile-vue BPM移动端UI

## app-其他应用以及Demo
BPM相关的app和demo

# getting start
## spring boot 启动

### 准备

数据库mysql(5.6或以上)

修改数据库配置文件 bpm-boot/bpm-boot/src/main/resources/application.yml

缓存服务器redis

### spring boot启动
``` bash
cd openea-bpm
mvn clean install
cd ./bpm-boot/bpm-boot
mvn spring-boot:run
```
http://localhost:8080/

管理用户admin/admin

## docker 启动

### 安装docker环境
需要安装docker环境

### docker 启动
``` bash
cd openea-bpm
mvn clean install
cd ./bpm-boot
docker-compose up
```
http://localhost:7080/

管理用户admin/admin

# 参考项目 refer
参考众多开源项目实现，一并表示感谢，部分项目列出如下：
* activiti 开源工作流引擎
https://github.com/Activiti/Activiti

* spring boot 后端基础框架
https://github.com/spring-projects/spring-boot

* jeeSpringCloud 企业开发平台(包含基于Activiti工作流)
https://gitee.com/JeeHuangBingGui/jeeSpringCloud

* agile bpm 企业工作流管理(基于Activiti)
https://github.com/AgileBPM/agile-bpm-basic

* sz BPM 企业工作流管理(基于Activiti)

