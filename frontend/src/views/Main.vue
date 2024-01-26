<template>
  <div class="flex flex-col h-screen w-full">
    <Header/>
    <main class="flex flex-wrap">
      <section class="md:w-3/4 p-4">
        <article class="mb-4">

        </article>
        <!-- Добавьте больше статей по мере необходимости -->
      </section>
    </main>
    <footer class="absolute bottom-0 w-full bg-purple-800 text-white p-1 text-xs">
      <p>© 2024 Моя Газета</p>
    </footer>
  </div>
</template>

<script setup lang="ts">
import Header from "@/components/Header.vue";
</script>