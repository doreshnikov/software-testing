<template>
  <div class='aspect-ratio-container' :style='style'>
    <div class='aspect-ratio-data'>
      <slot/>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AspectRatioContainer',
  props: {
    ratio: Number
  },
  computed: {
    style() {
      return `padding-bottom: ${this.ratio * 100}%`
    }
  }
}
</script>

<style scoped>
.aspect-ratio-container {
  height: 0;
  overflow: hidden;
  position: relative;
}

.aspect-ratio-data {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}
</style>