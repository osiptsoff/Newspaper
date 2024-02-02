<template>
  <div class="w-screen h-screen">
    <Header class="w-screen"/>
    <div class="card w-full bg-base-100 shadow-xl h-full">
      <div class="card-body justify-center content-center">
        <h2 class="block text-2xl mb-3 font-bold">Мой аккаунт</h2>
        <form class="space-y-4 md:space-y-6">
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label
                  for="name"
                  class="block mb-2 text-sm font-medium text-gray-900"
              >Имя</label
              >
              <p id="name" class="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5">{{ userStore.user.name }}</p>
            </div>
            <div>
              <label
                  for="lastName"
                  class="block mb-2 text-sm font-medium text-gray-900"
              >Фамилия</label
              >
              <p id="lastName" class="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5">{{ userStore.user.lastName }}</p>
            </div>
            <div>
              <label
                  for="login"
                  class="block mb-2 text-sm font-medium text-gray-900"
              >login</label
              >
              <p id="login" class="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5">{{ userStore.user.login }}</p>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {getCurrentInstance} from "vue";
import Header from '@/components/Header.vue';
import "vue3-toastify/dist/index.css";
import {onMounted, ref} from "vue";
import {useUserStore} from '@/stores/userStore';
import "vue3-toastify/dist/index.css";
const instance = getCurrentInstance();
const userStore = useUserStore();

let errorMsg = ref("");
let name = ref(userStore.user.name);
let lastName = ref(userStore.user.lastName);
let login = ref(userStore.user.login);
let password = ref("");
let id = ref(userStore.user.id);
const isSign = () => {
  instance.emit("userUpdate", true);
};

</script>
<style scoped>
</style>
