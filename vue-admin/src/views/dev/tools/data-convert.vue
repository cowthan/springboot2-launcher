<template>
  <div class="warp-box">
    <el-card style="width: 50%">
      <div slot="header">数据转换</div>
    <el-row :gutter="15">
      <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="130px">
      <el-col :span="24">
          <el-form-item label="内容" prop="base64">
            <el-input v-model="formData.base64" placeholder="请输入" clearable :style="{width: '100%'}">
            </el-input>
              <span class='form-tip'>{{ hint }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="内容类型" prop="type">
            <el-radio-group v-model="formData.type" size="mini" @change="onRadioChange">
              <el-row>
                <el-col v-for="(item, index) in typeOptions" :key="index" :span="6" style="margin-bottom: 10px;">
              <el-radio :key="index" :label="item.value"
                :disabled="item.disabled" border>{{item.label}}</el-radio>
                </el-col>
                </el-row>
            </el-radio-group>
          </el-form-item>
        </el-col>


        <el-col :span="24">
          <el-form-item size="large">
            <el-button type="primary" @click="submitForm">提交</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    </el-card>
    <el-card style="width: 50%; margin-left: 12px;font-size: 1em;font-family: SFMono-Regular,Consolas,Liberation Mono,Menlo,Courier,monospace;"
      ><div slot="header">返回内容</div>
      <pre class='response'>{{ res }}</pre>
    </el-card>
  </div>
</template>
<script>
import request from '@/utils/request'



function formatDateTime(date, format) {
  if(!date) date = new Date();
  const o = {
    'M+': date.getMonth() + 1, // 月份
    'd+': date.getDate(), // 日
    'h+': date.getHours() % 12 === 0 ? 12 : date.getHours() % 12, // 小时
    'H+': date.getHours(), // 小时
    'm+': date.getMinutes(), // 分
    's+': date.getSeconds(), // 秒
    'q+': Math.floor((date.getMonth() + 3) / 3), // 季度
    S: date.getMilliseconds(), // 毫秒
    a: date.getHours() < 12 ? '上午' : '下午', // 上午/下午
    A: date.getHours() < 12 ? 'AM' : 'PM', // AM/PM
  };
  if (/(y+)/.test(format)) {
    format = format.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
  }
  for (let k in o) {
    if (new RegExp('(' + k + ')').test(format)) {
      format = format.replace(
        RegExp.$1,
        RegExp.$1.length === 1 ? o[k] : ('00' + o[k]).substr(('' + o[k]).length)
      );
    }
  }
  return format;
}

export default {
  components: {},
  props: [],
  data() {
    return {
      res: "",
      hint: "填普通文本",
      formData: {
        type: 'string',
        base64: 'abc123'
      },
      rules: {
        type: [{
          required: true,
          message: '不能为空',
          trigger: 'change'
        }],
        base64: [{
          required: true,
          message: '不能为空',
          trigger: 'change'
        }],

      },
      typeOptions: [
       {
        "label": "普通文本",
        "value": 'string',
        "defaultValue": '这只是普通文本',
        'hint': '普通文本',
      },{
        "label": "Hex串",
        "value": 'hex',
        "defaultValue": 'ad7b000003e8204400800521080302999999000047656e2c3430312c61619435c565',
        'hint': 'Hex串',
      },{
        "label": "Base64串",
        "value": 'base64',
        "defaultValue": 'rXsAAAPoIEQAgAUhCAMCmZmZAABHZW4sNDAxLGFhlDXFZQ==',
        'hint': 'Base64串',
      },{
        "label": "十进制数",
        "value": 'rad10',
        "defaultValue": '19',
        'hint': '十进制数',
      },{
        "label": "十六进制数",
        "value": 'rad16',
        "defaultValue": '0x13',
        'hint': '十六进制数',
      },{
        "label": "二进制数",
        "value": 'rad2',
        "defaultValue": '1001',
        'hint': '二进制数',
      }
      ],
    }
  },
  computed: {},
  watch: {},
  created() {


  },
  mounted() {},
  methods: {
    submitForm() {
      this.$refs["elForm"].validate((valid) => {
        if (!valid) return;
        console.log(this.formData);
        //this.res = this.formData;
        // TODO 提交表单
        request({
          url: '/api/tools/dataConvert',
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
    onRadioChange() {
      for(var i = 0; i<this.typeOptions.length; i++){
        if(this.typeOptions[i].value === this.formData.type){
            this.formData.base64 = this.typeOptions[i].defaultValue;
            this.hint = this.typeOptions[i].hint;
        }
      }
      //console.log("select -> " + this.formData.type)
    }
  }
}

</script>
<style>
.warp-box {
  display: flex;
  padding: 12px;
}
.response {
    margin-top: 0;
    margin-bottom: 1em;
    overflow: auto;
    font-size: 1em;
    font-family: SFMono-Regular,Consolas,Liberation Mono,Menlo,Courier,monospace;
    color: rgba(0,0,0,.65);
    font-variant: tabular-nums;
    line-height: 1.5;
    list-style: none;
    font-feature-settings: "tnum";
}
</style>
