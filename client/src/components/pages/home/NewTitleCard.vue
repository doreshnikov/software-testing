<template>
  <md-card ref='card' class='md-elevation-5'>
    <md-ripple>
      <md-card-header>
        <div class='md-title'>Add title</div>
      </md-card-header>
      <md-button class='md-icon-button md-primary' @click='$router.push("/title/add")'>
        <plus/>
      </md-button>
    </md-ripple>
  </md-card>
</template>

<script>
import Plus from 'vue-material-design-icons/Plus'

export default {
  name: 'NewTitleCard',
  components: {Plus}
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

.md-card-media-cover {
  height: 100%;
}

.md-icon-button {
  width: 80px;
  height: 80px;
}

.md-title {
  padding: 15% 16px;
}

.material-design-icon__svg {
  width: 4em !important;
  height: 4em !important;
  left: calc(50% - 2em) !important;
  bottom: calc(50% - 2.2em) !important;
}
</style>