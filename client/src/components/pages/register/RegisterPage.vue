<template>
  <md-card class='md-elevation-3'>
    <div class='title'>
      <img src='@/assets/logo.png' alt='Logo'>
      <div class='md-title'>MangaReader Register</div>
      <div class='md-body-1'>Read manga collected from multiple sources</div>
    </div>
    <div class='md-body-2 md-error'>Registration is unavailable at the moment</div>
    <md-button class='md-raised md-accent' @click='goBack'>Go back</md-button>
  </md-card>
</template>

<script>
export default {
  name: 'RegisterPage',
  methods: {
    goBack() {
      this.$router.back()
    }
  }
}
</script>

<style scoped>
.md-card {
  max-width: 500px;
  position: relative;
  margin: 30px auto auto;
  padding: 20px;
}

.title {
  text-align: center;
  margin-bottom: 30px;
}

img {
  margin-bottom: 16px;
  max-width: 80px;
}
</style>