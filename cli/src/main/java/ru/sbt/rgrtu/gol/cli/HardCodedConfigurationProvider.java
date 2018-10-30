package ru.sbt.rgrtu.gol.cli;

import ru.sbt.rgrtu.gol.config.Configuration;
import ru.sbt.rgrtu.gol.config.ConfigurationProvider;

import java.math.BigInteger;

public class HardCodedConfigurationProvider implements ConfigurationProvider {

    @Override
    public Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setSeed(20180921L);
        configuration.setSizeX(BigInteger.valueOf(150));
        configuration.setSizeY(BigInteger.valueOf(35));
        return configuration;
    }
}
