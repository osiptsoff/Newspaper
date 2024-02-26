import {$host} from "@/api";
import Cookie from 'js-cookie';
import {getCookie, deleteCookie} from "@/hooks/useUser";
export interface Post {
    title: string,
    content: {
        blockNumber: number,
        text: string
    },
    tag: string,
    id: number,
    allId: [],
    image: HTMLImageElement,
    pageNumber?: number
    moreText: [];
}

export const allNews = async (pageNumber) => {
    try {
        const response = await $host.get(`/news?pageNumber=${pageNumber}`);
        return response;
    }catch (e) {
        return e;
    }
};
export const getNewsById = async (id) => {
    try {
       const response = await $host.get(`/news/${id}`)
        if (response.data.isLastContentPage === false){
           const moreText = await getMoreContent(id);
           return {res: response.data,
           text: moreText};
        }
        return {res: response.data};
    } catch (e) {
        return e;
    }
};
export const getMoreContent = async (id) => {
    let i =  0;
    let isLast;
    let allContent = [];
    do {
        try {
            const response = await $host.get(`/news/${id}/content?pageNumber=${i}`);
            isLast = response.data.isLast;
            allContent = [...allContent, ...response.data.content];
            i++;
        } catch (e) {
            return e;
        }
    } while (!isLast);

    return allContent;
};
export const getPicture =  async (id) => {
    try {
        const response = await $host.get(`/news/${id}/image`, {headers:
                {
                    "Content-Type": "multipart/form-data"
                }})
        console.log(response)
        return response;
    } catch (e) {
        return e;
    }

}



export const postNews = async (title: string) => {
    const token = getCookie('token');
    try {
        const response = await $host.post(`/news`, { title }, {
            headers: {
                Authorization: "Bearer " + token,
            },
        });
        Cookie.set('indexNewPost', response.data.id)
        console.log(response.data.id)
        console.log(getCookie('indexNewPost'))
        return response.data;
    } catch (e) {
        return e;
    }
};

export const addNewsText = async (text) => {
    const token = getCookie('token');
    const newsId = getCookie('indexNewPost') ;
    const blocks = [];
    for (let i = 0; i < text.length; i += 255) {
        blocks.push({
            blockNumber: Math.floor(i / 255) + 1,
            text: text.slice(i, i + 255)
        });
    }
    try {
        const response = await $host.post(`/news/${newsId}/content`, blocks, {
            headers: {
                Authorization: "Bearer " + token
            }
        });
        return response.data;
    } catch (e) {
        return e;
    }
};

export const addNewsTag = async () => {
    const token = getCookie('token');
    const newsId = getCookie('indexNewPost');
    try {
        const response = await $host.post(`/news/${newsId}/tag`, {
            "tag": name,
            "belongs": true
        }, {
            headers: {
                Authorization: "Bearer " + token
            }
        });
        console.log(response.data)
        return response.data;
    } catch (e) {
        return e;
    }
};


export const addNewsImg = async () => {
    const newsId = getCookie('indexNewPost');
    const token = getCookie('token');
    try {
        const formData = new FormData();
        formData.append('image',  HTMLImageElement);
        const response = await $host.post(`/news/${newsId}/image`, formData, {
            headers: {
                Authorization: "Bearer " + token,
                'Content-Type': 'multipart/form-data'
            }
        });
        return response.data;
    } catch (e) {
        return e;
    }
};





//
//
// export const deleteNews = async (newsId: number) => {
//     const token = getCookie('token');
//     try {
//         const response = await $host.delete(`/news/${newsId}`, {
//             headers: {
//                 Authorization: "Bearer " + token
//             }
//         });
//         return response.data;
//     } catch (e) {
//         return e;
//     }
// };
// export const deleteNewsPic = async (newsId:number) => {
//     const token = getCookie('token');
//     try {
//         const response = await $host.delete(`/news/${newsId}/image`, {
//             headers: {
//                 Authorization: "Bearer " + token,
//             }
//         });
//         return response.data;
//     } catch (e) {
//         return e;
//     }
// };
// export const deleteNewsContent = async (newsId:number) => {
//     const token = getCookie('token');
//     try {
//         const response = await $host.delete(`/news/${newsId}/content`, {
//             headers: {
//                 Authorization: "Bearer " + token
//             }
//         });
//         return response.data;
//     } catch (e) {
//         return e;
//     }
// }