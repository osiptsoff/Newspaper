import { defineStore } from "pinia";
import { ref } from "vue";

interface User {
  login: string;
  name?: string;
  lastName?: string;
  role: Array<{value:string}>;
}

export const useUserStore = defineStore("userStore", () => {
  const user = ref<User>({
    name: "",
    login: "",
    lastName: "",
    role: [],
  });
  const setUserData = (lastName: string, name: string, login: string, role: []) => {
    user.value.name = name;
    user.value.login = login;
    user.value.lastName =  lastName;
    user.value.role = role;
  };

  const deleteUserData = () => {
    user.value.name = "";
    user.value.login = "";
    user.value.lastName = "";
    user.value.role = [];
  };

  return {
    user,
    setUserData,
    deleteUserData,
  };
});
