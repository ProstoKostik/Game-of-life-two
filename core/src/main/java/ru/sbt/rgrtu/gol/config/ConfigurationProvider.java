package ru.sbt.rgrtu.gol.config;

/** Load simulation settings from any source. */
public interface ConfigurationProvider {

    /** Get simulation settings. */
    Configuration getConfiguration();
}
