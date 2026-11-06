package com.syberpunk.content;

import java.util.List;
import tools.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class ContentController {
    private final ContentRepository repository;
    public ContentController(ContentRepository repository) { this.repository = repository; }

    @GetMapping("/categories")
    public List<?> categories() { return repository.categories(); }

    @GetMapping("/articles")
    public List<ObjectNode> articles(@RequestParam(required = false) String category) {
        return repository.articles(category);
    }

    @GetMapping("/articles/{id}")
    public ObjectNode article(@PathVariable String id) {
        return repository.article(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "资料不存在"));
    }

    @GetMapping("/gallery")
    public List<ObjectNode> gallery(@RequestParam(required = false) String category) {
        return repository.gallery(category);
    }

    @GetMapping("/gallery/{id}")
    public ObjectNode galleryItem(@PathVariable String id) {
        return repository.galleryItem(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "图片不存在"));
    }
}
