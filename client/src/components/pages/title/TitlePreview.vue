<template>
  <div>
    <div class='info-container'>
      <aspect-ratio-container :ratio="1.4">
        <img :src='title.image' alt='Preview'>
      </aspect-ratio-container>
    </div>
    <div class='info-container info-container-wide md-elevation-3'>
      <md-toolbar class='md-elevation-2'>
        <span class='md-title'>{{ title.name }}</span>
      </md-toolbar>
      <md-list class='md-double-line'>
        <md-subheader>Description</md-subheader>
        <md-list-item>
          <md-icon>info</md-icon>
          <div class='md-list-item-text'>{{ title.description }}</div>
        </md-list-item>
        <md-list-item>
          <md-icon>library_books</md-icon>
          <div class='md-list-item-text'>{{ title.chapters + ' chapters' }}</div>
        </md-list-item>
      </md-list>
    </div>
  </div>
</template>

<script>
import AspectRatioContainer from '@/components/fragments/AspectRatioContainer'

export default {
  name: 'TitlePreview',
  components: {AspectRatioContainer},
  props: {
    title: Object
  }
}
</script>

<style scoped>
.info-container {
  min-width: 200px;
  width: 350px;
  max-width: calc(90% - 350px);
  height: auto;
  display: inline-block;
  vertical-align: top;
  overflow: hidden;
  margin: 10px;
  border: 1px solid rgba(0, 0, 0, 0.12);
}

.info-container-wide {
  max-height: 490px;
  overflow-y: auto;
  min-width: 350px;
  width: auto;
}

.info-container-wide .md-list-item-container {
  white-space: normal;
}

.info-container-wide .md-list-item-text {
  white-space: normal;
  overflow: auto;
}

img {
  width: 100%;
  height: auto;
}

.md-progress-spinner {
  position: absolute;
  top: calc(50% - 15px);
  left: calc(50% - 15px);
}

.md-title {
  white-space: normal;
}
</style>