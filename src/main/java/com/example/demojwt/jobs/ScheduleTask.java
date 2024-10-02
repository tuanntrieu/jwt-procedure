package com.example.demojwt.jobs;


import com.example.demojwt.repository.TokenInvalidRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduleTask {
    private static final Logger log = LoggerFactory.getLogger(ScheduleTask.class);

    @Autowired
    private TokenInvalidRepository tokenInvalidRepository;

    @Scheduled(cron = "00 00 9 * * *", zone = "Asia/Ho_Chi_Minh")
    public void removeExpiredTokens() {
        Date now = new Date();
        int deletedCount = tokenInvalidRepository.deleteByExpiresTimeBefore(now);
        log.info("Removed {} expired tokens.", deletedCount);
    }
}
