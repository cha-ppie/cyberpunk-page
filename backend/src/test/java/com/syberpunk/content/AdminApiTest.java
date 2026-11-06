package com.syberpunk.content;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
    "spring.datasource.url=${TEST_DB_URL:jdbc:h2:mem:content-test;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1}",
    "spring.datasource.username=${TEST_DB_USERNAME:sa}",
    "spring.datasource.password=${TEST_DB_PASSWORD:}"
})
@AutoConfigureMockMvc
@Transactional
class AdminApiTest {
    private static final String PASSWORD = "integration-test-password";
    private static final String HASH = new BCryptPasswordEncoder(4).encode(PASSWORD);
    private static final AtomicInteger CLIENT = new AtomicInteger();
    private final MockMvc mvc;
    private final JdbcTemplate jdbc;
    private final ObjectMapper mapper;
    private String address;
    @Autowired AdminApiTest(MockMvc mvc, JdbcTemplate jdbc, ObjectMapper mapper) {
        this.mvc = mvc; this.jdbc = jdbc; this.mapper = mapper;
    }
    @BeforeEach void account() {
        address = "198.51.100." + CLIENT.incrementAndGet();
        jdbc.update("INSERT INTO users (username,password_hash,role) VALUES ('integration-admin',?,'ADMIN')", HASH);
    }
    private MvcResult login(String password, MockHttpSession session) throws Exception {
        return mvc.perform(post("/api/auth/login").session(session).with(csrf())
            .with(request -> { request.setRemoteAddr(address); return request; })
            .param("username", "integration-admin").param("password", password)).andReturn();
    }
    private MockHttpSession session() throws Exception {
        var result = login(PASSWORD, new MockHttpSession());
        assertThat(result.getResponse().getStatus()).isEqualTo(204);
        return (MockHttpSession) result.getRequest().getSession(false);
    }
    private ObjectNode articleInput() throws Exception {
        return (ObjectNode) mapper.readTree("""
          {"id":"integration-article","category":"world","title":"测试城区","description":"测试说明",
           "image":"game-media/watson.jpg","sortOrder":25,"published":false,
           "content":{"english":"Test District","label":"测试","scene":"测试城区画面"}}
          """);
    }
    private ObjectNode galleryInput() throws Exception {
        return (ObjectNode) mapper.readTree("""
          {"id":"integration-picture","category":"城市","title":"测试画面","image":"game-media/watson.jpg",
           "alt":"测试图片说明","width":1000,"height":600,"sortOrder":25,"published":false}
          """);
    }
    @Test void sessionLoginLogoutAndCsrfTokenRotation() throws Exception {
        var initial = mvc.perform(get("/api/auth/csrf")).andExpect(status().isOk()).andReturn();
        var token = mapper.readTree(initial.getResponse().getContentAsString());
        var session = (MockHttpSession) initial.getRequest().getSession(false);
        String oldId = session.getId();
        mvc.perform(post("/api/auth/login").session(session).param("username", "integration-admin").param("password", PASSWORD))
            .andExpect(status().isForbidden());
        var signedIn = mvc.perform(post("/api/auth/login").session(session)
            .header(token.path("headerName").asString(), token.path("token").asString())
            .param("username", "integration-admin").param("password", PASSWORD)).andExpect(status().isNoContent()).andReturn();
        var authenticated = (MockHttpSession) signedIn.getRequest().getSession(false);
        assertThat(authenticated.getId()).isNotEqualTo(oldId);
        mvc.perform(get("/api/auth/me").session(authenticated)).andExpect(jsonPath("$.username").value("integration-admin"));
        mvc.perform(post("/api/admin/articles").session(authenticated)
            .header(token.path("headerName").asString(), token.path("token").asString())
            .contentType("application/json").content(articleInput().toString())).andExpect(status().isForbidden());
        mvc.perform(post("/api/auth/logout").session(authenticated).with(csrf())).andExpect(status().isNoContent());
        assertThat(authenticated.isInvalid()).isTrue();
        mvc.perform(get("/api/admin/articles")).andExpect(status().isUnauthorized());
    }
    @Test void rejectsWrongPasswordAndLimitsRepeatedFailures() throws Exception {
        for (int i = 0; i < 5; i++) assertThat(login("wrong-password", new MockHttpSession()).getResponse().getStatus()).isEqualTo(401);
        assertThat(login(PASSWORD, new MockHttpSession()).getResponse().getStatus()).isEqualTo(429);
    }
    @Test void rejectsOversizedLoginCredentialsWithoutServerError() throws Exception {
        assertThat(login("x".repeat(73), new MockHttpSession()).getResponse().getStatus()).isEqualTo(401);
    }
    @Test @WithMockUser(roles = "USER") void deniesNonAdministrator() throws Exception {
        mvc.perform(get("/api/admin/articles")).andExpect(status().isForbidden());
        mvc.perform(post("/api/admin/articles").with(csrf()).contentType("application/json").content(articleInput().toString()))
            .andExpect(status().isForbidden());
    }
    @Test void articleDraftPublishUnpublishEditAndDelete() throws Exception {
        var session = session(); var input = articleInput();
        mvc.perform(post("/api/admin/articles").session(session).contentType("application/json").content(input.toString()))
            .andExpect(status().isForbidden());
        mvc.perform(post("/api/admin/articles").session(session).with(csrf()).contentType("application/json").content(input.toString()))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.published").value(false));
        mvc.perform(get("/api/articles/integration-article")).andExpect(status().isNotFound());
        mvc.perform(get("/api/admin/articles").session(session)).andExpect(jsonPath("$[?(@.id == 'integration-article')]").isNotEmpty());
        input.put("published", true).put("title", "新的中文标题");
        mvc.perform(put("/api/admin/articles/integration-article").session(session).with(csrf()).contentType("application/json").content(input.toString()))
            .andExpect(status().isOk());
        mvc.perform(get("/api/articles/integration-article")).andExpect(jsonPath("$.name").value("新的中文标题"));
        input.put("published", false);
        mvc.perform(put("/api/admin/articles/integration-article").session(session).with(csrf()).contentType("application/json").content(input.toString())).andExpect(status().isOk());
        mvc.perform(get("/api/articles/integration-article")).andExpect(status().isNotFound());
        mvc.perform(delete("/api/admin/articles/integration-article").session(session).with(csrf())).andExpect(status().isNoContent());
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM articles WHERE id='integration-article'", Integer.class)).isZero();
    }
    @Test void galleryDraftEditPublishAndDelete() throws Exception {
        var session = session(); var input = galleryInput();
        mvc.perform(post("/api/admin/gallery").session(session).with(csrf()).contentType("application/json").content(input.toString())).andExpect(status().isCreated());
        mvc.perform(get("/api/gallery/integration-picture")).andExpect(status().isNotFound());
        input.put("published", true).put("title", "发布的画面");
        mvc.perform(put("/api/admin/gallery/integration-picture").session(session).with(csrf()).contentType("application/json").content(input.toString())).andExpect(status().isOk());
        mvc.perform(get("/api/gallery/integration-picture")).andExpect(jsonPath("$.title").value("发布的画面"));
        mvc.perform(delete("/api/admin/gallery/integration-picture").session(session).with(csrf())).andExpect(status().isNoContent());
        mvc.perform(get("/api/gallery/integration-picture")).andExpect(status().isNotFound());
    }
    @Test void rejectsDuplicatesInvalidFieldsAndUnknownAssets() throws Exception {
        var session = session(); var input = articleInput();
        input.put("id", "watson");
        mvc.perform(post("/api/admin/articles").session(session).with(csrf()).contentType("application/json").content(input.toString())).andExpect(status().isConflict());
        input.put("id", "bad/path");
        mvc.perform(post("/api/admin/articles").session(session).with(csrf()).contentType("application/json").content(input.toString())).andExpect(status().isBadRequest());
        input.put("id", "integration-article").put("image", "../outside.jpg");
        mvc.perform(post("/api/admin/articles").session(session).with(csrf()).contentType("application/json").content(input.toString())).andExpect(status().isBadRequest());
        input.put("image", "game-media/watson.jpg").put("category", "guide");
        mvc.perform(post("/api/admin/articles").session(session).with(csrf()).contentType("application/json").content(input.toString())).andExpect(status().isBadRequest());
        var image = galleryInput().put("width", 0);
        mvc.perform(post("/api/admin/gallery").session(session).with(csrf()).contentType("application/json").content(image.toString())).andExpect(status().isBadRequest());
        mvc.perform(get("/api/admin/assets").session(session)).andExpect(status().isOk()).andExpect(jsonPath("$[0]").isString());
    }
}
