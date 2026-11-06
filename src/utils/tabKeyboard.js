// Arrow keys move within a tab list; Home/End jump to its first/last item.
export function moveTab(event, index, count, select) {
  let next = index
  if (event.key === 'ArrowRight' || event.key === 'ArrowDown') next = (index + 1) % count
  else if (event.key === 'ArrowLeft' || event.key === 'ArrowUp') next = (index - 1 + count) % count
  else if (event.key === 'Home') next = 0
  else if (event.key === 'End') next = count - 1
  else return
  event.preventDefault()
  select(next)
  event.currentTarget.parentElement.querySelectorAll('[role="tab"]')[next]?.focus()
}
