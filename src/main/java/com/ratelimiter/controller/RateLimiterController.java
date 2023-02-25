package com.ratelimiter.controller;

import com.ratelimiter.service.RateLimiterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rate-limiter")
public class RateLimiterController {
    private final RateLimiterService rateLimitedService;


    @GetMapping("/info")
    public ResponseEntity<?> getDetails(HttpServletRequest request) throws UnknownHostException {
        rateLimitedService.processApiRequest(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

