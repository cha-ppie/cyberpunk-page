<script setup>
import ArrowIcon from '../components/ArrowIcon.vue'
</script>

<template>
  <main class="page-shell home-page">
    <section id="home" class="nc-hero" aria-labelledby="home-title">
      <div class="nc-hero-scene"><img src="/images/city-photos/night-city-street.jpg" alt="雨夜的夜之城，霓虹映在街道与跑车上" fetchpriority="high"></div>
      <div class="nc-hero-type" aria-hidden="true">CYBER<br>PUNK<span>2077</span></div>
      <div class="nc-hero-floor"><div class="nc-wrap nc-hero-bottom">
        <h1 id="home-title">欢迎来到<br>黑暗未来。</h1>
        <div class="nc-hero-invitation"><p>在夜之城，成为自己想成为的人。<br>然后，付出代价。</p><RouterLink class="nc-button" to="/world">进入夜之城 <ArrowIcon /></RouterLink></div>
        <a class="nc-official" href="https://www.cyberpunk.net" target="_blank" rel="noopener">访问官方网站 <ArrowIcon /></a>
      </div></div>
    </section>

    <section id="world" class="nc-world nc-wrap" aria-labelledby="world-title">
      <div class="nc-world-copy"><h2 id="world-title">梦想很贵。<br>活着也是。</h2><p>巨型企业占据天际线，帮派瓜分街道。义体可以换掉你的身体，却换不来一条不必选择的路。</p><p>从城区、企业到街头势力，读懂这座城市如何运转，再决定站在哪一边。</p><RouterLink class="nc-link" to="/world">读取世界观档案 <ArrowIcon /></RouterLink></div>
      <div class="nc-world-image"><img src="/images/city-photos/20260202000029_1.jpg" alt="V 站在巨型建筑前，仰望高处的灯光" loading="lazy"></div>
      <p class="nc-world-word" aria-hidden="true">NIGHT CITY</p>
    </section>

    <section id="games" class="nc-paths" aria-labelledby="paths-title"><div class="nc-wrap">
      <div class="nc-section-heading"><h2 id="paths-title">你的路。<br>你的规矩。</h2><p>想清楚怎么活，再决定怎么赢。</p></div>
      <div class="nc-path-grid">
        <RouterLink class="nc-destination nc-game" to="/game"><div class="nc-destination-image"><img src="/images/places/20260616222833_1.jpg" alt="第一人称视角中的任务与装备界面" loading="lazy"></div><div class="nc-destination-copy"><h3>改造你的生存方式</h3><p>出身、属性、义体与技能。每一次升级，都在定义你是谁。</p><span class="nc-link">探索游戏系统 <ArrowIcon /></span></div></RouterLink>
        <RouterLink class="nc-destination nc-guide" to="/guide"><div class="nc-destination-image"><img src="/images/characters/V.png" alt="V 站在武器陈列墙前" loading="lazy"></div><div class="nc-destination-copy"><h3>选一套狠角色的打法</h3><p>网络黑客、反应武士、潜行刺客。让你的装备为你的选择服务。</p><span class="nc-link">查看攻略流派 <ArrowIcon /></span></div></RouterLink>
      </div>
    </div></section>

    <section id="news" class="nc-show" aria-labelledby="show-title">
      <div class="nc-show-image"><img src="/images/characters/Lucy.jpg" alt="边缘行者中的露西" loading="lazy"></div>
      <div class="nc-show-copy"><p class="nc-show-name">Cyberpunk: Edgerunners</p><h2 id="show-title">有些人，<br>注定燃烧。</h2><p>大卫、露西、瑞贝卡。他们曾想逃离这座城市，最终成为了它的一部分。</p><RouterLink class="nc-button" to="/edgerunners">走近边缘行者 <ArrowIcon /></RouterLink></div>
    </section>

    <section class="nc-archive" aria-labelledby="archive-title">
      <div class="nc-wrap nc-archive-heading"><h2 id="archive-title">把夜之城<br>留在视网膜上。</h2><div><p>街头、人物、霓虹。<br>换个角度，再看一次。</p><RouterLink class="nc-link" to="/gallery">打开视觉档案 <ArrowIcon /></RouterLink></div></div>
      <RouterLink class="nc-filmstrip" to="/gallery" aria-label="浏览夜之城视觉档案"><img src="/images/places/20260130104420_1.jpg" alt="义体手臂与夜之城街头的灯饰" loading="lazy"><img src="/images/city-photos/20260202105333_1.jpg" alt="夜之城光影档案" loading="lazy"><img src="/images/characters/Johnny Silver Hand.png" alt="强尼·银手" loading="lazy"></RouterLink>
    </section>
  </main>
</template>
