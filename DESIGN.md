# Syberpunk Design System

## Direction

Syberpunk is a Night City broadcast wall. The site uses official-site-adjacent editorial pacing: one cinematic image establishes the scene, then structured routes lead to game, world, show, community, and archive dossiers. The old card-heavy surface was replaced with media-led splits, indexed rows, and yellow content modules whose own top edge is torn into the black field.

## Visual language

- One dark theme across the site: void black (`#030507`), soft black (`#080b10`), and steel panels (`#10161d`, `#172029`).
- Signal colors are structural as well as interactive: neon yellow (`#fcee09`) owns the torn transitions and closing signal bands, while terminal cyan (`#00f0ff`), synth pink (`#ff2a6d`), and alert red (`#ff003c`) remain accents for state and interaction.
- Backgrounds are layered broadcast fields rather than flat fills: a yellow-black hero wash sits behind the local scene image, while cool/hot atmospheric pools, architectural scan grids, dust, and low-opacity signal bands keep content sections spatially alive.
- Section changes are expressed through direct dark-to-dark continuity. Only a true yellow content module receives a jagged clip-path edge, so the tear reads as the module's silhouette rather than a separator line. Dark rows use alternating opacity only to preserve scanning order.
- No rounded corners. Media, buttons, and panels use asymmetric clip-path cuts.
- Images lead the experience. Filters lower saturation and lift contrast so text remains legible without hiding the subject.
- Body text uses Tomorrow with local Chinese fallbacks. Display titles use Rajdhani. Courier New is reserved for terminal readouts.

## Layout

- Fixed 72px desktop header and compact mobile navigation panel.
- Content uses a 1200px max-width container with 48px desktop gutters and 32px mobile gutters.
- Home first viewport: full-bleed image, oversized title, typewriter signal, two actions, and a small right-side readout.
- Home flow: city context, asymmetric route wall, one story feature, closing transmission, and a high-contrast yellow newsletter signal band.
- Content pages use an image-led page hero, then alternating split, indexed list, and grid modules.
- Gallery uses an asymmetric 12-column image grid and a keyboard-closeable lightbox.

## Motion and behavior

- Ambient canvas particles and data streams provide slow background motion only when reduced motion is not requested.
- The background backplane uses a slow drift across scan grids and color pools. Hero media adds a slow camera-breath scale, one restrained signal sweep, and desktop-only pointer parallax. These effects belong to the broadcast-wall focal image and stop under reduced motion while the static scene remains visible.
- Headings use rare sliced signal interference. The home subtitle uses a restrained typewriter loop.
- IntersectionObserver reveals content as it enters the viewport. Hover feedback changes image treatment or uses transform, never layout shifts.
- Mobile navigation is a right-side panel toggled by a labelled button.
- `prefers-reduced-motion: reduce` disables background motion, typewriter, glitch, and reveal transitions.

## Content boundaries

The site is a non-official Chinese fan archive. It does not claim official ownership, live news, newsletter delivery, user accounts, or connected services. The newsletter area is explicitly marked as a local demonstration placeholder.

## Source of truth

Implemented in `index.html`, `world.html`, `game.html`, `guide.html`, `edgerunners.html`, `gallery.html`, `css/cyberpunk.css`, and `js/cyberpunk.js`.
