<template>
  <div class="min-vh-100">
  <div class="flex flex-col min-h-full w-full">
    <Header/>
    <div class="flex">
      <section class="flex flex-col items-center justify-center w-full">
        <News class="w-3/4"/>
      </section>
    </div>
  </div>
  </div>
</template>

<script setup lang="ts">
import Header from "@/components/Header.vue";
import News from "@/components/News.vue";

</script>