import { onBeforeUnmount } from 'vue'

export function useVisualEffects() {
  // 尊重系统的“减少动态效果”设置，为易晕动用户停用非必要动画。
  const reducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches
  let revealObserver
  let heroCleanup
  let canvas
  let animationFrame
  let resizeHandler

  // 创建全站背景粒子与流光画布。
  function startAmbient() {
    if (reducedMotion || document.querySelector('.ambient-canvas')) return
    canvas = document.createElement('canvas')
    canvas.className = 'ambient-canvas'
    canvas.setAttribute('aria-hidden', 'true')
    document.body.prepend(canvas)

    const context = canvas.getContext('2d')
    const particles = []
    const streams = []
    let width = 0
    let height = 0
    const dpr = Math.min(window.devicePixelRatio || 1, 2)
    // Canvas 按设备像素比缩放，在高分屏上保持清晰，同时将倍率限制为 2 以控制性能。
    const resize = () => {
      width = window.innerWidth
      height = window.innerHeight
      canvas.width = Math.floor(width * dpr)
      canvas.height = Math.floor(height * dpr)
      canvas.style.width = `${width}px`
      canvas.style.height = `${height}px`
      context.setTransform(dpr, 0, 0, dpr, 0, 0)
    }
    const newParticle = () => ({
      x: Math.random() * width, y: Math.random() * height,
      radius: Math.random() < 0.85 ? 0.7 : 1.4, speed: 0.08 + Math.random() * 0.18,
      drift: (Math.random() - 0.5) * 0.12, color: Math.random() > 0.56 ? '0,240,255' : '252,238,9', alpha: 0.14 + Math.random() * 0.32
    })
    const newStream = () => {
      const fromLeft = Math.random() < 0.5
      return { x: fromLeft ? -60 : width + 60, y: 80 + Math.random() * height * 0.45, length: 40 + Math.random() * 160, speed: (fromLeft ? 1 : -1) * (0.22 + Math.random() * 0.42), color: Math.random() > 0.5 ? '0,240,255' : '252,238,9', life: 0, maxLife: 450 + Math.random() * 500 }
    }
    // 每帧更新粒子位置和流光生命周期，然后绘制下一帧。
    const render = () => {
      context.clearRect(0, 0, width, height)
      particles.forEach((item) => {
        item.y -= item.speed; item.x += item.drift
        if (item.y < -5) item.y = height + 5
        if (item.x < -5) item.x = width + 5
        if (item.x > width + 5) item.x = -5
        context.beginPath(); context.arc(item.x, item.y, item.radius, 0, Math.PI * 2)
        context.fillStyle = `rgba(${item.color},${item.alpha})`; context.fill()
      })
      streams.forEach((item, index) => {
        item.x += item.speed; item.life += 1
        if (item.life > item.maxLife) { streams[index] = newStream(); return }
        const fade = Math.min(1, item.life / 90) * Math.min(1, (item.maxLife - item.life) / 90)
        const gradient = context.createLinearGradient(item.x, item.y, item.x - item.speed * item.length, item.y)
        gradient.addColorStop(0, `rgba(${item.color},${0.28 * fade})`); gradient.addColorStop(1, `rgba(${item.color},0)`)
        context.strokeStyle = gradient; context.lineWidth = 1; context.beginPath(); context.moveTo(item.x, item.y); context.lineTo(item.x - item.speed * item.length, item.y); context.stroke()
      })
      animationFrame = window.requestAnimationFrame(render)
    }

    resize()
    for (let index = 0; index < 46; index += 1) particles.push(newParticle())
    for (let index = 0; index < 4; index += 1) streams.push(newStream())
    resizeHandler = resize
    window.addEventListener('resize', resizeHandler, { passive: true })
    animationFrame = window.requestAnimationFrame(render)
  }

  function stopAmbient() {
    // 停止动画循环并移除画布及窗口监听。
    if (animationFrame) window.cancelAnimationFrame(animationFrame)
    if (resizeHandler) window.removeEventListener('resize', resizeHandler)
    canvas?.remove()
  }

  function setupReveal() {
    // 元素首次进入视口时添加 is-visible，完成一次性的滚动显现。
    revealObserver?.disconnect()
    const elements = [...document.querySelectorAll('.reveal')]
    if (!elements.length || reducedMotion || !('IntersectionObserver' in window)) {
      elements.forEach((element) => element.classList.add('is-visible'))
      return
    }
    revealObserver = new IntersectionObserver((entries, observer) => {
      entries.forEach((entry) => {
        if (!entry.isIntersecting) return
        entry.target.classList.add('is-visible')
        observer.unobserve(entry.target)
      })
    }, { threshold: 0.12, rootMargin: '0px 0px -40px' })
    elements.forEach((element) => revealObserver.observe(element))
  }

  function setupHeroMotion() {
    // 桌面端根据指针位置轻微移动首屏图片，触屏和减少动效模式下不启用。
    heroCleanup?.()
    if (reducedMotion || !window.matchMedia('(hover: hover) and (pointer: fine)').matches) return
    const hero = document.querySelector('.home-hero, .page-hero')
    const media = hero?.querySelector('.hero-media, .page-hero-media')
    if (!hero || !media) return
    let targetX = 0; let targetY = 0; let currentX = 0; let currentY = 0; let frameId = 0
    const render = () => {
      currentX += (targetX - currentX) * 0.08; currentY += (targetY - currentY) * 0.08
      media.style.setProperty('--hero-x', `${currentX.toFixed(2)}px`); media.style.setProperty('--hero-y', `${currentY.toFixed(2)}px`)
      frameId = Math.abs(targetX - currentX) > 0.02 || Math.abs(targetY - currentY) > 0.02 ? window.requestAnimationFrame(render) : 0
    }
    const wake = () => { if (!frameId) frameId = window.requestAnimationFrame(render) }
    const move = (event) => {
      const bounds = hero.getBoundingClientRect()
      targetX = ((event.clientX - bounds.left) / bounds.width - 0.5) * 10
      targetY = ((event.clientY - bounds.top) / bounds.height - 0.5) * 7
      wake()
    }
    const leave = () => { targetX = 0; targetY = 0; wake() }
    hero.addEventListener('pointermove', move, { passive: true })
    hero.addEventListener('pointerleave', leave, { passive: true })
    heroCleanup = () => {
      hero.removeEventListener('pointermove', move)
      hero.removeEventListener('pointerleave', leave)
      if (frameId) window.cancelAnimationFrame(frameId)
    }
  }

  // 每次路由页面渲染后重新扫描当前页面需要动效的元素。
  function refreshPageEffects() { setupReveal(); setupHeroMotion() }
  function stopPageEffects() { revealObserver?.disconnect(); heroCleanup?.() }

  onBeforeUnmount(stopPageEffects)
  return { refreshPageEffects, stopPageEffects, startAmbient, stopAmbient }
}
