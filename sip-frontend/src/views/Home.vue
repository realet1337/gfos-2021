<template>
  <div class="home">
    <img alt="Vue logo" src="../assets/logo.png">
    <test/>
  </div>
</template>

<script>
// @ is an alias to /src
import test from '@/components/test.vue'

export default {
  name: 'Home',
  components: {
    test
  }
}
</script>
