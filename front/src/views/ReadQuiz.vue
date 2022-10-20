<script setup lang="ts">
import {defineProps, onMounted, ref} from "vue";
import axios from "axios";
import router from "@/router";

const props = defineProps({
  quizId: {
    type: [Number, String],
    required: true
  }
});

const quiz = ref({
  id: '',
  description: '',
  submitterName: '',
  examples: []
});

const moveToEdit = () => {
  router.push({name: "edit", params: {quizId: quiz.value.id}})
}

onMounted(() => {
  axios.get('/quidev/api/quiz/' + props.quizId,
      {
        headers: {
          'Authorization': 'eyJlbWFpbCI6InNoYW5lIiwicGFzc3dvcmQiOiIxMjM0IiwiaXNzdWVkQXQiOlsyMDIyLDEwLDE5LDIxLDEyLDU3LDg5MTM4OTAwMF19'
        }
      }).then(response => {
    quiz.value = response.data.body
  }).catch(function (error) {
    console.log(error);
  });
});
</script>

<template>
  <h3>{{ quiz.description }}</h3>
  <div>answer: {{ quiz.answer }}</div>
  <button class="btn btn-warning btn-sm" @click="moveToEdit()">수정</button>
</template>


<style scoped>

</style>
