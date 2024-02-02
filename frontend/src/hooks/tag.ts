import { $host } from "@/api";
import { useTagStore } from "@/stores/article";

interface Tag {
    name: string;
}

export const saveTag = async (tag: Tag) => {
    try {
        const response = await $host.post("/api/tag", tag, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem('accessToken')}`
            }
        });
        return response;
    } catch (e) {
        return e;
    }
};

export const deleteTag = async (tagName: string) => {
    try {
        const response = await $host.delete(`/api/tag?name=${encodeURIComponent(tagName)}`, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem('accessToken')}`
            }
        });
        return response;
    } catch (e) {
        return e;
    }
};
