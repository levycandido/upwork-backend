package com.upwork.service;

import com.upwork.entity.Url;
import com.upwork.repository.ShortUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UrlShortenerService {

    private final ShortUrlRepository shortUrlRepository;

    @Autowired
    public UrlShortenerService(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    public String shortenUrl(String originalUrl) {
        String shortUrl = generateShortUrl();
        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortUrl(shortUrl);
        shortUrlRepository.save(url);
        return shortUrl;
    }

    public Optional<String> getOriginalUrl(String shortUrl) {
        Optional<Url> urlOpt = shortUrlRepository.findByShortUrl(shortUrl);
        if (urlOpt.isPresent()) {
            Url url = urlOpt.get();
            if (url.isExpired()) {
                shortUrlRepository.delete(url);
                return Optional.empty();
            } else {
                url.updateLastAccessTime();
                shortUrlRepository.save(url);
                return Optional.of(url.getOriginalUrl());
            }
        } else {
            return Optional.empty();
        }
    }

    public void deleteExpiredUrls(LocalDateTime threshold) {
        shortUrlRepository.findAll().stream()
                .filter(url -> url.getLastAccessTime().isBefore(threshold))
                .forEach(shortUrlRepository::delete);
    }

    protected String generateShortUrl() {
        return Long.toHexString(Double.doubleToLongBits(Math.random()));
    }
}
