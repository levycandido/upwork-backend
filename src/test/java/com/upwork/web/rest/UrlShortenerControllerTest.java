package com.upwork.web.rest;

import com.upwork.service.UrlShortenerService;
import com.upwork.service.dao.UrlDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UrlShortenerControllerTest {

    @Mock
    private UrlShortenerService urlShortenerService;

    @InjectMocks
    private UrlShortenerController urlShortenerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(urlShortenerController).build();
    }

    @Test
    void getUrls() throws Exception {
        UrlDTO url1 = new UrlDTO();
        url1.setOriginalUrl("http://example.com/1");
        url1.setShortUrl("http://short.url/1");
        UrlDTO url2 = new UrlDTO();
        url2.setOriginalUrl("http://example.com/2");
        url2.setShortUrl("http://short.url/2");
        List<UrlDTO> urls = Arrays.asList(url1, url2);

        when(urlShortenerService.getAllUrls())
                .thenReturn(urls);

        mockMvc.perform(get("/api"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].originalUrl").value("http://example.com/1"))
                .andExpect(jsonPath("$[0].shortUrl").value("http://short.url/1"))
                .andExpect(jsonPath("$[1].originalUrl").value("http://example.com/2"))
                .andExpect(jsonPath("$[1].shortUrl").value("http://short.url/2"));
    }

    @Test
    void shortenUrl() throws Exception {
        UrlDTO originalUrl = new UrlDTO();
        originalUrl.setOriginalUrl("http://example.com");
        UrlDTO shortUrl = new UrlDTO();
        shortUrl.setOriginalUrl("http://example.com");
        shortUrl.setShortUrl("http://short.url");

        when(urlShortenerService.shortenUrl(any(UrlDTO.class))).thenReturn(shortUrl);

        mockMvc.perform(post("/api/shorter")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"originalUrl\":\"http://example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortUrl").value("http://short.url"));
    }

    @Test
    void updateUrl() throws Exception {
        UrlDTO updatedUrl = new UrlDTO();
        updatedUrl.setOriginalUrl("http://updated.com");
        updatedUrl.setShortUrl("http://short.url");

        when(urlShortenerService.updateUrl(anyLong(), any(UrlDTO.class))).thenReturn(Optional.of(updatedUrl));

        mockMvc.perform(put("/api/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"originalUrl\":\"http://updated.com\", \"shortUrl\":\"http://short.url\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originalUrl").value("http://updated.com"))
                .andExpect(jsonPath("$.shortUrl").value("http://short.url"));
    }

    @Test
    void deleteUrl() throws Exception {
        when(urlShortenerService.deleteUrl(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/api/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getOriginalUrl() throws Exception {
        when(urlShortenerService.getOriginalUrl("shortUrl")).thenReturn(Optional.of("http://original.com"));

        mockMvc.perform(get("/api/shortUrl"))
                .andExpect(status().isOk())
                .andExpect(content().string("http://original.com"));
    }
}