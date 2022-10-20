<script setup lang="ts">
import {defineProps, onMounted, ref} from "vue";
import axios from "axios";

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
  questions: []
});

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
</template>


<style scoped>

</style>
