package com.upwork.service;

import com.upwork.ObjectMapperUtils;
import com.upwork.entity.Url;
import com.upwork.repository.ShortUrlRepository;
import com.upwork.service.dao.UrlDTO;
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
     *
     * @param urlDTO the original URL to shorten.
     * @return the shortened URL.
     */
    public UrlDTO shortenUrl(UrlDTO urlDTO) {
        Url url = ObjectMapperUtils.map(urlDTO, Url.class);
        String shortUrl = generateShortUrl();
        urlDTO.setOriginalUrl(url.getOriginalUrl());
        urlDTO.setShortUrl(shortUrl);
        return ObjectMapperUtils.map(shortUrlRepository.save(url), UrlDTO.class);
    }

    /**
     * Retrieves the original URL for a given shortened URL.
     *
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
     *
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
     *
     * @param threshold the date threshold for deletion.
     */
    public void deleteExpiredUrls(LocalDateTime threshold) {
        shortUrlRepository.findAll().stream()
                .filter(url -> url.getLastAccessTime().isBefore(threshold))
                .forEach(shortUrlRepository::delete);
    }

    /**
     * Generates a random short URL.
     *
     * @return the generated short URL.
     */
    protected String generateShortUrl() {
        return Long.toHexString(Double.doubleToLongBits(Math.random()));
    }

    /**
     * Retrieves all URLs.
     *
     * @return a list of all URLs.
     */
    public List<UrlDTO> getAllUrls() {
        return ObjectMapperUtils.mapAll(shortUrlRepository.findAll(), UrlDTO.class);
    }

    /**
     * Updates a URL.
     *
     * @param id         the ID of the URL to update.
     * @param updatedUrl the updated URL details.
     * @return an optional containing the updated URL if successful, or empty if not found.
     */
    public Optional<UrlDTO> updateUrl(Long id, UrlDTO updatedUrl) {


        Optional<Url> urlOptional = shortUrlRepository.findById(id).map(url -> {
            url.setOriginalUrl(updatedUrl.getOriginalUrl());
            url.setShortUrl(updatedUrl.getShortUrl());
            url.setExpiryDate(updatedUrl.getExpiryDate());
            return shortUrlRepository.save(url);
        });

        return urlOptional
                .map(url -> Optional.ofNullable(ObjectMapperUtils.map(url, UrlDTO.class)))
                .orElse(null);
    }
}
