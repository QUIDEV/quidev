import {createRouter, createWebHistory} from "vue-router";
import HomeView from "../views/HomeView.vue";
import CreateQuiz from "../views/CreateQuiz.vue";
import ReadQuiz from "../views/ReadQuiz.vue";
import EditQuiz from "../views/EditQuiz.vue";

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
      path: "/read/:quizId",
      name: "read",
      component: ReadQuiz,
      props: true,
    },
    {
      path: "/edit/:quizId",
      name: "edit",
      component: EditQuiz,
      props: true,
    }
  ],
});

export default router;
