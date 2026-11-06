package com.syberpunk.content;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
    "spring.datasource.url=${TEST_DB_URL:jdbc:h2:mem:content-test;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1}",
    "spring.datasource.username=${TEST_DB_USERNAME:sa}",
    "spring.datasource.password=${TEST_DB_PASSWORD:}"
})
@AutoConfigureMockMvc
@Transactional
class ContentApiTest {
    private final MockMvc mvc;
    private final JdbcTemplate jdbc;
    private final ContentSeeder seeder;
    private final ApplicationArguments args;
    @Autowired ContentApiTest(MockMvc mvc, JdbcTemplate jdbc, ContentSeeder seeder, ApplicationArguments args) {
        this.mvc = mvc; this.jdbc = jdbc; this.seeder = seeder; this.args = args;
    }

    @Test void readsSeededContentInDisplayOrder() throws Exception {
        mvc.perform(get("/api/articles").param("category", "world"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(7))
            .andExpect(jsonPath("$[0].id").value("watson")).andExpect(jsonPath("$[0].name").value("沃森"));
        mvc.perform(get("/api/articles").param("category", "guide"))
            .andExpect(jsonPath("$.length()").value(6)).andExpect(jsonPath("$[0].steps.length()").value(3));
        mvc.perform(get("/api/gallery"))
            .andExpect(jsonPath("$.length()").value(18)).andExpect(jsonPath("$[0].width").value(2560));
    }

    @Test void filtersGalleryAndRejectsSqlInjectionAsPlainText() throws Exception {
        mvc.perform(get("/api/gallery").param("category", "装备"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(4));
        mvc.perform(get("/api/articles").param("category", "world' OR 1=1 --"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));
    }

    @Test void exposesDetailAndProblemResponse() throws Exception {
        mvc.perform(get("/api/articles/netrunner")).andExpect(status().isOk())
            .andExpect(jsonPath("$.equipment").exists());
        mvc.perform(get("/api/gallery/gallery-01")).andExpect(status().isOk())
            .andExpect(jsonPath("$.image").value("characters/V.jpg"));
        mvc.perform(get("/api/articles/missing")).andExpect(status().isNotFound())
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(jsonPath("$.detail").value("资料不存在"));
    }

    @Test void readsDatabaseEditsAndHidesUnpublishedContent() throws Exception {
        jdbc.update("UPDATE articles SET title = ? WHERE id = ?", "编辑后的标题", "watson");
        mvc.perform(get("/api/articles/watson")).andExpect(jsonPath("$.name").value("编辑后的标题"));
        jdbc.update("UPDATE articles SET published = FALSE WHERE id = 'watson'");
        mvc.perform(get("/api/articles/watson")).andExpect(status().isNotFound());
        mvc.perform(get("/api/articles").param("category", "world")).andExpect(jsonPath("$.length()").value(6));
        jdbc.update("UPDATE gallery_items SET published = FALSE WHERE id = 'gallery-01'");
        mvc.perform(get("/api/gallery/gallery-01")).andExpect(status().isNotFound());
        mvc.perform(get("/api/gallery")).andExpect(jsonPath("$.length()").value(17));
    }

    @Test void restartDoesNotOverwriteChangesOrRestoreDeletedRecords() throws Exception {
        jdbc.update("DELETE FROM articles WHERE id = 'watson'");
        jdbc.update("UPDATE gallery_items SET title = '保留修改' WHERE id = 'gallery-01'");
        seeder.run(args);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM articles", Integer.class)).isEqualTo(12);
        assertThat(jdbc.queryForObject("SELECT title FROM gallery_items WHERE id = 'gallery-01'", String.class)).isEqualTo("保留修改");
    }

    @Test void exposesHealthAndCategoriesButNoAnonymousWriteApi() throws Exception {
        mvc.perform(get("/api/health")).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("UP"));
        mvc.perform(get("/api/categories")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2));
        mvc.perform(post("/api/articles").contentType("application/json").content("{}"))
            .andExpect(status().isForbidden());
    }
}
