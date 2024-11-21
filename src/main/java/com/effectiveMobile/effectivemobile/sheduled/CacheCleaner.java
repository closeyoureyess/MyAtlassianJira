package com.effectiveMobile.effectivemobile.sheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * <pre>
 *     Класс-планировщик задач по очистке кэша
 * </pre>
 *
 */
@Component
@Slf4j
public class CacheCleaner {

    /**
     * Метод, очищающий кэш
     */
    // Очистка нескольких кэшей
    @CacheEvict(value = {"customUsers", "defaultSettingsCache"}, allEntries = true)
    @Scheduled(fixedRate = 3600000)
    public void clearCache() {
        log.info("Кэши очищен");
    }
}

