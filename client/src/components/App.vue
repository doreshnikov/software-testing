<template>
  <div id="app">
    <Header/>
    <keep-alive include='HomePage'>
      <router-view/>
    </keep-alive>

    <md-dialog :md-active='accessError !== null'>
      <md-dialog-title>Access error</md-dialog-title>
      <md-content>
        <p>During execution the following error occurred:</p>
        <p class='md-error' v-if='accessError !== null'>{{ accessError.error }}</p>
      </md-content>
      <md-dialog-actions>
        <md-button class='md-raised md-primary' @click='toLogin'>To Login</md-button>
      </md-dialog-actions>
    </md-dialog>
  </div>
</template>

<script>
import Header from "@/components/fragments/Header"
import {mapState} from 'vuex'

export default {
  name: "App",
  components: {
    Header,
  },
  computed: {
    ...mapState('alerts', [
        'accessError'
    ])
  },
  methods: {
    toLogin() {
      this.$store.commit('alerts/setAccessError', null)
      this.$router.push('/login')
    }
  }
}
</script>

<style>
/*noinspection CssUnknownTarget*/
@import url("https://fonts.googleapis.com/css?family=Material+Icons");

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  padding-bottom: 10px;
}

.material-design-icon__svg {
  width: 1.75em !important;
  height: 1.75em !important;
  left: calc(50% - 0.9em) !important;
  bottom: calc(50% - 1em) !important;
}

img {
  /*noinspection CssUnknownProperty*/
  user-drag: none;
  user-select: none;
  -moz-user-select: none;
  -webkit-user-drag: none;
  -webkit-user-select: none;
  -ms-user-select: none;
}

.md-error {
  color: red;
  text-align: left;
}
</style>

<style scoped>
.md-content {
  padding: 16px;
}
</style>