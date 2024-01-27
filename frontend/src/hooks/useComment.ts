import { $host} from "@/api";

export const getComment = async () => {
    try {
        const response = await $host.get("/news?pageNumber=0");
        return response.data;
    } catch (e) {
        return e;
    }
};