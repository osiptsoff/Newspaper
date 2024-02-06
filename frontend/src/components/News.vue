<script setup lang="ts">

</script>


<template>
  <div class="flex flex-col items-center justify-center min-h-screen p-4">
    <div  class="border-4 rounded-2xl border-purple-500">
  <h1 class="font-bold text-xl m-2 text-center mr-auto">{{}}</h1>
  <div   class="flex items-center">
  <p>  <img class="w-1/6 float-left"  src="/public/favicon.ico"> {{}}</p>
  </div>
      <p class="text-blue-600">{{}}</p>
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