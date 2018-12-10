package ru.sbt.rgrtu.gol.game;

import ru.sbt.rgrtu.gol.config.Configuration;
import ru.sbt.rgrtu.gol.config.ConfigurationProvider;

import java.math.BigInteger;
import java.util.Random;

public abstract class BDBoardService implements BoardService {

    private final ConfigurationProvider configurationProvider;

    private long seed;
    private BigInteger sizeX;
    private BigInteger sizeY;
    private int generation;
    private String userId = "";

    BDBoardService(ConfigurationProvider configurationProvider){
        this.configurationProvider = configurationProvider;
    }

    public void init() {
        Configuration configuration = configurationProvider.getConfiguration();
        this.seed = configuration.getSeed();
        this.sizeX = configuration.getSizeX();
        this.sizeY = configuration.getSizeY();

        if (userId != "") {
            Random random = new Random(seed);
            for (BigInteger y = BigInteger.ZERO; !y.equals(sizeY); y = y.add(BigInteger.ONE)) {
                for (BigInteger x = BigInteger.ZERO; !x.equals(sizeX); x = x.add(BigInteger.ONE)) {
                    if (random.nextBoolean()) setAlive(generation, x, y);
                }
            }
        }
    }

    protected abstract void setAlive(int generation, BigInteger x, BigInteger y);

    protected abstract boolean isAlive(int generation, BigInteger x, BigInteger y);

    public abstract void clearGeneration(int generation);

    @Override
    public void setPoint(BigInteger x, BigInteger y, boolean alive) {
        if (alive) setAlive(generation + 1, x, y);
    }

    @Override
    public boolean getPoint(BigInteger x, BigInteger y) {
        return isAlive(generation, (sizeX.add(x)).remainder(sizeX), (sizeY.add(y)).remainder(sizeY));
    }

    @Override
    public void applyNewValues() {
        clearGeneration(generation);
        generation++;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public void setSizeX(BigInteger sizeX) {
        this.sizeX = sizeX;
    }

    public void setSizeY(BigInteger sizeY) {
        this.sizeY = sizeY;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ConfigurationProvider getConfigurationProvider() {
        return configurationProvider;
    }

    public long getSeed() {
        return seed;
    }

    @Override
    public BigInteger getSizeX() {
        return sizeX;
    }

    @Override
    public BigInteger getSizeY() {
        return sizeY;
    }

    @Override
    public int getGeneration() {
        return generation;
    }

    public String getUserId() {
        return userId;
    }
}
