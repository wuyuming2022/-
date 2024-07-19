<script setup>

import Card from "@/components/Card.vue";
import {Setting} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
import {get, post} from "@/net";
import {ElMessage, ElMessageBox} from "element-plus";
import StaffDetail from "@/views/staff/StaffDetail.vue";

const editor = ref(false)
const condition = reactive({
  name: '',
  gender: '',
  department: '',
  page: 0
})
const table = reactive({
  loading: false,
  data: []
})

function getStaffList() {
  table.loading = true
  post('/api/staff/list-staff', condition, data => {
    table.data.length = 0
    for (let i = 0; i < data.length; i++) {
      table.data.push(data[i])
    }
    table.loading = false
  })
}

getStaffList()
function onStaffCreate(){
  editor.value = false
  getStaffList()
}
function deleteStaff(id) {
  ElMessageBox.confirm(
      '确认删除该员工吗?', '提示',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }
  ).then(() => {
    get('/api/staff/delete-staff?id=' + id, () => {
      ElMessage.success("删除成功！")
      getStaffList()
    })
  })
}


</script>

<template>
  <div style="margin: 20px;">
    <card :icon="Setting" title="员工管理"
          desc="在这里可以管理公司所有的员工哦！（供查询或编辑）" v-loading="">
      <div>
        <div style="display: flex;justify-content: flex-end;margin-right: 20px">
          <el-button @click="getStaffList" type="primary">查询</el-button>
          <el-button type="success">重置</el-button>
        </div>
        <div style="margin-top: 20px;">
          <el-form :inline="true" :model="condition" class="demo-form-inline">
            <el-form-item label="员工姓名">
              <el-input v-model="condition.name" placeholder="请填写员工姓名" clearable/>
            </el-form-item>
            <el-form-item label="性别">

              <el-select v-model="condition.gender" placeholder="请选择员工性别" style="width: 240px;" clearable>
                <el-option value="0" label="男"></el-option>
                <el-option value="1" label="女"></el-option>
              </el-select>

            </el-form-item>
            <el-form-item label="所属部门">
              <el-input v-model="condition.department" placeholder="先选择员工所属部门" clearable/>
            </el-form-item>
          </el-form>
        </div>
      </div>


    </card>
    <card :icon="Setting" title="查询结果" style="margin-top: 20px;"
          desc="在这里显示查询结果列表" v-loading="">
      <div class="staff-table">
        <el-table :data="table.data" style="width: 100%" v-loading="table.loading">
          <el-table-column fixed prop="name" label="员工姓名" width="150"/>
          <el-table-column prop="gender" label="性别" width="120"/>
          <el-table-column prop="department" label="所属部门" width="120"/>
          <el-table-column fixed="right" label="操作" min-width="120">
            <template #default="scope">
              <el-button link type="primary" size="small" @click="editor = true">编辑</el-button>
              <el-button link @click="deleteStaff(scope.row.id)" type="danger" size="small">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </card>

    <staff-detail :show="editor" @success="onStaffCreate" @close="editor = false"/>
  </div>
</template>

<style scoped>
.staff-table {
  margin-top: 20px;
}
</style>
