<script setup>
import { ref } from 'vue'
import DossierHero from '../components/DossierHero.vue'
import DossierEnd from '../components/DossierEnd.vue'
import { imageUrl } from '../utils/assets'
import { moveTab } from '../utils/tabKeyboard'
const lifepaths = [
  ['Nomad / 荒原', '流浪者', '熟悉荒原、车队与引擎声。你从城市之外进入夜之城，知道自由有时需要一条可以逃走的路。', 'nomad', '荒原营地里，流浪者围在篝火和车辆之间'],
  ['Street kid / 街头', '街头小子', '你在酒吧、巷道与黑市里长大。这里的人知道你的名字，也知道你欠过谁。', 'street-kid', '街头小子命途画面，人物坐在霓虹酒吧的卡座里'],
  ['Corpo / 企业', '公司员工', '你曾经在荒坂反情报部门工作，熟悉谈判、威胁和权力如何穿过一张办公桌。', 'corpo', '企业命途画面，车辆停在荒坂大楼入口']
].map(([label, title, description, image, alt]) => ({ label, title, description, image: imageUrl(`game-media/${image}.jpg`), alt }))
const attributes = [['肉体', '生命值、耐力、近战伤害，以及用力量解决问题的底气。'], ['反应', '移动、闪避、暴击与武士刀，让速度成为你的护甲。'], ['技术能力', '义体容量、科技武器和升级选项。技术让你打开更多门。'], ['智力', '快速破解、RAM 与智能武器。敌人还没发现你时，战斗已经结束。'], ['镇定', '潜行、精准射击与冷静对话。越接近危险，越不能失去节奏。']]
const cyberware = [['01 / OS', 'Sandevistan', '进入子弹时间，拉开速度差。反应武士常用的核心操作系统。', '时间'], ['02 / ARM', 'Gorilla Arms', '提升近战伤害和力量检定，适合把问题直接砸开的人。', '力量'], ['03 / LEGS', 'Reinforced Tendons', '二段跳改变了城市的垂直空间，也改变了你逃离战场的方式。', '机动'], ['04 / ARM', 'Mantis Blades', '从手臂弹出的利刃，把近战变成一次短距离的高速切入。', '锋刃'], ['05 / HAND', 'Smart Link', '让智能武器接管一部分瞄准，把注意力留给下一步位置。', '锁定'], ['06 / SKIN', 'Optical Camo', '短暂隐身，切断敌人的视线，再从他们最意想不到的角度出现。', '潜行']]

const activeAttribute = ref(0)
const selectAttribute = (index) => { activeAttribute.value = index }
const attributeHints = ['适合近身搏斗与正面承压的玩法。', '适合刀剑、快速位移与主动切入。', '适合重视义体、科技武器与装备协同的玩法。', '适合快速破解与智能武器的玩法。', '适合潜行、精准射击与控制战斗节奏。']
</script>

<template>
  <main class="page-shell dossier game-page">
    <DossierHero variant="game" :title="'PLAY\nYOUR WAY'" subtitle="游戏系统" description="出身只是起点。属性、义体与每一次选择，才决定你会成为谁。" :image="imageUrl('characters/V(Johnny).jpg')" alt="红蓝灯光中的 V 肖像" position="60% center" jump="#lifepath" action="选择角色起点" />
    <section class="d-wrap d-section game-prologue"><img :src="imageUrl('characters/VandJohnny.jpg')" alt="V 与强尼在夜之城街头合影" loading="lazy"><div><h2>一枚芯片。<br>两个灵魂。</h2><p>一次失败的抢劫把 Relic 芯片植入 V 的脑中，也把强尼·银手的数字人格带进了这场生存竞赛。</p><p>时间正在流逝。你需要找到自己的处理方式，让每一次升级都服务于下一次选择。</p></div></section>
    <section id="lifepath" class="d-section d-steel-section"><div class="d-wrap"><div class="d-section-heading"><h2>你从哪里来。</h2><p>三条命途，三种看待城市的方式。它们塑造你的开场与对话，不把你锁进某种职业。</p></div><div class="origin-grid"><article v-for="item in lifepaths" :key="item.title" class="origin-card"><img :src="item.image" :alt="item.alt" width="2048" height="1152" loading="lazy"><small>{{ item.label }}</small><h3>{{ item.title }}</h3><p>{{ item.description }}</p></article></div></div></section>
    <section class="d-wrap d-section"><div class="d-section-heading"><h2>五条属性，五种解法。</h2><p>点击属性，看看它会如何改变你处理问题的方式。</p></div><div class="attribute-selector"><div class="attribute-tabs" role="tablist" aria-label="角色属性"><button v-for="(item,index) in attributes" :id="`attribute-${index}`" :key="item[0]" type="button" role="tab" :aria-selected="activeAttribute === index" :tabindex="activeAttribute === index ? 0 : -1" aria-controls="attribute-panel" @click="activeAttribute = index" @keydown="moveTab($event,index,attributes.length,selectAttribute)">{{ item[0] }}</button></div><div id="attribute-panel" class="attribute-detail" role="tabpanel" :aria-labelledby="`attribute-${activeAttribute}`" tabindex="0"><h3>{{ attributes[activeAttribute][0] }}</h3><p>{{ attributes[activeAttribute][1] }}</p><small>{{ attributeHints[activeAttribute] }}</small></div></div></section>
    <section class="d-section d-steel-section"><div class="d-wrap"><div class="d-section-heading"><h2>给身体装上答案。</h2><p>先确定战斗方式，再选择义体。每一种改造，都应该成为你的下一步动作。</p></div><div class="ware-grid"><article v-for="item in cyberware" :key="item[1]" class="ware-item"><small>{{ item[0].split(' / ')[1] }} / {{ item[3] }}</small><h3>{{ item[1] }}</h3><p>{{ item[2] }}</p></article></div></div></section>
    <DossierEnd title="把选择连成一套打法。" description="从网络黑客到反应武士，找到装备、属性与战斗节奏之间的连接。" to="/guide" action="查看攻略流派" />
  </main>
</template>
