package com.upwork.service;

import com.upwork.entity.Url;
import com.upwork.repository.ShortUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UrlShortenerService {

    private final ShortUrlRepository shortUrlRepository;

    @Autowired
    public UrlShortenerService(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    /**
     * Shortens a given URL and saves it in the repository.
     * @param url the original URL to shorten.
     * @return the shortened URL.
     */
    public Url shortenUrl(Url url) {
        String shortUrl = generateShortUrl();
        url.setOriginalUrl(url.getOriginalUrl());
        url.setShortUrl(shortUrl);
        return shortUrlRepository.save(url);
    }

    /**
     * Retrieves the original URL for a given shortened URL.
     * @param shortUrl the shortened URL.
     * @return an optional containing the original URL if found, or empty if not found or expired.
     */
    public Optional<String> getOriginalUrl(String shortUrl) {
        Optional<Url> optionalUrl = shortUrlRepository.findByShortUrl(shortUrl);
        if (optionalUrl.isPresent()) {
            Url url = optionalUrl.get();
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

    /**
     * Deletes a URL.
     * @param id the ID of the URL to delete.
     * @return true if the URL was deleted, false if not found.
     */
    public boolean deleteUrl(Long id) {
        return shortUrlRepository.findById(id).map(url -> {
            shortUrlRepository.delete(url);
            return true;
        }).orElse(false);
    }

    /**
     * Deletes URLs that have not been accessed before the given threshold date.
     * @param threshold the date threshold for deletion.
     */
    public void deleteExpiredUrls(LocalDateTime threshold) {
        shortUrlRepository.findAll().stream()
                .filter(url -> url.getLastAccessTime().isBefore(threshold))
                .forEach(shortUrlRepository::delete);
    }

    /**
     * Generates a random short URL.
     * @return the generated short URL.
     */
    protected String generateShortUrl() {
        return Long.toHexString(Double.doubleToLongBits(Math.random()));
    }

    /**
     * Retrieves all URLs.
     * @return a list of all URLs.
     */
    public List<Url> getAllUrls() {
        return shortUrlRepository.findAll();
    }

    /**
     * Updates a URL.
     * @param id the ID of the URL to update.
     * @param updatedUrl the updated URL details.
     * @return an optional containing the updated URL if successful, or empty if not found.
     */
    public Optional<Url> updateUrl(Long id, Url updatedUrl) {
        return shortUrlRepository.findById(id).map(url -> {
            url.setOriginalUrl(updatedUrl.getOriginalUrl());
            url.setShortUrl(updatedUrl.getShortUrl());
            url.setExpiryDate(updatedUrl.getExpiryDate());
            return shortUrlRepository.save(url);
        });
    }
}
