<template>
  <div>
    <title-preview :title='loadedTitle' v-if='isLoaded'/>
    <md-progress-spinner :md-stroke='3' :md-diameter='30' md-mode='indeterminate' v-else/>
  </div>
</template>

<script>
import TitlePreview from '@/components/pages/title/TitlePreview'

export default {
  name: 'TitlePage',
  components: {TitlePreview},
  props: {
    id: Number
  },
  computed: {
    loadedTitle() {
      return this.isLoaded ? this.title : {}
    }
  },
  data() {
    return {
      isLoaded: false,
      title: Object
    }
  },
  watch: {
    '$route.params': {
      handler() {
        if (!isNaN(this.id)) {
          this.loadTitle()
        }
      },
      immediate: true,
    }
  },
  created() {
    if (isNaN(this.id)) {
      this.goHome({error: `Invalid title id ${this.id}`})
    }
  },
  methods: {
    async loadTitle() {
      await this.$http.get(`/titles/${this.id}`).then(r => {
        this.title = r.data
      }).catch(err => {
        this.goHome(err)
      })
      this.isLoaded = true
    },
    goHome(err) {
      this.$store.commit('alerts/setFetchError', err.error)
      this.$router.replace('/')
    }
  }
}
</script>

<style scoped>

</style>