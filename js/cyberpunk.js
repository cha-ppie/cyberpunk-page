(function () {
    "use strict";

    var reducedMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;
    var pagePath = window.location.pathname.split("/").pop() || "index.html";

    function initAmbientCanvas() {
        if (reducedMotion || document.querySelector(".ambient-canvas")) return;

        var canvas = document.createElement("canvas");
        canvas.className = "ambient-canvas";
        canvas.setAttribute("aria-hidden", "true");
        document.body.prepend(canvas);

        var ctx = canvas.getContext("2d");
        var particles = [];
        var streams = [];
        var width = 0;
        var height = 0;
        var dpr = Math.min(window.devicePixelRatio || 1, 2);

        function resize() {
            width = window.innerWidth;
            height = window.innerHeight;
            canvas.width = Math.floor(width * dpr);
            canvas.height = Math.floor(height * dpr);
            canvas.style.width = width + "px";
            canvas.style.height = height + "px";
            ctx.setTransform(dpr, 0, 0, dpr, 0, 0);
        }

        function particle() {
            return {
                x: Math.random() * width,
                y: Math.random() * height,
                radius: Math.random() < 0.85 ? 0.7 : 1.4,
                speed: 0.08 + Math.random() * 0.18,
                drift: (Math.random() - 0.5) * 0.12,
                color: Math.random() > 0.56 ? "0,240,255" : "252,238,9",
                alpha: 0.14 + Math.random() * 0.32
            };
        }

        function stream() {
            var fromLeft = Math.random() < 0.5;
            return {
                x: fromLeft ? -60 : width + 60,
                y: 80 + Math.random() * height * 0.45,
                length: 40 + Math.random() * 160,
                speed: (fromLeft ? 1 : -1) * (0.22 + Math.random() * 0.42),
                color: Math.random() > 0.5 ? "0,240,255" : "252,238,9",
                life: 0,
                maxLife: 450 + Math.random() * 500
            };
        }

        function frame() {
            ctx.clearRect(0, 0, width, height);

            particles.forEach(function (item) {
                item.y -= item.speed;
                item.x += item.drift;
                if (item.y < -5) item.y = height + 5;
                if (item.x < -5) item.x = width + 5;
                if (item.x > width + 5) item.x = -5;
                ctx.beginPath();
                ctx.arc(item.x, item.y, item.radius, 0, Math.PI * 2);
                ctx.fillStyle = "rgba(" + item.color + "," + item.alpha + ")";
                ctx.fill();
            });

            streams.forEach(function (item) {
                item.x += item.speed;
                item.life += 1;
                if (item.life > item.maxLife) {
                    var index = streams.indexOf(item);
                    streams.splice(index, 1, stream());
                    return;
                }
                var fade = Math.min(1, item.life / 90) * Math.min(1, (item.maxLife - item.life) / 90);
                var gradient = ctx.createLinearGradient(item.x, item.y, item.x - item.speed * item.length, item.y);
                gradient.addColorStop(0, "rgba(" + item.color + "," + (0.28 * fade) + ")");
                gradient.addColorStop(1, "rgba(" + item.color + ",0)");
                ctx.strokeStyle = gradient;
                ctx.lineWidth = 1;
                ctx.beginPath();
                ctx.moveTo(item.x, item.y);
                ctx.lineTo(item.x - item.speed * item.length, item.y);
                ctx.stroke();
            });

            window.requestAnimationFrame(frame);
        }

        resize();
        for (var i = 0; i < 46; i += 1) particles.push(particle());
        for (var j = 0; j < 4; j += 1) streams.push(stream());
        window.addEventListener("resize", resize, { passive: true });
        window.requestAnimationFrame(frame);
    }

    function initHeader() {
        var header = document.querySelector(".site-header");
        var toggle = document.querySelector(".nav-toggle");
        var panel = document.querySelector(".mobile-panel");
        if (!header) return;

        var observed = document.querySelector(".home-hero, .page-hero");
        if (observed && "IntersectionObserver" in window) {
            var headerObserver = new IntersectionObserver(function (entries) {
                header.classList.toggle("scrolled", !entries[0].isIntersecting);
            }, { threshold: 0.15 });
            headerObserver.observe(observed);
        }

        document.querySelectorAll(".site-nav a, .mobile-nav a").forEach(function (link) {
            var href = link.getAttribute("href") || "";
            if (href.split("#")[0] === pagePath || (pagePath === "" && href.indexOf("index.html") === 0)) {
                link.classList.add("active");
            }
        });

        if (!toggle || !panel) return;
        toggle.addEventListener("click", function () {
            var open = toggle.classList.toggle("is-open");
            panel.classList.toggle("is-open", open);
            toggle.setAttribute("aria-expanded", String(open));
        });

        panel.querySelectorAll("a").forEach(function (link) {
            link.addEventListener("click", function () {
                toggle.classList.remove("is-open");
                panel.classList.remove("is-open");
                toggle.setAttribute("aria-expanded", "false");
            });
        });
    }

    function initHeroMotion() {
        if (reducedMotion || !window.matchMedia("(hover: hover) and (pointer: fine)").matches) return;

        var hero = document.querySelector(".home-hero, .page-hero");
        var media = hero && hero.querySelector(".hero-media, .page-hero-media");
        if (!hero || !media) return;

        var targetX = 0;
        var targetY = 0;
        var currentX = 0;
        var currentY = 0;
        var frameId = 0;

        function render() {
            currentX += (targetX - currentX) * 0.08;
            currentY += (targetY - currentY) * 0.08;
            media.style.setProperty("--hero-x", currentX.toFixed(2) + "px");
            media.style.setProperty("--hero-y", currentY.toFixed(2) + "px");

            if (Math.abs(targetX - currentX) > 0.02 || Math.abs(targetY - currentY) > 0.02) {
                frameId = window.requestAnimationFrame(render);
            } else {
                frameId = 0;
            }
        }

        function wake() {
            if (!frameId) frameId = window.requestAnimationFrame(render);
        }

        hero.addEventListener("pointermove", function (event) {
            var bounds = hero.getBoundingClientRect();
            targetX = ((event.clientX - bounds.left) / bounds.width - 0.5) * 10;
            targetY = ((event.clientY - bounds.top) / bounds.height - 0.5) * 7;
            wake();
        }, { passive: true });

        hero.addEventListener("pointerleave", function () {
            targetX = 0;
            targetY = 0;
            wake();
        }, { passive: true });
    }

    function initReveal() {
        var items = document.querySelectorAll(".reveal");
        if (!items.length || reducedMotion || !("IntersectionObserver" in window)) {
            items.forEach(function (item) { item.classList.add("is-visible"); });
            return;
        }

        var observer = new IntersectionObserver(function (entries, io) {
            entries.forEach(function (entry) {
                if (!entry.isIntersecting) return;
                entry.target.classList.add("is-visible");
                io.unobserve(entry.target);
            });
        }, { threshold: 0.12, rootMargin: "0px 0px -40px" });

        items.forEach(function (item) { observer.observe(item); });
    }

    function initTypewriter() {
        var target = document.querySelector("[data-typewriter]");
        if (!target) return;

        var phrases = [
            "欢迎来到夜之城。这里的每一个选择都要付出代价。",
            "Wake up, Samurai. We have a city to burn.",
            "没有活着的传奇，只有被记住的死人。",
            "Never Fade Away."
        ];
        var phraseIndex = 0;
        var charIndex = 0;
        var deleting = false;

        if (reducedMotion) {
            target.textContent = phrases[0];
            return;
        }

        function tick() {
            var phrase = phrases[phraseIndex];
            var current = deleting ? phrase.slice(0, charIndex - 1) : phrase.slice(0, charIndex + 1);
            target.innerHTML = current + '<span class="type-cursor" aria-hidden="true"></span>';
            charIndex += deleting ? -1 : 1;

            if (!deleting && charIndex === phrase.length) {
                deleting = true;
                window.setTimeout(tick, 2500);
                return;
            }

            if (deleting && charIndex === 0) {
                deleting = false;
                phraseIndex = (phraseIndex + 1) % phrases.length;
            }

            window.setTimeout(tick, deleting ? 26 : 58);
        }

        window.setTimeout(tick, 500);
    }

    function initGallery() {
        var lightbox = document.querySelector(".lightbox");
        var lightboxImage = lightbox && lightbox.querySelector("img");
        var caption = lightbox && lightbox.querySelector(".lightbox-caption");
        var close = lightbox && lightbox.querySelector(".lightbox-close");
        if (!lightbox || !lightboxImage || !close) return;

        function closeLightbox() {
            lightbox.classList.remove("is-open");
            lightbox.setAttribute("aria-hidden", "true");
        }

        document.querySelectorAll(".gallery-item").forEach(function (item) {
            item.addEventListener("click", function () {
                var image = item.querySelector("img");
                if (!image) return;
                lightboxImage.src = image.currentSrc || image.src;
                lightboxImage.alt = image.alt;
                if (caption) caption.textContent = item.dataset.caption || image.alt;
                lightbox.classList.add("is-open");
                lightbox.setAttribute("aria-hidden", "false");
                close.focus();
            });
        });

        close.addEventListener("click", closeLightbox);
        lightbox.addEventListener("click", function (event) {
            if (event.target === lightbox) closeLightbox();
        });
        document.addEventListener("keydown", function (event) {
            if (event.key === "Escape") closeLightbox();
        });
    }

    function initForm() {
        var form = document.querySelector("[data-fake-form]");
        var message = document.querySelector("[data-form-message]");
        if (!form || !message) return;
        form.addEventListener("submit", function (event) {
            event.preventDefault();
            message.textContent = "本地演示模式：订阅接口未连接。";
            message.style.color = "var(--yellow)";
        });
    }

    initAmbientCanvas();
    initHeader();
    initHeroMotion();
    initReveal();
    initTypewriter();
    initGallery();
    initForm();
})();
