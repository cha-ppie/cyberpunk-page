<script setup>
import { computed, ref, watch } from 'vue'
import { imageUrl } from '../utils/assets'
import initialBuilds from '../../content/builds.json'
import { useContent } from '../composables/useContent'
import { moveTab } from '../utils/tabKeyboard'

const builds = useContent('articles?category=guide', initialBuilds)

const activeIndex = ref(0)
watch(builds, () => { activeIndex.value = Math.min(activeIndex.value, Math.max(0, builds.value.length - 1)) })
const selected = computed(() => builds.value[activeIndex.value])
const selectBuild = (index) => { activeIndex.value = index }
</script>

<template>
  <section id="builds" class="build-explorer d-wrap" aria-labelledby="build-heading">
    <div class="build-explorer-heading">
      <h2 id="build-heading">找到你的打法。</h2>
      <p>选一种战斗节奏，看看装备如何配合。也可以从两种方向里，各拿走一部分。</p>
    </div>
    <div class="build-choices" role="tablist" aria-label="选择玩法方向">
      <button v-for="(build, index) in builds" :id="`build-tab-${build.id}`" :key="build.id" type="button" role="tab" :aria-selected="activeIndex === index" :tabindex="activeIndex === index ? 0 : -1" aria-controls="build-panel" @click="selectBuild(index)" @keydown="moveTab($event, index, builds.length, selectBuild)">
        <span>{{ build.english }}</span><strong>{{ build.name }}</strong>
      </button>
    </div>
    <p v-if="!selected" role="status">暂无已发布的攻略流派。</p>
    <div v-if="selected" id="build-panel" class="build-panel" role="tabpanel" :aria-labelledby="`build-tab-${selected.id}`" tabindex="0">
      <div class="build-feature">
        <div class="build-scene">
          <img :key="selected.id" :src="imageUrl(selected.image)" :alt="selected.alt" loading="lazy">
        </div>
        <div class="build-approach">
          <h3>{{ selected.name }}</h3>
          <p class="build-approach-line">{{ selected.approach }}</p>
          <p class="build-description">{{ selected.description }}</p>
          <dl class="build-loadout"><dt>属性侧重</dt><dd>{{ selected.attributes }}</dd><dt>核心搭配</dt><dd>{{ selected.equipment }}</dd></dl>
        </div>
      </div>
      <div class="build-sequence"><h4>实战节奏</h4><ol><li v-for="step in selected.steps" :key="step">{{ step }}</li></ol></div>
      <p class="build-mixing"><strong>试着混搭。</strong>{{ selected.mixing }}</p>
    </div>
  </section>
</template>
