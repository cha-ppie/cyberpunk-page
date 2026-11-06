# Cyberpunk Design System

## Direction

A Chinese fan archive for Cyberpunk 2077. The homepage is a city poster ripped open to reveal its stories; the inner routes use topic-specific opening compositions, readable task-specific dossiers, and a yellow closing destination. Preserve the Cyberpunk wordmark, six routes, local fonts and photographs, and the non-official ownership notice.

## Foundation

- Ink `#030507`, signal yellow `#fcee09`, paper `#eceee8`, steel `#151c20`, secondary text `#a9b3b7`; cyan is used for keyboard focus on dark surfaces.
- Local Rajdhani for condensed Latin display. Tomorrow with Microsoft YaHei and PingFang SC fallbacks for body and Chinese headings.
- Large left-aligned headings, readable paragraphs and image-led navigation. Unequal image proportions create rhythm without repeated bordered cards.
- The homepage hero uses a real night street photograph and one dominant rising torn yellow plane. Additional tears separate the guide and animation blocks.
- One short hero arrival animation; hover feedback responds to the visitor. Homepage ambient overlays, looping typewriter, repeated reveals and decorative route numbers are removed. Reduced motion disables the arrival animation and transitions.
- Desktop containers use 48px gutters and a 1360px maximum. Mobile uses 20px gutters and a single content column; images keep defined heights and content remains in normal flow.

## Implementation

Vue 3 and Vite. `src/views/HomeView.vue` contains the homepage, `css/home-fracture.css` isolates its component styles, and `css/cyberpunk.css` supplies the shared foundation. The shared inner-page system is in `css/dossier.css` and `DossierHero.vue` / `DossierEnd.vue`. `css/hero-compositions.css` defines the world, game and guide variants and the poster-led series opening; styles are imported by `src/main.js`. Shared navigation and footer are in `src/components/`; routes are in `src/router/index.js`.

## Content boundaries

This is a non-official fan archive. Do not imply official ownership, live news, account services or newsletter delivery. The homepage closes with a working visual archive destination in place of the demonstration subscription form.

## Homepage redesign / frontend-design

Source: https://github.com/anthropics/skills/tree/main/skills/frontend-design
Installed: C:/Users/Lenovo/.codex/skills/frontend-design

Plan: a city poster ripped open to reveal its stories. Ink #030507, signal yellow #fcee09, paper #eceee8, steel #151c20, muted #a9b3b7, cyan #00f0ff for focus only. Rajdhani supplies condensed Latin display; Tomorrow and Microsoft YaHei supply readable body and Chinese display. Left alignment; one full-width city photograph and a large diagonal torn yellow plane supply the memorable gesture. Other modules use scale and image proportion, with no repeated card kit or arbitrary numbering.

Wireframe:
[ navigation                                      ]
[ CYBER / PUNK          full-width night city photo ]
[        torn diagonal edge / Chinese intro + CTA  ]
[ world context copy     | tall city photograph    ]
[ large game photograph | smaller guide photograph ]
[ character photograph         | animation story   ]
[ visual archive / wide image strip + archive CTA  ]

Brief review: the prior vertical split, rotated headings, numbered rows and placeholder newsletter repeated generic devices. Replace them with one dominant diagonal tear, clean horizontal headings, image-led destinations and a useful archive closing. Keep the local photography, brand palette and existing six route destinations. Motion is one brief hero arrival plus user-triggered image responses. At mobile widths, use full-width stacked images, smaller torn edges and visible normal-flow text.


## All-page redesign / frontend-design

Keep the existing palette and typography tokens. Every inner page uses an image-led torn hero and a shared yellow closing destination; body content follows the task, rather than sharing a repeated card grid. Boldness lives in the hero and major transitions. Text remains left aligned, in normal flow, with no clipping or animated reveals.

World: [torn city hero] [city statement] [district selector | scenic reading panel] [six faction image dossiers].
Game: [torn character hero] [Relic story] [three illustrated origins] [attribute selector | description] [cyberware reference].
Guide: [torn weapons hero] [quick context] [play-style filter] [expandable build dossiers] [three practical steps].
Series: [series hero] [work tabs] [poster | story] [extensible portrait grid]. Second work uses verified official character art and concise spoiler-free descriptions; first work uses the supplied vertical poster. City images and unnamed filler are not cast entries.
Gallery: [large typographic archive header] [category controls] [original-ratio masonry] [keyboard-enabled modal viewer].

Review: keep the site's intentional black/yellow visual language; replace the old dark overlay card kit, fake V console, decorative indexes, repeated glitch labels and cropped image thumbnails. No new libraries. Use native tabs, details, buttons and dialog semantics; implement keyboard support and small-screen collapse. Verify every route at desktop and mobile widths, verify filters and modal behavior, then build.


## Official reference and content provenance

Visual reference: https://www.cyberpunk.net/us/en/ (reviewed in browser). Adopt the official site's dominant signal yellow, black condensed display, strong photographic surfaces, angular buttons, and irregular module silhouettes. Preserve this project's fan-site identity rather than copying the official masthead, commercial CTAs or claims.

Edgerunners 2 character source: https://www.cyberpunk.net/en/edgerunners2 . Downloaded official cover and transparent portraits are in `images/edgerunners/`; the first-work poster is user supplied. The existing `NewGender.jpg` is correctly used as a second-work promotional poster, never as an unnamed character. Character grids auto-fit so future additions do not need filler cards.


## Validation

All six routes reviewed at 320px, 768px and 1440px widths. No horizontal text/layout overflow detected. Checked district and attribute tab switching with arrow keys; work tabs and full portrait containment; guide filtering and expansion; gallery category counts, modal next/previous, arrow navigation, Escape and return focus. Browser console reports no errors or warnings. Production build passes. Gallery thumbnails declare intrinsic dimensions so lazy loading reserves the correct space. Shared faction image frames constrain grid children to avoid artwork overflowing onto copy.


## Tear refinement

The earlier low-point polygon seams looked like regular sawteeth. Replace them with deterministic multi-scale SVG masks, generated by `scripts/generate-tears.py`: isolated long gouges establish the gesture; fine hooked nicks, close splinters and sparse scratches supply detail. An offset dark yellow underprint exposes a thin backing layer, while a deeper black cut separates the hero sheet from its image. Side, rising diagonal and horizontal seams use different paths. Keep all readable content and controls outside the masks. Quiet steel transitions use reduced amplitude; the hero and yellow closing sheet carry the strongest rupture. Mobile reduces the seam depth. Assets are vector geometry without runtime animation or raster filters.


## Four distinct entry compositions / frontend-design

Retain the established palette, local Rajdhani and readable Chinese typography. The shared half-yellow, half-image hero flattened the differences between topics. Separate composition from the tear material: World becomes a panoramic street with a full-width yellow horizontal fault; Game layers oversized type across a rising photographic cut, with V's face kept clear; Guide uses a full-width yellow headline above an asymmetric weapons collage; Series removes the generic hero entirely and brings work tabs and an uncropped poster into the opening. Paper backing and multi-scale chips remain consistent, while direction, mass, negative space and image placement vary. Keep text and controls unmasked. On small screens preserve these distinctions with different heading/image order and surface colors. Do not repeat a poster farther down the page.

Validation of the four compositions: reviewed at 320px, 768px and 1440px; no clipped heading/copy or horizontal overflow, no broken images, and no browser warnings/errors. Verified work switching by click and arrow key, full poster containment, and the world hero anchor. Final production build passes. Actual route screenshots and a four-up comparison are saved in the session visualization folder.

## Gallery caption typography / frontend-design

The requested text is the white caption under each thumbnail. Preserve the gallery composition, header lead, utility count and modal typography. Keep ink, yellow, paper, steel and muted colors. Use the site's Rajdhani / Chinese sans-serif display stack for 18–20px, 600-weight caption subjects in signal yellow. Contextual prefixes and scene descriptions use muted gray at 14–15px, normal weight. Keep the original title order: a person's name is the subject; generic prefixes such as city, street and equipment are context. Preserve 1.4 line-height and 14px clearance below the photograph. Mobile captions remain 18px. This gives each photograph a clear title while the images remain dominant; no extra ornaments or labels. The prior header-lead adjustment was outside the intended scope and is reverted.

## Guide play-style explorer / frontend-design

Use the existing ink #030507, yellow #fcee09, paper #eceee8, steel #151c20 and muted #afb9c1 tokens. Keep Rajdhani for display and Tomorrow / Chinese sans-serif for readable descriptions. Replace six identical closed accordion rows with an open photographic play-style explorer. Six compact keyboard-operable tabs select a large atmospheric photograph and its associated loadout; ordered steps convey the actual combat sequence. Each direction also offers a short mixing suggestion, with attribute priorities rather than rigid point prescriptions. Typography remains left aligned, with no decorative counters or ambient animation.

[ heading / invitation to choose a combat rhythm ]
[ six play-style selectors, two columns on mobile ]
[ large local image + English name | Chinese name / approach / equipment ]
[ three ordered actions across a continuous yellow strip ]
[ mixing suggestion in normal reading flow ]

Review: the previous filter plus repeated closed dossiers hid the differences between styles. Keep all six directions directly discoverable and their details immediately visible after selection. Do not replace the rows with six identical cards. Illustration provides atmosphere, never claims the pictured story character is the playable build. Mask only the photograph's decorative edge; leave controls and copy intact. On mobile the image precedes details and the action sequence stacks. Retain the existing hero and surrounding guide content.

Implementation: `src/components/BuildExplorer.vue` and `css/build-explorer.css`. Removed the obsolete accordion and filter styling from the shared dossier sheet. Checked all six selections at 320px, 768px and 1440px with no clipped copy or horizontal overflow. Verified selected-tab/panel association, all six image loads, Home and arrow-key navigation, and a clean browser console. Production build passes.


## Admin workspace / frontend-design

The admin route uses the established ink #030507, signal yellow #fcee09, paper #eceee8, steel #151c20, muted #a9b3b7 and cyan focus tokens. Rajdhani supplies the wordmark and main heading; Tomorrow / Chinese fallbacks support forms. A yellow rule anchors the login form; after login, a searchable archive list sits beside one editing surface. No dashboard metrics, decorative hero, or repeated card kit. At small widths the list and editor stack, with bounded list scrolling. All fields have labels; draft, publish, unpublish and delete actions use explicit names. Native confirmation protects deletion and unsaved changes.

[ brand / return to site / current account / sign out ]
[ content heading                                     ]
[ type + filters + list | editor + image + actions     ]

The familiar site palette stays intact, while the layout prioritizes routine content maintenance. Browser validation covered real session login and logout, new drafts, publishing and unpublishing, image association and intrinsic size detection. Checked 390px and 1440px layouts with no horizontal overflow or broken images. Screenshots are saved in the session visualization folder.
