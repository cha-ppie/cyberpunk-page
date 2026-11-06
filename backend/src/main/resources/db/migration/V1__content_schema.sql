CREATE TABLE categories (
    id VARCHAR(32) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    sort_order INT NOT NULL
);
CREATE TABLE articles (
    id VARCHAR(64) PRIMARY KEY,
    category_id VARCHAR(32) NOT NULL,
    title VARCHAR(200) NOT NULL,
    summary TEXT NOT NULL,
    content TEXT NOT NULL,
    image_path VARCHAR(500) NOT NULL,
    sort_order INT NOT NULL,
    published BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_articles_category FOREIGN KEY (category_id) REFERENCES categories(id)
);
CREATE INDEX idx_articles_category_order ON articles(category_id, sort_order);
CREATE TABLE gallery_items (
    id VARCHAR(64) PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    category VARCHAR(32) NOT NULL,
    image_path VARCHAR(500) NOT NULL,
    alt VARCHAR(500) NOT NULL,
    width INT NOT NULL,
    height INT NOT NULL,
    sort_order INT NOT NULL,
    published BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE INDEX idx_gallery_category_order ON gallery_items(category, sort_order);
