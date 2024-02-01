import { $host } from "@/api";

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
        userStore.setUserData(response.data.id, response.data.role, response.data.name, response.data.login);
        return response;
    } catch (e) {
        return e;
    }
};


export const refreshUser = async (newUserData: User) => {
    try {
        const response = await $host.get("/auth", newUserData);
        return response.data;
    } catch (e) {
        return e;
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

// import { $host } from "@/api";
//
// export interface User {
//     name: string,
//     lastName: string,
//     login: string,
//     password: string
// }
// export const createUser = async (newUser: User) => {
//     try {
//         const response = await $host.post("/auth/register", newUser, {});
//         return response;
//     } catch (e) {
//         return e;
//     }
// }
// export const loginUser = async (logUser: User) => {
//     const token = localStorage.getItem("jwt");
//     try {
//         const response = await $host.post("/auth", logUser);
//         return response;
//     } catch (e) {
//         return e;
//     }
// };
// export const refreshUser = async () => {
//     const response = await $host.get("/auth");
//     return response.data;
// }
// export const logoutUser = async (logoutAUser: any) => {
//     const token = localStorage.getItem("jwt");
//     try {
//         const response = await $host.post("/auth/logout", logoutAUser)
//         return response;
//     } catch (e) {
//         return e;
//     }
// };