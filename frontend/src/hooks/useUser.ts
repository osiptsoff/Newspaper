import { $host } from "@/api";
import axios from "axios";

export interface User {
    name: string,
    lastname: string,
    login: string,
    password: string
}
export const createUser = async (newUser: User) => {
    try {
        const response = await $host.post("/auth/register", newUser, {});
        //const response = await axios.post("http://localhost:8080/api/auth/register", newUser, {});
        console.log(response)
        return response;
    } catch (e) {
        return e;
    }
}
export const loginUser = async (logUser: User) => {
    try {
        const response = await $host.post("/auth", logUser);
        return response;
    } catch (e) {
        return e;
    }
};
export const refreshUser = async () => {
    const response = await $host.get("/auth");
    return response.data;
}
export const logoutUser = async (logoutAUser: any) => {
    try {
        const response = await $host.post("/auth/logout", logoutAUser);
        return response;
    } catch (e) {
        return e;
    }
};