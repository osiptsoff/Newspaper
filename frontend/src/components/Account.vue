<template>
  <Header class="w-screen"/>
  <div class="h-screen bg-gray-100 p-4">
    <div class="max-w-md mx-auto bg-white rounded-lg shadow-lg p-6">
      <h2 class="text-2xl font-bold mb-4">Информация о пользователе</h2>
      <p v-if="isLoggedIn" class="text-lg font-medium text-gray-800">
        Здравствуйте, {{ userName }} {{ userLastName }}
      </p>
      <p v-if="isLoggedIn" class="text-lg font-medium text-gray-800">
        Ваш логин: {{login}}
      </p>
      <p v-if="isLoggedIn" class="text-lg font-medium text-gray-800">
        Вы: {{userRole}}
      </p>
      <p v-else class="text-lg font-medium text-gray-800 hover:text-pink-500" @click="router.push('/login')">Please log in</p>
    </div>
    <div>избранные новости:</div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { getCookie } from "../hooks/useUser";
import Header from "../components/Header.vue";
import router from "../router";
const userName = getCookie('name');
const userLastName = getCookie('lastName');
const userRole = JSON.parse(getCookie('roles'));
const login = getCookie('login');
const isLoggedIn = computed(() => !!userName && !!userLastName);
</script>