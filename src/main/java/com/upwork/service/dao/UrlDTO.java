package com.upwork.service.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode()
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlDTO {

    private Long id;
    private String originalUrl;
    private String shortUrl;
    private LocalDateTime expiryDate;
    private LocalDateTime lastAccessTime;

}

