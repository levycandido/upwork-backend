package com.upwork.service;

import com.upwork.ObjectMapperUtils;
import com.upwork.entity.Url;
import com.upwork.repository.ShortUrlRepository;
import com.upwork.service.dao.UrlDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

public class UrlShortenerServiceTest {

    @Spy
    @InjectMocks
    private UrlShortenerService urlShortenerService;

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShortenAndRetrieveUrl() {
        String originalUrl = "https://example.com";
        String shortUrl = "abc123";

        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortUrl(shortUrl);

        when(shortUrlRepository.save(any(Url.class))).thenReturn(url);
        when(shortUrlRepository.findByShortUrl(anyString())).thenReturn(Optional.of(url));
        doReturn(shortUrl).when(urlShortenerService).generateShortUrl(); // Mock generateShortUrl

        // Shortening the URL
        UrlDTO generatedShortUrl = urlShortenerService.shortenUrl(ObjectMapperUtils.map(url, UrlDTO.class));
        System.out.println("Generated Short URL: " + generatedShortUrl); // Debug statement
        assertEquals(shortUrl, generatedShortUrl.getShortUrl());

        // Retrieving the original URL
        Optional<String> retrievedUrl = urlShortenerService.getOriginalUrl(shortUrl);
        System.out.println("Retrieved Original URL: " + retrievedUrl); // Debug statement
        assertEquals(Optional.of(originalUrl), retrievedUrl);
    }
}
