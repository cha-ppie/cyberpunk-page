<script setup>
import { computed, ref } from 'vue'
import DossierEnd from '../components/DossierEnd.vue'
import ArrowIcon from '../components/ArrowIcon.vue'
import { imageUrl } from '../utils/assets'
import { moveTab } from '../utils/tabKeyboard'

// 按作品存储资料；增加人物只需追加数据，不用补场景卡占位。
const works = [
  {
    id: 'edgerunners-1', label: '边缘行者', title: '燃烧过，\n就不会熄灭。', status: '第一部 / 2022',
    poster: 'edgerunners/season1-poster.jpg', posterWidth: 1025, posterHeight: 1528, alt: '边缘行者第一部全员宣传海报',
    description: '大卫的故事从一次失去开始，也从一次相遇开始。一群在城市边缘谋生的人，让夜之城短暂地像一个可以回去的地方。',
    note: '速度、忠诚与一场去月球的梦。这里记录那些把彼此当作家人的人。',
    source: 'https://www.cyberpunk.net/en/edgerunners',
    cast: [
      {name:'大卫·马丁内斯',english:'David Martinez',image:'characters/David.jpg',description:'想要出人头地的少年，在失去与相遇之间走上边缘行者的道路。'},
      {name:'露西',english:'Lucy',image:'characters/Lucy.jpg',description:'不轻易交出信任的网络黑客。离开夜之城，是她始终没有放下的愿望。'},
      {name:'曼恩',english:'Maine',image:'characters/Maine.jpg',description:'团队的核心与保护者。他把家人放在城市的生存规则之前。'},
      {name:'瑞贝卡',english:'Rebecca',image:'characters/Rebecca.jpg',description:'不按常理行动的火力手。直率、可靠，总是在最混乱的地方站稳脚跟。'}
    ]
  },
  {
    id: 'edgerunners-2', label: '边缘行者 2', title: '新的故事。\n同一座城。', status: '第二部 / 2026 · 已上线',
    poster: 'characters/NewGender.jpg', posterWidth: 1224, posterHeight: 1529, alt: '边缘行者第二部的角色群像宣传海报',
    description: 'CD PROJEKT RED 与 TRIGGER 再次回到夜之城。十集独立故事，围绕新的角色，讲述复仇与救赎。',
    note: '失去昔日荣光的老手、追寻复仇的黑客，以及执着于真实影像的年轻人，各自寻找生存的意义。',
    source: 'https://www.cyberpunk.net/en/edgerunners2',
    cast: [
      {name:'Weak',english:'昔日的传奇',image:'edgerunners/weak.png',description:'曾以 King 之名活跃的资深边缘行者，如今生活在昔日荣光的阴影里。'},
      {name:'D',english:'蛇邦黑客',image:'edgerunners/d.png',description:'来自蛇邦的网络黑客，追寻毁灭自己氏族的凶手。'},
      {name:'Roman',english:'追寻真实的镜头',image:'edgerunners/roman.png',description:'热爱电影的年轻人，试图在超梦盛行的城市里记录真实故事。'},
      {name:'Talia',english:'企业与街头之间',image:'edgerunners/talia.png',description:'成长于企业高塔，后来被漩涡帮的文化深刻影响。'}
    ]
  }
]
const activeWork = ref(1)
const work = computed(() => works[activeWork.value])
const selectWork = (index) => { activeWork.value = index }
</script>

<template>
  <main class="page-shell dossier series-page">
    <header class="series-masthead d-wrap">
      <h1 id="page-title">EDGE<span>RUNNERS</span></h1>
      <div class="series-heading-copy"><h2>边缘行者 / 系列档案</h2><p>有人离开，有人被记住。城市还在继续，新的故事正在发生。</p></div>
    </header>
    <section id="series" class="d-wrap series-works" aria-labelledby="page-title">
      <div class="series-tabs" role="tablist" aria-label="选择作品"><button v-for="(item,index) in works" :id="`work-tab-${index}`" :key="item.id" type="button" role="tab" :aria-selected="activeWork === index" aria-controls="work-panel" :tabindex="activeWork === index ? 0 : -1" @click="activeWork = index" @keydown="moveTab($event,index,works.length,selectWork)">{{ item.label }}</button></div>
      <div id="work-panel" role="tabpanel" :aria-labelledby="`work-tab-${activeWork}`" tabindex="0">
        <div class="series-intro" ><div class="series-poster"><img :src="imageUrl(work.poster)" :alt="work.alt" :width="work.posterWidth" :height="work.posterHeight" fetchpriority="high"></div><div class="series-story"><p class="series-status">{{ work.status }}</p><h2>{{ work.title }}</h2><p>{{ work.description }}</p><p>{{ work.note }}</p><a class="d-link" :href="work.source" target="_blank" rel="noopener">访问作品官网 <ArrowIcon /></a></div></div>
        <div id="cast"><div class="d-section-heading"><h2>记住这些名字。</h2><p>{{ work.label }}的人物档案。各有来处，也各有想要抵达的地方。</p></div><div class="series-cast"><article v-for="person in work.cast" :key="person.name" class="series-character"><div class="series-character-image"><img :src="imageUrl(person.image)" :alt="person.name + '的角色肖像'" loading="lazy"></div><h3>{{ person.name }}</h3><small>{{ person.english }}</small><p>{{ person.description }}</p></article></div></div>
        <p class="series-source">{{ activeWork === 1 ? '第二部人物资料与图片来自' : '更多作品信息见' }} <a :href="work.source" target="_blank" rel="noopener">Cyberpunk 官方作品页</a>。人物简介不涉及结局。</p>
      </div>
    </section>
    <DossierEnd title="传奇之外，城市仍在。" description="从动画回到街头，看看他们生活的世界如何运转。" to="/world" action="进入世界观档案" />
  </main>
</template>
