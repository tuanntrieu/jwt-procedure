package com.example.demojwt.jobs;


import com.example.demojwt.repository.TokenInvalidRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class ScheduleTask {
    private static final Logger log = LoggerFactory.getLogger(ScheduleTask.class);


    private final TokenInvalidRepository tokenInvalidRepository;

    @Scheduled(cron = "00 02 9 * * *", zone = "Asia/Ho_Chi_Minh")
    public void removeExpiredTokens() {
        Date now = new Date();
        int deletedCount = tokenInvalidRepository.deleteByExpiresTimeBefore(now);
        log.info("Removed {} expired tokens.", deletedCount);
    }
}
