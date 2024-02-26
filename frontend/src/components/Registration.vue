
<template>
  <div>
    <div
      class="w-full bg-white rounded-b-lg border border-t-0 rounded-tr-lg xl:p-0"
    >
      <div class="p-6 space-y-4 md:space-y-6 sm:p-8">
        <form class="space-y-4 md:space-y-6" @submit.prevent="sendData()">
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label
                for="username"
                class="block mb-2 text-sm font-medium text-gray-900"
                >имя</label
              >
              <input
                v-model="name"
                type="text"
                name="name"
                id="username"
                class="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
                placeholder="Иван"
                required=""
              />
            </div>
            <div>
              <label
                for="lastname"
                class="block mb-2 text-sm font-medium text-gray-900"
                >фамилия</label
              >
              <input
                v-model="lastName"
                type="text"
                name="lastName"
                id="lastName"
                class="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
                placeholder="Иванов"
                required=""
              />
            </div>
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
                placeholder="Ivan228"
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
                  :class="{ 'border-red-500': passwordNotConfirmed }"
                  placeholder="••••••••"
                  class="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
                  required=""
                />
                <a
                    v-if="password.length > 0"
                  class="absolute cursor-pointer tooltip top-1 right-3"
                >
                </a>
              </div>
              <div>
                <label for="confirmPassword" class="block mb-2 text-sm font-medium text-gray-900">Подтвердите пароль</label>
                <input v-model="confirmPassword" type="password" name="confirmPassword" id="confirmPassword" :class="{ 'border-red-500': !passwordsMatch }" class="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5" placeholder="••••••••" required />
              </div>
            </div>
            <div>
            </div>
          </div>
          <button
            type="submit"
            @click="sendData" :disabled="!passwordsMatch"
            class="w-full text-white bg-purple-600 hover:bg-orange-400 focus:ring-4 focus:outline-none transition-all-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center"
          >
            Зарегистрироваться
          </button>
          <p class="text-sm font-light text-gray-500">
            Уже есть аккаунт?
            <a
              @click="isSign()"
              class="font-medium cursor-pointer text-primary-600 hover:text-[#02346f]"
              >Войдите</a
            >
          </p>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang='ts'>
import { ref, getCurrentInstance, computed } from "vue";
import { useRouter } from "vue-router";
import "vue3-toastify/dist/index.css";
import { User, createUser } from "@/hooks/useUser";
const router = useRouter();


const instance = getCurrentInstance();
let passwordNotConfirmed = ref(false);

let errorMsg = ref("");
let name = ref("");
let lastName = ref("");
let login = ref("");
let password = ref("");

const isSign = () => {
  instance.emit("createUser", true);
};
const confirmPassword = ref('');
const passwordsMatch = computed(() => password.value === confirmPassword.value);
const sendData = async () => {
  errorMsg.value = "";
    const newUser: User = {
      login: login.value,
      name: name.value,
      lastName: lastName.value,
      password: password.value,
    };
    await createUser(newUser);

};
</script>

<style scoped>
</style>