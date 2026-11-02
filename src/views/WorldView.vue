<script setup>
import ArrowIcon from '../components/ArrowIcon.vue'
import { imageUrl } from '../utils/assets'

// 城区和阵营资料由数组驱动；图片通过 imageUrl 交给 Vite 统一处理。
const districts = [['NC-01', '沃森 / Watson', '工业、贫民区和义体诊所并存的旧城区。歌舞伎町的灯光从不真正熄灭。', '旧城'], ['NC-02', '市中心 / City Center', '企业广场与纪念碑式高楼的核心区域。这里的空气和规则都由公司过滤。', '企业'], ['NC-03', '威斯布鲁克 / Westbrook', '日本街、豪华酒店和虎爪帮势力交错，消费与危险只隔一面霓虹招牌。', '霓虹'], ['NC-04', '海伍德 / Heywood', '社区、街头家族和瓦伦蒂诺帮的地盘。这里的人记得你的名字。', '街区'], ['NC-05', '圣多明戈 / Santo Domingo', '发电厂、工厂和六街帮的工业腹地，城市的机器心脏在这里发热。', '工业'], ['NC-06', '太平洲 / Pacifica', '被遗弃的度假区，巫毒男孩在旧网络边缘守着自己的秘密。', '边界'], ['NC-07', '荒原 / Badlands', '城市之外的沙尘与公路。流浪者家族在这里用引擎声维持自由。', '废土']]
const factions = [[imageUrl('factions/arasaka.jpg'), '荒坂 Arasaka', '以安全、金融和家族秩序为名的全球巨头。它拥有最先进的技术，也拥有最漫长的记忆。'], [imageUrl('factions/militech.jpg'), '军用科技 Militech', '军工与国家力量的结合体。它相信稳定来自武器，合同比承诺更可靠。'], [imageUrl('factions/maelstrom.jpg'), '漩涡帮 Maelstrom', '痴迷于极端义体改造的暴力团体，几乎把肉身本身视作一种需要淘汰的旧硬件。'], [imageUrl('factions/tyger-claws.jpg'), '虎爪帮 Tyger Claws', '控制威斯布鲁克多处街区的帮派。速度、忠诚和暴力是他们的街头语言。'], [imageUrl('factions/voodoo-boys.jpg'), '巫毒男孩 Voodoo Boys', '在太平洲与旧网络之间寻找答案的黑客帮派。他们知道黑墙另一侧并不安静。'], [imageUrl('factions/aldecaldos.jpg'), '流浪者 Aldecaldos', '在荒原上迁徙的家族。对他们而言，家不是固定地点，而是愿意一起上路的人。']]
</script>

<template>
  <main class="page-shell">
    <section class="page-hero" aria-labelledby="page-title"><div class="page-hero-media" aria-hidden="true"><img src="/images/characters/gallery (3).jpg" alt=""></div><div class="page-hero-content"><h1 id="page-title" class="page-title glitch" data-text="NIGHT CITY">NIGHT CITY<span>世界观档案</span></h1><p class="page-lede">一座由企业、帮派、霓虹与旧梦共同维持的城市。了解它的分区，才能读懂每一场交易背后的真正筹码。</p></div></section>
    <section class="section section-soft"><div class="container"><div class="story-layout"><div class="story-copy reveal"><h2 class="section-title">城市比<span>任何人都长命</span></h2><p>夜之城位于北加州海岸，是一座把自由包装成商品的独立城市。高墙之内，企业塑造秩序；高墙之外，荒原提醒所有人秩序并不牢固。</p><p>从企业广场的玻璃塔到太平洲的废墟，每个区域都有自己的节奏和生存规则。</p><a class="text-link" href="#/world#districts">读取城区地图 <ArrowIcon /></a></div><div class="media-frame reveal"><img src="/images/city-photos/night-city-street.jpg" alt="雨夜中的夜之城霓虹街道"></div></div></div></section>
    <section id="districts" class="section section-dark"><div class="container"><h2 class="section-title">七个方向，<span>七种生存法则</span></h2><p class="section-description">城区不是背景，它们会决定你遇到谁、能买到什么，以及哪一条街会在你身后关门。</p><div class="district-list"><div v-for="item in districts" :key="item[0]" class="district-row reveal"><span class="row-index">{{ item[0] }}</span><h3>{{ item[1] }}</h3><p>{{ item[2] }}</p><strong>{{ item[3] }}</strong></div></div></div></section>
    <section class="section section-soft"><div class="container"><h2 class="section-title">谁在<span>控制城市</span></h2><p class="section-description">夜之城没有唯一的主人。企业、帮派和旧时代的遗产彼此借力，也彼此等待失控。</p><div class="faction-grid"><article v-for="item in factions" :key="item[1]" class="faction reveal"><h3><img :src="item[0]" :alt="item[1]">{{ item[1] }}</h3><p>{{ item[2] }}</p></article></div></div></section>
  </main>
</template>
