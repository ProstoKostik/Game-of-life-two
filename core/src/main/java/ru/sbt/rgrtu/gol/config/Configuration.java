package ru.sbt.rgrtu.gol.config;

import java.math.BigInteger;

/**
 * Game settings
 */
public class Configuration {

    /** Horizontal size of the board. */
    private BigInteger sizeX;
    /** Vertical size of the board. */
    private BigInteger sizeY;

    /** Seed for random board initialization. */
    private long seed;


    public BigInteger getSizeX() {
        return sizeX;
    }

    public void setSizeX(BigInteger sizeX) {
        this.sizeX = sizeX;
    }

    public BigInteger getSizeY() {
        return sizeY;
    }

    public void setSizeY(BigInteger sizeY) {
        this.sizeY = sizeY;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }
}
