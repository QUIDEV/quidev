<script setup lang="ts">
import {ref} from "vue";
import axios from "axios";

const answer = ref("")
const description = ref("")

const save = function() {
  axios.post('http://localhost:8080/quiz', {
    answer: answer.value,
    description: description.value
  })
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
    <button @click="save()" class="btn btn-primary">SAVE</button>
  </div>
</template>

<style></style>
