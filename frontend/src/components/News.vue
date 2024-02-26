<script setup lang="ts">
import {onMounted, ref} from "vue";
import {allNews, getMoreContent, getNewsById, getPicture} from "@/hooks/usePost";
import {comment} from "postcss";

const content = ref([]);
const newsDyId = ref({});
const showDropdown = ref(false);
const moreText =ref([]);
const showComment = ref(false);
const imageUrl = ref({});
async function fetchNews() {
  try {
    const response = await allNews(0);
    content.value = response.data.content;
    return content;
  } catch (e) {
    return e;
  }
}
const toggleDropdown = () => {
  showDropdown.value = !showDropdown.value;
};
const toggleComment = () => {
  showComment.value = !showComment.value;
};
async function expandNews(id) {
  const response = await getNewsById(id);
  const pict = await getPicture(id)
  newsDyId.value = response.res;
  moreText.value = response.text;
  imageUrl.value = pict;
  toggleDropdown();
  return {newsDyId: newsDyId,
    moreText: moreText,
    imageUrl: imageUrl};
}

onMounted(fetchNews);
</script>


<template>
  <div class="flex-column items-center justify-center min-h-screen p-4">
    <div class="inline-flex">
      <div v-for="(item, index) in content" :key="index" class="border-4 rounded-2xl border-purple-500 mb-8"
           @click="expandNews(item.id)">
        <h1 class="font-bold text-xl m-2 text-center mr-auto">{{ item.title }}</h1>
        <p v-for="(items, index) in item.tags" :key="index" class="text-purple-600 font-bold"># {{ items.name }}</p>
        <div class="inline-flex space-x-2 justify-end float-right mr-3 my-1">
          <p class="text-purple-600 text-xs">{{ item.postTime }}</p>
        </div>
      </div>
    </div>
        <div  v-if="showDropdown" class="flex flex-col items-center justify-center  p-4 border-4 rounded-2xl border-purple-500">
          <h1 class="font-bold text-xl m-2 text-center">{{newsDyId.newsSignature.title}}</h1>
          <div class="flex-grow">
            <div class="block w-full">
              <img class="w-1/6 float-left"  :src="imageUrl" alt="">
            <p v-for="(item, index)  in newsDyId.content" :key="index" class="text-pink-500">
           {{item.text}}
            </p>
            <p v-for="(item, index)  in moreText" :key="index" class="text-pink-500">
              {{item.text}}
            </p>
            </div>
              <div v-for="(item, index) in newsDyId.newsSignature.tags" :key="index">
               # {{item.name}}
              </div>
            <div class="flex-col">
            <div class="inline-flex space-x-2 justify-end float-right mr-3 my-1">
              <p class=" text-purple-600 font-bold text-xs">{{newsDyId.newsSignature.postTime}}</p>
              <img class="size-6" src="/public/com.png" @click="toggleComment">
             <p class="inline-flex"><img class="size-6" src="/public/like.png">{{newsDyId.likesCount}}</p>
            </div>
            </div>
          </div>
          <div v-if="showComment">
            <div v-if="newsDyId.comments.length">
              <div v-for="(item, index) in newsDyId.comments" :key="index" class="text-purple-800">
                <div><strong>{{item.author}}</strong></div>
                <div>{{item.text}}</div>
                <div class="text-xs">{{item.postTime}}</div>
                <div class="picture">
                </div>
              </div>
            </div>
          </div>
      </div>
    </div>
</template>





<style scoped>

</style>