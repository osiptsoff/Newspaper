import {$host} from "@/api";

export interface Post {
    title: string,
    content: {
        blockNumber: number,
        text: string
    },
    tag: string,
    newsId: number,
    image: HTMLImageElement,
}


const token = localStorage.getItem("access");

export const postNews = async (title) => {
    try {
        const response = await $host.post(`/news`, { title }, {
            headers: {
                Authorization: "Bearer " + token,
            },
        });
        return response.data;
    } catch (e) {
        return e;
        throw e;
    }
};

export const addNewsText = async (newsId, text, blockNumber) => {
    try {
        const response = await $host.post(`/news/${newsId}/content`, { blockNumber, text }, {
            headers: {
                Authorization: "Bearer " + token
            }
        });
        return response.data;
    } catch (e) {
        return e;
        throw e;
    }
};

export const addNewsTag = async (newsId, tag) => {
    try {
        const response = await $host.post(`/news/${newsId}/tag`, { tag }, {
            headers: {
                Authorization: "Bearer " + token
            }
        });
        return response.data;
    } catch (e) {
        return e;
        throw e;
    }
};

export const addNewsImg = async (newsId, image) => {
    try {
        const formData = new FormData();
        formData.append('image', image);

        const response = await $host.post(`/news/${newsId}/image`, formData, {
            headers: {
                Authorization: "Bearer " + token,
                'Content-Type': 'multipart/form-data'
            }
        });
        return response.data;
    } catch (e) {
        return e;
        throw e;
    }
};


export const getNews = async () => {
    try {
        const response = await $host.get(`/news`);
        return response.data;
    } catch (e) {
        return e;
        throw e;
    }
};

export const getNewsById = async (newsId) => {
    try {
        const response = await $host.get(`/news/${newsId}`);
        return response.data;
    } catch (e) {
        return e;
        throw e;
    }
};

export const deleteNews = async (newsId) => {
   const token = localStorage.getItem("jwt");
    try {
        const response = await $host.delete(`/news/${newsId}`, {
            headers: {
                Authorization: "Bearer " + token
            }
        });
        return response.data;
    } catch (e) {
        return e;
        throw e;
    }
};
export const deleteNewsPic = async (newsId) => {
    const token = localStorage.getItem("jwt");
    try {
        const response = await $host.delete(`/news/${newsId}/image`, {
            headers: {
                Authorization: "Bearer " + token
            }
        });
        return response.data;
    } catch (e) {
        return e;
        throw e;
    }
};
export const deleteNewsContent = async (newsId) => {
    const token = localStorage.getItem("jwt");
    try {
        const response = await $host.delete(`/news/${newsId}/content`, {
            headers: {
                Authorization: "Bearer " + token
            }
        });
        return response.data;
    } catch (e) {
        return e;
        throw e;
    }
}