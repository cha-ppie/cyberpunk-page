export function isContentList(items, example = {}) {
  const fields = Object.entries(example)
  return Array.isArray(items) && items.every(item => item && fields.every(([key, value]) => {
    const field = item[key]
    return Array.isArray(value)
      ? Array.isArray(field) && field.every(entry => typeof entry === typeof value[0])
      : typeof field === typeof value
  })) && new Set(items.map(item => item.id)).size === items.length
}
