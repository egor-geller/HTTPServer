package com.egor.httpserver.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest {

    private HttpParser httpParser;

    @BeforeAll
    public void beforeClass() {
        httpParser = new HttpParser();
    }

    @Test
    void parseHttpRequestTest() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateValidGETTestCase());
        } catch (HttpParsingException e) {
            fail(e);
        }

        assertEquals(request.getMethod(), HttpMethod.GET);
    }

    @Test
    void parseBadHttpRequestBadMethodOneTest() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadMethodName1());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_METHOD_NOT_IMPLEMENTED);
        }
    }

    @Test
    void parseBadHttpRequestBadMethodTwoTest() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadMethodName2());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_METHOD_NOT_IMPLEMENTED);
        }
    }

    @Test
    void parseBadHttpRequestBadMethodThreeTest() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadMethodName3());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
    void parseBadHttpRequestBadMethodForthTest() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadMethodName4());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
    void parseBadHttpRequestBadMethodFifthTest() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadMethodName5());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    private InputStream generateValidGETTestCase() {
        String rawData = "GET / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "sec-ch-ua: \"Chromium\";v=\"122\", \"Not(A:Brand\";v=\"24\", \"Google Chrome\";v=\"122\"\r\n" +
                "sec-ch-ua-mobile: ?0\r\n" +
                "sec-ch-ua-platform: \"macOS\"\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36\r\n" +
                "Sec-Purpose: prefetch;prerender\r\n" +
                "Purpose: prefetch\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                "Accept-Language: en-US,en;q=0.9,ru;q=0.8\r\n";

        return new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );
    }

    private InputStream generateBadMethodName1() {
        String rawData = "GeT / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-US,en;q=0.9,ru;q=0.8\r\n";

        return new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );
    }

    private InputStream generateBadMethodName2() {
        String rawData = "GETTTT / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-US,en;q=0.9,ru;q=0.8\r\n";

        return new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );
    }

    private InputStream generateBadMethodName3() {
        String rawData = "GET / HELLOWORLD HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-US,en;q=0.9,ru;q=0.8\r\n";

        return new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );
    }

    private InputStream generateBadMethodName4() {
        String rawData = "\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-US,en;q=0.9,ru;q=0.8\r\n";

        return new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );
    }

    private InputStream generateBadMethodName5() {
        String rawData = "GET / HTTP/1.1\r" + // <= No LF
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-US,en;q=0.9,ru;q=0.8\r\n";

        return new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );
    }
}