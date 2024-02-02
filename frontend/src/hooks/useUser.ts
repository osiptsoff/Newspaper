import { $host } from "@/api";
import {useUserStore} from "@/stores/userStore";
export interface User {
    name: string,
    lastName: string,
    login: string,
    password: string
}

export const createUser = async (newUser: User) => {
    try {
        const response = await $host.post("/auth/register", newUser);
        return response;
    } catch (e) {
        return e;
    }
}

export const loginUser = async (logUser: User) => {
    try {
        const response = await $host.post("/auth", logUser);
        const userStore = useUserStore();
        userStore.setUserData(response.data.id, response.data.name, response.data.login, response.data.lastName);
        return response;
    } catch (e) {
        return e;
    }
};

export const refreshToken = async () => {
    const refreshToken = localStorage.getItem("refreshToken");
    try {
        const response = await $host.post('api/auth', { refreshToken }, {
            headers: {
                'Content-Type': 'application/json'
            }
        });
        return response.data;
    } catch (e) {
        return e;
        throw e;
    }
};

export const logoutUser = async (logoutAUser: any) => {
    try {
        const response = await $host.post("/auth/logout", logoutAUser);
        return response;
    } catch (e) {
        return e;
    }
};
