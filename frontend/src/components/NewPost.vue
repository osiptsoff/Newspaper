<template>
  <Header class="w-screen"/>
  <div class="flex flex-col w-full items-center justify-center min-h-screen p-4 bg-gray-100">
    <div class="border-4 border-purple-400 bg-white shadow-md rounded-2xl p-6 max-w-xs">
      <h1 class="font-bold text-xl mb-4 text-center">
        <input v-model="title" class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:border-blue-500" placeholder="What's on your mind?" />
      </h1>
      <button @click="addBlock" class="px-3 py-2 bg-purple-500 text-white rounded hover:bg-purple-700 mb-2">+</button>
      <div class="space-y-4">
        <div v-for="(block, index) in blocks" :key="index" class="flex justify-between items-center">
          <textarea v-model="block.text" class="w-full px-3 py-2 border border-gray-300 rounded resize-none" placeholder="Write something..." maxlength="255"></textarea>
          <button class="px-3 py-2 bg-orange-400 text-white rounded hover:bg-red-600" @click="deleteBlock(index)">-</button>
        </div>
      </div>
      <div class="mb-4 space-x-2">
        <button @click="addTag" class="px-3 py-2 bg-purple-500 text-white rounded hover:bg-purple-700">+</button>
      <div v-for="(tagItem, tagIndex) in tag" :key="tagIndex" class="flex items-center space-x-2">
        <input v-model="tag[tagIndex]" class="w-full px-3 py-2 border border-gray-300 rounded" placeholder="tag" />
        <button class="px-3 py-2 bg-orange-400 text-white rounded hover:bg-red-600" @click="deleteTag(tagIndex)">-</button>
      </div>
    </div>
    <button @click="sendData" class="mt-4 px-4 py-2 bg-pink-400 text-white rounded hover:bg-pink-600">Post</button>
  </div>
  </div>
</template>


<script setup lang="ts">
import { ref } from 'vue';
import { postNews, addNewsText, addNewsTag, addNewsImg } from '@/hooks/usePost'
import Header from "@/components/Header.vue";
let file;

const title = ref('');
const blocks = ref([{ blockNumber:  1, text: '' }]);
const tag = ref(['']);

async function onFileChange(event) {
  file = event.target.files[0];
}

function addBlock() {
  blocks.value.push({ blockNumber: blocks.value.length +  1, text: '' });
}

function deleteBlock(index) {
  blocks.value.splice(index,  1);
}

function addTag() {
  tag.value.push('');
}
function deleteTag(index) {
  tag.value.splice(index,  1);
}


async function sendData() {
  const newPost = {
    title: title.value,
    content: blocks.value,
    tag: tag.value,
    image: file,
  };
  const result = await postNews(newPost.title);
  if (result && result.status) {
    for (const block of newPost.content) {
      await addNewsText(result.id, block.text, block.blockNumber);
    }
    await addNewsTag(result.id, newPost.tag);
    if (file) {
      await addNewsImg(result.id, file);
    }
  }
}
</script>

<style scoped>
/* Add your styles here */
</style>
