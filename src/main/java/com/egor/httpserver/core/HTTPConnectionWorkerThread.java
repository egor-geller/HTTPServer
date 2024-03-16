package com.egor.httpserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HTTPConnectionWorkerThread extends Thread {
    private final static Logger logger = LoggerFactory.getLogger(ServerListenerThread.class);
    private final Socket socket;

    public HTTPConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream(); OutputStream outputStream = socket.getOutputStream()) {

            int _byte;
            while ((_byte = inputStream.read()) >= 0) {
                System.out.print((char) _byte);
            }

            String html = "<html><head><title>Welcome</title></head><body><h1>This page was served using my simple HTTP Server</h1></body></html>";
            final String CRLF = "\n\r"; // ASCII 13, 10
            String response =
                    "HTTP/1.1 200 OK" + CRLF + // Status Line: HTTP version Response_code Response_message;
                            "Content-Length: " + html.getBytes().length + // Header
                            CRLF + CRLF +
                            html +
                            CRLF + CRLF;

            outputStream.write(response.getBytes());

            inputStream.close();
            outputStream.close();
            socket.close();
            logger.info("Connection Processing Finished");
        } catch (IOException e) {
            logger.error("Problem with communication", e);
        } finally {

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
