<template>
  <Header class="w-screen"/>
  <div class="flex flex-col w-full items-center justify-center min-h-screen p-4 bg-gray-100">
    <div class="border-4 border-purple-400 bg-white shadow-md rounded-2xl p-6 max-w-xs">
      <h1 class="font-bold text-xl mb-4 text-center">
        <input v-model="title" class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:border-blue-500" placeholder="What's on your mind?" />
      </h1>
      <div class="space-y-4">
        <div  class="flex justify-between items-center">
          <input v-model="text" class="w-full px-3 py-2 border border-gray-300 rounded resize-none" placeholder="Write something..."/>
        </div>
      </div>
<!--      <div v-for="(tag, index) in selectedTags" :key="index">-->
<!--        {{ tag }}-->
<!--      </div>-->
      <select v-model="newTag"  class="bg-blue-400">
        <option v-for="tag in tags" :key="tag">{{ tag }}</option>
      </select>
<!--      <button @click="addTag">Add</button>-->
    <button @click="sendData" class="mt-4 px-4 py-2 bg-pink-400 text-white rounded hover:bg-pink-600">Post</button>
  </div>
  </div>
</template>


<script setup lang="ts">
import {onMounted, ref, watch} from 'vue';
import { postNews, addNewsText, addNewsTag, addNewsImg, Post } from '@/hooks/usePost'
import Header from "@/components/Header.vue";
import {getAllTag} from "@/hooks/tag";

const title = ref("");
const text = ref("");
const tags = ref([]);

onMounted(async () => {
  const response = await getAllTag();
  if (response.data.map(obj => obj.name)) {
    tags.value = response.data.map(obj => obj.name);
  }
});
// function addTag() {
//   if (newTag.value.trim() !== '' && !selectedTags.value.some(tag => tag.name === newTag.value.trim())) {
//     selectedTags.value.push({ name: newTag.value.trim()})
//     newTag.value = ''
//   }
// }

async function sendData() {
  await postNews(title.value);
  await addNewsText(text.value);
  await addNewsTag();

}
</script>

<style scoped>
/* Add your styles here */
</style>
