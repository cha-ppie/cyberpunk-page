<script setup>
import ArrowIcon from '../components/ArrowIcon.vue'
import { imageUrl } from '../utils/assets'

// 每项依次为图片地址、替代文本、角色名称和角色简介。
const cast = [
  [imageUrl('characters/David.jpg'), '大卫的角色画面', 'David Martinez', '天赋、愤怒和想要出人头地的少年，在城市的高速路上被迫成长。'],
  [imageUrl('characters/Lucy.jpg'), '露西的角色画面', 'Lucy', '想要离开夜之城的人。她知道自由在哪里，也知道离开需要付出什么。'],
  [imageUrl('characters/Maine.jpg'), '曼恩的角色画面', 'Maine', '团队的核心与保护者。他把“家人”放在城市规则之前。'],
  [imageUrl('characters/Rebecca.jpg'), '丽贝卡的角色画面', 'Rebecca', '不按常理行动的火力手，越是混乱的场面，越能看见她的速度。'],
  [imageUrl('city-photos/b7c99908bbf44bc93cd21d2b757e2212.jpg'), '夜之城景色', 'Night City', '真正的主角始终是城市。它给你机会，也会在你最接近梦想时收回它。'],
  [imageUrl('characters/NewGender.jpg'), '新生代', 'New signal', '新的故事还会继续。只要还有人愿意把镜头对准城市边缘，传奇就不会停止。']
]
</script>

<template>
  <main class="page-shell">
    <section class="page-hero" aria-labelledby="page-title"><div class="page-hero-media" aria-hidden="true"><img src="/images/characters/gallery (2).jpg" alt=""></div><div class="page-hero-content"><h1 id="page-title" class="page-title glitch" data-text="EDGERUNNERS">EDGERUNNERS<span>边缘行者</span></h1><p class="page-lede">在一座不为普通人留位置的城市里，一群人用速度、忠诚和失控的梦想争取一段自己的传奇。</p></div></section>
    <section class="section section-soft"><div class="container"><div class="feature-card reveal"><div class="feature-card-media"><img src="/images/characters/runners.jpg" alt="夜之城中的角色剪影"></div><div class="feature-card-copy"><h3>一群人，试着不被城市吞掉</h3><p>David 的故事从一次失去开始，也从一次相遇开始。Lucy、Maine、Dorio、Rebecca 和他们的团队，让夜之城短暂地像一个可以回去的地方。</p><a class="text-link" href="#/edgerunners#cast">读取团队档案 <ArrowIcon /></a></div></div></div></section>
    <section id="cast" class="section section-dark"><div class="container"><h2 class="section-title">记住这些<span>名字</span></h2><p class="section-description">边缘行者的故事不只属于一个主角。每一个名字都在说明，传奇的代价从来不是一个人承担。</p><div class="cast-grid"><article v-for="item in cast" :key="item[2]" class="cast-card reveal"><img :src="item[0]" :alt="item[1]"><div class="cast-card-body"><h3>{{ item[2] }}</h3><p>{{ item[3] }}</p></div></article></div></div></section>
    <section class="section section-soft"><div class="container"><div class="dispatch"><p class="dispatch-quote reveal">Never fade<br><span>away.</span></p><div class="dispatch-note reveal"><strong>Soundtrack / afterlife</strong><p>有些人离开了，有些人留在记忆里。边缘行者的故事提醒我们，真正的传奇不等于活得最久。</p><RouterLink class="text-link" to="/gallery">浏览视觉档案 <ArrowIcon /></RouterLink></div></div></div></section>
  </main>
</template>
