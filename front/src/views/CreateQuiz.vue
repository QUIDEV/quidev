<script setup lang="ts">
import {ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";

const answer = ref("")
const description = ref("")
const explanation = ref("")
const example1 = ref("")
const example2 = ref("")
const example3 = ref("")
const router = useRouter()

const save = function () {
  axios.post(
      '/quidev/api/quiz',
      {
        answer: answer.value,
        description: description.value,
        explanation: explanation.value,
        skillId: 1,
        examples: [
          example1.value,
          example2.value,
          example3.value
        ]
      }, {
        headers: {
          'Authorization': 'eyJlbWFpbCI6InNoYW5lIiwicGFzc3dvcmQiOiIxMjM0IiwiaXNzdWVkQXQiOlsyMDIyLDEwLDE5LDIxLDEyLDU3LDg5MTM4OTAwMF19'
        }
      }).then(() => {
    router.replace({name: 'myQuiz'});
  }).catch(function (error) {
    console.log(error);
  });
}

</script>

<template>
  <div>
    <textarea v-model="description" class="input-group" rows="15" placeholder="Description about the Quiz"/>
  </div>
  <div class="mt-2">
    <input v-model="answer" class="input-group" type="text" placeholder="The answer of the Quiz"/>
  </div>
  <div class="mt-2">
    <textarea v-model="explanation" class="input-group" rows="4" type="text" placeholder="explanation about the Quiz"/>
  </div>
  <div class="mt-2">
    <input v-model="example1" class="input-group" type="text" placeholder="example 1"/>
  </div>
  <div class="mt-2">
    <input v-model="example2" class="input-group" type="text" placeholder="example 2"/>
  </div>
  <div class="mt-2">
    <input v-model="example3" class="input-group" type="text" placeholder="example 3"/>
  </div>
  <div class="mt-2">
    <button @click="save()" class="btn btn-primary">SAVE</button>
  </div>
</template>

<style></style>
