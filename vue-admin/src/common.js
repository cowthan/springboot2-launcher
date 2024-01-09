import { Loading, Message } from 'element-ui'
//
// function loading(fullscreen, target, text) {
//   const loadingOptions = {
//     target: target, // 可以是标签名，class带., id带# ，会被传入document.querySelector以获取到对应 DOM 节点
//     text: text, // 显示在加载图标下方的加载文案
//     spinner: 'el-icon-loading',
//     lock: false,
//     fullscreen: fullscreen,
//     body: !fullscreen,
//     background: 'rgba(0, 0, 0, 0.7)'
//   }
//   const loadingInstance = Loading.service(loadingOptions)
//   return loadingInstance
//   // this.$nextTick(() => { // 以服务的方式调用的 Loading 需要异步关闭
//   //     loadingInstance.close();
//   // });
// }

export default {
  baseUrl: process.env.VUE_APP_BASE_API, // 本地

  confirm(vueObj, msg, type) {
    if (!type) type = 'warning'
    return vueObj.$confirm(msg, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: type
    })
  },
  // alertOk(vueObj, msg) {
  //   vueObj.$message({
  //     type: 'success',
  //     message: msg
  //   })
  // },
  // alertError(vueObj, msg) {
  //   vueObj.$message({
  //     type: 'error',
  //     message: msg
  //   })
  // },
  alertOk2(msg) {
    Message({
      type: 'success',
      message: msg,
      duration: 3 * 1000
    })
  },
  alertError2(msg) {
    Message({
      type: 'error',
      message: msg,
      duration: 3 * 1000
    })
  }

}
