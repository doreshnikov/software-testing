<template>
  <md-card ref='card' class='md-elevation-3'>
    <md-ripple>
      <md-card-media-cover md-solid>
        <md-card-media>
          <aspect-ratio-container :ratio="1.4">
            <img :src='title.image' alt='Preview'>
          </aspect-ratio-container>
        </md-card-media>
        <md-card-area>
          <md-card-header>
            <div class='md-title'>{{ title.name }}</div>
          </md-card-header>
          <md-card-actions>
            <md-button class='md-dense md-raised md-primary' @click='openTitle'>Explore</md-button>
            <md-button class='md-dense md-raised md-accent md-icon-button' v-if='isLoggedIn' @click='removeThisTitle'>
              <delete/>
            </md-button>
          </md-card-actions>
        </md-card-area>
      </md-card-media-cover>
    </md-ripple>
  </md-card>
</template>

<script>
import Delete from 'vue-material-design-icons/Delete'
import AspectRatioContainer from '@/components/fragments/AspectRatioContainer'

export default {
  name: 'TitleCard',
  components: {AspectRatioContainer, Delete},
  data() {
    return {
      isMounted: false
    }
  },
  computed: {
    isLoggedIn() {
      return this.$store.state.user.login != null
    }
  },
  props: {
    title: Object
  },
  mounted() {
    this.isMounted = true
  },
  methods: {
    openTitle() {
      this.$router.push({
        name: 'title',
        params: {id: this.title.id}
      })
    },
    removeThisTitle() {
      this.$store.dispatch('titles/removeTitle', this.title.id)
    }
  }
}
</script>

<style scoped>
.md-card {
  min-width: 180px;
  width: calc(20vw - 25px);
  min-height: 252px;
  height: calc(28vw - 35px);
  display: inline-block;
  vertical-align: top;
  margin: 10px;
  padding: 0;
}

img {
  width: 100%;
  height: auto;
}

.md-card-media-cover {
  height: 100%;
}

.md-title {
  padding: 4px 16px;
}
</style>