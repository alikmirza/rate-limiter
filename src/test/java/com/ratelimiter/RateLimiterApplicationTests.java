package com.ratelimiter;

import com.ratelimiter.configuration.TestRedisConfiguration;
import com.ratelimiter.service.RateLimiterService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestRedisConfiguration.class)
class RateLimiterApplicationTests {
    private final String IP_1 = "192.168.0.105";
    private final String IP_2 = "192.168.0.106";
    private final String IP_3 = "192.168.0.107";
    @Autowired
    private RateLimiterService rateLimiterService;

    @Test
    void emulateSimultaneousApiRequestsTest() {
        generateMockRequestData()
                .stream()
                .parallel()
                .forEach(o -> {
                    try {
                        rateLimiterService.processApiRequest(o);
                    } catch (Exception ignored) {

                    }
                });

        Assertions.assertTrue(0 < Integer.parseInt(rateLimiterService.getApiRequestCount(IP_1)));
        Assertions.assertTrue(0 < Integer.parseInt(rateLimiterService.getApiRequestCount(IP_2)));
        Assertions.assertTrue(0 < Integer.parseInt(rateLimiterService.getApiRequestCount(IP_3)));
    }

    private List<HttpServletRequest> generateMockRequestData() {
        ArrayList<HttpServletRequest> mockRequests = new ArrayList<>();
        MockHttpServletRequest firstRequest = new MockHttpServletRequest();
        MockHttpServletRequest secondRequest = new MockHttpServletRequest();
        MockHttpServletRequest thirdRequest = new MockHttpServletRequest();

        firstRequest.setRemoteAddr(IP_1);
        secondRequest.setRemoteAddr(IP_2);
        thirdRequest.setRemoteAddr(IP_3);

        mockRequests.add(firstRequest);
        mockRequests.add(secondRequest);
        mockRequests.add(thirdRequest);
        return mockRequests;
    }
}
