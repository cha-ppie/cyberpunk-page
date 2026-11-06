package com.syberpunk.content;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ContentRepository {
    private final JdbcTemplate jdbc;
    private final ObjectMapper mapper;

    public ContentRepository(JdbcTemplate jdbc, ObjectMapper mapper) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    public List<ObjectNode> articles(String category) {
        if (category == null) return jdbc.query("SELECT * FROM articles WHERE published = TRUE ORDER BY category_id, sort_order, id", this::readArticle);
        return jdbc.query("SELECT * FROM articles WHERE published = TRUE AND category_id = ? ORDER BY sort_order, id", this::readArticle, category);
    }

    public Optional<ObjectNode> article(String id) {
        return jdbc.query("SELECT * FROM articles WHERE published = TRUE AND id = ?", this::readArticle, id).stream().findFirst();
    }

    public List<ObjectNode> gallery(String category) {
        if (category == null) return jdbc.query("SELECT * FROM gallery_items WHERE published = TRUE ORDER BY sort_order, id", this::readGalleryItem);
        return jdbc.query("SELECT * FROM gallery_items WHERE published = TRUE AND category = ? ORDER BY sort_order, id", this::readGalleryItem, category);
    }

    public Optional<ObjectNode> galleryItem(String id) {
        return jdbc.query("SELECT * FROM gallery_items WHERE published = TRUE AND id = ?", this::readGalleryItem, id).stream().findFirst();
    }

    public List<?> categories() {
        return jdbc.queryForList("SELECT id, name FROM categories ORDER BY sort_order, id");
    }

    public List<ObjectNode> allArticles() {
        return jdbc.query("SELECT * FROM articles ORDER BY category_id, sort_order, id", this::readArticle);
    }
    public Optional<ObjectNode> adminArticle(String id) {
        return jdbc.query("SELECT * FROM articles WHERE id = ?", this::readArticle, id).stream().findFirst();
    }
    public List<ObjectNode> allGallery() {
        return jdbc.query("SELECT * FROM gallery_items ORDER BY sort_order, id", this::readGalleryItem);
    }
    public Optional<ObjectNode> adminGalleryItem(String id) {
        return jdbc.query("SELECT * FROM gallery_items WHERE id = ?", this::readGalleryItem, id).stream().findFirst();
    }

    private ObjectNode readArticle(ResultSet row, int index) throws SQLException {
        try {
            ObjectNode node = (ObjectNode) mapper.readTree(row.getString("content"));
            node.put("id", row.getString("id"));
            node.put("category", row.getString("category_id"));
            node.put("title", row.getString("title"));
            node.put("name", row.getString("title"));
            node.put("description", row.getString("summary"));
            node.put("image", row.getString("image_path"));
            node.put("published", row.getBoolean("published"));
            node.put("sortOrder", row.getInt("sort_order"));
            return node;
        } catch (JacksonException error) {
            throw new SQLException("Invalid stored article content", error);
        }
    }

    private ObjectNode readGalleryItem(ResultSet row, int index) throws SQLException {
        ObjectNode node = mapper.createObjectNode();
        for (String field : List.of("id", "title", "category", "alt")) node.put(field, row.getString(field));
        node.put("image", row.getString("image_path"));
        node.put("width", row.getInt("width"));
        node.put("height", row.getInt("height"));
        node.put("published", row.getBoolean("published"));
        node.put("sortOrder", row.getInt("sort_order"));
        return node;
    }
}
