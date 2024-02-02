import axios from "axios"

export const $host = axios.create({
    baseURL: 'http://localhost:8080/api',
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json'
    }
})

import { refreshToken } from '@/hooks/useUser';
import router from "@/router";

axios.interceptors.response.use(
    response => response,
    async error => {
        const originalRequest = error.config;
        if (error.response.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;
            const accessToken = await refreshToken();
            axios.defaults.headers.common['Authorization'] = 'Bearer ' + accessToken;
            return axios(originalRequest);
        }
        return Promise.reject(error);
    }
);
