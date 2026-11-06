package com.syberpunk.content;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ContentSeeder implements ApplicationRunner {
    private final JdbcTemplate jdbc;
    private final ObjectMapper mapper;

    public ContentSeeder(JdbcTemplate jdbc, ObjectMapper mapper) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        // 栏目存在即表示已初始化；重启不覆盖编辑，也不会恢复已删除的资料。
        if (jdbc.queryForObject("SELECT COUNT(*) FROM categories", Integer.class) > 0) return;
        jdbc.update("INSERT INTO categories (id, name, sort_order) VALUES ('world', '世界观', 0), ('guide', '攻略流派', 1)");
        seedArticles("districts", "world");
        seedArticles("builds", "guide");
        int order = 0;
        for (JsonNode item : load("gallery")) {
            jdbc.update("INSERT INTO gallery_items (id,title,category,image_path,alt,width,height,sort_order) VALUES (?,?,?,?,?,?,?,?)",
                item.path("id").asText(), item.path("title").asText(), item.path("category").asText(),
                item.path("image").asText(), item.path("alt").asText(), item.path("width").asInt(), item.path("height").asInt(), order++);
        }
    }

    private void seedArticles(String resource, String category) throws Exception {
        int order = 0;
        for (JsonNode item : load(resource)) {
            jdbc.update("INSERT INTO articles (id,category_id,title,summary,content,image_path,sort_order) VALUES (?,?,?,?,?,?,?)",
                item.path("id").asText(), category, item.path("name").asText(), item.path("description").asText(),
                mapper.writeValueAsString(item), item.path("image").asText(), order++);
        }
    }

    private JsonNode load(String name) throws Exception {
        try (var stream = new ClassPathResource("content/" + name + ".json").getInputStream()) {
            return mapper.readTree(stream);
        }
    }
}
