<script setup lang="ts">
import {ref} from "vue";
import axios from "axios";

let email = ref("");
let password = ref("");
const login = function () {
  let formData = new FormData();
  formData.append("username", email.value);
  formData.append("password", password.value);
  axios({
    method: 'post',
    url: '/api/login',
    data: formData,
  }).then(function (response) {
    console.log(response);
  }).catch(function (error) {
    console.log(error);
  });
}
</script>

<template>
  <main>
    <div>
      <h1>Login</h1>
      <form @submit.prevent="login">
        <label for="email">email</label>
        <input type="text" id="email" v-model="email" required placeholder="email">
        <label for="password">Password</label>
        <input type="password" id="password" v-model="password" required>
        <button class="btn btn-success" type="submit">Login</button>
      </form>
    </div>
  </main>
</template>
