
本平台是简易版管理后台

## 开发

```bash
>node -v
v16.15.1

# 克隆项目
git clone https://gitee.com/y_project/RuoYi-Vue

# 进入项目目录
cd ruoyi-ui

# 安装依赖
npm install

# 建议不要直接使用 cnpm 安装依赖，会有各种诡异的 bug。可以通过如下操作解决 npm 下载速度慢的问题
npm install --registry=https://registry.npm.taobao.org

但如果node-saas安装不上，则只能：
```
npm install -g cnpm --registry=https://registry.npm.taobao.org

cnpm install node-sass
```

# 启动服务
npm run dev
```

浏览器访问 http://localhost:80

## 发布

```bash
# 构建测试环境
npm run build:stage

# 构建生产环境
npm run build:prod
```

## 修改配置

```
1、build:prod的输出目录
修改：vue.config.js
- outputDir: 'dist',
- outputDir: '../demo-with-admin/src/main/resources/static',
- outputDir: '../demo-with-usercenter/src/main/resources/static',

2、请求公共参数
修改：request.js

3、登录相关接口
修改：login.js

4、菜单接口
修改：menu.js

```



聊天页面：https://learnku.com/articles/65044
http://wiki.linyiyuan.top/#/./docs/Lemon/%E6%A8%A1%E5%9D%97%E4%BB%8B%E7%BB%8D

http://june000.gitee.io/lemon-im/