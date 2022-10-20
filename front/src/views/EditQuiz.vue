<script setup lang="ts">
import {useRouter} from "vue-router";
import {defineProps, ref} from "vue";
import axios from "axios";

const router = useRouter();

const props = defineProps({
  quizId: {
    type: [Number, String],
    required: true
  }
});

const quiz = ref({
  description: '',
  answer: '',
  explanation: '',
  examples: []
});

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

const edit = () => {
  axios.patch('/quidev/api/quiz/' + props.quizId,
      quiz.value,
      {
        headers: {
          'Authorization': 'eyJlbWFpbCI6InNoYW5lIiwicGFzc3dvcmQiOiIxMjM0IiwiaXNzdWVkQXQiOlsyMDIyLDEwLDE5LDIxLDEyLDU3LDg5MTM4OTAwMF19'
        }
      }).then(response => {
    router.push({name: "read", params: {quizId: props.quizId}})
  }).catch(function (error) {
    console.log(error);
  });
}
</script>

<template>
  <div>
    <textarea v-model="quiz.description" class="input-group" rows="15" placeholder="Description about the Quiz"/>
  </div>
  <div class="mt-2">
    <input v-model="quiz.answer" class="input-group" type="text" placeholder="The answer of the Quiz"/>
  </div>
  <div class="mt-2">
    <textarea v-model="quiz.explanation" class="input-group" rows="4" type="text"
              placeholder="explanation about the Quiz"/>
  </div>
  <div class="mt-2">
    <input v-model="quiz.examples[0]" class="input-group" type="text" placeholder="example 1"/>
  </div>
  <div class="mt-2">
    <input v-model="quiz.examples[1]" class="input-group" type="text" placeholder="example 2"/>
  </div>
  <div class="mt-2">
    <input v-model="quiz.examples[2]" class="input-group" type="text" placeholder="example 3"/>
  </div>
  <div class="mt-2">
    <button @click="edit()" class="btn btn-success">UPDATE</button>
  </div>
</template>

<style></style>
