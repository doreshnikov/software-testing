<template>
  <div>
    <md-subheader>
      <md-button class='md-raised' @click='loadTitles()'>Refresh</md-button>
    </md-subheader>
    <new-title-card v-if='isLoggedIn'></new-title-card>
    <title-card v-for='t in titles' :key='t.id' :title='t'></title-card>
  </div>
</template>

<script>
import TitleCard from '@/components/pages/home/TitleCard'
import NewTitleCard from '@/components/pages/home/NewTitleCard'
import {mapActions, mapGetters, mapState} from 'vuex'

export default {
  name: 'HomePage',
  components: {NewTitleCard, TitleCard},
  computed: {
    ...mapGetters('user', [
      'isLoggedIn'
    ]),
    ...mapState('titles', [
      'titles'
    ])
  },
  created() {
    this.loadTitles()
  },
  methods: {
    ...mapActions('titles', [
      'loadTitles'
    ])
  }
}
</script>

<style scoped>
</style>