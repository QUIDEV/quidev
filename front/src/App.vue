<script setup lang="ts">
import {RouterLink, RouterView} from "vue-router";</script>

<template>
  <header>
    <nav>
      <RouterLink to="/">Home</RouterLink><br/>
      <RouterLink to="/create">Create</RouterLink><br/>
      <RouterLink to="/login">login</RouterLink>
    </nav>
  </header>

  <RouterView/>
</template>

<style>
</style>
