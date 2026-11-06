<script setup>
import { computed, nextTick, onBeforeUnmount, onMounted, reactive, ref } from 'vue'
import { onBeforeRouteLeave } from 'vue-router'
import { adminApi } from '../utils/adminApi'
import { imageUrl } from '../utils/assets'
import '../../css/admin.css'

const enabled = import.meta.env.VITE_API_ENABLED === 'true'
const ready = ref(false)
const user = ref(null)
const busy = ref(false)
const error = ref('')
const message = ref('')
const loginForm = reactive({ username: '', password: '' })
const collection = ref('articles')
const lists = reactive({ articles: [], gallery: [] })
const assets = ref([])
const query = ref('')
const categoryFilter = ref('')
const statusFilter = ref('')
const form = ref(null)
const editingId = ref(null)
const baseline = ref('')
const editor = ref(null)
const editorForm = ref(null)
const deleteDialog = ref(null)
const deleteTrigger = ref(null)
const isGallery = computed(() => collection.value === 'gallery')
const dirty = computed(() => form.value && JSON.stringify(form.value) !== baseline.value)
const options = computed(() => isGallery.value ? [['人物', '人物'], ['城市', '城市'], ['装备', '装备']] : [['world', '世界观'], ['guide', '攻略流派']])
const visible = computed(() => lists[collection.value].filter(item =>
  (!categoryFilter.value || item.category === categoryFilter.value) &&
  (!statusFilter.value || item.published === (statusFilter.value === 'published')) &&
  `${item.title} ${item.id}`.toLowerCase().includes(query.value.trim().toLowerCase())
))
const detailFields = computed(() => form.value?.category === 'world'
  ? [{ key: 'english', label: '英文名称', max: 100 }, { key: 'label', label: '城区标签', max: 100 }, { key: 'scene', label: '画面说明', max: 500 }]
  : [{ key: 'english', label: '英文名称', max: 100 }, { key: 'approach', label: '打法概述', max: 500 }, { key: 'attributes', label: '属性侧重', max: 500 }, { key: 'equipment', label: '核心搭配', max: 500 }, { key: 'alt', label: '画面说明', max: 500 }])

async function run(action) {
  if (busy.value) return
  busy.value = true; error.value = ''; message.value = ''
  try { await action() } catch (cause) {
    error.value = cause.message
    if (cause.status === 401) { deleteDialog.value?.close(); user.value = null; form.value = null; loginForm.password = '' }
  } finally { busy.value = false }
}
function allowDiscard() { return !dirty.value || window.confirm('还有未保存的修改，确定放弃吗？') }
function setForm(item) {
  editingId.value = item?.id || null
  form.value = item ? { ...item, steps: (item.steps || []).join('\n') } : {
    id: '', title: '', category: isGallery.value ? '人物' : 'world', description: '',
    image: assets.value[0] || '', alt: '', width: 0, height: 0, sortOrder: lists[collection.value].length,
    published: false, english: '', label: '', scene: '', approach: '', attributes: '', equipment: '', steps: '', mixing: ''
  }
  baseline.value = JSON.stringify(form.value)
}
async function choose(item) {
  if (!allowDiscard()) return
  setForm(item)
  await nextTick()
  editor.value?.focus({ preventScroll: true })
  if (window.innerWidth < 900) editor.value?.scrollIntoView({ block: 'start', behavior: 'smooth' })
}
function switchCollection(value) {
  if (value === collection.value || !allowDiscard()) return
  collection.value = value; categoryFilter.value = ''; statusFilter.value = ''; query.value = ''
  setForm(lists[value][0] || null)
}
async function load() {
  const [articles, gallery, images] = await Promise.all(['articles', 'gallery', 'assets'].map(adminApi.list))
  lists.articles = articles; lists.gallery = gallery; assets.value = images
}
async function login() {
  await run(async () => {
    user.value = await adminApi.login(loginForm.username, loginForm.password)
    loginForm.password = ''
    if (user.value.role !== 'ADMIN') throw new Error('此账号没有内容管理权限。')
    await load(); setForm(lists[collection.value][0] || null)
  })
}
async function logout() {
  if (!allowDiscard()) return
  await run(async () => { await adminApi.logout(); user.value = null; form.value = null })
}
async function refresh() {
  if (!allowDiscard()) return
  await run(async () => { await load(); setForm(lists[collection.value].find(item => item.id === editingId.value) || lists[collection.value][0] || null); message.value = '列表已刷新。' })
}
function payload(published) {
  const source = form.value
  const common = { id: source.id, title: source.title, category: source.category, image: source.image, sortOrder: Number(source.sortOrder), published }
  if (isGallery.value) return { ...common, alt: source.alt, width: Number(source.width), height: Number(source.height) }
  const content = Object.fromEntries(detailFields.value.map(field => [field.key, source[field.key] || '']))
  if (source.category === 'guide') { content.steps = source.steps.split(/\r?\n/).map(step => step.trim()).filter(Boolean); content.mixing = source.mixing }
  return { ...common, description: source.description, content }
}
async function save(published) {
  if (!editorForm.value?.reportValidity()) return
  await run(async () => {
    const saved = await adminApi.save(collection.value, editingId.value, payload(published))
    const index = lists[collection.value].findIndex(item => item.id === saved.id)
    if (index < 0) lists[collection.value].push(saved)
    else lists[collection.value][index] = saved
    lists[collection.value].sort((a, b) => a.sortOrder - b.sortOrder || a.id.localeCompare(b.id))
    setForm(saved); message.value = published ? '资料已发布，访客可查看最新内容。' : '已保存为草稿，访客无法查看。'
  })
}
function showDelete() { deleteDialog.value?.showModal() }
function closeDelete() { deleteDialog.value?.close(); deleteTrigger.value?.focus() }
async function remove() {
  await run(async () => {
    await adminApi.remove(collection.value, editingId.value)
    lists[collection.value] = lists[collection.value].filter(item => item.id !== editingId.value)
    closeDelete(); setForm(lists[collection.value][0] || null); message.value = '资料已删除。'
  })
}
function imageChanged() {
  if (isGallery.value) { form.value.width = 0; form.value.height = 0 }
}
function imageLoaded(event) {
  const wasDirty = dirty.value
  if (isGallery.value && form.value.width === 0) {
    form.value.width = event.target.naturalWidth; form.value.height = event.target.naturalHeight
    if (!editingId.value && !wasDirty) baseline.value = JSON.stringify(form.value)
  }
}
function beforeUnload(event) { if (dirty.value) { event.preventDefault(); event.returnValue = '' } }
onBeforeRouteLeave(() => allowDiscard())
onBeforeUnmount(() => window.removeEventListener('beforeunload', beforeUnload))
onMounted(async () => {
  window.addEventListener('beforeunload', beforeUnload)
  if (!enabled) { ready.value = true; return }
  busy.value = true
  try {
    user.value = await adminApi.me()
    if (user.value.role === 'ADMIN') { await load(); setForm(lists[collection.value][0] || null) }
    else error.value = '此账号没有内容管理权限。'
  } catch (cause) { if (cause.status !== 401) error.value = cause.message }
  finally { ready.value = true; busy.value = false }
})
</script>

<template>
  <main class="admin-shell">
    <header class="admin-header"><RouterLink to="/" class="admin-brand">Cyberpunk<span>内容管理</span></RouterLink><div class="admin-account"><RouterLink to="/">浏览网站</RouterLink><span v-if="user">{{ user.username }}</span><button v-if="user" type="button" :disabled="busy" @click="logout">退出登录</button></div></header>
    <p v-if="error" class="admin-notice admin-error" role="alert">{{ error }}</p>
    <p v-if="message" class="admin-notice" role="status">{{ message }}</p>
    <p v-if="!ready" class="admin-loading" role="status">正在检查登录状态…</p>
    <section v-else-if="!enabled" class="admin-login"><h1>内容管理</h1><p>当前站点未启用内容管理服务。</p><RouterLink to="/">返回网站</RouterLink></section>
    <section v-else-if="!user" class="admin-login"><h1>管理夜之城的档案。</h1><p>登录后维护世界观、攻略流派与视觉档案。</p><form @submit.prevent="login"><fieldset :disabled="busy"><label>管理员账号<input v-model="loginForm.username" name="username" autocomplete="username" required maxlength="64" autofocus></label><label>密码<input v-model="loginForm.password" name="password" type="password" autocomplete="current-password" required maxlength="72"></label><button class="admin-primary" type="submit">{{ busy ? '正在登录…' : '登录后台' }}</button></fieldset></form></section>
    <template v-else-if="user.role === 'ADMIN'">
      <div class="admin-heading"><div><h1>内容工作台</h1><p>编辑资料，决定哪些故事被看见。</p></div><button type="button" :disabled="busy" @click="refresh">刷新列表</button></div>
      <div class="admin-workspace" :aria-busy="busy">
        <section class="admin-list" aria-label="资料列表"><div class="admin-tabs" role="group" aria-label="内容类型"><button v-for="item in [['articles','世界与攻略'],['gallery','视觉档案']]" :key="item[0]" type="button" :aria-pressed="collection === item[0]" :disabled="busy" @click="switchCollection(item[0])">{{ item[1] }}</button></div>
          <div class="admin-filter"><label>搜索资料<input v-model="query" type="search" placeholder="标题或标识"></label><div><label>栏目<select v-model="categoryFilter"><option value="">全部栏目</option><option v-for="[value,label] in options" :key="value" :value="value">{{ label }}</option></select></label><label>状态<select v-model="statusFilter"><option value="">全部状态</option><option value="published">已发布</option><option value="draft">草稿</option></select></label></div></div>
          <div class="admin-list-toolbar"><span>{{ visible.length }} 条资料</span><button type="button" :disabled="busy" @click="choose(null)">{{ isGallery ? '新增图片' : '新增资料' }}</button></div>
          <ul class="admin-records"><li v-for="item in visible" :key="item.id"><button type="button" :aria-current="editingId === item.id ? 'true' : undefined" :disabled="busy" @click="choose(item)"><span><strong>{{ item.title }}</strong><small>{{ item.id }}</small></span><span class="admin-status" :class="{ published: item.published }">{{ item.published ? '已发布' : '草稿' }}</span></button></li></ul><p v-if="!visible.length" class="admin-empty">没有匹配的资料。调整筛选，或新增一条资料。</p>
        </section>
        <section v-if="form" class="admin-editor"><div class="admin-editor-heading"><h2 ref="editor" tabindex="-1">{{ editingId ? '编辑' : '新增' }}{{ isGallery ? '图片' : '资料' }}</h2><span v-if="dirty" class="admin-unsaved">尚未保存</span></div>
          <form ref="editorForm" @submit.prevent="save(form.published)"><fieldset :disabled="busy">
            <div class="admin-form-row"><label>资料标识<input v-model="form.id" :readonly="!!editingId" required pattern="[a-z0-9]+(-[a-z0-9]+)*" maxlength="64" placeholder="例如：city-story"><small>使用小写字母、数字和短横线，保存后固定。</small></label><label>栏目<select v-model="form.category" required><option v-for="[value,label] in options" :key="value" :value="value">{{ label }}</option></select></label></div>
            <label>标题<input v-model="form.title" required maxlength="200"></label>
            <label v-if="!isGallery">资料简介<textarea v-model="form.description" rows="3" required maxlength="4000"></textarea></label>
            <div class="admin-media"><label>关联图片<select v-model="form.image" required @change="imageChanged"><option v-for="path in assets" :key="path" :value="path">{{ path }}</option></select><small>选择项目已有素材。图片会保持原始比例。</small></label><img :key="form.image" :src="imageUrl(form.image)" alt="所选图片预览" @load="imageLoaded"></div>
            <template v-if="isGallery"><label>图片说明<input v-model="form.alt" required maxlength="500"></label><div class="admin-form-row"><label>原图宽度<input v-model.number="form.width" type="number" min="1" max="30000" required></label><label>原图高度<input v-model.number="form.height" type="number" min="1" max="30000" required></label></div></template>
            <template v-else><label v-for="field in detailFields" :key="field.key">{{ field.label }}<input v-model="form[field.key]" required :maxlength="field.max"></label><template v-if="form.category === 'guide'"><label>实战步骤<textarea v-model="form.steps" rows="4" required maxlength="2010" placeholder="每行一个步骤，最多 10 个"></textarea></label><label>混搭建议<textarea v-model="form.mixing" rows="3" required maxlength="2000"></textarea></label></template></template>
            <label class="admin-order">排列顺序<input v-model.number="form.sortOrder" type="number" min="0" max="99999" required><small>数字越小，展示越靠前。</small></label>
            <div class="admin-actions"><button class="admin-primary" type="submit">{{ busy ? '正在处理…' : form.published ? '保存修改' : '保存草稿' }}</button><button type="button" @click="save(!form.published)">{{ form.published ? '保存并下线' : '发布资料' }}</button><button v-if="editingId" ref="deleteTrigger" class="admin-danger" type="button" @click="showDelete">删除资料</button></div>
          </fieldset></form>
        </section>
      </div>
    </template>
    <dialog ref="deleteDialog" class="admin-dialog" aria-labelledby="delete-title" @cancel.prevent="closeDelete"><h2 id="delete-title">删除这条资料？</h2><p>“{{ form?.title }}”将被永久删除，无法撤销。</p><p v-if="error" role="alert">{{ error }}</p><div><button type="button" :disabled="busy" @click="closeDelete">保留资料</button><button class="admin-danger" type="button" :disabled="busy" @click="remove">确认删除</button></div></dialog>
  </main>
</template>
