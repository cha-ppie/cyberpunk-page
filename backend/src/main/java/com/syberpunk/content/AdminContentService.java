package com.syberpunk.content;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.syberpunk.content.ContentRequests.ArticleInput;
import com.syberpunk.content.ContentRequests.GalleryInput;

@Service
@Transactional(readOnly = true)
public class AdminContentService {
    private final JdbcTemplate jdbc;
    private final ContentRepository repository;
    private final ObjectMapper mapper;
    private final Set<String> assets;

    public AdminContentService(JdbcTemplate jdbc, ContentRepository repository, ObjectMapper mapper) throws IOException {
        this.jdbc = jdbc; this.repository = repository; this.mapper = mapper;
        this.assets = new HashSet<>();
        try (var stream = new ClassPathResource("content/assets.json").getInputStream()) {
            for (var asset : mapper.readTree(stream)) this.assets.add(asset.asString());
        }
    }
    public List<ObjectNode> articles() { return repository.allArticles(); }
    public List<ObjectNode> gallery() { return repository.allGallery(); }
    public List<String> assets() { return assets.stream().sorted().toList(); }
    public ObjectNode article(String id) { return repository.adminArticle(id).orElseThrow(AdminContentService::missing); }
    public ObjectNode galleryItem(String id) { return repository.adminGalleryItem(id).orElseThrow(AdminContentService::missing); }

    @Transactional
    public ObjectNode createArticle(ArticleInput input) {
        validateArticle(input);
        jdbc.update("INSERT INTO articles (id,category_id,title,summary,content,image_path,sort_order,published) VALUES (?,?,?,?,?,?,?,?)",
            input.id(), input.category(), input.title(), input.description(), mapper.writeValueAsString(input.content()), input.image(), input.sortOrder(), input.published());
        return article(input.id());
    }
    @Transactional
    public ObjectNode updateArticle(String id, ArticleInput input) {
        if (!id.equals(input.id())) throw badRequest("保存时不能更改资料标识");
        validateArticle(input);
        int changed = jdbc.update("UPDATE articles SET category_id=?,title=?,summary=?,content=?,image_path=?,sort_order=?,published=? WHERE id=?",
            input.category(), input.title(), input.description(), mapper.writeValueAsString(input.content()), input.image(), input.sortOrder(), input.published(), id);
        if (changed == 0) throw missing();
        return article(id);
    }
    @Transactional
    public ObjectNode createGalleryItem(GalleryInput input) {
        validateImage(input.image());
        jdbc.update("INSERT INTO gallery_items (id,title,category,image_path,alt,width,height,sort_order,published) VALUES (?,?,?,?,?,?,?,?,?)",
            input.id(), input.title(), input.category(), input.image(), input.alt(), input.width(), input.height(), input.sortOrder(), input.published());
        return galleryItem(input.id());
    }
    @Transactional
    public ObjectNode updateGalleryItem(String id, GalleryInput input) {
        if (!id.equals(input.id())) throw badRequest("保存时不能更改图片标识");
        validateImage(input.image());
        int changed = jdbc.update("UPDATE gallery_items SET title=?,category=?,image_path=?,alt=?,width=?,height=?,sort_order=?,published=? WHERE id=?",
            input.title(), input.category(), input.image(), input.alt(), input.width(), input.height(), input.sortOrder(), input.published(), id);
        if (changed == 0) throw missing();
        return galleryItem(id);
    }
    @Transactional
    public void deleteArticle(String id) {
        if (jdbc.update("DELETE FROM articles WHERE id=?", id) == 0) throw missing();
    }
    @Transactional
    public void deleteGalleryItem(String id) {
        if (jdbc.update("DELETE FROM gallery_items WHERE id=?", id) == 0) throw missing();
    }
    private void validateArticle(ArticleInput input) {
        validateImage(input.image());
        var details = input.content();
        require(details.english());
        if ("world".equals(input.category())) require(details.label(), details.scene());
        else {
            require(details.approach(), details.attributes(), details.equipment(), details.mixing(), details.alt());
            if (details.steps() == null || details.steps().isEmpty()) throw badRequest("攻略至少需要一个实战步骤");
        }
    }
    private void validateImage(String image) {
        if (!assets.contains(image)) throw badRequest("请选择项目图片目录中的素材");
    }
    private static void require(String... values) {
        for (String value : values) if (value == null || value.isBlank()) throw badRequest("请填写所选栏目需要的全部资料字段");
    }
    private static ResponseStatusException missing() { return new ResponseStatusException(HttpStatus.NOT_FOUND, "资料不存在"); }
    private static ResponseStatusException badRequest(String message) { return new ResponseStatusException(HttpStatus.BAD_REQUEST, message); }
}
