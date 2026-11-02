<script setup>
import { onBeforeUnmount, onMounted, ref } from 'vue'
import ArrowIcon from '../components/ArrowIcon.vue'

const typewriter = ref(null)
const formMessage = ref('')
let timer

onMounted(() => {
  // 首页打字机循环展示多条标语；减少动效模式下直接显示第一条。
  const reducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches
  const phrases = ['欢迎来到夜之城。这里的每一个选择都要付出代价。', 'Wake up, Samurai. We have a city to burn.', '没有活着的传奇，只有被记住的死人。', 'Never Fade Away.']
  if (reducedMotion) { typewriter.value.textContent = phrases[0]; return }
  let phraseIndex = 0
  let characterIndex = 0
  let deleting = false
  // 在输入和删除之间循环，句子显示完毕后停留片刻。
  const tick = () => {
    const phrase = phrases[phraseIndex]
    const text = deleting ? phrase.slice(0, characterIndex - 1) : phrase.slice(0, characterIndex + 1)
    if (typewriter.value) typewriter.value.innerHTML = `${text}<span class="type-cursor" aria-hidden="true"></span>`
    characterIndex += deleting ? -1 : 1
    if (!deleting && characterIndex === phrase.length) { deleting = true; timer = window.setTimeout(tick, 2500); return }
    if (deleting && characterIndex === 0) { deleting = false; phraseIndex = (phraseIndex + 1) % phrases.length }
    timer = window.setTimeout(tick, deleting ? 26 : 58)
  }
  timer = window.setTimeout(tick, 500)
})

// 离开首页时清理定时器，避免继续修改已卸载的 DOM。
onBeforeUnmount(() => window.clearTimeout(timer))
</script>

<template>
  <main class="page-shell">
    <!-- 首页首屏：背景图、主标题、打字机标语与主要入口。 -->
    <section id="home" class="home-hero" aria-labelledby="home-title">
      <div class="hero-media" aria-hidden="true"><img src="/images/city-photos/20260116095549_1.jpg" alt="夜之城街头与人物剪影"></div>
      <div class="hero-content">
        <h1 id="home-title" class="hero-title glitch" data-text="CYBERPUNK">CYBERPUNK<span>2077</span></h1>
        <p ref="typewriter" class="hero-subtitle" aria-live="polite"></p>
        <div class="hero-actions">
          <RouterLink class="button button-primary" to="/world">进入夜之城 <ArrowIcon /></RouterLink>
          <a class="button button-secondary" href="https://www.cyberpunk.net" target="_blank" rel="noopener">访问官方网站 <ArrowIcon /></a>
        </div>
        <div class="hero-readout"><strong>LIVE / NIGHT CITY</strong><span>Signal stable</span><span>Archive channel 01</span></div>
      </div>
      <div class="hero-rail" aria-hidden="true"><span>NC / 2077</span><span>Broadcast online</span></div>
    </section>

    <section id="world" class="section section-soft"><div class="container story-layout">
      <div class="media-frame reveal"><img src="/images/city-photos/20260202000029_1.jpg" alt="V 走进夜之城的高架空间"></div>
      <div class="story-copy reveal"><h2 class="section-title">欢迎来到<span>黑暗未来</span></h2><p>夜之城是一座把梦想、科技和暴力压进同一块混凝土里的城市。这里的每一条街都有价格，每一段关系都可能改变你的结局。</p><p>Syberpunk 从游戏、动画和截图中整理这座城市的入口，让你先读懂它，再决定要以什么方式活下去。</p><div class="story-facts" aria-label="站点内容范围"><div><strong>06</strong><span>内容频道</span></div><div><strong>∞</strong><span>可以重走的选择</span></div></div><RouterLink class="text-link" to="/world">读取世界观档案 <ArrowIcon /></RouterLink></div>
    </div></section>

    <section id="games" class="section section-dark"><div class="container"><h2 class="section-title">选择你的<span>入口</span></h2><p class="section-description">像官方站点一样，从游戏、世界、动画和社区四个方向进入。每个入口都通往一套不同的阅读节奏。</p><div class="route-layout">
      <RouterLink class="route-feature reveal" to="/game"><img src="/images/city-photos/game.jpg" alt="游戏系统档案封面"><div class="route-feature-content"><h3>游戏系统</h3><p>出身、属性、义体和技能成长。先决定你是谁，再决定你怎么战斗。</p><span class="text-link">打开系统档案 <ArrowIcon /></span></div></RouterLink>
      <div class="route-list">
        <RouterLink class="route-row reveal" to="/world"><span class="route-number">01</span><div><h3>世界观</h3><p>七大城区、企业与帮派的权力版图。</p></div><span class="route-arrow" aria-hidden="true"><ArrowIcon /></span></RouterLink>
        <RouterLink class="route-row reveal" to="/guide"><span class="route-number">02</span><div><h3>攻略流派</h3><p>从网络黑客到反应武士的构筑选择。</p></div><span class="route-arrow" aria-hidden="true"><ArrowIcon /></span></RouterLink>
        <RouterLink class="route-row reveal" to="/edgerunners"><span class="route-number">03</span><div><h3>边缘行者</h3><p>动画角色、故事与新的传奇。</p></div><span class="route-arrow" aria-hidden="true"><ArrowIcon /></span></RouterLink>
        <RouterLink class="route-row reveal" to="/gallery"><span class="route-number">04</span><div><h3>视觉档案</h3><p>角色、街区、装备和城市光影。</p></div><span class="route-arrow" aria-hidden="true"><ArrowIcon /></span></RouterLink>
      </div>
    </div></div></section>

    <section id="news" class="section section-soft"><div class="container"><h2 class="section-title">来自夜之城的<span>信号</span></h2><div class="feature-card reveal" style="margin-top: 3rem"><div class="feature-card-media"><img src="/images/places/20260130104420_1.jpg" alt="夜之城街头，义体手臂伸向彩色灯饰"></div><div class="feature-card-copy"><h3>每个故事，都从一次选择开始</h3><p>从主线任务到街头小事，夜之城不会替你决定谁值得信任。读取角色关系，理解那些藏在城市霓虹下的代价。</p><RouterLink class="text-link" to="/edgerunners">查看角色与故事 <ArrowIcon /></RouterLink></div></div></div></section>
    <section class="section section-dark"><div class="container dispatch"><p class="dispatch-quote reveal">Wake up,<br><span>Samurai.</span></p><div class="dispatch-note reveal"><strong>Transmission / Johnny Silverhand</strong><p>传奇不会在安静里发生。去街上，去寻找一条不会被任何人替你写好的路。</p><RouterLink class="text-link" to="/gallery">浏览城市档案 <ArrowIcon /></RouterLink></div></div></section>
    <section class="section section-soft"><div class="container newsletter"><h2>Keep your feed <span>alive.</span></h2><div><p>本地演示站点不会收集邮箱。这里保留官方站点式的通讯入口，用来标记未来可以接入的内容模块。</p><form class="fake-form" @submit.prevent="formMessage = '本地演示模式：订阅接口未连接。'"><input class="fake-input" type="email" placeholder="输入邮箱地址" aria-label="邮箱地址"><button class="button button-primary" type="submit">登记信号</button></form><p class="form-message" aria-live="polite" style="margin-top: .8rem; font-family: var(--mono); font-size: .68rem; color: var(--yellow)">{{ formMessage }}</p></div></div></section>
  </main>
</template>
