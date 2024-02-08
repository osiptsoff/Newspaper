<script setup lang="ts">
import router from "@/router";
import { ref, onMounted } from "vue";
import {infoUser, logoutUser, getCookie} from "@/hooks/useUser";
import {last} from "@volar/typescript/lib/typescript/core";

const active = ref('user');
const showDropdown = ref(false);
const login = ref("")
const name = ref("")
const lastName = ref("")
const roles = ref("")

onMounted(async () => {
  await infoUser();
  login.value = getCookie('login');
  name.value = getCookie('name');
  lastName.value = getCookie('lastName');
  roles.value = getCookie('roles');
});

const logout = async () => {
  await logoutUser();
};

const toggleDropdown = () => {
  showDropdown.value = !showDropdown.value;
}




</script>

<template>
  <div class="inline-flex bg-purple-800 text-white space-x-3 p-1 justify-between max-h-10">
    <div class="inline-flex" @click="$router.push('/')" >
      <img class="w-8 h-8" src="/public/favicon.ico" alt="-">
      <h1 class="text-2xl font-bold ml-1">News</h1>
    </div>
    <div @click="toggleDropdown" class="">
      <img class="w-8 h-8 mr-0 ml-auto" src="/img.png">
      <div v-if="showDropdown" class="card h-full w-full bg-base-100 shadow-xl">
        <div class="card-body bg-gray-200">
          <div class="flex mb-6 justify-center" v-if="active === 'user' "></div>
          <h2 class="block text-2xl font-bold" v-if="login">{{name +' '+ lastName}}</h2>
          <hr>
          <button class="w-full text-start text-md py-2 px-3 border-y hover:bg-gray-700 rounded-none" :class="{ 'bg-gray-800 font-semibold': active === 'user' }" @click="router.push('/login')"  v-if="!login">Войти</button>
          <button @click="logout" class="w-full text-start -mt-2 text-md py-2 px-3 border-y hover:bg-gray-700 rounded-none"  v-if="login">Выйти</button>
        </div>
      </div>
    </div>
  </div>
</template>
