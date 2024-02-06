import { $host } from "@/api";
import {getCookie} from "@/hooks/useUser";

interface Tag {
    name: string;
}

export const saveTag = async (tag: Tag) => {
    const token = getCookie('token');
    try {
        const response = await $host.post("/api/tag", tag, {
            headers: {
                Authorization: "Bearer " + token
            }
        });
        return response;
    } catch (e) {
        return e;
    }
};

export const deleteTag = async (tagName: string) => {
    const token = getCookie('token');
    try {
        const response = await $host.delete(`/api/tag?name=${encodeURIComponent(tagName)}`, {
            headers: {
                Authorization: "Bearer " + token
            }
        });
        return response;
    } catch (e) {
        return e;
    }
};
