package com.egor.httpserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpParser {
    private final static Logger logger = LoggerFactory.getLogger(HttpParser.class);

    private static final int SP = 0x20; //32
    private static final int CR = 0x0D; //13
    private static final int LF = 0x0A; //10

    public HttpRequest parseHttpRequest(InputStream input) throws HttpParsingException {
        InputStreamReader reader = new InputStreamReader(input, StandardCharsets.US_ASCII);

        HttpRequest request = new HttpRequest();
        parserRequestLine(reader, request);
        parserReaders(reader, request);
        parserBody(reader, request);

        return request;
    }

    private void parserRequestLine(InputStreamReader reader, HttpRequest request) throws HttpParsingException {
        StringBuilder sb = new StringBuilder();

        boolean methodParsed = false;
        boolean requestTargetParsed = false;

        int _byte;
        try {
            while ((_byte = reader.read()) >= 0) {
                if (_byte == CR) {
                    _byte = reader.read();
                    if (_byte == LF) {
                        logger.debug("Request Line VERSION to Process: {}", sb);
                        if(!methodParsed || !requestTargetParsed) {
                            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                        }
                        return;
                    }
                }

                if (_byte == SP) {
                    if(!methodParsed) {
                        logger.debug("Request Line METHOD to Process: {}", sb);
                        request.setMethod(sb.toString());
                        methodParsed = true;
                    } else if (!requestTargetParsed) {
                        logger.debug("Request Line REQUEST TARGET to Process: {}", sb);
                        requestTargetParsed = true;
                    }else {
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }
                    sb.delete(0, sb.length());
                } else {
                    sb.append((char) _byte);
                    if(!methodParsed && sb.length() > HttpMethod.MAX_LENGTH) {
                        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_METHOD_NOT_IMPLEMENTED);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("There was problem in parsing the Request Line", e);
        }
    }

    private void parserReaders(InputStreamReader reader, HttpRequest request) {
    }

    private void parserBody(InputStreamReader reader, HttpRequest request) {
    }

}
