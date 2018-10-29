package ru.sbt.rgrtu.gol.monitoring;

public class MonitoringServiceImpl implements MonitoringService {

    private long started;

    @Override
    public void calculationsStarts() {
        this.started = System.currentTimeMillis();
    }

    @Override
    public long elapsed() {
        return System.currentTimeMillis() - started;
    }
}
