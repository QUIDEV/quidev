declare var bootstrap: any;

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

const moveTo = (param: any) => {
  router.push({name: param, params: {quizId: quiz.value.id}})
}

const deleteQuiz = () => {
  axios.delete('/quidev/api/quiz/' + props.quizId,
      {
        headers: {
          'Authorization': 'eyJlbWFpbCI6InNoYW5lIiwicGFzc3dvcmQiOiIxMjM0IiwiaXNzdWVkQXQiOlsyMDIyLDEwLDE5LDIxLDEyLDU3LDg5MTM4OTAwMF19'
        }
      }).then(response => {
    document.querySelector('.modal-backdrop')?.remove();
    router.push({name: "myQuiz"})
  }).catch(function (error) {
    console.log(error);
  });
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
  <table class="table">
    <thead>
    <button class="btn btn-success btn-sm" @click="moveTo('create')">New</button>
    <button class="btn btn-warning btn-sm" @click="moveTo('edit')">Edit</button>
    <button class="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#deleteModal">Delete</button>
    </thead>
    <tbody>
    <tr>
      <th scope="col">Skill</th>
      <td>{{ quiz.skill }}</td>
    </tr>
    <tr>
      <th scope="col">Description</th>
      <td>{{ quiz.description }}</td>
    </tr>
    <tr>
      <th scope="row">Answer</th>
      <td>{{ quiz.answer }}</td>
    </tr>
    <tr>
      <th scope="row">Explanation</th>
      <td>{{ quiz.explanation }}</td>
    </tr>
    <tr>
      <th scope="row">Last Modified</th>
      <td>{{ quiz.updatedDate }}</td>
    </tr>
    </tbody>
  </table>

  <!-- Delete Modal -->
  <div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel">Confirm Deletion</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          Do you really want to delete the quiz?
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
          <button class="btn btn-danger" @click="deleteQuiz()">Delete</button>
        </div>
      </div>
    </div>
  </div>

</template>

<style scoped>
td {
  white-space: break-spaces;
}

thead button {
  margin-left: 5px;
}
</style>
