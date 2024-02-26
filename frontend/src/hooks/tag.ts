import { $host } from "@/api";
import {getCookie} from "@/hooks/useUser";

interface tag {
    name: string;
}
export const getAllTag = async () => {
    try {
        const response = await $host.get("/tag");
        return response;
    } catch (e) {
        return e;
    }
};

export const saveTag = async (tag) => {
    const token = getCookie('token');
    try {
        const response = await $host.post("/api/tag", { body: {
                "name": "tag"
            }}, {
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
