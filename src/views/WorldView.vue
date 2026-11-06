<script setup>
import { ref, watch } from 'vue'
import DossierHero from '../components/DossierHero.vue'
import DossierEnd from '../components/DossierEnd.vue'
import { imageUrl } from '../utils/assets'
import initialDistricts from '../../content/districts.json'
import { useContent } from '../composables/useContent'
import { moveTab } from '../utils/tabKeyboard'
const districts = useContent('articles?category=world', initialDistricts, items => items.map(item => ({ ...item, image: imageUrl(item.image) })))
const factions = [[imageUrl('factions/arasaka.jpg'), '荒坂 Arasaka', '以安全、金融和家族秩序为名的全球巨头。它拥有最先进的技术，也拥有最漫长的记忆。'], [imageUrl('factions/militech.jpg'), '军用科技 Militech', '军工与国家力量的结合体。它相信稳定来自武器，合同比承诺更可靠。'], [imageUrl('factions/maelstrom.jpg'), '漩涡帮 Maelstrom', '痴迷于极端义体改造的暴力团体，几乎把肉身本身视作一种需要淘汰的旧硬件。'], [imageUrl('factions/tyger-claws.jpg'), '虎爪帮 Tyger Claws', '控制威斯布鲁克多处街区的帮派。速度、忠诚和暴力是他们的街头语言。'], [imageUrl('factions/voodoo-boys.jpg'), '巫毒男孩 Voodoo Boys', '在太平洲与旧网络之间寻找答案的黑客帮派。他们知道黑墙另一侧并不安静。'], [imageUrl('factions/aldecaldos.jpg'), '流浪者 Aldecaldos', '在荒原上迁徙的家族。对他们而言，家不是固定地点，而是愿意一起上路的人。']]

const activeDistrict = ref(0)
watch(districts, () => { activeDistrict.value = Math.min(activeDistrict.value, Math.max(0, districts.value.length - 1)) })
const selectDistrict = (index) => { activeDistrict.value = index }
</script>

<template>
  <main class="page-shell dossier world-page">
    <DossierHero variant="world" :title="'NIGHT\nCITY'" subtitle="世界观档案" description="企业的天际线，街头的生存法则。在踏进这座城之前，先读懂它。" :image="imageUrl('city-photos/night-city-street.jpg')" alt="雨夜霓虹中的夜之城街道" position="65% center" jump="#districts" action="浏览城区档案" />
    <section class="d-wrap d-section world-statement"><h2>城市比任何人<br>都长命。</h2><div><p>夜之城位于北加州海岸，是一座把自由包装成商品的独立城市。高墙之内，企业塑造秩序；高墙之外，荒原提醒所有人秩序并不牢固。</p><p>从企业广场的玻璃塔到太平洲的废墟，每个区域都有自己的节奏和生存规则。</p></div></section>
    <section id="districts" class="d-wrap d-section" style="padding-top: 0"><div class="d-section-heading"><h2>从哪条街开始？</h2><p>选择一个区域，了解它的面貌与势力。城市中心与荒原，遵守的是两套规则。</p></div>
      <div class="district-stage"><div class="district-tabs" role="tablist" aria-label="城区" aria-orientation="vertical"><button v-for="(item,index) in districts" :id="`district-tab-${index}`" :key="item.id" type="button" role="tab" :aria-selected="activeDistrict === index" aria-controls="district-panel" :tabindex="activeDistrict === index ? 0 : -1" @click="activeDistrict = index" @keydown="moveTab($event,index,districts.length,selectDistrict)"><span>{{ item.name }}</span><small>{{ item.english }}</small></button></div>
      <p v-if="!districts.length" role="status">暂无已发布的城区资料。</p>
      <div v-if="districts.length" id="district-panel" class="district-detail" role="tabpanel" :aria-labelledby="`district-tab-${activeDistrict}`" tabindex="0"><img :src="districts[activeDistrict].image" :alt="districts[activeDistrict].scene + '，城区概念图'" loading="lazy"><div class="district-detail-copy"><h3>{{ districts[activeDistrict].name }} / {{ districts[activeDistrict].english }}</h3><p>{{ districts[activeDistrict].description }}</p><small>{{ districts[activeDistrict].label }} · 概念图</small></div></div></div>
    </section>
    <section class="d-section d-steel-section"><div class="d-wrap"><div class="d-section-heading"><h2>谁在控制城市。</h2><p>企业制定合同，帮派划定边界。想在这里活下去，先记住这些名字。</p></div><div class="faction-wall"><article v-for="item in factions" :key="item[1]" class="faction-entry"><figure><img :src="item[0]" :alt="item[1]" loading="lazy"></figure><h3>{{ item[1] }}</h3><p>{{ item[2] }}</p></article></div></div></section>
    <DossierEnd title="读懂规则，然后打破它。" description="选择你的出身、属性与义体，决定你会怎样进入夜之城。" to="/game" action="探索游戏系统" />
  </main>
</template>
