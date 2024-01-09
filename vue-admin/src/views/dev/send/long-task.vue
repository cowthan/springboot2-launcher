<template>
  <div class="warp-box">
    <el-card style="width: 50%">
      <div slot="header">长耗时任务</div>
    <el-row :gutter="15">
    <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="120px">
      <el-form-item label="任务启动" prop="enable">
        <el-radio-group v-model="formData.enable" size="medium">
          <el-radio v-for="(item, index) in enableOptions" :key="index" :label="item.value"
            :disabled="item.disabled">{{item.label}}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="任务类型" prop="taskType">
        <el-input v-model="formData.taskType" placeholder="请输入任务类型"  clearable
          :style="{width: '100%'}"></el-input>
      </el-form-item>
       <el-form-item label="任务参数" prop="taskParam">
        <el-input v-model="formData.taskParam" placeholder="请输入任务参数"  clearable
          :style="{width: '100%'}"></el-input>
      </el-form-item>
      <el-form-item label="消息类型" prop="msgType">
        <el-radio-group v-model="formData.msgType" size="medium">
          <el-radio v-for="(item, index) in messageTypeOptions" :key="index" :label="item.value"
            :disabled="item.disabled">{{item.label}}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item size="large">
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>
    </el-row>
    </el-card>
    <el-card style="width: 50%; margin-left: 12px;font-size: 1em;font-family: SFMono-Regular,Consolas,Liberation Mono,Menlo,Courier,monospace;"
      ><div slot="header">返回内容</div>
      <div class='response' id='response'></div>
    </el-card>
  </div>
</template>
<script>
import request from '@/utils/request'
export default {
  components: {},
  props: [],
  data() {
    return {
      // ws://${host}:${port}${endpoint}?uid=${clientId}
      //websorcketUrl: "ws://localhost:3333/ws/conn?uid=sendALot",
      websorcketUrl: process.env.VUE_APP_WS_API,
      socket: "",
      res: "",
      formData: {
        enable: false,
        taskType: 1,
        taskParam: 20,
        msgType:'COLLECTION',
      },
      rules: {
        enable: [{required: true, message: '不能为空', trigger: 'change' }],
        taskType: [{required: true, message: '不能为空', trigger: 'change' }],
        taskParam: [{required: true, message: '不能为空', trigger: 'change' }],
      },
      enableOptions: [{
        "label": "启动",
        "value": true
      }, {
        "label": "不启动",
        "value": false
      }],
      messageTypeOptions: [{
        "label": "类型1",
        "value": "1"
      }, {
        "label": "类型2",
        "value": "类型2"
      }, {
        "label": "类型3",
        "value": "类型3"
      }],

    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {
    this.init()
  },
  methods: {
    init: function () {
        if(typeof(WebSocket) === "undefined"){
                alert("您的浏览器不支持socket")
        }else{

            if(!this.websorcketUrl.startsWith('ws')){
                  this.websorcketUrl = 'ws://' + location.host + '/ws/conn?uid=sendALot';
            }
            // 实例化socket
            this.socket = new WebSocket(this.websorcketUrl)
            // 监听socket连接
            this.socket.onopen = this.open
            // 监听socket错误信息
            this.socket.onerror = this.error
            // 监听socket消息
            this.socket.onmessage = this.onMsg
        }
    },
    open: function () {
        console.log("socket连接成功")
    },
    error: function () {
        console.log("连接错误")
    },
    onMsg: function (msg) {
        var m = JSON.parse(msg.data);
        if(m.topic == 'sendALot'){
          console.log(m.topic + "--" + m.data)
          var elements = document.getElementsByClassName('response')

          let paragraph = document.createElement('p');
          paragraph.innerText = m.data + "\n";
          elements[0].appendChild(paragraph)

         // var div=this.$refs.response;
	        //div.scrollTop = div.scrollHeight;

          //var objDiv = document.getElementById("response");
         elements[0].scrollTop = elements[0].scrollHeight;
        }
    },
    // 发送消息给被连接的服务端
    send: function (params) {
        this.socket.send(params)
    },
    close: function () {
        console.log("socket已经关闭")
    },
    destroyed () {
        // 销毁监听
        this.socket.onclose = this.close
    },
    submitForm() {
      this.$refs["elForm"].validate((valid) => {
        if (!valid) return;
        request({
          url: '/api/tools/longTask',
          method: 'post',
          data: this.formData
        }).then(res => {
            this.res = res.data
        }).catch(error => {
            console.dir(error);
            this.res = error.response.data
        })
      });
    },
    resetForm() {
      this.$refs['elForm'].resetFields()
    },
  }
}

</script>
<style>
.response {
  height: 600px;
  margin-top: 0;
  margin-bottom: 1em;
  overflow: auto;
  font-size: 0.7em;
  font-family: SFMono-Regular, Consolas, Liberation Mono, Menlo, Courier, monospace;
  color: rgba(0, 0, 0, .65);
  font-variant: tabular-nums;
  line-height: 1;
  list-style: none;
  font-feature-settings: "tnum";
}
</style>
