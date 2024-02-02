import { $host } from "@/api";
export const postComment = async () => {
        try {
            const response = await $host.post(`/comment`);
            return response.data;
        } catch (e) {
            return e;
        }
    };
export const editComment = async () => {
        try {
            const response = await $host.patch(`/comment`);
            return response.data;
        } catch (e) {
            return e;
        }
    };
