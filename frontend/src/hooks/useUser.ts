import { $host } from "@/api";
import Cookies from 'js-cookie';
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
export const refreshToken = async () => {
    const token = getCookie('token');
    try {
        const response = await $host.post('api/auth/refresh', token,{
            headers: {
                'Content-Type': 'application/json',
                'Authorization': "Bearer " + token
            }
        });
        Cookies.set('token', response.data.value);
        return response.data;
    } catch (e) {
        return e;
    }
};

export const loginUser = async (logUser: User) => {
    try {
        const response = await $host.post("/auth" , logUser);
        Cookies.set('token', response.data.value);
        const token = response.data.value;
        if (response.data.type === "refresh"){
            await refreshToken(token)
            return response
        }
        return response;
    } catch (e) {
        return e;
    }
};


export const getCookie = function getCookie(name) {
    let matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}


export const logoutUser = async (logoutAUser: any) => {
    try {
        const response = await $host.post("/auth/logout", logoutAUser);
        return response;
    } catch (e) {
        return e;
    }
};
