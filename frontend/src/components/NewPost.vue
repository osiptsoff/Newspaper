<template>
  <div class="flex flex-col w-full items-center justify-center min-h-screen p-4">
    <div class="border-4 rounded-2xl border-purple-500">
      <h1 class="font-bold text-xl m-2 text-center mr-auto">
        <input v-model="title" class="w-full h-auto" placeholder="Добавьте заголовок" />
      </h1>
      <button @click="addBlock">+</button>
      <div class="flex w-full">
        <div v-for="(block, index) in blocks" :key="index" class="flex w-full">
          <textarea v-model="block.text" class="w-5/6 h-auto" placeholder="text"></textarea>
          <button class="w-1/6" @click="deleteBlock(index)">-</button>
        </div>
      </div>
      <input v-model="tag" class="" placeholder="tag" />
      <input type="file" @change="onFileChange" class="w-full h-auto" />
    </div>
    <button @click="sendData">cюд</button>
  </div>
</template>


<script setup lang="ts">
import { ref } from 'vue';
import { postNews, addNewsText, addNewsTag, addNewsImg } from '@/hooks/usePost'
let file;

const title = ref('');
const blocks = ref([{ blockNumber: 1, text: '' }]);
const tag = ref('');

async function onFileChange(event) {
  file = event.target.files[0];
}

async function addBlock() {
  blocks.value.push({ blockNumber: blocks.value.length + 1, text: '' });
}

async function deleteBlock(index) {
  blocks.value.splice(index, 1);
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
