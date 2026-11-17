import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

export function useContentSelection(items, queryKey) {
  const route = useRoute()
  const router = useRouter()
  const requestedId = computed(() => route.query[queryKey])
  const requestedIndex = computed(() => items.value.findIndex(item => item.id === requestedId.value))
  const activeIndex = computed(() => Math.max(0, requestedIndex.value))
  const unavailable = computed(() => typeof requestedId.value === 'string' && requestedIndex.value < 0)

  function select(index) {
    router.replace({ query: { ...route.query, [queryKey]: items.value[index].id }, hash: route.hash })
  }

  return { activeIndex, unavailable, select }
}
