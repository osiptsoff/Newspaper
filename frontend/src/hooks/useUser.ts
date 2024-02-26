import { $host } from "@/api";
import Cookies from 'js-cookie';
export interface User {
    name?: string,
    lastName?: string,
    login: string,
    password: string,
    roles: [string]
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
        const response = await $host.post("/auth/refresh", {
            type: 'refresh',
            value: token,
        }, {
            headers: {
                'Content-Type': 'application/json',
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
        if (response.data.type === "refresh") {
            await refreshToken();
        }
        return response;
    } catch (e) {
        return e;
    }
};


export function getCookie(name) {
    let cookieArr = document.cookie.split(";");

    for(let i =  0; i < cookieArr.length; i++) {
        let cookiePair = cookieArr[i].split("=");

        if(name == cookiePair[0].trim()) {
            return decodeURIComponent(cookiePair[1]);
        }
    }

    return null;
}
export function deleteCookie(name) {
    document.cookie = name + '=; expires=Thu,  01 Jan  1970  00:00:00 UTC; path=/;';
}

export const infoUser = async () => {
    const token = getCookie('token');
    try {
        const response = await $host.get("/user", {
            headers:{
                'Content-Type': 'application/json',
                Authorization: "Bearer "+ token
            }
        });
        Cookies.set('login', response.data.login);
        Cookies.set('name', response.data.name);
        Cookies.set('lastName', response.data.lastName);
        Cookies.set('roles', JSON.stringify(response.data.roles.map(roles => roles.value)));
        return response;
    } catch (e) {
        return e;
    }
};

export const logoutUser = async () => {
    const  token = getCookie('token');
    try {
        const response = await $host.post("/auth/logout",{
            type: 'refresh',
            value: token,
        }, {
            headers: {
                'Content-Type': 'application/json',
            }});
        deleteCookie('login');
        deleteCookie('name');
        deleteCookie('lastName');
        deleteCookie('token');
        deleteCookie('roles');
        location.reload();
        return response;
    } catch (e) {
        return e;
    }
};
