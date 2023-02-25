package com.ratelimiter.service;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;

public interface RateLimiterService {
    String getApiRequestCount(String ip);
    void incrementApiRequestCount(String ip);
    void processApiRequest(HttpServletRequest request) throws UnknownHostException;
    boolean isExceededApiRequestCount(String ip);
    void notifyAboutIrresponsibleServer();

}
