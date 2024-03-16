package com.egor.httpserver.config;

import com.egor.httpserver.config.Exception.HttpConfigurationException;
import com.egor.httpserver.util.Json;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationManager {

    private static ConfigurationManager manager;
    private static Configuration myCurrentConfig;

    private ConfigurationManager() {
    }

    public static ConfigurationManager getInstance() {
        if (manager == null) {
            manager = new ConfigurationManager();
        }
        return manager;
    }

    /**
     * Used to load a config file by
     *
     * @param filePath provided
     */
    public void loadConfigFile(String filePath) throws IOException {
        StringBuffer sb = new StringBuffer();

        try (var fileReader = new FileReader(filePath)) {
            int i;
            while ((i = fileReader.read()) != -1) {
                sb.append((char) i);
            }
        } catch (IOException e) {
            throw new HttpConfigurationException(e);
        }

        JsonNode conf = Json.parse(sb.toString());
        myCurrentConfig = Json.fromJson(conf, Configuration.class);
    }

    /**
     * @return the Current loaded Configuration
     */
    public Configuration getCurrentConfigFile() {
        if (myCurrentConfig == null) {
            throw new HttpConfigurationException("No Current Configuration Set.");
        }
        return myCurrentConfig;
    }
}
