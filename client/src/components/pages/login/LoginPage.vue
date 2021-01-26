<template>
  <md-card class='md-elevation-3'>
    <div class='title'>
      <img src='@/assets/logo.png' alt='Logo'>
      <div class='md-title'>MangaReader Login</div>
      <div class='md-body-1'>Read manga collected from multiple sources</div>
    </div>
    <div class='form'>
      <md-field>
        <label>Login</label>
        <md-input v-model='formData.login' autofocus></md-input>
      </md-field>
      <md-field md-has-password>
        <label>Password</label>
        <md-input v-model='formData.password' type='password'></md-input>
      </md-field>
      <div class='md-error' v-if='error != null'>
        {{ error }}
      </div>
    </div>

    <div class='actions md-layout md-alignment-center-space-between'>
      <md-button class='md-raised md-primary' @click='authenticate'>Log in</md-button>
    </div>
    <div class='loading-overlay' v-if='isLoading'>
      <md-progress-spinner md-mode='indeterminate' :md-stroke='2'></md-progress-spinner>
    </div>
  </md-card>
</template>

<script>
export default {
  name: 'LoginPage',
  data() {
    return {
      isLoading: false,
      error: null,
      formData: {
        login: '',
        password: ''
      }
    }
  },
  methods: {
    authenticate() {
      this.isLoading = true
      this.error = null
      this.$store.dispatch('user/login', this.formData).then(() => {
        this.$router.replace('/')
      }).catch(err => {
        this.error = err.view
      }).finally(() => {
        this.isLoading = false
      })
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

.actions > .md-button {
  margin-right: 0;
}

.form {
  margin-bottom: 60px;
}
</style>