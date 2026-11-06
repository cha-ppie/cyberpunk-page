package com.syberpunk.content;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public final class ContentRequests {
    private ContentRequests() {}
    public record ArticleInput(
        @NotBlank @Size(max = 64) @Pattern(regexp = "[a-z0-9]+(?:-[a-z0-9]+)*") String id,
        @NotNull @Pattern(regexp = "world|guide") String category,
        @NotBlank @Size(max = 200) String title,
        @NotBlank @Size(max = 4000) String description,
        @NotBlank @Size(max = 500) String image,
        @Min(0) @Max(99999) int sortOrder,
        @NotNull Boolean published,
        @NotNull @Valid ArticleDetails content) {}
    public record ArticleDetails(
        @Size(max = 100) String english,
        @Size(max = 100) String label,
        @Size(max = 500) String scene,
        @Size(max = 500) String approach,
        @Size(max = 500) String attributes,
        @Size(max = 500) String equipment,
        @Size(max = 10) List<@NotBlank @Size(max = 200) String> steps,
        @Size(max = 2000) String mixing,
        @Size(max = 500) String alt) {}
    public record GalleryInput(
        @NotBlank @Size(max = 64) @Pattern(regexp = "[a-z0-9]+(?:-[a-z0-9]+)*") String id,
        @NotBlank @Size(max = 200) String title,
        @NotNull @Pattern(regexp = "人物|城市|装备") String category,
        @NotBlank @Size(max = 500) String image,
        @NotBlank @Size(max = 500) String alt,
        @Min(1) @Max(30000) int width,
        @Min(1) @Max(30000) int height,
        @Min(0) @Max(99999) int sortOrder,
        @NotNull Boolean published) {}
}
