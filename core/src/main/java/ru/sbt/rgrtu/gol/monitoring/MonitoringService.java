package ru.sbt.rgrtu.gol.monitoring;

public interface MonitoringService {
    /** Algorithm is starting. */
    void calculationsStarts();
    long elapsed();
}
