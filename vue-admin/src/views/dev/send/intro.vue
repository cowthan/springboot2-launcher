<template>
  <div class="app-container home">
    <h1>111111111111</h1>

    <p>
      本11111111111
    </p>


  </div>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      baseUrl: process.env.VUE_APP_BASE_API,
    };
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    },
  },
};
</script>

<style scoped lang="scss">
</style>
