import { $host} from "@/api";

export const getComment = async () => {
    try {
        const response = await $host.get("/news");
        return response.data;
    } catch (e) {
        return e;
    }
};