//
// import { defineStore } from 'pinia';
//
// interface NewsItem {
//     id: number;
//     title: string;
//     content: string[];
//     tags: string[];
//     image?: File | null;
// }
//
// export const useNewsStore = defineStore('news', {
//     state: () => ({
//         newsList: [] as NewsItem[],
//         loading: false,
//         error: null as Error | null,
//     }),
//     actions: {
//         async fetchNews() {
//             this.loading = true;
//             this.error = null;
//             try {
//                 const response = await allNews();
//                 this.newsList = response.data;
//             } catch (error) {
//                 this.error = error;
//             } finally {
//                 this.loading = false;
//             }
//         },
//         addNewsItem(newsItem: Omit<NewsItem, 'id'>) {
//             this.newsList.unshift({
//                 id: Date.now(),
//                 ...newsItem,
//             });
//         },
//         removeNewsItemById(id: number) {
//             this.newsList = this.newsList.filter((item) => item.id !== id);
//         },
//     },
// });
