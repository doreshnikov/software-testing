<template>
  <div class='header'>
    <md-toolbar class='md-primary'>
      <div class='md-toolbar-section-start'>
        <md-tabs class='md-primary' md-sync-route>
          <md-tab id='tab-home' md-label='Home' to='/' exact/>
          <div v-if='!isLoggedIn'>
            <md-tab id='tab-register' md-label='Register' to='/register'/>
            <md-tab id='tab-login' md-label='Login' to='/login'/>
          </div>
        </md-tabs>
      </div>
      <div class='md-toolbar-section-end'>
        <md-button class='md-raised md-accent header-error' @click='showFetchError = true' v-if='fetchError !== null'>Error occurred
        </md-button>
        <md-menu class='header-login' v-if='isLoggedIn' md-align-trigger>
          <md-button md-menu-trigger>{{ login }}</md-button>
          <md-menu-content>
            <md-menu-item @click='logout'>Log out</md-menu-item>
          </md-menu-content>
        </md-menu>
      </div>
    </md-toolbar>

    <md-dialog :md-active.sync='showFetchError'>
      <md-dialog-title>Error</md-dialog-title>
      <md-content>
        <p>During the request, the following error occurred:</p>
        <p class='md-error'>{{ fetchError }}</p>
      </md-content>
      <md-dialog-actions>
        <md-button class='md-raised md-primary' @click='showFetchError = false'>Close</md-button>
        <md-button class='md-primary' @click='clearError'>Clear</md-button>
      </md-dialog-actions>
    </md-dialog>
  </div>
</template>

<script>
import {mapGetters, mapState} from 'vuex'

export default {
  name: 'Header',
  data() {
    return {
      showFetchError: false
    }
  },
  computed: {
    ...mapState('alerts', [
      'fetchError',
    ]),
    ...mapState('user', [
      'login'
    ]),
    ...mapGetters('user', [
      'isLoggedIn'
    ])
  },
  methods: {
    logout() {
      this.$http.post('/user/logout').then(() => {
        this.$store.commit('user/logout')
      })
    },
    clearError() {
      this.$store.commit('alerts/setFetchError', null)
      this.showFetchError = false
    }
  }
}
</script>

<style>
.header {
  padding-bottom: 10px;
}

.md-tab-nav-button {
  font-size: 1em !important;
}

.md-tab-nav-button:first-of-type {
  flex: 1;
}

.md-content {
  padding: 16px;
}
</style>