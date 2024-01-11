<template>
  <div class="app-container">
    <el-row :gutter="20">

      <!--用户数据-->
      <el-col>
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="关键词" prop="keyword">
            <el-input
              v-model="queryParams.keyword"
              placeholder="请输入关键词"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        


        <el-table v-loading="loading" :data="dataList">
          <el-table-column label="序号" align="center" key="id" prop="id" />
          <el-table-column label="账号" align="center" key="username" prop="username" />
          <el-table-column label="昵称" align="center" key="user.nickname" prop="user.nickname" :show-overflow-tooltip="true" />
          <el-table-column label="Token" align="center" key="token" prop="token" :show-overflow-tooltip="true" />
          <el-table-column label="设备" align="center" key="device" prop="device"  />
          <el-table-column label="头像" align="center" >
            <template slot-scope="scope">
              <a :href="scope.row.user.avatar" target="_blank" class="buttonText">
                <el-avatar :size="size" :src="scope.row.user.avatar"></el-avatar>
              </a>
            </template>
          </el-table-column>
          <el-table-column label="失效时间" align="center" prop="dieTime"  width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.dieTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" align="center" prop="gmtCreate"  width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.gmtCreate) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            width="160"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                v-if="scope.row.id !== 1"
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleKickoff(scope.row)"
                v-hasPermi="['system:user:remove']"
              >强制下线</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>

  </div>
</template>

<script>
import { listSessions, kickoff } from "./api-session.js";
import { getToken } from "@/utils/auth";

export default {
  name: "User",
  components: {  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 用户表格数据
      dataList: null,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 默认密码
      initPassword: undefined,
      // 日期范围
      dateRange: [],
      // 状态数据字典
      statusOptions: [],
      // 性别状态字典
      sexOptions: [],
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() }
        
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        keyword: undefined
      }
    };
  },
  watch: {
  },
  created() {
    this.getList();
    
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listSessions(this.queryParams).then(response => {
          this.dataList = response.data.list;
          this.total = response.data.totalCount;
          this.loading = false;
        }
      );
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 下线按钮操作 */
    handleKickoff(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认强制编号为"' + ids + '"的会话下线?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return kickoff(ids);
      }).then(() => {
        this.getList();
        this.msgSuccess("下线成功");
      })
    },
    
  }
};
</script>
