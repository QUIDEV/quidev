<template>
  <header>
    <nav class="navbar navbar-expand-lg bg-light">
      <div class="container-fluid">
        <a class="navbar-brand" href="#">Quidev</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav">
            <li class="nav-item">
              <RouterLink class="nav-link active" aria-current="page" to="/myquiz">MyQuiz</RouterLink>
            </li>
            <li class="nav-item">
              <RouterLink class="nav-link" to="/create">AddQuiz</RouterLink>
            </li>
            <li class="nav-item">
              <RouterLink class="nav-link" to="/login">login</RouterLink>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  </header>
</template>

<script setup lang="ts">
</script>

<style scoped>
header {
  font-size: 1.3em;
  margin-bottom: 1rem;
}
</style>
