import {createRouter, createWebHistory} from "vue-router";
import QuizList from "../views/QuizList.vue";
import CreateQuiz from "../views/CreateQuiz.vue";
import QuizRead from "../views/QuizRead.vue";
import EditQuiz from "../views/EditQuiz.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/myquiz",
      name: "myQuiz",
      component: QuizList,
    },
    {
      path: "/create",
      name: "create",
      component: CreateQuiz,
    },
    {
      path: "/read/:quizId",
      name: "read",
      component: QuizRead,
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
