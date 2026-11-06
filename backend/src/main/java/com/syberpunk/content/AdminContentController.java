package com.syberpunk.content;

import java.util.List;
import jakarta.validation.Valid;
import tools.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.syberpunk.content.ContentRequests.ArticleInput;
import com.syberpunk.content.ContentRequests.GalleryInput;

@RestController
@RequestMapping("/api/admin")
public class AdminContentController {
    private final AdminContentService service;
    public AdminContentController(AdminContentService service) { this.service = service; }
    @GetMapping("/articles") public List<ObjectNode> articles() { return service.articles(); }
    @GetMapping("/gallery") public List<ObjectNode> gallery() { return service.gallery(); }
    @GetMapping("/assets") public List<String> assets() { return service.assets(); }
    @PostMapping("/articles") @ResponseStatus(HttpStatus.CREATED)
    public ObjectNode createArticle(@Valid @RequestBody ArticleInput input) { return service.createArticle(input); }
    @PutMapping("/articles/{id}")
    public ObjectNode updateArticle(@PathVariable String id, @Valid @RequestBody ArticleInput input) { return service.updateArticle(id, input); }
    @DeleteMapping("/articles/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArticle(@PathVariable String id) { service.deleteArticle(id); }
    @PostMapping("/gallery") @ResponseStatus(HttpStatus.CREATED)
    public ObjectNode createGalleryItem(@Valid @RequestBody GalleryInput input) { return service.createGalleryItem(input); }
    @PutMapping("/gallery/{id}")
    public ObjectNode updateGalleryItem(@PathVariable String id, @Valid @RequestBody GalleryInput input) { return service.updateGalleryItem(id, input); }
    @DeleteMapping("/gallery/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGalleryItem(@PathVariable String id) { service.deleteGalleryItem(id); }
}
