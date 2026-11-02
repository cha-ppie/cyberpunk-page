<script setup>
import ArrowIcon from '../components/ArrowIcon.vue'

// 页面展示数据与模板分离，模板通过 v-for 生成命途、属性和义体卡片。
const lifepaths = [
  ['Nomad / 荒原', '流浪者', '熟悉荒原、车队与引擎声。你从城市之外进入夜之城，知道自由有时需要一条可以逃走的路。'],
  ['Street kid / 街头', '街头小子', '你在酒吧、巷道与黑市里长大。这里的人知道你的脸，也知道你欠过谁。'],
  ['Corpo / 企业', '公司员工', '你曾经在荒坂反情报部门工作，熟悉谈判、威胁和权力如何穿过一张办公桌。']
]
const attributes = [['肉体', '生命值、耐力、近战伤害，以及用力量解决问题的底气。'], ['反应', '移动、闪避、暴击与武士刀，让速度成为你的护甲。'], ['技术能力', '义体容量、科技武器和升级选项。技术让你打开更多门。'], ['智力', '快速破解、RAM 与智能武器。敌人还没发现你时，战斗已经结束。'], ['镇定', '潜行、精准射击与冷静对话。越接近危险，越不能失去节奏。']]
const cyberware = [['01 / OS', 'Sandevistan', '进入子弹时间，拉开速度差。反应武士常用的核心操作系统。', '时间'], ['02 / ARM', 'Gorilla Arms', '提升近战伤害和力量检定，适合把问题直接砸开的人。', '力量'], ['03 / LEGS', 'Reinforced Tendons', '二段跳改变了城市的垂直空间，也改变了你逃离战场的方式。', '机动'], ['04 / ARM', 'Mantis Blades', '从手臂弹出的利刃，把近战变成一次短距离的高速切入。', '锋刃'], ['05 / HAND', 'Smart Link', '让智能武器接管一部分瞄准，把注意力留给下一步位置。', '锁定'], ['06 / SKIN', 'Optical Camo', '短暂隐身，切断敌人的视线，再从他们最意想不到的角度出现。', '潜行']]
</script>

<template>
  <main class="page-shell">
    <section class="page-hero" aria-labelledby="page-title"><div class="page-hero-media" aria-hidden="true"><img src="/images/characters/V.png" alt=""></div><div class="page-hero-content"><h1 id="page-title" class="page-title glitch" data-text="PLAY YOUR WAY">PLAY YOUR WAY<span>游戏系统</span></h1><p class="page-lede">你不是被写好的角色。出身、属性、义体和技能共同决定 V 如何在夜之城留下痕迹。</p></div></section>
    <section class="section section-soft"><div class="container"><div class="system-layout"><div class="system-console reveal"><span class="console-label">Character matrix / active</span><div class="console-number">V</div><div class="console-line" aria-hidden="true"></div></div><div class="system-copy reveal"><figure class="system-media"><img src="/images/characters/VandJohnny.jpg" alt="V 与 Johnny Silverhand 并肩站在夜之城"></figure><h3>一枚芯片，两个灵魂</h3><p>一次失败的抢劫把 Relic 芯片植入 V 的脑中，也把 Johnny Silverhand 的数字人格带进了这场生存竞赛。你需要在时间、身份和选择之间找到自己的节奏。</p><p>这套系统的核心不是追求唯一最强，而是让每一次升级都改变你的处理方式。</p><a class="text-link" href="#/game#lifepath">查看角色起点 <ArrowIcon /></a></div></div></div></section>
    <section id="lifepath" class="section section-dark"><div class="container"><h2 class="section-title">先决定<span>你从哪里来</span></h2><p class="section-description">三条命途不是职业选择，而是你看待这座城市的第一套语言。</p><div class="content-grid"><article v-for="item in lifepaths" :key="item[1]" class="info-panel reveal"><span class="panel-tag">{{ item[0] }}</span><h3>{{ item[1] }}</h3><p>{{ item[2] }}</p></article></div></div></section>
    <section class="section section-soft"><div class="container"><h2 class="section-title">五条属性，<span>五种解法</span></h2><p class="section-description">属性会改变战斗效率，也会改变你面对问题时能否选择另一条路。</p><div class="attribute-grid"><article v-for="item in attributes" :key="item[0]" class="attribute reveal"><h3>{{ item[0] }}</h3><p>{{ item[1] }}</p></article></div></div></section>
    <section class="section section-dark"><div class="container"><h2 class="section-title">给身体装上<span>答案</span></h2><p class="section-description">义体不是装饰，它是你对危险的具体回应。选择系统，等于选择你如何进入一场战斗。</p><div class="cyber-list"><div v-for="item in cyberware" :key="item[0]" class="cyber-row reveal"><span class="row-index">{{ item[0] }}</span><h3>{{ item[1] }}</h3><p>{{ item[2] }}</p><strong>{{ item[3] }}</strong></div></div></div></section>
  </main>
</template>
