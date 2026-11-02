<script setup>
import ArrowIcon from '../components/ArrowIcon.vue'

// 每项包含英文代号、中文名称、玩法说明和推荐属性。
const builds = [
  ['NETRUNNER', '纯网络黑客', '以快速破解为主要输出，利用 RAM 和摄像头控制战场，在进入房间前就完成第一轮清场。', ['智力 20', '镇定 20', '技术 20']],
  ['CYBER SAMURAI', '反应武士', '武士刀、Sandevistan、冲刺和空中位移。切入、斩击、离场，整套循环像一段压缩过的时间。', ['反应 20', '肉体 20', '镇定 18']],
  ['HITMAN', '潜行枪手', '消音手枪配合光学迷彩，先让敌人失去视线，再让每一次爆头成为下一次隐身的起点。', ['镇定 20', '反应 20', '智力 16']],
  ['BERSERK', '狂暴战士', '大猩猩手臂和狂暴操作系统把生存变成输出。正面进入，承受伤害，再用近距离力量结束冲突。', ['肉体 20', '技术 20', '反应 18']],
  ['TECH RUNNER', '震撼科技兵', '科技霰弹枪与连锁效果把掩体变成临时目标。适合喜欢一次处理多个问题的人。', ['技术 20', '肉体 20', '反应 18']],
  ['SMART GUNNER', '智能武器专家', '让智能武器接管瞄准，同时用快速破解削弱敌方防御。你只需要判断位置，然后扣下扳机。', ['智力 20', '反应 20', '肉体 16']]
]
</script>

<template>
  <main class="page-shell">
    <section class="page-hero" aria-labelledby="page-title"><div class="page-hero-media" aria-hidden="true"><img src="/images/characters/Two Johnny.png" alt=""></div><div class="page-hero-content"><h1 id="page-title" class="page-title glitch" data-text="BUILD YOUR LEGEND">BUILD YOUR LEGEND<span>攻略流派</span></h1><p class="page-lede">没有一条流派能解决所有问题。找到最像你的那一种，再用它把夜之城的规则改写一遍。</p></div></section>
    <section class="section section-soft"><div class="container"><div class="story-layout"><div class="media-frame reveal"><img src="/images/characters/suvival.png" alt="陈列在墙上的武器与装备"></div><div class="story-copy reveal"><h2 class="section-title">先学会<span>活下来</span></h2><p>前期优先把属性点投入到一个明确方向，装备围绕战斗循环选择。不要急着把每一项都点满，真正的强大来自一套能够重复执行的节奏。</p><p>潜入、破解、冲刺、正面火力都能通往同一条街。只要构筑让你的选择更快、更准，它就是适合你的流派。</p><a class="text-link" href="#/guide#builds">跳转构筑清单 <ArrowIcon /></a></div></div></div></section>
    <section id="builds" class="section section-dark"><div class="container"><h2 class="section-title">六种<span>强力构筑</span></h2><p class="section-description">它们不是固定答案，而是六种不同的战斗语言。可以从一条主线开始，再向其他方向混合。</p><div class="build-list"><article v-for="item in builds" :key="item[0]" class="build-row reveal"><strong>{{ item[0] }}</strong><h3>{{ item[1] }}</h3><p>{{ item[2] }}</p><div class="build-attrs"><span v-for="attribute in item[3]" :key="attribute">{{ attribute }}</span></div></article></div></div></section>
    <section class="section section-soft"><div class="container"><div class="dispatch"><p class="dispatch-quote reveal">Your build.<br><span>Your rules.</span></p><div class="dispatch-note reveal"><strong>Field note / night city</strong><p>攻略的价值不是告诉你唯一正确的路线，而是帮你更快找到那种让你愿意继续走下去的节奏。</p><RouterLink class="text-link" to="/game">回到游戏系统 <ArrowIcon /></RouterLink></div></div></div></section>
  </main>
</template>
