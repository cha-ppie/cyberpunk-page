<script setup>
import ArrowIcon from './ArrowIcon.vue'
defineProps({ title: String, description: String, to: String, action: String })
</script>

<template>
  <section class="d-end"><div class="d-wrap d-end-inner">
    <div><h2>{{ title }}</h2><p>{{ description }}</p></div>
    <RouterLink class="d-action" :to="to">{{ action }} <ArrowIcon /></RouterLink>
  </div></section>
</template>
