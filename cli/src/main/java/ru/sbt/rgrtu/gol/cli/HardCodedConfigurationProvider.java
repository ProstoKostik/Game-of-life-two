package ru.sbt.rgrtu.gol.cli;

import ru.sbt.rgrtu.gol.config.Configuration;
import ru.sbt.rgrtu.gol.config.ConfigurationProvider;

public class HardCodedConfigurationProvider implements ConfigurationProvider {

    @Override
    public Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setSeed(20180921L);
        configuration.setSizeX(150);
        configuration.setSizeY(35);
        return configuration;
    }
}
