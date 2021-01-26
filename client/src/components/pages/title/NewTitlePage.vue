<template>
  <md-card class='md-elevation-3'>
    <div class='title'>
      <img src='@/assets/mal-logo.png' alt='Logo'>
    </div>
    <div class='form'>
      <md-field>
        <label>MAL Link</label>
        <md-input v-model='url' autofocus></md-input>
      </md-field>
    </div>

    <div v-if='isLoaded'>
      <title-preview :title='loadedTitle'></title-preview>
    </div>
    <div v-else-if='urlValid'>
      <md-progress-spinner md-mode='indeterminate' :md-diameter='30' :md-stroke='3'></md-progress-spinner>
    </div>
    <div v-if='!isLoaded && !urlValid && url || error'>
      <p class='md-error'>{{ errorLabel }}</p>
    </div>

    <div class='actions md-layout md-alignment-center-space-between'>
      <md-button class='md-raised md-primary' :disabled='!isLoaded' @click='addTitle'>Add</md-button>
    </div>
  </md-card>
</template>

<script>
import TitlePreview from '@/components/pages/title/TitlePreview'

function checkUrl(url) {
  let regex = /^(https?:\/\/)?myanimelist.net\/manga\/(?<loc>[/\w\d-]+)$/g
  return regex.exec(url)
}

export default {
  name: 'NewTitlePage',
  components: {TitlePreview},
  data() {
    return {
      url: '',
      isLoaded: false,
      title: Object,
      error: null
    }
  },
  computed: {
    loadedTitle() {
      return this.isLoaded ? this.title : {}
    },
    urlEntered() {
      return this.url.length > 0
    },
    urlValid() {
      let match = checkUrl(this.url)
      if (match !== null) {
        this.loadTitle(match.groups.loc)
        return true
      }
      return false
    },
    urlLocator() {
      let match = checkUrl(this.url)
      return match !== null ? match.groups.loc : null
    },
    errorLabel() {
      return this.urlValid ? this.error : 'Invalid url'
    }
  },
  watch: {
    url() {
      this.isLoaded = false
    }
  },
  methods: {
    loadTitle(locator) {
      this.error = null
      this.$http.get(`/titles/mal/${locator}`).then(r => {
        if (locator === this.urlLocator) {
          this.title = r.data
        }
      }).catch(err => {
        this.error = err.error
      }).finally(() => {
        this.isLoaded = true
      })
    },
    addTitle() {
      this.$store.dispatch('titles/addTitle', this.title).then(() => {
        this.$router.back()
      }).catch(err => {
        this.error = err.error
      })
    }
  }
}
</script>

<style scoped>
.md-card {
  max-width: 80%;
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
  max-height: 50px;
}
</style>