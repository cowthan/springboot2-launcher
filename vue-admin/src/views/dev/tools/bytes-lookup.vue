<template>
  <div class="wrap-box22">
    <el-card style="width: 100%">
      <div slot="header">字节流</div>

      <el-row :gutter="15">

      <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="130px">
        <el-form-item label="字节流的Base64" prop="base64">
                <el-input
                  v-model="formData.base64"
                  placeholder="请输入字节流的base64"
                  clearable
                  :style="{ width: '100%' }"
                >
                </el-input>
              </el-form-item>
        <el-form-item size="large">
          <el-button type="primary" @click="submitForm">提交</el-button>
        </el-form-item>
      </el-form>

      </el-row>
    </el-card>
    <el-card style="width: 100%; margin-top: 12px;font-size: 1em;font-family: SFMono-Regular,Consolas,Liberation Mono,Menlo,Courier,monospace;"
      ><div slot="header">十六进制显示</div>
      <pre class='response'>{{ res }}</pre>
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
      res: "",
      formData: {
        base64: 'rXsBAAAGIGl1ABwAAAABVFQsRHd2NDVfMEEwQTMsONVlazbIARYEWIw9BFiMPQAAr8LFZa17AQAABiBpdYAcAAAAAVRULER3djQ1XzBBMEE0LDjVZWs2yAEWBFiMPgRYjD4AANFbxWWtewEAAAYgaXYAHAAAAAFUVCxEd3Y0NV8wQTBBNSw41WVrNsgBDARYjD8EWIw',
      },
      rules: {},
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
        request({
          url: '/api/tools/bytesLookup',
          method: 'post',
          data: this.formData
        }).then(res => {
          console.log(res);
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
.wrap-box2 {
  display: flex;
  padding: 12px;
}
</style>
