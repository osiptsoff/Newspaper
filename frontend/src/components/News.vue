<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { getNews } from '@/hooks/usePost'

const newsList = ref<Array>([]);

async function loadNews() {
  const response = await getNews();
  newsList.value = response.map((news) => ({
    ...news,
    blocks: news.content.map((block) => ({ blockNumber: block.blockNumber, text: block.text })),
  }));
}

onMounted(loadNews);
</script>


<template>
  <div class="flex flex-col items-center justify-center min-h-screen p-4">
    <div v-for="(news, index) in newsList" :key="index" class="border-4 rounded-2xl border-purple-500">
  <h1 class="font-bold text-xl m-2 text-center mr-auto">{{news.title}}</h1>
  <div v-for="(block, blockIndex) in news.blocks" :key="blockIndex" class="flex items-center">
  <p>  <img class="w-1/6 float-left"  src="/public/favicon.ico"> {{block.text}}</p>
  </div>
      <p class="text-blue-600">{{ news.tag }}</p>
      <div class="inline-flex space-x-2 justify-end float-right mr-3 my-1">
        <p class=" text-purple-600 font-bold">date</p>
        <img class="size-1/6" src="/public/com.png">
        <img class="size-1/6" src="/public/likeact.png">
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>