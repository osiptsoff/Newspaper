import { $host} from "@/api";
export const postComment = async () => {
        try {
            const response = await axios.post(`/comment`);
            return response.data;
        } catch (e) {
            return e;
            throw e;
        }
    };
export const editComment = async () => {
        try {
            const response = await axios.patch(`/comment`);
            return response.data;
        } catch (e) {
            return e;
            throw e;
        }
    };
