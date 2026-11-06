<script setup>
import DossierHero from '../components/DossierHero.vue'
import DossierEnd from '../components/DossierEnd.vue'
import BuildExplorer from '../components/BuildExplorer.vue'
import { imageUrl } from '../utils/assets'
</script>

<template>
  <main class="page-shell dossier guide-page">
    <DossierHero variant="guide" :title="'BUILD\nYOURSELF'" subtitle="攻略流派" description="刀锋、代码，还是正面火力？找到你愿意反复执行的战斗节奏。" :image="imageUrl('game-media/guide-street-combat.jpg')" alt="夜之城巷战，V 从第一人称视角举枪迎敌" position="65% center" jump="#builds" action="浏览流派档案" />
    <section class="d-wrap d-section guide-intro"><h2>强大的不是装备。<br>是配合。</h2><p>前期先把资源投入一个明确方向。属性服务于技能，义体补足弱点，武器完成输出。围绕一套稳定的战斗循环搭配，远比把所有选项都点亮更容易上手。</p></section>
    <BuildExplorer />
    <section class="d-section d-steel-section"><div class="d-wrap guide-field"><img :src="imageUrl('game-media/guide-cyberware.webp')" alt="义体医生的配置界面，按身体部位展示义体插槽" loading="lazy"><div><h2>下一次进战场前。</h2><ol><li>确认操作系统与核心武器能配合。</li><li>用一场小规模战斗试出资源与冷却节奏。</li><li>找到薄弱环节，再决定下一笔钱花在哪里。</li></ol></div></div></section>
    <DossierEnd title="先理解系统，再改写规则。" description="回到命途、属性与义体档案，把每一次升级放进你的计划。" to="/game" action="回到游戏系统" />
  </main>
</template>
