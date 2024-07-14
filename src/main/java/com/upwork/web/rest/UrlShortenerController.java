package com.upwork.web.rest;

import com.upwork.entity.Url;
import com.upwork.service.UrlShortenerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Tag(name = "URL Shortener", description = "Operations related to URL shortening")
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    @Autowired
    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    /**
     * Retrieves all URLs.
     * @return a list of all URLs.
     */
    @GetMapping()
    @Operation(summary = "Get all URLs", description = "Retrieves a list of all URLs")
    public ResponseEntity<List<Url>> getUrls() {
        List<Url> urls = urlShortenerService.getAllUrls();
        return ResponseEntity.ok(urls);
    }

    /**
     * Shortens a URL.
     * @param originalUrl the original URL to shorten.
     * @return the shortened URL.
     */
    @PostMapping("/shorter")
    @Operation(summary = "Shorten a URL", description = "Generates a short URL for the given original URL")
    public ResponseEntity<Url> shortenUrl(@RequestBody Url originalUrl) {
        Url shortUrl = urlShortenerService.shortenUrl(originalUrl);
        return ResponseEntity.created(URI.create(shortUrl.getShortUrl())).body(shortUrl);
    }

    /**
     * Updates a URL.
     * @param id the ID of the URL to update.
     * @param url the updated URL details.
     * @return the updated URL.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a URL", description = "Updates the details of an existing URL")
    public ResponseEntity<Url> updateUrl(@PathVariable Long id, @RequestBody Url url) {
        Optional<Url> updatedUrl = urlShortenerService.updateUrl(id, url);
        return updatedUrl.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Deletes a URL.
     * @param id the ID of the URL to delete.
     * @return a response indicating the result of the deletion.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a URL", description = "Deletes an existing URL by its ID")
    public ResponseEntity<Void> deleteUrl(@PathVariable Long id) {
        boolean deleted = urlShortenerService.deleteUrl(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Retrieves the original URL for a given shortened URL.
     * @param shortUrl the shortened URL.
     * @return the original URL if found, or 404 status if not found.
     */
    @GetMapping("/{shortUrl}")
    @Operation(summary = "Get original URL", description = "Retrieves the original URL for the given short URL")
    public ResponseEntity<String> getOriginalUrl(@PathVariable String shortUrl) {
        return urlShortenerService.getOriginalUrl(shortUrl)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
