package ru.sbt.rgrtu.gol.game;

/**
 * Game board represents a field.
 * <p>Stores current values and values for the next step
 * <p>Top left corner has coordinates (0,0)
 * <p>Cycles over borders: -1 == sizeX - 1
 */
public interface BoardService {

    int getSizeX();

    int getSizeY();

    /**
     * Sets a new value for a point.
     * @param x abscissa
     * @param y ordinate
     * @param alive new value
     */
    void setPoint(int x, int y, boolean alive);

    /**
     * Get current value of the point.
     * @param x abscissa
     * @param y ordinate
     * @return current value
     */
    boolean getPoint(int x, int y);

    /** Applies all values of a new generation and creates the next one. */
    void applyNewValues();

    /** Get the number of last applied generation. */
    long getGeneration();
}
