package ru.sbt.rgrtu.gol.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationPropertiesLoaderTest {

    @Test
    void getConfiguration() {
        ConfigurationPropertiesLoader loader = new ConfigurationPropertiesLoader("config-test.properties");

        Configuration configuration = loader.getConfiguration();

        assertEquals(100500, configuration.getSizeX(), "Should load sizeX");
        assertEquals(42, configuration.getSizeY(), "Should load sizeY");
        assertEquals(20180928, configuration.getSeed(), "Should load seed");
    }
}
