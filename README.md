# Meshed Cloud

## 本地开发

VM 参数

> -DPORT=9989 -DACTIVE=dev -DNACOS_HOST=nacos-ip -DNACOS_PORT=nacos-port

参数说明：

- PORT: 项目启动端口
- ACTIVE： 项目启动配置环境
- NACOS_HOST: 项目依赖nacos 服务器ip
- NACOS_PORT： 项目依赖nacos 服务器端口

## Swagger Doc

> http://localhost:9989/workflow/doc.html


## 部署

### Docker

```shell
docker login --username=meshed registry.cn-shanghai.aliyuncs.com
docker pull registry.cn-shanghai.aliyuncs.com/meshed/workflow:[最新版本号]
docker run -d -e JAVA_OPTS='-DPORT=9989 -DDUBBO_IP_TO_REGISTRY=public-ip -DACTIVE=prod -DNACOS_HOST=nacos-ip -DNACOS_PORT=nacos-port' --net=host registry.cn-shanghai.aliyuncs.com/meshed/workflow:[最新版本号]
```
