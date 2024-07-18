<script setup>

import Card from "@/components/Card.vue";

defineProps({
  show: Boolean
})

const emit = defineEmits(['close', 'success'])

function initEditor(){
  // refEditor.value.setContents('', 'user')
  // editor.title = ''
  // editor.type = null
}
</script>

<template>
  <div>

    <el-drawer :model-value="show"
               direction="rtl"
               @open="initEditor"
               :close-on-click-modal="false"
               @close="emit('close')"
               :size="650">
      <template #header>
        <div style="background-color: var(--el-bg-color)">
          <div style="font-weight: bold">员工详细信息</div>
          <div style="font-size: 13px;">编辑后记得保存哦~ 亲！</div>
        </div>
      </template>
      <div style="display: flex;gap: 10px;">
        <el-form>
          <el-form-item label="员工姓名">
            <el-input />
          </el-form-item>
          <el-form-item label="性别">
            <el-select  placeholder="请选择员工性别" style="width: 240px;" clearable>
              <el-option value="0" label="男"></el-option>
              <el-option value="1" label="女"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="员工部门">
            <el-input/>
          </el-form-item>
        </el-form>
      </div>
    </el-drawer>
  </div>
</template>

<style scoped>

</style>
