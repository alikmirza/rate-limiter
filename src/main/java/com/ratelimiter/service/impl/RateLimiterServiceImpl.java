package com.ratelimiter.service.impl;

import com.ratelimiter.constant.Constant;
import com.ratelimiter.exception.CommonAPIException;
import com.ratelimiter.service.RateLimiterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateLimiterServiceImpl implements RateLimiterService {
    @Value("${api.count.limit}")
    private int apiCountLimit;
    private final RedisTemplate<String, Object> template;

    @Override
    public void processApiRequest(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        log.info("Request from IP address: {}", ip);
        if (isExceededApiRequestCount(ip)) {
            log.error("Exceeded api request count");
            notifyAboutIrresponsibleServer();
        }
        incrementApiRequestCount(ip);
    }

    @Override
    public boolean isExceededApiRequestCount(String ip) {
        return Integer.parseInt(getApiRequestCount(ip)) >= apiCountLimit;
    }

    @Override
    @Cacheable(cacheNames = Constant.API_REQUEST_COUNT, key = "{#ip}")
    public String getApiRequestCount(String ip) {
        log.info("Getting data from redis");
        Object cachedData = template.opsForValue().get(Constant.API_REQUEST_COUNT + Constant.DELIMITER + ip);
        return nonNull(cachedData) ? cachedData.toString() : Constant.ZERO;
    }

    @Override
    public void incrementApiRequestCount(String ip) {
        log.info("Incrementing count of requests of data");
        template.opsForValue().increment(Constant.API_REQUEST_COUNT + Constant.DELIMITER + ip);
    }

    @Override
    public void notifyAboutIrresponsibleServer() {
        throw new CommonAPIException(HttpStatus.BAD_GATEWAY, "Server is not responding");
    }
}
