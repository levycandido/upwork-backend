package com.upwork.web.rest;

import com.upwork.service.UrlShortenerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api")
@Tag(name = "URL Shortener", description = "Operations related to URL shortening")
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @PostMapping("/shorten")
    @Operation(summary = "Shorten a URL", description = "Generates a short URL for the given original URL")
    public ResponseEntity<String> shortenUrl(@RequestBody String originalUrl) {
        String shortUrl = urlShortenerService.shortenUrl(originalUrl);
        return ResponseEntity.created(URI.create(shortUrl)).body(shortUrl);
    }

    @GetMapping("/{shortUrl}")
    @Operation(summary = "Get original URL", description = "Retrieves the original URL for the given short URL")
    public ResponseEntity<String> getOriginalUrl(@PathVariable String shortUrl) {
        return urlShortenerService.getOriginalUrl(shortUrl)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
