import axios from "axios"

export const $host = axios.create({
    baseURL: 'http://localhost:8080/api/api',
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json'
    }
})