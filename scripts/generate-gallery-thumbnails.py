import argparse
import json
from pathlib import Path

from PIL import Image, ImageOps


ROOT = Path(__file__).resolve().parent.parent
WIDTHS = (480, 960)


def load_json(name):
    return json.loads((ROOT / "content" / name).read_text(encoding="utf-8"))


def generate(paths):
    count = 0
    images = ROOT / "images"
    output = ROOT / "gallery-thumbnails"
    for path in sorted(set(paths)):
        source = (images / path).resolve()
        if not source.is_relative_to(images):
            raise ValueError(f"Invalid image path: {path}")
        relative = source.relative_to(images)
        with Image.open(source) as original:
            image = ImageOps.exif_transpose(original)
            image = image.convert("RGBA" if "A" in image.getbands() or "transparency" in image.info else "RGB")
            for width in WIDTHS:
                destination = output / f"{relative}.{width}.webp"
                if width > image.width:
                    destination.unlink(missing_ok=True)
                    continue
                height = max(1, round(image.height * width / image.width))
                destination.parent.mkdir(parents=True, exist_ok=True)
                thumbnail = image.resize((width, height), Image.Resampling.LANCZOS)
                thumbnail.save(destination, "WEBP", quality=78, method=6)
                count += 1
    print(f"Generated {count} WebP gallery thumbnails.")


def main():
    parser = argparse.ArgumentParser(description="Generate 480w and 960w gallery thumbnails.")
    parser.add_argument("--all-assets", action="store_true", help="Include every image available in the admin editor.")
    args = parser.parse_args()
    if args.all_assets:
        generate(load_json("assets.json"))
        return
    snapshot = ROOT / "content" / "published.json"
    entries = load_json("published.json")["gallery"] if snapshot.exists() else load_json("gallery.json")
    generate(entry["image"] for entry in entries)


if __name__ == "__main__":
    main()
