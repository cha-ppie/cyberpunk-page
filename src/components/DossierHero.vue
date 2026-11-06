<script setup>
import ArrowIcon from './ArrowIcon.vue'

defineProps({
  variant: { type: String, required: true },
  title: { type: String, required: true },
  subtitle: { type: String, required: true },
  description: { type: String, required: true },
  image: { type: String, required: true },
  alt: { type: String, default: '' },
  position: { type: String, default: 'center' },
  jump: { type: String, default: '' },
  action: { type: String, default: '查看档案' }
})
</script>

<template>
  <section class="d-hero" :class="`d-hero--${variant}`" aria-labelledby="page-title">
    <div class="d-hero-image"><img :src="image" :alt="alt" :style="{ objectPosition: position }" fetchpriority="high"></div>
    <div class="hero-rupture" aria-hidden="true"><span class="rupture-underprint"></span><span class="rupture-scars"></span></div>
    <div class="d-wrap d-hero-content">
      <h1 id="page-title">{{ title }}</h1>
      <div class="d-hero-copy">
        <h2>{{ subtitle }}</h2>
        <p>{{ description }}</p>
        <RouterLink v-if="jump" class="d-action" :to="{ hash: jump }">{{ action }} <ArrowIcon /></RouterLink>
      </div>
    </div>
  </section>
</template>
