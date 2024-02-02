import { defineStore } from "pinia";
import { ref } from "vue";


interface User {
  id: number;
  login: string;
  name: string;
  lastName: string;
}

export const useUserStore = defineStore("userStore", () => {
  const user = ref<User>({
    id: 0,
    name: "",
    login: "",
    lastName: "",
  });
  const setUserData = (id: number, lastName: string, name: string, login: string) => {
    user.value.id = id;
    user.value.name = name;
    user.value.login = login;
    user.value.lastName =  lastName;
  };

  const deleteUserData = () => {
    user.value.id = 0;
    user.value.name = "";
    user.value.login = "";
    user.value.lastName = "";
  };

  return {
    user,
    setUserData,
    deleteUserData,
  };
});
