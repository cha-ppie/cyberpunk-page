"""Generate deterministic, multi-scale vector tears. No raster filters or runtime work."""
from pathlib import Path
import random

ROOT = Path(__file__).resolve().parents[1] / 'images' / 'edges'
ROOT.mkdir(parents=True, exist_ok=True)


def interpolate(x, anchors):
    for (a, va), (b, vb) in zip(anchors, anchors[1:]):
        if a <= x <= b:
            return va + (vb - va) * (x - a) / (b - a)
    return anchors[-1][1]


def rough_line(anchors, seed, fine=3.5):
    rng = random.Random(seed)
    points = []
    x = 0
    while x < anchors[-1][0]:
        # Broad ruptures come from anchors; tiny hooked chips break their straight edges.
        y = interpolate(x, anchors) + rng.uniform(-fine, fine)
        points.append((x, y))
        if rng.random() < .12:
            points.extend([(x + .7, y + rng.uniform(5, 12)), (x + 2, y - 2)])
        x += rng.uniform(2.5, 7)
    points.append(anchors[-1])
    return points


def path(points):
    return 'M' + ' L'.join(f'{x:.1f},{y:.1f}' for x, y in points) + ' Z'


def svg(name, width, height, shapes):
    paths = ''.join(f'<path d="{path(points)}"/>' for points in shapes)
    (ROOT / name).write_text(
        f'<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 {width} {height}" preserveAspectRatio="none"><g fill="white">{paths}</g></svg>\n',
        encoding='utf-8',
    )


def horizontal(name, seed, anchors):
    edge = rough_line(anchors, seed)
    # Detached splinters stay close to the seam instead of filling the whole page with noise.
    chips = []
    rng = random.Random(seed + 9)
    for x in [126, 307, 518, 742, 978, 1210, 1455]:
        y = interpolate(x, anchors) - rng.uniform(8, 18)
        width = rng.uniform(6, 25)
        chips.append([(x, y), (x + width, y - 2), (x + width * .6, y + 3), (x + 2, y + 5)])
    svg(name, 1600, 160, [edge + [(1600, 160), (0, 160)]] + chips)


horizontal('tear-a.svg', 23, [(0, 74), (110, 65), (184, 84), (232, 69), (315, 70), (350, 18), (368, 66), (450, 77), (569, 58), (646, 82), (704, 80), (720, 110), (746, 68), (880, 74), (964, 42), (1028, 70), (1163, 59), (1230, 83), (1284, 72), (1310, 112), (1330, 64), (1470, 49), (1600, 75)])
horizontal('tear-b.svg', 71, [(0, 65), (99, 82), (166, 72), (225, 103), (247, 62), (379, 69), (480, 44), (510, 76), (643, 66), (711, 26), (731, 68), (824, 80), (970, 53), (1084, 73), (1168, 65), (1200, 102), (1218, 58), (1342, 76), (1460, 52), (1510, 71), (1600, 66)])
horizontal('tear-diagonal.svg', 109, [(0, 135), (92, 118), (210, 126), (279, 94), (357, 107), (446, 84), (500, 95), (536, 51), (552, 89), (666, 78), (727, 97), (748, 66), (893, 70), (973, 43), (1050, 59), (1122, 19), (1140, 51), (1271, 40), (1333, 56), (1368, 20), (1483, 35), (1600, 10)])

# The hero's diagonal side tear has several long gouges and hundreds of fine nicks.
vertical_anchors = [(0, 984), (78, 965), (118, 977), (164, 943), (204, 954), (228, 896), (240, 947), (316, 923), (365, 939), (408, 899), (448, 912), (495, 867), (528, 894), (570, 831), (584, 880), (635, 858), (690, 871), (741, 824), (782, 842), (810, 778), (822, 830), (889, 792), (936, 808), (1000, 760)]
line = rough_line(vertical_anchors, 31, 3)
edge = [(y, x) for x, y in line]
side_chips = [[(934, 249), (962, 232), (953, 249), (940, 257)], [(880, 593), (906, 577), (897, 596)], [(831, 834), (855, 816), (843, 836)], [(803, 951), (826, 939), (819, 951)]]
svg('tear-side.svg', 1000, 1000, [[(0, 0)] + edge + [(0, 1000)]] + side_chips)

# Sparse scratches on the torn face; only the last few pixels of the sheet are distressed.
rng = random.Random(5)
scars = []
for y in range(85, 980, 13):
    x = interpolate(y, vertical_anchors)
    if rng.random() < .74:
        inset = rng.uniform(3, 20)
        length = rng.uniform(5, 25)
        scars.append([(x - inset, y), (x + 1, y - length), (x - inset + 3, y + 3)])
svg('tear-scars.svg', 1000, 1000, scars)

band_line = rough_line([(0, 76), (100, 62), (190, 85), (230, 24), (245, 76), (370, 48), (470, 79), (520, 35), (560, 65), (690, 44), (750, 84), (810, 31), (840, 68), (950, 48), (1000, 70)], 55, 2.2)
svg('tear-band.svg', 140, 1000, [[(140, 0), (140, 1000)] + [(y, x) for x, y in reversed(band_line)]])

print('Generated six deterministic SVG tear masks.')
