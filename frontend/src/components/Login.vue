

<template>
  <div>
    <div
      class="w-full bg-white rounded-b-lg border border-t-0 rounded-tr-lg xl:p-0"
    >
      <div class="p-6 space-y-4 md:space-y-6 sm:p-8">
        <form class="space-y-4 md:space-y-6" @submit.prevent="sendData()">
          <div>
            <label
              for="login"
              class="block mb-2 text-sm font-medium text-gray-900"
              >login</label
            >
            <input
            v-model="login"
              type="text"
              name="login"
              id="login"
              class="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
              placeholder="ivan228"
              required=""
            />
          </div>
          <div>
            <label
              for="password"
              class="block mb-2 text-sm font-medium text-gray-900"
              >пароль</label
            >
            <div class="relative">
              <input
                v-model="password"
                type="password"
                name="password"
                id="password"
                placeholder="••••••••"
                class="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
                required=""
              />
            </div>
          </div>
          <button
            type="submit"
            class="w-full text-white bg-purple-600 hover:bg-orange-400 focus:ring-4 focus:outline-none transition-all-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center"
          >
            Войти
          </button>
          <p class="text-sm font-light text-gray-500">
            Нет аккаунта?
            <a
              @click="isSign()"
              class="font-medium cursor-pointer text-primary-600 hover:text-[#02346f]"
              >Зарегистрируйтесь</a
            >
          </p>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang='ts'>
import { ref, getCurrentInstance } from "vue";
import {loginUser, User} from "@/hooks/useUser";

const instance = getCurrentInstance();

 const isHovered = ref(false);
let login = ref("");
let password = ref("");

const isSign = () => {
  instance.emit("createUser", true);
};

const sendData = async () => {
  const loginUser = {
   login: login.value,
    password: password.value,
  };
};
</script>

<style scoped>
</style>