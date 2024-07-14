<script setup>

import LightCard from "@/components/LightCard.vue";
import {Calendar, CollectionTag, Edit, EditPen, Link} from "@element-plus/icons-vue";
import Weathier from "@/components/Weathier.vue";
import {computed, reactive, ref} from "vue";
import {get} from "@/net";
import {ElMessage} from "element-plus";
import TopicEditor from "@/components/TopicEditor.vue";

const weather = reactive({
  location: {},
  now: {},
  hourly: [],
  success: false
})



const editor = ref(false)

const today = computed(() => {
  const date = new Date()
  const weekDays = ['日', '一', '二', '三', '四', '五', '六'];
  return `${date.getFullYear()} 年 ${date.getMonth() + 1} 月 ${date.getDate()} 日 星期${weekDays[date.getDay()]}`
})
/**
 * 获取当前位置（谷歌浏览器不好使）
 */
navigator.geolocation.getCurrentPosition(position => {
  debugger
  const longitude = position.coords.longitude
  const latitude = position.coords.latitude
  get(`/api/forum/weather?longitude=${longitude}&latitude=${latitude}`, data =>{
    debugger
    Object.assign(weather, data)
    weather.success = true
  })
}, error =>{
  console.info(error)
  // ElMessage.warning('位置信息获取超时，请检查网络设置')
  get(`/api/forum/weather?longitude=121.593474&latitude=38.948706`, data =>{
    Object.assign(weather, data)
    weather.success = true
  })
}, {
  timeout: 3000,
  enableHighAccuracy: true
})
</script>

<template>
  <div style="display: flex;margin: 20px auto;gap: 20px;max-width: 900px;">
    <div style="flex: 1">
      <light-card>
        <div class="create-topic" @click="editor = true">
          <el-icon><EditPen/></el-icon> 点击发表主题...
        </div>
      </light-card>
      <light-card style="margin-top: 10px;height: 30px;">

      </light-card>
      <div style="margin-top: 10px;display: flex;flex-direction: column;gap: 10px;">
        <light-card style="height: 150px" v-for="item in 10">

        </light-card>
      </div>

    </div>
    <div style="width: 280px;">
      <div style="position: sticky;top: 20px">
        <light-card>
          <div style="font-weight: bold">
            <el-icon><CollectionTag/></el-icon>
            论坛公告
          </div>
          <el-divider style="margin: 10px 0;"/>
          <div style="font-size: 14px;margin: 10px;color: grey;">
            欢迎来到我们的论坛！在这里，你可以与其他用户交流、分享经验和知识。请遵守论坛规则，尊重他人，保持友善和礼貌。如果你有任何问题或建议，请随时联系我们。
          </div>
        </light-card>
        <light-card style="margin-top: 10px;">
          <div style="font-weight: bold">
            <el-icon><Calendar/></el-icon>
            天气信息
          </div>
          <div style="font-size: 10px;color: grey">谷歌浏览器无法获取当前位置的天气,请使用其他浏览器</div>
          <el-divider style="margin: 10px 0;"/>
          <weathier :data="weather"/>
        </light-card>
        <light-card style="margin-top: 10px">
          <div class="info-text">
            <div>当前日期 :</div>
            <div>{{today}}</div>
          </div>
          <div class="info-text">
            <div>当前ip地址 :</div>
            <div>127.0.0.1（写死了还没改）</div>
          </div>
        </light-card>
        <div style="font-size: 14px;margin-top: 10px;color: grey">
          <el-icon><Link/></el-icon>
          友情链接
          <el-divider style="margin: 10px 0"/>
        </div>
        <div style="display: grid;grid-template-columns: repeat(2, 1fr);grid-gap: 10px;margin-top: 10px;">
          <div class="friend-link">
            <el-image style="height: 100%" src="https://element-plus.org/images/js-design-banner.jpg"></el-image>
          </div>
          <div class="friend-link">
            <el-image style="height: 100%" src="https://element-plus.org/images/js-design-banner.jpg"></el-image>
          </div>
          <div class="friend-link">
            <el-image style="height: 100%" src="https://element-plus.org/images/js-design-banner.jpg"></el-image>
          </div>

        </div>
      </div>
    </div>
    <topic-editor :show="editor" @success="editor = false" @close="editor = false"/>
  </div>
</template>

<style lang="less" scoped>
.info-text {
  display: flex;
  justify-content: space-between;
  color: grey;
  font-size: 14px;
}
.friend-link {
  border-radius: 5px;
  overflow: hidden;
}
.create-topic {
  background-color: #efefef;
  border-radius: 5px;
  height: 40px;
  color: grey;
  font-size: 14px;
  line-height: 40px;
  padding: 0 10px;

  &:hover {
    cursor: pointer;
  }
}
.dark .create-topic {
  background-color: #232323;
}
</style>
