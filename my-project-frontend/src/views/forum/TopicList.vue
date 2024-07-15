<script setup>

import LightCard from "@/components/LightCard.vue";
import {Calendar, Clock, CollectionTag, Edit, EditPen, Link} from "@element-plus/icons-vue";
import Weathier from "@/components/Weathier.vue";
import {computed, reactive, ref} from "vue";
import {get} from "@/net";
import TopicEditor from "@/components/TopicEditor.vue";
import {useStore} from "@/store";
import axios from "axios";

const store = useStore()

const weather = reactive({
  location: {},
  now: {},
  hourly: [],
  success: false
})

const editor = ref(false)
const list = ref(null)

const today = computed(() => {
  const date = new Date()
  const weekDays = ['日', '一', '二', '三', '四', '五', '六'];
  return `${date.getFullYear()} 年 ${date.getMonth() + 1} 月 ${date.getDate()} 日 星期${weekDays[date.getDay()]}`
})
get('/api/forum/types',data => store.forum.types = data)

function updateList(){
  get('/api/forum/list-topic?page=0&type=0', data => list.value = data)
}

updateList()

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
        <light-card  v-for="item in list" class="topic-card">
          <div>
            <div style="display: flex">
              <div>
                <el-avatar :size="30" :src="`${axios.defaults.baseURL}/images${item.avatar}`"/>
              </div>
              <div style="margin-left: 7px;transform: translateY(-2px)">
                <div style="font-size: 13px;font-weight: bold">{{item.username}}</div>
                <div style="font-size: 12px;color: grey">
                  <el-icon><Clock/></el-icon>
                  <div style="margin-left: 2px;display: inline-block;transform: translateY(-2px)">{{new Date(item.time).toLocaleString()}}</div>
                </div>
              </div>
            </div>
          </div>
          <div style="margin-top: 5px;">
            <div class="tipic-type"
                 :style="{
                  color: store.findTypeById(item.type)?.color + 'EE',
                  'border-color': store.findTypeById(item.type)?.color + '77',
                  'background': store.findTypeById(item.type)?.color + '33',
                 }">
              {{store.findTypeById(item.type).name}}
            </div>
            <span style="font-weight: bold;margin-left: 7px;">{{item.title}}</span>
          </div>
          <div class="top-content">{{item.text}}</div>
          <div style="display: grid;grid-template-columns: repeat(3, 1fr);grid-gap: 10px;">
            <el-image class="topic-image" v-for="img in item.images" :src="img" fit="cover"></el-image>
          </div>
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
    <topic-editor :show="editor" @success="editor = false;updateList()" @close="editor = false"/>
  </div>
</template>

<style lang="less" scoped>
.topic-card {
  padding: 15px;
  transition: scale .3s;

  &:hover {
    scale: 1.015;
    cursor: pointer;
  }


  .top-content {
    font-size: 13px;
    color: grey;
    margin: 5px 0;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 3;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  .tipic-type {
    display: inline-block;
    border: solid 0.5px grey;
    border-radius: 3px;
    font-size: 12px;
    padding: 0 5px;
    height: 18px;
  }
  .topic-image {
    width: 100%;
    height: 100%;
    border-radius: 5px;
    max-height: 110px;
  }
}
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
