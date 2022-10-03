import {createRouter, createWebHistory} from "vue-router";
import HomeView from "../views/HomeView.vue";
import CreateQuiz from "../views/CreateQuiz.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: "/",
            name: "home",
            component: HomeView,
        },
        {
            path: "/create",
            name: "home",
            component: CreateQuiz,
        },
    ],
});

export default router;
