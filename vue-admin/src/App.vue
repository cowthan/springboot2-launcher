<template>
  <div id="app">
    <router-view />
    <audio muted hidden src ref="audio" id="eventAudio"></audio>
  </div>
</template>

<script>
export default  {
  name:  'App'
}

</script>
