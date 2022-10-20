import {createRouter, createWebHistory} from "vue-router";
import HomeView from "../views/HomeView.vue";
import CreateQuiz from "../views/CreateQuiz.vue";
import Login from "../views/Login.vue";
import readView from "../views/ReadView.vue";

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
      name: "create",
      component: CreateQuiz,
    },
    {
      path: "/login",
      name: "login",
      component: Login,
    },
    {
      path: "/read/:quizId",
      name: "read",
      component: readView,
      props: true,
    }
  ],
});

export default router;
