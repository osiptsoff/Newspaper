import { $host } from "@/api";
import {getCookie} from "@/hooks/useUser";
export const postComment = async () => {
    const token = getCookie('token');
        try {
            const response = await $host.post(`/comment`, { headers : {
                    Authorization: "Bearer " + token
                }});
            return response.data;
        } catch (e) {
            return e;
        }
    };
export const editComment = async () => {
    const token = getCookie('token');
        try {
            const response = await $host.patch(`/comment`, {
                headers : {
                    Authorization: "Bearer " + token
                }
            });
            return response.data;
        } catch (e) {
            return e;
        }
    };
