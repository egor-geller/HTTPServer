package com.egor.httpserver;

import com.egor.httpserver.config.ConfigurationManager;
import com.egor.httpserver.core.ServerListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpServer {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) {
        LOGGER.info("Start Server...");

        try {
            ConfigurationManager.getInstance().loadConfigFile("src/main/resources/http.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var conf = ConfigurationManager.getInstance().getCurrentConfigFile();

        LOGGER.info("Using port: " + conf.getPort());
        LOGGER.info("Using webroot: " + conf.getWebroot());

        try {
            ServerListenerThread listener = new ServerListenerThread(conf.getPort(), conf.getWebroot());
            listener.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}