<template>
  <div class="flex flex-col min-h-full w-full">
    <Header/>
    <main class="flex">
      <section class="flex flex-col items-center justify-center">
          <News class="w-3/4"/>
      </section>
    </main>
    <footer class="grid-rows-none w-full bg-purple-800 text-white p-1 text-xs">
      <p>© 2024 Newspaper</p>
    </footer>
  </div>

</template>

<script setup lang="ts">
import Header from "@/components/Header.vue";
import News from "@/components/News.vue";

</script>