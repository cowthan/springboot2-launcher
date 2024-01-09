import Vue from 'vue'
import VueClipboard from 'vue-clipboard2'
Vue.use(VueClipboard)
import Cookies from 'js-cookie'

import Element from 'element-ui'
import './assets/styles/element-variables.scss'

import '@/assets/styles/index.scss' // global css
import '@/assets/styles/ruoyi.scss' // ruoyi css
import App from './App'
import store from './store'
import router from './router'
import permission from './directive/permission'

import './assets/icons' // icon
import './permission' // permission control
import { parseTime, resetForm, addDateRange, download, handleTree } from "@/utils/ruoyi";
import Pagination from "@/components/Pagination";
// 自定义表格工具扩展
import RightToolbar from "@/components/RightToolbar"


import clipboard from "clipboard";
import VCharts from "v-charts";
import VideoPlayer from "vue-video-player";

import LemonMessageForward from "@/views/layout/components/Chat/components/LemonMessageForward";
import LemonMessageVideo from "@/views/layout/components/Chat/components/LemonMessageVideo";
import LemonMessageLink from "@/views/layout/components/Chat/components/LemonMessageLink";
import lemonMessageText from "@/views/layout/components/Chat/components/lemonMessageText";
// import {
//     parseTime,
//     resetForm,
//     addDateRange,
//     selectDictLabel,
//     selectDictLabels,
//     download,
//     handleTree,
//     getImages
//   } from "@/utils/global";
import { getfilesize } from "@/utils/file";
import LemonIMUI from "lemon-imui";
import "normalize.css/normalize.css"; // A modern alternative to CSS resets
import "../public/static/js/particles.min.js";
import "element-ui/lib/theme-chalk/index.css";
// import "@/styles/index.scss"; // global css
// import "@/styles/ruoyi.scss"; // global css
import "@/icons"; // icon
import "@/permission"; // permission control
import "vue-video-player/src/custom-theme.css";
import "video.js/dist/video-js.css";
import "lemon-imui/dist/index.css";
Vue.use(VideoPlayer);
Vue.use(VCharts);
Vue.use(LemonIMUI);
// 挂在全局的音频文件，播放音频
Vue.prototype.playAudio = messageToneType => {
  let buttonAudio = document.getElementById("eventAudio");
  buttonAudio.setAttribute("src", "../public/static/audio/" + messageToneType);
  buttonAudio.play();
};
Vue.prototype.clipboard = clipboard;
Vue.prototype.copy = function(data, className) {
  let clipboard = new Clipboard("." + className, {
    text: function() {
      return data;
    }
  });
  clipboard.on("success", e => {
    this.$message({ message: "复制成功", showClose: true, type: "success" });
    // 释放内存
    clipboard.destroy();
  });
  clipboard.on("error", e => {
    this.$message({ message: "复制失败,", showClose: true, type: "error" });
    clipboard.destroy();
  });
  this.$forceUpdate();
};
Vue.component(LemonMessageForward.name, LemonMessageForward);
Vue.component(LemonMessageVideo.name, LemonMessageVideo);
Vue.component(LemonMessageLink.name, LemonMessageLink);
Vue.component(lemonMessageText.name, lemonMessageText);


// 全局方法挂载
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.download = download
Vue.prototype.handleTree = handleTree

Vue.prototype.msgSuccess = function(msg) {
    this.$message({ showClose: true, message: msg, type: "success" });
}

Vue.prototype.msgError = function(msg) {
    this.$message({ showClose: true, message: msg, type: "error" });
}

Vue.prototype.msgInfo = function(msg) {
    this.$message.info(msg);
}

// 全局组件挂载
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)

Vue.use(permission)

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */

Vue.use(Element, {
    size: Cookies.get('size') || 'medium' // set element-ui default size
})

Vue.config.productionTip = false

new Vue({
    el: '#app',
    router,
    store,
    render: h => h(App)
})
