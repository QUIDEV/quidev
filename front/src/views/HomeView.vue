<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";
import {useRouter} from "vue-router";

const quizzes = ref([]);
const router = useRouter();

axios.get('/quidev/api/quiz',
    {
      headers: {
        'Authorization': 'eyJlbWFpbCI6InNoYW5lIiwicGFzc3dvcmQiOiIxMjM0IiwiaXNzdWVkQXQiOlsyMDIyLDEwLDE5LDIxLDEyLDU3LDg5MTM4OTAwMF19'
      }
    }).then(response => {
  response.data.body.content.forEach((q: any) => {
    quizzes.value.push(q);
  });
}).catch(function (error) {
  console.log(error);
});

const moveToRead = () => {
  router.push({name: "read"})
}
</script>

<template>
  <ul>
    <li v-for="quiz in quizzes" :key="quiz.id">
      <div class="description">
        <router-link :to="{name: 'read', params: { quizId: quiz.id}}">{{ quiz.description }}</router-link>
      </div>
      <div class="submitter">
        {{ quiz.submitterName }}
      </div>
    </li>
  </ul>

</template>

<style scoped>

ul {
  list-style-type: none;
  margin: 0;
}

li {
  margin-bottom: 1rem;
}

li:last-child {
  margin-bottom: 0;
}

li .description a {
  font-size: 1.3em;
  margin-bottom: 0.5rem;
  text-decoration: none;
  color: royalblue;
}

li .submitter {
  color: #666;
}
</style>
