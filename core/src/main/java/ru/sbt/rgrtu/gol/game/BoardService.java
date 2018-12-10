package ru.sbt.rgrtu.gol.game;

import java.math.BigInteger;

/**
 * Game board represents a field.
 * <p>Stores current values and values for the next step
 * <p>Top left corner has coordinates (0,0)
 * <p>Cycles over borders: -1 == sizeX - 1
 */
public interface BoardService {

    BigInteger getSizeX();

    BigInteger getSizeY();

    /**
     * Sets a new value for a point.
     *
     * @param x     abscissa
     * @param y     ordinate
     * @param alive new value
     */
    void setPoint(BigInteger x, BigInteger y, boolean alive);

    /**
     * Get current value of the point.
     *
     * @param x abscissa
     * @param y ordinate
     * @return current value
     */
    boolean getPoint(BigInteger x, BigInteger y);

    /**
     * Applies all values of a new generation and creates the next one.
     */
    void applyNewValues();

    /**
     * Get the number of last applied generation.
     */
    int getGeneration();

    void setUserId(String userId);
}
