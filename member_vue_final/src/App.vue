<template>
  <div class="container mt-4">
    <router-view></router-view>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'App'
})
</script>

<style>
body {
  font-family: Avenir, Helvetica, Arial, sans-serif;
}
</style>