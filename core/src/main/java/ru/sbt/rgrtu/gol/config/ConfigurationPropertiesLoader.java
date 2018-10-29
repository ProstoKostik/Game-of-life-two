package ru.sbt.rgrtu.gol.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Loads configuration from a file in classpath. */
public class ConfigurationPropertiesLoader implements ConfigurationProvider {

    /** Properties file location. */
    private final String file;

    /**
     * Create provider with given file location.
     * @param file location of a file in classpath
     */
    public ConfigurationPropertiesLoader(String file) {
        this.file = file;
    }

    /**
     * Load settings from a file.
     *
     * @return filled Configuration object
     * @throws RuntimeException when unable to locate or parse a required file
     */
    public Configuration getConfiguration() {
        Properties properties = loadProperties(file);
        Configuration configuration = toConfiguration(properties);
        return configuration;
    }

    private Properties loadProperties(String file) {
        try {
            Properties properties = new Properties();
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
            properties.load(in);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Could not load properties", e);
        }
    }

    private Configuration toConfiguration(Properties properties) {
        Configuration configuration = new Configuration();
        configuration.setSizeX(Integer.parseInt(properties.getProperty("game.board.size.x")));
        configuration.setSizeY(Integer.parseInt(properties.getProperty("game.board.size.y")));
        configuration.setSeed(Long.parseLong(properties.getProperty("game.seed")));
        return configuration;
    }
}
